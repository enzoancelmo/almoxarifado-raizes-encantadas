import { Routes } from '@angular/router';
import { LoginComponent } from './features/login/login.component';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import { AlertsComponent } from './features/alerts/alerts.component';
import { ProductFormComponent } from './features/products/product-form.component';
import { ProductListComponent } from './features/products/product-list.component';
import { StockMovementFormComponent } from './features/stock-movements/stock-movement-form.component';
import { StockMovementListComponent } from './features/stock-movements/stock-movement-list.component';
import { ReportsComponent } from './features/reports/reports.component';
import { SmartSuggestionsComponent } from './features/suggestions/smart-suggestions.component';
import { WhatsappMessagesComponent } from './features/whatsapp/whatsapp-messages.component';
import { SupplierListComponent } from './features/suppliers/supplier-list.component';
import { SupplierFormComponent } from './features/suppliers/supplier-form.component';
import { CategoryListComponent } from './features/categories/category-list.component';
import { CategoryFormComponent } from './features/categories/category-form.component';
import { ShellComponent } from './layout/shell.component';
import { authGuard } from './core/auth/auth.guard';
import { TeamComponent } from './features/team/team.component';
import { FinancialValuesComponent } from './features/financial/financial-values.component';
import { ExitTypeListComponent } from './features/exit-types/exit-type-list.component';
import { QuickEntryComponent } from './features/quick-entry/quick-entry.component';
import { QuickExitComponent } from './features/quick-exit/quick-exit.component';
import { EntryTypeListComponent } from './features/entry-types/entry-type-list.component';
import { EventTemplatesComponent } from './features/event-templates/event-templates.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: '', component: ShellComponent, canActivate: [authGuard], children: [
    { path: 'dashboard', component: DashboardComponent },
    { path: 'produtos', component: ProductListComponent },
    { path: 'produtos/novo', component: ProductFormComponent },
    { path: 'produtos/:id/editar', component: ProductFormComponent },
    { path: 'categorias', component: CategoryListComponent },
    { path: 'categorias/nova', component: CategoryFormComponent },
    { path: 'categorias/:id/editar', component: CategoryFormComponent },
    { path: 'fornecedores', component: SupplierListComponent },
    { path: 'fornecedores/novo', component: SupplierFormComponent },
    { path: 'fornecedores/:id/editar', component: SupplierFormComponent },
    { path: 'movimentacoes', component: StockMovementListComponent },
    { path: 'entrada-rapida', component: QuickEntryComponent },
    { path: 'saida-rapida', component: QuickExitComponent },
    { path: 'modelos-evento', component: EventTemplatesComponent },
    { path: 'movimentacoes/nova', component: StockMovementFormComponent },
    { path: 'alertas', component: AlertsComponent },
    { path: 'relatorios', component: ReportsComponent },
    { path: 'valores', component: FinancialValuesComponent },
    { path: 'tipos-saida', component: ExitTypeListComponent },
    { path: 'tipos-entrada', component: EntryTypeListComponent },
    { path: 'sugestoes', component: SmartSuggestionsComponent },
    { path: 'whatsapp', component: WhatsappMessagesComponent },
    { path: 'equipe', component: TeamComponent },
    { path: '', pathMatch: 'full', redirectTo: 'dashboard' }
  ]},
  { path: '**', redirectTo: '' }
];

