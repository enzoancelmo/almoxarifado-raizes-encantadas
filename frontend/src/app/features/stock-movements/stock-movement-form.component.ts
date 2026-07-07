import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { finalize, forkJoin, timeout } from 'rxjs';
import { ExitType } from '../../core/models/exit-type.model';
import { Product } from '../../core/models/product.model';
import { StockMovementPayload, StockMovementType } from '../../core/models/stock-movement.model';
import { ExitTypeService } from '../../core/services/exit-type.service';
import { ProductService } from '../../core/services/product.service';
import { StockMovementService } from '../../core/services/stock-movement.service';

@Component({
  selector: 'app-stock-movement-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './stock-movement-form.component.html',
  styleUrl: './stock-movement-form.component.css'
})
export class StockMovementFormComponent implements OnInit {
  products: Product[] = [];
  exitTypes: ExitType[] = [];
  saving = false;
  loading = true;
  error = '';

  readonly types: StockMovementType[] = ['ENTRADA', 'SAIDA', 'AJUSTE'];

  readonly form = this.fb.group({
    productId: [null as number | null, Validators.required],
    movementType: [null as StockMovementType | null, Validators.required],
    quantity: [null as number | null, [Validators.required, Validators.min(1)]],
    unitValue: [null as number | null, [Validators.min(0)]],
    exitTypeId: [null as number | null],
    reason: ['', [Validators.maxLength(255)]],
    responsibleName: [''],
    purpose: [''],
    eventName: [''],
    notes: ['']
  });

  constructor(
    private readonly fb: FormBuilder,
    private readonly productsService: ProductService,
    private readonly exitTypeService: ExitTypeService,
    private readonly movements: StockMovementService,
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    forkJoin({
      products: this.productsService.list(),
      exitTypes: this.exitTypeService.list(true)
    }).pipe(
      timeout(8000),
      finalize(() => {
        this.loading = false;
        this.cdr.markForCheck();
      })
    ).subscribe({
      next: data => {
        this.products = data.products;
        this.exitTypes = data.exitTypes;
        const selected = this.route.snapshot.queryParamMap.get('productId');
        if (selected) this.form.patchValue({ productId: Number(selected) });
      },
      error: error => this.error = this.apiError(error, 'Não foi possível carregar os dados. Confirme se você está logado e se o sistema está ativo.')
    });
  }

  get selectedProduct(): Product | undefined {
    return this.products.find(product => product.id === Number(this.form.value.productId));
  }

  get isAdjustment(): boolean {
    return this.form.value.movementType === 'AJUSTE';
  }

  get isExit(): boolean {
    return this.form.value.movementType === 'SAIDA';
  }

  get suggestedUnitValue(): number {
    const product = this.selectedProduct;
    if (!product) return 0;
    if (this.form.value.movementType === 'ENTRADA') return product.purchaseValue || 0;
    if (this.form.value.movementType === 'SAIDA') return product.averageCost || 0;
    if (this.form.value.movementType === 'AJUSTE') {
      return Number(this.form.value.quantity || 0) > product.currentQuantity
        ? product.purchaseValue || 0
        : product.averageCost || 0;
    }
    return 0;
  }

  get calculatedTotal(): number {
    return Number(this.form.value.quantity || 0) * Number(this.form.value.unitValue ?? this.suggestedUnitValue);
  }

  save(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const value = this.form.getRawValue();
    const payload: StockMovementPayload = {
      productId: Number(value.productId),
      movementType: value.movementType!,
      quantity: Number(value.quantity),
      unitValue: value.unitValue === null ? null : Number(value.unitValue),
      entryTypeId: null,
      exitTypeId: value.movementType === 'SAIDA' && value.exitTypeId ? Number(value.exitTypeId) : null,
      reason: value.reason?.trim() || null,
      responsibleName: value.responsibleName?.trim() || null,
      purpose: value.purpose?.trim() || null,
      eventName: value.eventName?.trim() || null,
      notes: value.notes?.trim() || null
    };

    this.saving = true;
    this.error = '';
    this.movements.create(payload).subscribe({
      next: () => void this.router.navigate(['/movimentacoes'], { queryParams: { success: 'Movimentação registrada e estoque atualizado.' } }),
      error: error => {
        this.error = this.apiError(error, 'Não foi possível registrar a movimentação.');
        this.saving = false;
        this.cdr.markForCheck();
      }
    });
  }

  invalid(field: string): boolean {
    const control = this.form.get(field);
    return !!control && control.invalid && control.touched;
  }

  typeLabel(type: StockMovementType): string {
    return { ENTRADA: 'Entrada', SAIDA: 'Saída', AJUSTE: 'Ajuste' }[type];
  }

  private apiError(error: any, fallback: string): string {
    if (error?.status === 401) return 'Sua sessão expirou. Faça login novamente e tente salvar de novo.';
    const fields = error?.error?.fields ? Object.values(error.error.fields).join(' ') : '';
    return fields || error?.error?.message || fallback;
  }
}