import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { finalize } from 'rxjs';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-team',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './team.component.html',
  styleUrl: './team.component.css'
})
export class TeamComponent {
  name = '';
  email = '';
  password = '';
  saving = false;
  message = '';
  error = '';

  constructor(private readonly auth: AuthService) {}

  save(): void {
    if (!this.name.trim() || !this.email.trim() || this.password.length < 6) {
      this.error = 'Preencha nome, e-mail e uma senha com pelo menos 6 caracteres.';
      return;
    }

    this.saving = true;
    this.error = '';
    this.message = '';
    this.auth.registerUser(this.name.trim(), this.email.trim(), this.password)
      .pipe(finalize(() => this.saving = false))
      .subscribe({
        next: user => {
          this.message = `Usuário ${user.name} criado com sucesso.`;
          this.name = '';
          this.email = '';
          this.password = '';
        },
        error: response => {
          this.error = response.error?.message || 'Não foi possível criar o usuário.';
        }
      });
  }
}
