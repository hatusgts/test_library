import { HttpClient } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { tap } from 'rxjs';

interface Sessao {
  nome: string;
  token: string;
}

const CHAVE = 'livraria-sessao';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly router = inject(Router);
  private readonly sessao = signal<Sessao | null>(this.carregar());

  readonly logado = computed(() => this.sessao() !== null);
  readonly nome = computed(() => this.sessao()?.nome ?? '');

  token(): string | null {
    return this.sessao()?.token ?? null;
  }

  login(email: string, senha: string) {
    return this.http.post<{ nome: string }>('/api/auth/login', { email, senha }).pipe(
      tap((usuario) => {
        const nova = { nome: usuario.nome, token: btoa(`${email}:${senha}`) };
        sessionStorage.setItem(CHAVE, JSON.stringify(nova));
        this.sessao.set(nova);
      }),
    );
  }

  cadastrar(nome: string, email: string, senha: string) {
    return this.http.post('/api/usuarios', { nome, email, senha });
  }

  logout(): void {
    sessionStorage.removeItem(CHAVE);
    this.sessao.set(null);
    this.router.navigate(['/login']);
  }

  private carregar(): Sessao | null {
    const valor = sessionStorage.getItem(CHAVE);
    return valor ? JSON.parse(valor) : null;
  }
}
