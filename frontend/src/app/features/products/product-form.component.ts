import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { forkJoin, of } from 'rxjs';
import { Category } from '../../core/models/category.model';
import { ProductPayload } from '../../core/models/product.model';
import { CategoryService } from '../../core/services/category.service';
import { ProductService } from '../../core/services/product.service';

@Component({
  selector: 'app-product-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './product-form.component.html',
  styleUrl: './product-form.component.css'
})
export class ProductFormComponent implements OnInit {
  categories: Category[] = [];
  saving = false;
  loading = true;
  error = '';
  averageCost = 0;
  currentStockValue = 0;
  readonly productId: number | null;

  readonly form = this.fb.group({
    name: ['', [Validators.required, Validators.maxLength(150)]],
    brand: [''],
    entityPurpose: [''],
    unitOfMeasure: [''],
    categoryId: [null as number | null, Validators.required],
    expirationDate: [''],
    currentQuantity: [0, [Validators.required, Validators.min(0)]],
    monthlyRequiredQuantity: [0, [Validators.required, Validators.min(0)]],
    purchaseValue: [0, [Validators.min(0)]],
    exitValue: [0, [Validators.min(0)]],
    countPending: [false],
    notes: ['']
  });

  constructor(
    private fb: FormBuilder,
    private products: ProductService,
    private categoriesService: CategoryService,
    route: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {
    const id = route.snapshot.paramMap.get('id');
    this.productId = id ? Number(id) : null;
  }

  get editing(): boolean {
    return this.productId !== null;
  }

  ngOnInit(): void {
    forkJoin({
      categories: this.categoriesService.list(),
      product: this.productId === null ? of(null) : this.products.get(this.productId)
    }).subscribe({
      next: data => {
        this.categories = data.categories;
        if (data.product) {
          this.form.patchValue(data.product);
          this.averageCost = data.product.averageCost;
          this.currentStockValue = data.product.currentStockValue;
        }
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.error = 'Não foi possível carregar os dados do item.';
        this.loading = false;
        this.cdr.markForCheck();
      }
    });
  }

  save(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const value = this.form.getRawValue();
    const payload: ProductPayload = {
      name: value.name!.trim(),
      brand: value.brand?.trim() || null,
      entityPurpose: value.entityPurpose?.trim() || null,
      unitOfMeasure: value.unitOfMeasure?.trim() || null,
      categoryId: Number(value.categoryId),
      currentQuantity: Number(value.currentQuantity),
      monthlyRequiredQuantity: Number(value.monthlyRequiredQuantity),
      purchaseValue: Number(value.purchaseValue || 0),
      exitValue: Number(value.exitValue || 0),
      expirationDate: value.expirationDate || null,
      countPending: !!value.countPending,
      notes: value.notes?.trim() || null
    };

    this.saving = true;
    const request = this.productId === null
      ? this.products.create(payload)
      : this.products.update(this.productId, payload);

    request.subscribe({
      next: () => void this.router.navigate(['/produtos'], { queryParams: { success: 'Item salvo com sucesso.' } }),
      error: response => {
        this.error = response.error?.message || 'Não foi possível salvar o item.';
        this.saving = false;
      }
    });
  }

  invalid(field: string): boolean {
    const control = this.form.get(field);
    return !!control && control.invalid && control.touched;
  }
}
