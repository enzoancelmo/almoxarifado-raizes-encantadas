import { StockMovement } from './stock-movement.model';

export interface QuickExitItemPayload {
  itemId: number | null;
  quantity: number;
  unitValue: number | null;
  notes: string | null;
}

export interface QuickExitPayload {
  eventName: string;
  exitTypeId: number | null;
  responsibleName: string | null;
  exitDate: string | null;
  notes: string | null;
  items: QuickExitItemPayload[];
}

export interface QuickExitResponse {
  id: number;
  eventName: string;
  exitTypeName: string | null;
  responsibleName: string | null;
  exitDate: string | null;
  notes: string | null;
  totalItems: number;
  totalQuantity: number;
  totalValue: number;
  movements: StockMovement[];
}