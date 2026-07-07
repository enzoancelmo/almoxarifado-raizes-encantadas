import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Product } from '../../core/models/product.model';
import { StockMovement, StockMovementType } from '../../core/models/stock-movement.model';
import { ProductService } from '../../core/services/product.service';
import { StockMovementService } from '../../core/services/stock-movement.service';

@Component({
  selector: 'app-stock-movement-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './stock-movement-list.component.html',
  styleUrl: './stock-movement-list.component.css'
})
export class StockMovementListComponent implements OnInit {
  movements: StockMovement[] = [];
  products: Product[] = [];
  productId = '';
  type = '';
  date = '';
  loading = true;
  error = '';
  success = '';
  readonly types: StockMovementType[] = ['ENTRADA', 'SAIDA', 'AJUSTE'];

  constructor(
    private readonly service: StockMovementService,
    private readonly productService: ProductService,
    private readonly route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.success = this.route.snapshot.queryParamMap.get('success') || '';
    this.load();
    this.productService.list().subscribe({ next: data => this.products = data });
  }

  get filtered(): StockMovement[] {
    return this.movements.filter(movement =>
      (!this.productId || movement.productId === Number(this.productId)) &&
      (!this.type || movement.movementType === this.type) &&
      (!this.date || movement.createdAt.slice(0, 10) === this.date)
    );
  }

  load(): void {
    this.loading = true;
    this.error = '';
    this.service.list().subscribe({
      next: data => {
        this.movements = data;
        this.loading = false;
      },
      error: error => {
        this.error = error?.status === 401
          ? 'Sua sessão expirou. Faça login novamente.'
          : 'Não foi possível carregar as movimentações. Verifique o backend.';
        this.loading = false;
      }
    });
  }

  typeLabel(type: StockMovementType): string {
    return { ENTRADA: 'Entrada', SAIDA: 'Saída', AJUSTE: 'Ajuste' }[type];
  }

  quantityPrefix(type: StockMovementType): string {
    return type === 'ENTRADA' ? '+' : type === 'SAIDA' ? '-' : '=';
  }
}