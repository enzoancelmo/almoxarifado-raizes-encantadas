package com.estoqueinteligente.dashboard;

import java.math.BigDecimal;
import java.util.List;
import com.estoqueinteligente.stockmovement.StockMovementResponse;

public class DashboardSummaryResponse {
    private final long totalProducts,lowStockProducts,expiringProducts,expiredProducts,totalEntriesThisMonth,totalOutputsThisMonth;
    private final BigDecimal totalStockValue; private final List<StockMovementResponse> latestMovements;
    public DashboardSummaryResponse(long totalProducts,long lowStockProducts,long expiringProducts,long expiredProducts,BigDecimal totalStockValue,long totalEntriesThisMonth,long totalOutputsThisMonth,List<StockMovementResponse> latestMovements){this.totalProducts=totalProducts;this.lowStockProducts=lowStockProducts;this.expiringProducts=expiringProducts;this.expiredProducts=expiredProducts;this.totalStockValue=totalStockValue;this.totalEntriesThisMonth=totalEntriesThisMonth;this.totalOutputsThisMonth=totalOutputsThisMonth;this.latestMovements=latestMovements;}
    public long getTotalProducts(){return totalProducts;} public long getLowStockProducts(){return lowStockProducts;} public long getExpiringProducts(){return expiringProducts;} public long getExpiredProducts(){return expiredProducts;} public BigDecimal getTotalStockValue(){return totalStockValue;} public long getTotalEntriesThisMonth(){return totalEntriesThisMonth;} public long getTotalOutputsThisMonth(){return totalOutputsThisMonth;} public List<StockMovementResponse> getLatestMovements(){return latestMovements;}
}
