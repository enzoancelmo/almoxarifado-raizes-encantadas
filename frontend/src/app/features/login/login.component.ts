import { CommonModule } from '@angular/common';
import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { finalize, timeout } from 'rxjs';

import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  email = '';
  password = '';

  readonly loading = signal(false);
  readonly error = signal('');

  constructor(
    private readonly auth: AuthService,
    private readonly router: Router,
    route: ActivatedRoute
  ) {
    if (route.snapshot.queryParamMap.get('sessionExpired')) {
      this.error.set('Sessão expirada. Faça login novamente.');
    }
  }

  enter(): void {
    const email = this.email.trim();
    const password = this.password.trim();

    if (!email || !password) {
      this.error.set('Informe e-mail e senha para entrar.');
      return;
    }

    this.loading.set(true);
    this.error.set('');

    this.auth.login(email, password)
      .pipe(timeout(8000), finalize(() => this.loading.set(false)))
      .subscribe({
        next: () => void this.router.navigate(['/dashboard']),
        error: error => this.error.set(this.loginErrorMessage(error))
      });
  }

  private loginErrorMessage(error: any): string {
    if (error?.status === 0) {
      return 'Não foi possível conectar ao servidor. Confira a liberação do frontend no CORS do backend.';
    }

    if (error?.name === 'TimeoutError') {
      return 'O servidor demorou para responder. Tente novamente em alguns segundos.';
    }

    return error?.error?.message || 'E-mail ou senha inválidos.';
  }
}
