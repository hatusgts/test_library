import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { Registro } from './models';

@Injectable({ providedIn: 'root' })
export class CrudService {
  private readonly http = inject(HttpClient);

  listar(recurso: string): Observable<Registro[]> {
    return this.http.get<Registro[]>(`/api/${recurso}`);
  }

  criar(recurso: string, dados: object): Observable<Registro> {
    return this.http.post<Registro>(`/api/${recurso}`, dados);
  }

  atualizar(recurso: string, id: number, dados: object): Observable<Registro> {
    return this.http.put<Registro>(`/api/${recurso}/${id}`, dados);
  }

  excluir(recurso: string, id: number): Observable<void> {
    return this.http.delete<void>(`/api/${recurso}/${id}`);
  }
}
