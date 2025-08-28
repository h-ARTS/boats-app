import { Routes } from '@angular/router';
import { inject } from '@angular/core';
import { AuthGuard } from './shared/auth/auth.guard';

export const routes: Routes = [
  {
    path: 'login',
    canMatch: [() => inject(AuthGuard).isAuthenticated()],
    loadComponent: () =>
      import('./module/login/page/login.component').then((m) => m.LoginComponent),
  },
  {
    path: 'home',
    canActivate: [() => inject(AuthGuard).canActivate()],
    loadComponent: () => import('./module/home/page/home.page').then((m) => m.HomePage),
  },
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: '**', redirectTo: 'login' },
];
