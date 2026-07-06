package com.estoqueinteligente.export;

import com.estoqueinteligente.financial.EventCostResponse;
import com.estoqueinteligente.financial.FinancialMovementResponse;
import com.estoqueinteligente.financial.FinancialReportService;
import com.estoqueinteligente.financial.FinancialSummaryResponse;
import com.estoqueinteligente.product.ProductResponse;
import com.estoqueinteligente.product.ProductService;
import com.estoqueinteligente.stockmovement.StockMovement;
import com.estoqueinteligente.stockmovement.StockMovementType;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CsvExportService {
    private final ProductService productService;
    private final FinancialReportService financialReportService;

    public CsvExportService(ProductService productService,FinancialReportService financialReportService){
        this.productService=productService;
        this.financialReportService=financialReportService;
    }

    @Transactional(readOnly=true)
    public String itemsCsv(){
        StringBuilder csv=new StringBuilder("\uFEFFItem,Tipo,Quantidade Atual,Necessidade Mensal,Saldo,Valor de Compra,Valor de Saida,Valor em Estoque,Status\n");
        for(ProductResponse p:productService.findAll())line(csv,p.getName(),p.getCategoryName(),p.getCurrentQuantity(),p.getMonthlyRequiredQuantity(),p.getBalance(),p.getPurchaseValue(),p.getExitValue(),p.getTotalInventoryValue(),p.getStatus());
        return csv.toString();
    }

    @Transactional(readOnly=true)
    public String movementsCsv(StockMovementType type,LocalDate start,LocalDate end,String eventName,Long exitTypeId){
        StringBuilder csv=new StringBuilder("\uFEFFData,Item,Tipo Movimentacao,Quantidade,Valor Unitario,Valor Total,Tipo Saida,Evento,Responsavel,Observacao\n");
        for(StockMovement m:financialReportService.filterMovements(type,start,end,eventName,exitTypeId))line(csv,m.getCreatedAt(),m.getProduct().getName(),m.getMovementType(),m.getQuantity(),m.getUnitValue(),m.getTotalValue(),m.getExitType()==null?null:m.getExitType().getName(),m.getEventName(),m.getResponsibleName(),m.getNotes()==null?m.getReason():m.getNotes());
        return csv.toString();
    }

    @Transactional(readOnly=true)
    public String eventCostsCsv(String eventName,LocalDate start,LocalDate end){
        EventCostResponse report=financialReportService.eventCosts(eventName,start,end,null);
        StringBuilder csv=new StringBuilder("\uFEFFEvento,Data,Item,Quantidade,Valor Unitario,Valor Total,Tipo Saida,Responsavel,Observacao\n");
        for(FinancialMovementResponse m:report.getMovements())line(csv,report.getEventName(),m.getCreatedAt(),m.getItemName(),m.getQuantity(),m.getUnitValue(),m.getTotalValue(),m.getExitTypeName(),m.getResponsibleName(),m.getNotes());
        line(csv,"TOTAL","","","","",report.getTotalOutputValue(),"","","");
        return csv.toString();
    }

    @Transactional(readOnly=true)
    public String financialSummaryCsv(){
        FinancialSummaryResponse s=financialReportService.summary();
        StringBuilder csv=new StringBuilder("\uFEFFIndicador,Valor\n");
        line(csv,"Valor estimado em estoque",s.getTotalInventoryValue());
        line(csv,"Total de entradas",s.getTotalEntryValue());
        line(csv,"Total de saidas",s.getTotalOutputValue());
        line(csv,"Entradas no mes",s.getTotalEntryValueThisMonth());
        line(csv,"Saidas no mes",s.getTotalOutputValueThisMonth());
        line(csv,"Saldo financeiro estimado",s.getBalanceValue());
        return csv.toString();
    }

    private void line(StringBuilder csv,Object... values){
        for(int i=0;i<values.length;i++){
            if(i>0)csv.append(',');
            csv.append(escape(values[i]));
        }
        csv.append('\n');
    }

    private String escape(Object value){
        String text=value==null?"":(value instanceof BigDecimal?((BigDecimal)value).toPlainString():String.valueOf(value));
        return "\"" + text.replace("\"","\"\"") + "\"";
    }
}
