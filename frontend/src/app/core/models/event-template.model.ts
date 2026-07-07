export type EventTemplateItemStatus = 'DISPONIVEL' | 'ESTOQUE_INSUFICIENTE' | 'PRECISA_COMPRAR' | 'NAO_CADASTRADO';

export interface EventTemplateItem {
  id: number;
  itemId: number | null;
  itemName: string;
  unitOfMeasure: string | null;
  suggestedQuantity: number;
  notes: string | null;
  currentQuantity: number;
  missingQuantity: number;
  status: EventTemplateItemStatus;
  averageCost: number;
  currentStockValue: number;
}

export interface EventTemplate {
  id: number;
  name: string;
  description: string | null;
  eventType: string | null;
  active: boolean;
  notes: string | null;
  createdAt: string;
  updatedAt: string;
  items: EventTemplateItem[];
}

export interface EventTemplatePayload { name: string; description: string | null; eventType: string | null; active: boolean; notes: string | null; }
export interface EventTemplateItemPayload { itemId: number | null; itemName: string | null; unitOfMeasure: string | null; suggestedQuantity: number; notes: string | null; }