import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { AuthService } from './auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(AuthService);
  const token = auth.token();
  const requisicao = token ? req.clone({ setHeaders: { Authorization: `Basic ${token}` } }) : req;

  return next(requisicao).pipe(
    catchError((erro) => {
      if (erro.status === 401 && token) {
        auth.logout();
      }
      return throwError(() => erro);
    }),
  );
};
