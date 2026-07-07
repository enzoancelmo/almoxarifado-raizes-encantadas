import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { EventTemplate, EventTemplatePayload } from '../../core/models/event-template.model';
import { Product } from '../../core/models/product.model';
import { EventTemplateService } from '../../core/services/event-template.service';
import { ProductService } from '../../core/services/product.service';

@Component({ selector: 'app-event-templates', standalone: true, imports: [CommonModule, FormsModule], templateUrl: './event-templates.component.html', styleUrl: './event-templates.component.css' })
export class EventTemplatesComponent implements OnInit {
  templates: EventTemplate[] = [];
  products: Product[] = [];
  selected: EventTemplate | null = null;
  loading = true;
  saving = false;
  error = '';
  success = '';
  search = '';
  form = { name: '', description: '', eventType: '', active: true, notes: '' };
  itemForm = { itemId: null as number | null, suggestedQuantity: 1, notes: '' };

  constructor(private readonly service: EventTemplateService, private readonly productsService: ProductService, private readonly cdr: ChangeDetectorRef) {}
  ngOnInit(): void { this.load(); this.productsService.list().subscribe(data => this.products = data); }
  get filtered(): EventTemplate[] { const t=this.search.trim().toLowerCase(); return this.templates.filter(m=>!t||m.name.toLowerCase().includes(t)||(m.eventType||'').toLowerCase().includes(t)); }
  newTemplate(): void { this.selected = null; this.form = { name: '', description: '', eventType: '', active: true, notes: '' }; this.success=''; this.error=''; }
  select(template: EventTemplate): void { this.service.get(template.id).subscribe({ next: data => { this.selected=data; this.form={name:data.name,description:data.description||'',eventType:data.eventType||'',active:data.active,notes:data.notes||''}; this.success=''; this.error=''; this.cdr.markForCheck(); }, error: e => this.error=this.apiError(e,'Não foi possível abrir o modelo.') }); }
  save(): void { if(!this.form.name.trim()){this.error='Informe o nome do modelo.'; return;} const payload:EventTemplatePayload={name:this.form.name.trim(),description:this.form.description.trim()||null,eventType:this.form.eventType.trim()||null,active:this.form.active,notes:this.form.notes.trim()||null}; this.saving=true; const call=this.selected?this.service.update(this.selected.id,payload):this.service.create(payload); call.subscribe({next:data=>{this.selected=data;this.success='Modelo salvo.';this.saving=false;this.load(false);},error:e=>{this.error=this.apiError(e,'Não foi possível salvar o modelo.');this.saving=false;}}); }
  deactivate(template: EventTemplate): void { this.service.deactivate(template.id).subscribe({ next:()=>{this.success='Modelo desativado.';this.load(); if(this.selected?.id===template.id)this.newTemplate();}, error:e=>this.error=this.apiError(e,'Não foi possível desativar o modelo.') }); }
  addItem(): void { if(!this.selected){this.error='Salve o modelo antes de adicionar itens.';return;} if(!this.itemForm.itemId){this.error='Selecione um item.';return;} if(!this.itemForm.suggestedQuantity||this.itemForm.suggestedQuantity<=0){this.error='Quantidade sugerida deve ser maior que zero.';return;} const payload={itemId:Number(this.itemForm.itemId),suggestedQuantity:Number(this.itemForm.suggestedQuantity),notes:this.itemForm.notes.trim()||null}; this.service.addItem(this.selected.id,payload).subscribe({next:data=>{this.selected=data;this.itemForm={itemId:null,suggestedQuantity:1,notes:''};this.success='Item adicionado ao modelo.';this.load(false);},error:e=>this.error=this.apiError(e,'Não foi possível adicionar o item.')}); }
  updateItem(itemId:number,itemProductId:number,quantity:number,notes:string|null): void { if(!this.selected)return; if(!quantity||quantity<=0){this.error='Quantidade sugerida deve ser maior que zero.';return;} this.service.updateItem(this.selected.id,itemId,{itemId:itemProductId,suggestedQuantity:Number(quantity),notes:notes||null}).subscribe({next:data=>{this.selected=data;this.success='Item atualizado.';this.load(false);},error:e=>this.error=this.apiError(e,'Não foi possível atualizar o item.')}); }
  removeItem(itemId:number): void { if(!this.selected)return; this.service.deleteItem(this.selected.id,itemId).subscribe({next:data=>{this.selected=data;this.success='Item removido.';this.load(false);},error:e=>this.error=this.apiError(e,'Não foi possível remover o item.')}); }
  productName(id:number|null): string { return this.products.find(p=>p.id===Number(id))?.name || ''; }
  private load(reset=true): void { this.loading=true; this.service.list().subscribe({next:data=>{this.templates=data;this.loading=false;if(reset&&!this.selected&&data.length)this.select(data[0]);this.cdr.markForCheck();},error:e=>{this.error=this.apiError(e,'Não foi possível carregar os modelos.');this.loading=false;}}); }
  private apiError(error:any,fallback:string): string { if(error?.status===401)return 'Sua sessão expirou. Faça login novamente.'; const fields=error?.error?.fields?Object.values(error.error.fields).join(' '):''; return fields||error?.error?.message||fallback; }
}