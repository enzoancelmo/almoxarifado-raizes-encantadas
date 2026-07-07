import{StockMovementType}from'./stock-movement.model';
export interface FinancialMovement{id:number;itemName:string;movementType:StockMovementType;quantity:number;unitValue:number;totalValue:number;exitTypeName:string|null;responsibleName:string|null;eventName:string|null;eventTemplateName:string|null;notes:string|null;createdAt:string;}
export interface FinancialSummary{totalInventoryValue:number;totalEntryValue:number;totalOutputValue:number;totalEntryValueThisMonth:number;totalOutputValueThisMonth:number;balanceValue:number;totalMovements:number;latestFinancialMovements:FinancialMovement[];}
export interface EventCost{eventName:string;totalOutputValue:number;totalItemsUsed:number;movements:FinancialMovement[];}
