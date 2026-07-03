export type ProductStatus = 'NORMAL' | 'ESTOQUE_BAIXO' | 'VENCENDO' | 'VENCIDO';
export interface Product{id:number;name:string;barcode:string|null;categoryId:number;categoryName:string;supplierId:number|null;supplierName:string|null;costPrice:number;salePrice:number;quantity:number;minimumStock:number;expirationDate:string|null;status:ProductStatus;createdAt:string;updatedAt:string;}
export interface ProductPayload{name:string;barcode:string|null;categoryId:number;supplierId:number|null;costPrice:number;salePrice:number;quantity:number;minimumStock:number;expirationDate:string|null;}
