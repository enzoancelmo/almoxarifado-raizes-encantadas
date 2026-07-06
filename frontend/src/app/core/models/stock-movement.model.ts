export type StockMovementType='ENTRADA'|'SAIDA'|'AJUSTE';
export interface StockMovement{id:number;productId:number;productName:string;movementType:StockMovementType;quantity:number;previousQuantity:number;newQuantity:number;reason:string|null;createdAt:string;}
export interface StockMovementPayload{productId:number;movementType:StockMovementType;quantity:number;reason:string|null;responsibleName:string|null;purpose:string|null;eventName:string|null;notes:string|null;}
