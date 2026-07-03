import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute,RouterLink } from '@angular/router';
import { Category } from '../../core/models/category.model';
import { CategoryService } from '../../core/services/category.service';
import { Product, ProductStatus } from '../../core/models/product.model';
import { ProductService } from '../../core/services/product.service';

@Component({selector:'app-product-list',standalone:true,imports:[CommonModule,FormsModule,RouterLink],templateUrl:'./product-list.component.html',styleUrl:'./product-list.component.css'})
export class ProductListComponent implements OnInit {
  products:Product[]=[]; categories:Category[]=[]; search=''; status=''; categoryId=''; loading=true; error=''; success='';
  readonly statuses:ProductStatus[]=['NORMAL','ESTOQUE_BAIXO','VENCENDO','VENCIDO'];
  constructor(private readonly service:ProductService,private readonly categoriesService:CategoryService,private readonly route:ActivatedRoute) {}
  ngOnInit():void { this.route.queryParamMap.subscribe(params=>{this.success=params.get('success')||'';this.search=params.get('q')||'';});this.categoriesService.list().subscribe({next:d=>this.categories=d});this.load(); }
  get filteredProducts():Product[] {
    const term=this.search.trim().toLocaleLowerCase('pt-BR');
    return this.products.filter(p=>(!term||p.name.toLocaleLowerCase('pt-BR').includes(term))&&(!this.status||p.status===this.status)&&(!this.categoryId||p.categoryId===Number(this.categoryId)));
  }
  load():void { this.loading=true; this.error=''; this.service.list().subscribe({next:data=>{this.products=data;this.loading=false;},error:()=>{this.error='Não foi possível carregar os produtos. Verifique se o backend está ativo.';this.loading=false;}}); }
  remove(product:Product):void {
    if(!confirm('Excluir “'+product.name+'”? Esta ação não pode ser desfeita.')) return;
    this.service.delete(product.id).subscribe({next:()=>{this.success='Produto excluído com sucesso.';this.load();},error:()=>this.error='Não foi possível excluir o produto.'});
  }
  statusLabel(status:ProductStatus):string { return {NORMAL:'Normal',ESTOQUE_BAIXO:'Estoque baixo',VENCENDO:'Vencendo',VENCIDO:'Vencido'}[status]; }
}
