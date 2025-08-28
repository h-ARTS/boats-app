import { HttpErrorResponse, HttpInterceptor, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';

const SKIP = ['api/auth/login'];

export const authIntercepterFn: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  const token = localStorage.getItem('auth_token');
  const needsAuth = !SKIP.some((u) => req.url.includes(u));

  if (token && needsAuth) {
    req = req.clone({
      headers: req.headers.set('Authorization', 'Bearer ' + token),
    });
  }

  return next(req).pipe(
    catchError((err: HttpErrorResponse) => {
      if (err.status === 401) {
        localStorage.removeItem('auth_token');
        router.navigate(['/login']);
      }

      return throwError(() => err);
    })
  );
};
