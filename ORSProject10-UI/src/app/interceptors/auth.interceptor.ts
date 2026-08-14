import { HttpInterceptorFn, HttpRequest } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, switchMap, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  if (req.url.includes('/login/') || req.url.includes('/token/refresh/')) {
    return next(req);
  }

  const authService = inject(AuthService);
  const router = inject(Router);

  const addToken = (r: HttpRequest<unknown>, token: string) => {
    const prefix = token.startsWith('eyJ') ? 'Bearer' : 'Token';
    return r.clone({ setHeaders: { Authorization: `${prefix} ${token}` } });
  };

  const token = authService.getToken();
  const authReq = token ? addToken(req, token) : req;

  return next(authReq).pipe(
    catchError((err) => {
      if (err.status === 401 && authService.getRefreshToken()) {
        return authService.refreshToken().pipe(
          switchMap((res) => {
            authService.saveToken(res.access);
            return next(addToken(req, res.access));
          }),
          catchError((refreshErr) => {
            authService.logout();
            router.navigate(['/login']);
            return throwError(() => refreshErr);
          })
        );
      }
      return throwError(() => err);
    })
  );
};
