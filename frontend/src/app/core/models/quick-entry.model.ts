import{Product}from'./product.model';
import{StockMovement}from'./stock-movement.model';
export interface QuickEntryNewItem{name:string;brand:string|null;entityPurpose:string|null;categoryId:number|null;unitOfMeasure:string|null;monthlyRequiredQuantity:number;}
export interface QuickEntryPayload{existingItemId:number|null;newItem:QuickEntryNewItem|null;quantity:number;unitValue:number|null;entryTypeId:number|null;entryOrigin:string|null;responsibleName:string|null;notes:string|null;entryDate:string|null;}
export interface QuickEntryResponse{item:Product;movement:StockMovement;message:string;}
