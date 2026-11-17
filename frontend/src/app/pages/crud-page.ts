import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { CrudService } from '../core/crud.service';
import { mensagemErro } from '../core/mensagem-erro';
import { Campo, CrudConfig, Registro } from '../core/models';

@Component({
  selector: 'app-crud-page',
  imports: [ReactiveFormsModule],
  templateUrl: './crud-page.html',
})
export class CrudPage implements OnInit {
  private readonly service = inject(CrudService);

  protected readonly config: CrudConfig = inject(ActivatedRoute).snapshot.data['config'];
  protected readonly colunas: Campo[] = this.config.campos.filter((c) => c.tipo !== 'password');
  protected readonly registros = signal<Registro[]>([]);
  protected readonly editandoId = signal<number | null>(null);
  protected readonly erro = signal('');
  protected readonly form = new FormGroup(
    Object.fromEntries(
      this.config.campos.map((c) => [
        c.chave,
        new FormControl<string | number>('', { nonNullable: true, validators: Validators.required }),
      ]),
    ),
  );

  ngOnInit(): void {
    this.carregar();
  }

  protected salvar(): void {
    const id = this.editandoId();
    const dados = this.form.getRawValue();
    const requisicao =
      id === null
        ? this.service.criar(this.config.recurso, dados)
        : this.service.atualizar(this.config.recurso, id, dados);

    requisicao.subscribe({
      next: () => {
        this.cancelar();
        this.carregar();
      },
      error: (e: HttpErrorResponse) => this.erro.set(mensagemErro(e)),
    });
  }

  protected editar(registro: Registro): void {
    this.erro.set('');
    this.editandoId.set(registro.id);
    this.form.reset(
      Object.fromEntries(
        this.config.campos.map((c) => [c.chave, c.tipo === 'password' ? '' : registro[c.chave]]),
      ),
    );
  }

  protected excluir(registro: Registro): void {
    this.service.excluir(this.config.recurso, registro.id).subscribe({
      next: () => this.carregar(),
      error: (e: HttpErrorResponse) => this.erro.set(mensagemErro(e)),
    });
  }

  protected cancelar(): void {
    this.editandoId.set(null);
    this.erro.set('');
    this.form.reset();
  }

  private carregar(): void {
    this.service.listar(this.config.recurso).subscribe({
      next: (lista) => this.registros.set(lista),
      error: (e: HttpErrorResponse) => this.erro.set(mensagemErro(e)),
    });
  }
}
