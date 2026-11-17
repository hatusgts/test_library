import { HttpErrorResponse } from '@angular/common/http';
import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../core/auth.service';
import { mensagemErro } from '../core/mensagem-erro';

@Component({
  selector: 'app-cadastro-page',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './cadastro-page.html',
})
export class CadastroPage {
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);

  protected readonly erro = signal('');
  protected readonly form = inject(FormBuilder).nonNullable.group({
    nome: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    senha: ['', [Validators.required, Validators.minLength(6)]],
  });

  protected cadastrar(): void {
    const { nome, email, senha } = this.form.getRawValue();
    this.auth.cadastrar(nome, email, senha).subscribe({
      next: () => this.router.navigate(['/login']),
      error: (e: HttpErrorResponse) => this.erro.set(mensagemErro(e)),
    });
  }
}
