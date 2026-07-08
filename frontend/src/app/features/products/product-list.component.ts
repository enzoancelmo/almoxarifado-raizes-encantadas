import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Category } from '../../core/models/category.model';
import { Product, ProductStatus } from '../../core/models/product.model';
import { CategoryService } from '../../core/services/category.service';
import { ProductService } from '../../core/services/product.service';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './product-list.component.html',
  styleUrl: './product-list.component.css'
})
export class ProductListComponent implements OnInit {
  products: Product[] = [];
  categories: Category[] = [];
  search = '';
  status = '';
  categoryId = '';
  purpose = '';
  loading = true;
  error = '';
  success = '';

  readonly statuses: ProductStatus[] = [
    'NORMAL',
    'SALDO_NEGATIVO',
    'PENDENTE_CONTAGEM',
    'NECESSIDADE_REPOSICAO',
    'VENCENDO',
    'VENCIDO'
  ];

  constructor(
    private service: ProductService,
    private cats: CategoryService,
    route: ActivatedRoute
  ) {
    route.queryParamMap.subscribe(params => this.search = params.get('q') || '');
  }

  ngOnInit(): void {
    this.cats.list().subscribe(data => this.categories = data);
    this.load();
  }

  get filteredProducts(): Product[] {
    const term = this.search.trim().toLowerCase();
    const purpose = this.purpose.trim().toLowerCase();
    return this.products.filter(product =>
      (!term || product.name.toLowerCase().includes(term)) &&
      (!purpose || (product.entityPurpose || '').toLowerCase().includes(purpose)) &&
      (!this.status || product.status === this.status) &&
      (!this.categoryId || product.categoryId === Number(this.categoryId))
    );
  }

  load(): void {
    this.service.list().subscribe({
      next: data => {
        this.products = data;
        this.loading = false;
      },
      error: () => {
        this.error = 'Não foi possível carregar os itens.';
        this.loading = false;
      }
    });
  }

  remove(product: Product): void {
    if (confirm('Excluir “' + product.name + '”?')) {
      this.service.delete(product.id).subscribe(() => this.load());
    }
  }

  statusLabel(status: ProductStatus): string {
    return {
      NORMAL: 'Normal',
      SALDO_NEGATIVO: 'Saldo negativo',
      PENDENTE_CONTAGEM: 'Pendente de contagem',
      NECESSIDADE_REPOSICAO: 'Necessidade de reposição',
      VENCENDO: 'Vencendo',
      VENCIDO: 'Vencido'
    }[status];
  }
}
