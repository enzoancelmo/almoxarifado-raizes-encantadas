import { StockMovement } from './stock-movement.model';
export interface DashboardSummary{totalProducts:number;lowStockProducts:number;expiringProducts:number;expiredProducts:number;totalStockValue:number;totalEntriesThisMonth:number;totalOutputsThisMonth:number;latestMovements:StockMovement[];}
