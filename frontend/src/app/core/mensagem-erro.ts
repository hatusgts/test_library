import { HttpErrorResponse } from '@angular/common/http';

export function mensagemErro(erro: HttpErrorResponse): string {
  const corpo = erro.error;
  if (corpo?.erros) {
    return Object.values(corpo.erros).join(', ');
  }
  return corpo?.erro ?? 'Não foi possível concluir a operação';
}
