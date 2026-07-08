package com.estoqueinteligente.dashboard;

import java.math.BigDecimal;
import java.time.*;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.estoqueinteligente.product.*;
import com.estoqueinteligente.stockmovement.*;
import com.estoqueinteligente.financial.*;

@Service
public class DashboardService {
    private final ProductService productService; private final StockMovementRepository movementRepository; private final FinancialReportService financialReportService;
    public DashboardService(ProductService productService,StockMovementRepository movementRepository,FinancialReportService financialReportService){this.productService=productService;this.movementRepository=movementRepository;this.financialReportService=financialReportService;}
    @Transactional(readOnly=true) public DashboardSummaryResponse getSummary(){
        List<ProductResponse> products=productService.findAll();
        long low=products.stream().filter(p->p.getStatus()==ProductStatus.SALDO_NEGATIVO).count();
        long expiring=products.stream().filter(p->p.getStatus()==ProductStatus.VENCENDO).count();
        long expired=products.stream().filter(p->p.getStatus()==ProductStatus.VENCIDO).count();
        BigDecimal value=products.stream().map(ProductResponse::getTotalInventoryValue).reduce(BigDecimal.ZERO,BigDecimal::add);
        ZoneId zone=ZoneId.systemDefault(); YearMonth month=YearMonth.now(zone); Instant start=month.atDay(1).atStartOfDay(zone).toInstant(); Instant end=month.plusMonths(1).atDay(1).atStartOfDay(zone).toInstant();
        long entries=movementRepository.sumQuantityByTypeAndPeriod(StockMovementType.ENTRADA,start,end); long outputs=movementRepository.sumQuantityByTypeAndPeriod(StockMovementType.SAIDA,start,end);
        List<StockMovementResponse> latest=movementRepository.findTop5ByOrderByCreatedAtDesc().stream().map(StockMovementResponse::from).toList();
        FinancialSummaryResponse financial=financialReportService.summary();
        return new DashboardSummaryResponse(products.size(),low,expiring,expired,value,financial.getTotalEntryValue(),financial.getTotalOutputValue(),financial.getTotalEntryValueThisMonth(),financial.getTotalOutputValueThisMonth(),financial.getBalanceValue(),entries,outputs,latest);
    }
}
