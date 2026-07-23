import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { finalize, forkJoin, timeout } from 'rxjs';
import { EventTemplate } from '../../core/models/event-template.model';
import { ExitType } from '../../core/models/exit-type.model';
import { Product, ProductStatus } from '../../core/models/product.model';
import { QuickExitPayload } from '../../core/models/quick-exit.model';
import { EventTemplateService } from '../../core/services/event-template.service';
import { ExitTypeService } from '../../core/services/exit-type.service';
import { ProductService } from '../../core/services/product.service';
import { QuickExitService } from '../../core/services/quick-exit.service';

interface QuickExitLine {
  uid: number;
  itemId: number | null;
  itemName: string;
  productSearch: string;
  unitOfMeasure: string | null;
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
  templates: EventTemplate[] = [];
  loading = true;
  saving = false;
  error = '';
  success = '';
  private uid = 1;

  form = {
    eventName: '',
    eventTemplateId: null as number | null,
    exitTypeId: null as number | null,
    responsibleName: '',
    exitDate: '',
    notes: ''
  };

  lines: QuickExitLine[] = [this.newLine()];

  constructor(
    private readonly productService: ProductService,
    private readonly exitTypeService: ExitTypeService,
    private readonly templateService: EventTemplateService,
    private readonly quickExit: QuickExitService,
    private readonly cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.load();
  }

  get totalItems(): number {
    return this.lines.filter(line => line.itemId || line.itemName).length;
  }

  get totalQuantity(): number {
    return this.lines.reduce((sum, line) => sum + Number(line.quantity || 0), 0);
  }

  get totalValue(): number {
    return this.lines.reduce((sum, line) => sum + this.lineTotal(line), 0);
  }

  get purchaseList(): QuickExitLine[] {
    return this.lines
      .filter(line => line.itemId || line.itemName)
      .filter(line => this.lineStatus(line) !== 'DISPONIVEL');
  }

  get activeTemplates(): EventTemplate[] {
    return this.templates.filter(template => template.active);
  }

  selectedProduct(line: QuickExitLine): Product | undefined {
    return this.products.find(product => product.id === Number(line.itemId));
  }

  filteredProducts(line: QuickExitLine): Product[] {
    const term = line.productSearch.trim().toLowerCase();
    return this.products
      .filter(product =>
        !term ||
        product.name.toLowerCase().includes(term) ||
        (product.brand || '').toLowerCase().includes(term) ||
        (product.categoryName || '').toLowerCase().includes(term)
      )
      .slice(0, 50);
  }

  productSuggestions(line: QuickExitLine): Product[] {
    const term = line.productSearch.trim();
    if (!term || line.itemId) return [];
    return this.filteredProducts(line).slice(0, 8);
  }

  lineTotal(line: QuickExitLine): number {
    return Number(line.quantity || 0) * Number(line.unitValue || 0);
  }

  onSearchChange(line: QuickExitLine): void {
    if (line.itemId && line.productSearch.trim() !== line.itemName) {
      line.itemId = null;
      line.unitValue = null;
      line.unitOfMeasure = null;
    }
  }

  selectSuggestedProduct(line: QuickExitLine, product: Product): void {
    line.itemId = product.id;
    this.onSelect(line);
  }

  onSelect(line: QuickExitLine): void {
    const product = this.selectedProduct(line);
    if (!product) {
      line.unitValue = null;
      return;
    }

    const duplicate = this.lines.find(other => other.uid !== line.uid && other.itemId === line.itemId);
    if (duplicate) {
      duplicate.quantity = Number(duplicate.quantity || 0) + Number(line.quantity || 1);
      duplicate.notes = duplicate.notes || line.notes;
      this.lines = this.lines.filter(other => other.uid !== line.uid);
      this.error = 'Item já estava no carrinho; somei a quantidade na linha existente.';
      return;
    }

    line.itemName = product.name;
    line.productSearch = product.name;
    line.unitOfMeasure = product.unitOfMeasure;
    line.unitValue = this.suggestedUnitValue(product);
    this.moveToNextEntryLine(line);
  }

  addLine(): void {
    const line = this.newLine();
    this.lines = [line, ...this.lines];
    this.focusSearch(line.uid);
  }

  removeLine(line: QuickExitLine): void {
    this.lines = this.lines.length === 1 ? [this.newLine()] : this.lines.filter(item => item.uid !== line.uid);
  }

  clearCart(): void {
    this.lines = [this.newLine()];
    this.error = '';
    this.success = '';
  }

  clear(): void {
    this.form = { eventName: '', eventTemplateId: null, exitTypeId: null, responsibleName: '', exitDate: '', notes: '' };
    this.clearCart();
  }

  loadTemplate(): void {
    if (!this.form.eventTemplateId) {
      this.error = 'Selecione um modelo para carregar.';
      return;
    }

    this.templateService.get(Number(this.form.eventTemplateId)).subscribe({
      next: template => {
        if (!this.form.eventName.trim()) this.form.eventName = template.name;

        const lines: QuickExitLine[] = [];
        for (const item of template.items) {
          const product = this.products.find(candidate => candidate.id === item.itemId);
          lines.push({
            uid: this.uid++,
            itemId: item.itemId,
            itemName: item.itemName,
            productSearch: item.itemName,
            unitOfMeasure: item.unitOfMeasure,
            quantity: item.suggestedQuantity,
            unitValue: product ? this.suggestedUnitValue(product) : item.averageCost || 0,
            notes: item.notes || ''
          });
        }

        this.lines = lines.length ? lines : [this.newLine()];
        this.success = 'Modelo carregado no carrinho. O estoque ainda não foi baixado.';
        this.error = '';
        this.cdr.markForCheck();
      },
      error: error => this.error = this.apiError(error, 'Não foi possível carregar o modelo.')
    });
  }

  save(): void {
    this.error = '';
    this.success = '';

    const validation = this.validate();
    if (validation) {
      this.error = validation;
      return;
    }

    const payload: QuickExitPayload = {
      eventName: this.form.eventName.trim(),
      eventTemplateId: this.form.eventTemplateId ? Number(this.form.eventTemplateId) : null,
      exitTypeId: Number(this.form.exitTypeId),
      responsibleName: this.form.responsibleName.trim() || null,
      exitDate: this.form.exitDate || null,
      notes: this.form.notes.trim() || null,
      items: this.lines
        .filter(line => line.itemId && this.availableToExit(line) > 0)
        .map(line => ({
          itemId: Number(line.itemId),
          quantity: this.availableToExit(line),
          unitValue: line.unitValue === null ? null : Number(line.unitValue),
          notes: line.notes.trim() || null
        }))
    };

    this.saving = true;
    this.quickExit.create(payload).subscribe({
      next: response => {
        this.success = `Saída salva: ${response.totalDifferentItems || response.totalItems} item(ns), ${response.totalQuantity} unidade(s), total ${this.currency(response.totalValue)}.`;
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
    return {
      NORMAL: 'Normal',
      SALDO_NEGATIVO: 'Saldo negativo',
      PENDENTE_CONTAGEM: 'Pendente de contagem',
      NECESSIDADE_REPOSICAO: 'Reposição necessária',
      VENCENDO: 'Vencendo',
      VENCIDO: 'Vencido'
    }[status];
  }

  availableToExit(line: QuickExitLine): number {
    const product = this.selectedProduct(line);
    if (!product) return 0;
    return Math.min(Number(line.quantity || 0), product.currentQuantity || 0);
  }

  missingQuantity(line: QuickExitLine): number {
    const product = this.selectedProduct(line);
    return Math.max(0, Number(line.quantity || 0) - (product?.currentQuantity || 0));
  }

  lineStatus(line: QuickExitLine): string {
    const product = this.selectedProduct(line);
    if (!product) return 'NAO_CADASTRADO';
    if ((product.currentQuantity || 0) >= Number(line.quantity || 0)) return 'DISPONIVEL';
    if ((product.currentQuantity || 0) > 0) return 'ESTOQUE_INSUFICIENTE';
    return 'PRECISA_COMPRAR';
  }

  statusText(status: string): string {
    return {
      DISPONIVEL: 'Disponível',
      ESTOQUE_INSUFICIENTE: 'Estoque insuficiente',
      PRECISA_COMPRAR: 'Precisa comprar',
      NAO_CADASTRADO: 'Não cadastrado'
    }[status] || status;
  }

  printList(): void {
    window.print();
  }

  private validate(): string {
    if (!this.form.exitTypeId) return 'Selecione o tipo de saída.';

    const filled = this.lines.filter(line => line.itemId || line.itemName);
    if (!filled.length) return 'Adicione pelo menos um item ao carrinho.';

    for (const line of filled) {
      if (!line.quantity || line.quantity <= 0) return 'Todas as quantidades devem ser maiores que zero.';
    }

    if (!this.lines.some(line => line.itemId && this.availableToExit(line) > 0)) {
      return 'Nenhum item cadastrado possui estoque disponível para baixa. Você ainda pode imprimir a lista de compras.';
    }

    return '';
  }

  private load(): void {
    this.loading = true;
    forkJoin({
      products: this.productService.list(),
      exitTypes: this.exitTypeService.list(true),
      templates: this.templateService.list()
    }).pipe(
      timeout(10000),
      finalize(() => {
        this.loading = false;
        this.cdr.markForCheck();
      })
    ).subscribe({
      next: data => {
        this.products = data.products;
        this.exitTypes = data.exitTypes;
        this.templates = data.templates;
      },
      error: error => this.error = this.apiError(error, 'Não foi possível carregar dados da saída rápida.')
    });
  }

  private reloadProducts(): void {
    this.productService.list().subscribe({
      next: products => {
        this.products = products;
        this.cdr.markForCheck();
      }
    });
  }

  private suggestedUnitValue(product: Product): number {
    if (product.averageCost && product.averageCost > 0) return product.averageCost;
    if (product.exitValue && product.exitValue > 0) return product.exitValue;
    if (product.purchaseValue && product.purchaseValue > 0) return product.purchaseValue;
    return 0;
  }

  private newLine(): QuickExitLine {
    return { uid: this.uid++, itemId: null, itemName: '', productSearch: '', unitOfMeasure: null, quantity: 1, unitValue: null, notes: '' };
  }

  private moveToNextEntryLine(currentLine: QuickExitLine): void {
    const blankLine = this.lines.find(line => line.uid !== currentLine.uid && !line.itemId && !line.itemName && !line.productSearch);
    if (blankLine) {
      this.focusSearch(blankLine.uid);
      return;
    }

    const line = this.newLine();
    this.lines = [line, ...this.lines];
    this.focusSearch(line.uid);
  }

  private focusSearch(uid: number): void {
    setTimeout(() => {
      document.getElementById(`quick-exit-search-${uid}`)?.focus();
    }, 0);
  }

  private apiError(error: any, fallback: string): string {
    if (error?.status === 401) return 'Sua sessão expirou. Faça login novamente.';
    const fields = error?.error?.fields ? Object.values(error.error.fields).join(' ') : '';
    return fields || error?.error?.message || fallback;
  }

  private currency(value: number): string {
    return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(value || 0);
  }
}
