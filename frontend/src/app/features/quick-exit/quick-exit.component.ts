import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { finalize, forkJoin, timeout } from 'rxjs';
import { ExitType } from '../../core/models/exit-type.model';
import { Product, ProductStatus } from '../../core/models/product.model';
import { QuickExitPayload } from '../../core/models/quick-exit.model';
import { ExitTypeService } from '../../core/services/exit-type.service';
import { ProductService } from '../../core/services/product.service';
import { QuickExitService } from '../../core/services/quick-exit.service';

interface QuickExitLine {
  uid: number;
  itemId: number | null;
  quantity: number;
  unitValue: number | null;
  notes: string;
}

@Component({
  selector: 'app-quick-exit',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './quick-exit.component.html',
  styleUrl: './quick-exit.component.css'
})
export class QuickExitComponent implements OnInit {
  products: Product[] = [];
  exitTypes: ExitType[] = [];
  loading = true;
  saving = false;
  error = '';
  success = '';
  private uid = 1;

  form = { eventName: '', exitTypeId: null as number | null, responsibleName: '', exitDate: '', notes: '' };
  lines: QuickExitLine[] = [this.newLine()];

  constructor(
    private readonly productService: ProductService,
    private readonly exitTypeService: ExitTypeService,
    private readonly quickExit: QuickExitService,
    private readonly router: Router,
    private readonly cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void { this.load(); }

  get totalItems(): number { return this.lines.filter(line => line.itemId).length; }
  get totalQuantity(): number { return this.lines.reduce((sum, line) => sum + Number(line.quantity || 0), 0); }
  get totalValue(): number { return this.lines.reduce((sum, line) => sum + this.lineTotal(line), 0); }

  selectedProduct(line: QuickExitLine): Product | undefined {
    return this.products.find(product => product.id === Number(line.itemId));
  }

  onSelect(line: QuickExitLine): void {
    const product = this.selectedProduct(line);
    if (!product) { line.unitValue = null; return; }
    line.unitValue = this.suggestedUnitValue(product);
  }

  addLine(): void { this.lines.push(this.newLine()); }

  removeLine(line: QuickExitLine): void {
    if (this.lines.length === 1) { this.lines = [this.newLine()]; return; }
    this.lines = this.lines.filter(item => item.uid !== line.uid);
  }

  clear(): void {
    this.form = { eventName: '', exitTypeId: null, responsibleName: '', exitDate: '', notes: '' };
    this.lines = [this.newLine()];
    this.error = '';
    this.success = '';
  }

  lineTotal(line: QuickExitLine): number { return Number(line.quantity || 0) * Number(line.unitValue || 0); }

  save(): void {
    this.error = '';
    this.success = '';
    const validation = this.validate();
    if (validation) { this.error = validation; return; }

    const payload: QuickExitPayload = {
      eventName: this.form.eventName.trim(),
      exitTypeId: this.form.exitTypeId ? Number(this.form.exitTypeId) : null,
      responsibleName: this.form.responsibleName.trim() || null,
      exitDate: this.form.exitDate || null,
      notes: this.form.notes.trim() || null,
      items: this.lines.filter(line => line.itemId).map(line => ({
        itemId: Number(line.itemId),
        quantity: Number(line.quantity),
        unitValue: line.unitValue === null ? null : Number(line.unitValue),
        notes: line.notes.trim() || null
      }))
    };

    this.saving = true;
    this.quickExit.create(payload).subscribe({
      next: response => {
        this.success = `Saída salva: ${response.totalItems} item(ns), ${response.totalQuantity} unidade(s), total ${this.currency(response.totalValue)}.`;
        this.saving = false;
        this.reloadProducts();
        this.lines = [this.newLine()];
      },
      error: error => {
        this.error = this.apiError(error, 'Não foi possível salvar a saída rápida.');
        this.saving = false;
        this.cdr.markForCheck();
      }
    });
  }

  statusLabel(status: ProductStatus): string {
    return { NORMAL: 'Normal', SALDO_NEGATIVO: 'Saldo negativo', PENDENTE_CONTAGEM: 'Pendente de contagem', NECESSIDADE_REPOSICAO: 'Reposição necessária' }[status];
  }

  private validate(): string {
    if (!this.form.eventName.trim()) return 'Informe o evento/cerimônia.';
    const filled = this.lines.filter(line => line.itemId);
    if (!filled.length) return 'Adicione pelo menos um item.';

    const totals = new Map<number, number>();
    for (const line of filled) {
      if (!line.quantity || line.quantity <= 0) return 'Todas as quantidades devem ser maiores que zero.';
      const itemId = Number(line.itemId);
      totals.set(itemId, (totals.get(itemId) || 0) + Number(line.quantity));
    }

    for (const [itemId, quantity] of totals.entries()) {
      const product = this.products.find(item => item.id === itemId);
      if (!product) return 'Existe um item inválido na lista.';
      if (quantity > product.currentQuantity) return `Estoque insuficiente para ${product.name}. Disponível: ${product.currentQuantity}. Solicitado: ${quantity}.`;
    }
    return '';
  }

  private load(): void {
    this.loading = true;
    forkJoin({ products: this.productService.list(), exitTypes: this.exitTypeService.list(true) }).pipe(
      timeout(10000),
      finalize(() => { this.loading = false; this.cdr.markForCheck(); })
    ).subscribe({
      next: data => { this.products = data.products; this.exitTypes = data.exitTypes; },
      error: error => this.error = this.apiError(error, 'Não foi possível carregar itens e tipos de saída.')
    });
  }

  private reloadProducts(): void {
    this.productService.list().subscribe({ next: products => { this.products = products; this.cdr.markForCheck(); } });
  }

  private suggestedUnitValue(product: Product): number {
    if (product.averageCost && product.averageCost > 0) return product.averageCost;
    if (product.exitValue && product.exitValue > 0) return product.exitValue;
    if (product.purchaseValue && product.purchaseValue > 0) return product.purchaseValue;
    return 0;
  }

  private newLine(): QuickExitLine { return { uid: this.uid++, itemId: null, quantity: 1, unitValue: null, notes: '' }; }

  private apiError(error: any, fallback: string): string {
    if (error?.status === 401) return 'Sua sessão expirou. Faça login novamente e tente salvar de novo.';
    const fields = error?.error?.fields ? Object.values(error.error.fields).join(' ') : '';
    return fields || error?.error?.message || fallback;
  }

  private currency(value: number): string { return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(value || 0); }
}