package com.estoqueinteligente.financial;

import com.estoqueinteligente.product.ProductRepository;
import com.estoqueinteligente.stockmovement.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FinancialReportService {
    private final ProductRepository productRepository; private final StockMovementRepository movementRepository;
    public FinancialReportService(ProductRepository productRepository,StockMovementRepository movementRepository){this.productRepository=productRepository;this.movementRepository=movementRepository;}
    @Transactional(readOnly=true) public FinancialSummaryResponse summary(){
        List<StockMovement> movements=movementRepository.findAllWithProductAndExitType();
        BigDecimal inventory=productRepository.findAllWithCategory().stream().map(p->money(p.getCurrentStockValue())).reduce(BigDecimal.ZERO,BigDecimal::add);
        BigDecimal entries=sum(movements,StockMovementType.ENTRADA,null,null,null,null);
        BigDecimal outputs=sum(movements,StockMovementType.SAIDA,null,null,null,null);
        ZoneId zone=ZoneId.systemDefault(); YearMonth month=YearMonth.now(zone); Instant start=month.atDay(1).atStartOfDay(zone).toInstant(); Instant end=month.plusMonths(1).atDay(1).atStartOfDay(zone).toInstant();
        List<FinancialMovementResponse> latest=movements.stream().filter(m->money(m.getTotalValue()).compareTo(BigDecimal.ZERO)>0).sorted(Comparator.comparing(StockMovement::getCreatedAt).reversed()).limit(10).map(FinancialMovementResponse::from).toList();
        return new FinancialSummaryResponse(inventory,entries,outputs,sum(movements,StockMovementType.ENTRADA,start,end,null,null),sum(movements,StockMovementType.SAIDA,start,end,null,null),entries.subtract(outputs),movements.size(),latest);
    }
    @Transactional(readOnly=true) public EventCostResponse eventCosts(String eventName,LocalDate startDate,LocalDate endDate,Long exitTypeId){
        Instant start=startDate==null?null:startDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant end=endDate==null?null:endDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();
        List<StockMovement> filtered=movementRepository.findAllWithProductAndExitType().stream().filter(m->matches(m,StockMovementType.SAIDA,start,end,eventName,exitTypeId)).toList();
        BigDecimal total=filtered.stream().map(m->money(m.getTotalValue())).reduce(BigDecimal.ZERO,BigDecimal::add);
        long items=filtered.stream().mapToLong(m->m.getQuantity()==null?0:m.getQuantity()).sum();
        String label=eventName==null||eventName.isBlank()?"Todos os eventos":eventName.trim();
        return new EventCostResponse(label,total,items,filtered.stream().map(FinancialMovementResponse::from).toList());
    }
    @Transactional(readOnly=true) public List<StockMovement> filterMovements(StockMovementType type,LocalDate startDate,LocalDate endDate,String eventName,Long exitTypeId){
        Instant start=startDate==null?null:startDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant end=endDate==null?null:endDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();
        return movementRepository.findAllWithProductAndExitType().stream().filter(m->matches(m,type,start,end,eventName,exitTypeId)).toList();
    }
    private BigDecimal sum(List<StockMovement> movements,StockMovementType type,Instant start,Instant end,String eventName,Long exitTypeId){return movements.stream().filter(m->matches(m,type,start,end,eventName,exitTypeId)).map(m->money(m.getTotalValue())).reduce(BigDecimal.ZERO,BigDecimal::add);}
    private boolean matches(StockMovement m,StockMovementType type,Instant start,Instant end,String eventName,Long exitTypeId){
        if(type!=null&&m.getMovementType()!=type)return false;
        if(start!=null&&m.getCreatedAt().isBefore(start))return false;
        if(end!=null&&!m.getCreatedAt().isBefore(end))return false;
        if(eventName!=null&&!eventName.isBlank()&&(m.getEventName()==null||!m.getEventName().toLowerCase().contains(eventName.trim().toLowerCase())))return false;
        return exitTypeId==null||(m.getExitType()!=null&&exitTypeId.equals(m.getExitType().getId()));
    }
    private BigDecimal money(BigDecimal value){return value==null?BigDecimal.ZERO:value;}
}
