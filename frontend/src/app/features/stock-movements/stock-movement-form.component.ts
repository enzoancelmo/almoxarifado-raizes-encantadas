import { CommonModule } from '@angular/common';
import { ChangeDetectorRef,Component,OnInit } from '@angular/core';
import { FormBuilder,ReactiveFormsModule,Validators } from '@angular/forms';
import { ActivatedRoute,Router,RouterLink } from '@angular/router';
import { finalize,timeout } from 'rxjs';
import { Product } from '../../core/models/product.model';
import { StockMovementPayload,StockMovementType } from '../../core/models/stock-movement.model';
import { ProductService } from '../../core/services/product.service';
import { StockMovementService } from '../../core/services/stock-movement.service';
@Component({selector:'app-stock-movement-form',standalone:true,imports:[CommonModule,ReactiveFormsModule,RouterLink],templateUrl:'./stock-movement-form.component.html',styleUrl:'./stock-movement-form.component.css'})
export class StockMovementFormComponent implements OnInit{
 products:Product[]=[];saving=false;loading=true;error='';readonly types:StockMovementType[]=['ENTRADA','SAIDA','AJUSTE'];
 readonly form=this.fb.group({productId:[null as number|null,Validators.required],movementType:[null as StockMovementType|null,Validators.required],quantity:[null as number|null,[Validators.required,Validators.min(0)]],reason:['',[Validators.maxLength(255)]],responsibleName:[''],purpose:[''],eventName:[''],notes:['']});
 constructor(private readonly fb:FormBuilder,private readonly productsService:ProductService,private readonly movements:StockMovementService,private readonly route:ActivatedRoute,private readonly router:Router,private readonly cdr:ChangeDetectorRef){}
 ngOnInit():void{this.productsService.list().pipe(timeout(8000),finalize(()=>{this.loading=false;this.cdr.markForCheck();})).subscribe({next:data=>{this.products=data;const selected=this.route.snapshot.queryParamMap.get('productId');if(selected)this.form.patchValue({productId:Number(selected)});},error:()=>this.error='Não foi possível carregar os itens. Confirme se o backend está ativo em /api.'});}
 get selectedProduct():Product|undefined{return this.products.find(p=>p.id===Number(this.form.value.productId));}
 get isAdjustment():boolean{return this.form.value.movementType==='AJUSTE';}
 save():void{if(this.form.invalid){this.form.markAllAsTouched();return;}const value=this.form.getRawValue();const payload:StockMovementPayload={productId:Number(value.productId),movementType:value.movementType!,quantity:Number(value.quantity),reason:value.reason?.trim()||null,responsibleName:value.responsibleName?.trim()||null,purpose:value.purpose?.trim()||null,eventName:value.eventName?.trim()||null,notes:value.notes?.trim()||null};this.saving=true;this.error='';this.movements.create(payload).subscribe({next:()=>void this.router.navigate(['/movimentacoes'],{queryParams:{success:'Movimentação registrada e estoque atualizado.'}}),error:r=>{this.error=r.error?.message||'Não foi possível registrar a movimentação.';this.saving=false;this.cdr.markForCheck();}});}
 invalid(field:string):boolean{const c=this.form.get(field);return !!c&&c.invalid&&c.touched;}
 typeLabel(type:StockMovementType):string{return {ENTRADA:'Entrada',SAIDA:'Saída',AJUSTE:'Ajuste'}[type];}
}
