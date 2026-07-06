package com.estoqueinteligente.financial;

import java.math.BigDecimal;
import java.util.List;

public class FinancialSummaryResponse {
    private final BigDecimal totalInventoryValue,totalEntryValue,totalOutputValue,totalEntryValueThisMonth,totalOutputValueThisMonth,balanceValue;
    private final long totalMovements;
    private final List<FinancialMovementResponse> latestFinancialMovements;
    public FinancialSummaryResponse(BigDecimal totalInventoryValue,BigDecimal totalEntryValue,BigDecimal totalOutputValue,BigDecimal totalEntryValueThisMonth,BigDecimal totalOutputValueThisMonth,BigDecimal balanceValue,long totalMovements,List<FinancialMovementResponse> latestFinancialMovements){this.totalInventoryValue=totalInventoryValue;this.totalEntryValue=totalEntryValue;this.totalOutputValue=totalOutputValue;this.totalEntryValueThisMonth=totalEntryValueThisMonth;this.totalOutputValueThisMonth=totalOutputValueThisMonth;this.balanceValue=balanceValue;this.totalMovements=totalMovements;this.latestFinancialMovements=latestFinancialMovements;}
    public BigDecimal getTotalInventoryValue(){return totalInventoryValue;} public BigDecimal getTotalEntryValue(){return totalEntryValue;} public BigDecimal getTotalOutputValue(){return totalOutputValue;} public BigDecimal getTotalEntryValueThisMonth(){return totalEntryValueThisMonth;} public BigDecimal getTotalOutputValueThisMonth(){return totalOutputValueThisMonth;} public BigDecimal getBalanceValue(){return balanceValue;} public long getTotalMovements(){return totalMovements;} public List<FinancialMovementResponse> getLatestFinancialMovements(){return latestFinancialMovements;}
}
