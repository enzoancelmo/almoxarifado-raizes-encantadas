import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { finalize, forkJoin, timeout } from 'rxjs';
import { Category } from '../../core/models/category.model';
import { EntryType } from '../../core/models/entry-type.model';
import { Product } from '../../core/models/product.model';
import { QuickEntryPayload } from '../../core/models/quick-entry.model';
import { CategoryService } from '../../core/services/category.service';
import { EntryTypeService } from '../../core/services/entry-type.service';
import { ProductService } from '../../core/services/product.service';
import { QuickEntryService } from '../../core/services/quick-entry.service';

@Component({
  selector: 'app-quick-entry',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './quick-entry.component.html',
  styleUrl: './quick-entry.component.css'
})
export class QuickEntryComponent implements OnInit {
  products: Product[] = [];
  categories: Category[] = [];
  entryTypes: EntryType[] = [];
  mode: 'existing' | 'new' = 'existing';
  search = '';
  selected: Product | null = null;
  saving = false;
  loading = true;
  success = '';
  error = '';

  entry = {
    quantity: 0,
    unitValue: null as number | null,
    entryTypeId: null as number | null,
    responsibleName: '',
    notes: '',
    entryDate: ''
  };

  newItem = {
    name: '',
    brand: '',
    entityPurpose: '',
    categoryId: null as number | null,
    unitOfMeasure: 'Unidade',
    monthlyRequiredQuantity: 0,
    expirationDate: ''
  };

  constructor(
    private readonly productsService: ProductService,
    private readonly categoriesService: CategoryService,
    private readonly entryTypeService: EntryTypeService,
    private readonly quickEntry: QuickEntryService,
    private readonly cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadInitialData();
  }

  get filtered(): Product[] {
    const term = this.search.trim().toLowerCase();
    return this.products
      .filter(product =>
        !term ||
        product.name.toLowerCase().includes(term) ||
        (product.brand || '').toLowerCase().includes(term) ||
        product.categoryName.toLowerCase().includes(term) ||
        (product.barcode || '').toLowerCase().includes(term)
      )
      .slice(0, 20);
  }

  get total(): number {
    return Number(this.entry.quantity || 0) * Number(this.entry.unitValue || 0);
  }

  selectExisting(product: Product): void {
    this.mode = 'existing';
    this.selected = product;
    this.entry.unitValue = product.purchaseValue || product.averageCost || 0;
    this.success = '';
    this.error = '';
  }

  showExisting(): void {
    this.mode = 'existing';
    this.success = '';
    this.error = '';
  }

  startNew(): void {
    this.mode = 'new';
    this.selected = null;
    this.entry.unitValue = null;
    this.success = '';
    this.error = '';
  }

  save(): void {
    this.error = '';
    this.success = '';

    if (this.entry.quantity <= 0) {
      this.error = 'Informe uma quantidade maior que zero.';
      return;
    }

    if (this.mode === 'new' && (!this.entry.unitValue || this.entry.unitValue <= 0)) {
      this.error = 'Para novo item, informe o valor unitário.';
      return;
    }

    if (this.mode === 'existing' && !this.selected) {
      this.error = 'Selecione um item existente ou escolha cadastrar novo item.';
      return;
    }

    if (this.mode === 'new' && (!this.newItem.name.trim() || !this.newItem.categoryId)) {
      this.error = 'Informe nome e tipo/categoria do novo item.';
      return;
    }

    const payload: QuickEntryPayload = {
      existingItemId: this.mode === 'existing' ? this.selected!.id : null,
      newItem: this.mode === 'new'
        ? {
            name: this.newItem.name.trim(),
            brand: this.newItem.brand.trim() || null,
            entityPurpose: this.newItem.entityPurpose.trim() || null,
            categoryId: Number(this.newItem.categoryId),
            unitOfMeasure: this.newItem.unitOfMeasure.trim() || null,
            monthlyRequiredQuantity: Number(this.newItem.monthlyRequiredQuantity || 0),
            expirationDate: this.newItem.expirationDate || null
          }
        : null,
      quantity: Number(this.entry.quantity),
      unitValue: this.entry.unitValue === null ? null : Number(this.entry.unitValue),
      entryTypeId: this.entry.entryTypeId ? Number(this.entry.entryTypeId) : null,
      entryOrigin: null,
      responsibleName: this.entry.responsibleName.trim() || null,
      notes: this.entry.notes.trim() || null,
      entryDate: this.entry.entryDate || null
    };

    this.saving = true;
    this.quickEntry.create(payload).subscribe({
      next: response => {
        this.success = response.message;
        this.saving = false;
        this.selected = response.item;
        this.mode = 'existing';
        this.entry = {
          quantity: 0,
          unitValue: response.item.purchaseValue || response.item.averageCost || 0,
          entryTypeId: null,
          responsibleName: '',
          notes: '',
          entryDate: ''
        };
        this.reloadProducts();
      },
      error: error => {
        this.error = this.apiError(error, 'Não foi possível salvar a entrada.');
        this.saving = false;
        this.cdr.markForCheck();
      }
    });
  }

  statusLabel(status: string): string {
    return status.replaceAll('_', ' ');
  }

  private loadInitialData(): void {
    this.loading = true;
    this.error = '';

    forkJoin({
      products: this.productsService.list(),
      categories: this.categoriesService.list(),
      entryTypes: this.entryTypeService.list(true)
    }).pipe(
      timeout(10000),
      finalize(() => {
        this.loading = false;
        this.cdr.markForCheck();
      })
    ).subscribe({
      next: data => {
        this.products = data.products;
        this.categories = data.categories;
        this.entryTypes = data.entryTypes;
      },
      error: error => {
        this.error = this.apiError(error, 'Não foi possível carregar os dados da Entrada Rápida. Confirme se você está logado e tente novamente.');
      }
    });
  }

  private reloadProducts(): void {
    this.productsService.list().subscribe({
      next: products => {
        this.products = products;
        const selectedId = this.selected?.id;
        if (selectedId) {
          this.selected = products.find(product => product.id === selectedId) || this.selected;
        }
        this.cdr.markForCheck();
      },
      error: () => this.cdr.markForCheck()
    });
  }

  private apiError(error: any, fallback: string): string {
    if (error?.status === 401) return 'Sua sessão expirou. Faça login novamente e tente salvar de novo.';
    const fields = error?.error?.fields ? Object.values(error.error.fields).join(' ') : '';
    return fields || error?.error?.message || fallback;
  }
}
