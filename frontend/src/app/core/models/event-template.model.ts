export interface EventTemplateItem {
  id: number;
  itemId: number;
  itemName: string;
  unitOfMeasure: string | null;
  suggestedQuantity: number;
  notes: string | null;
  currentQuantity: number;
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

export interface EventTemplatePayload {
  name: string;
  description: string | null;
  eventType: string | null;
  active: boolean;
  notes: string | null;
}

export interface EventTemplateItemPayload {
  itemId: number;
  suggestedQuantity: number;
  notes: string | null;
}