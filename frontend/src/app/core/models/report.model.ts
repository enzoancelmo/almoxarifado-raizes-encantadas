import { ProductStatus } from './product.model';
export interface StockReport{id:number;name:string;categoryName:string;quantity:number;minimumStock:number;costPrice:number;salePrice:number;averageCost:number;expirationDate:string|null;status:ProductStatus;stockValue:number;}
export interface LowStockReport{id:number;name:string;categoryName:string;quantity:number;minimumStock:number;quantityToBuy:number;status:ProductStatus;}
export interface ExpiringReport{id:number;name:string;categoryName:string;quantity:number;expirationDate:string;daysToExpire:number;status:ProductStatus;}
export interface ExpiredReport{id:number;name:string;categoryName:string;quantity:number;expirationDate:string;daysExpired:number;status:ProductStatus;}
export interface PurchaseSuggestion{productId:number;productName:string;categoryName:string;currentQuantity:number;minimumStock:number;suggestedQuantity:number;reason:string;}
