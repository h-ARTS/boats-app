import { Routes } from '@angular/router';
import { inject } from '@angular/core';
import { AuthGuard } from './shared/auth/auth.guard';
import { boatResolver } from './module/home/boats/boat-detail/boat-detail.resolver';

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
  {
    path: 'boats/add',
    canActivate: [() => inject(AuthGuard).canActivate()],
    loadComponent: () =>
      import('./module/home/boats/boat-form/boat-form.component').then((m) => m.BoatFormComponent),
  },
  {
    path: 'boats/edit/:id',
    canActivate: [() => inject(AuthGuard).canActivate()],
    loadComponent: () =>
      import('./module/home/boats/boat-form/boat-form.component').then((m) => m.BoatFormComponent),
  },
  {
    path: 'boats/detail/:id',
    canActivate: [() => inject(AuthGuard).canActivate()],
    resolve: {
      boat: boatResolver,
    },
    loadComponent: () =>
      import('./module/home/boats/boat-detail/boat-detail.component').then(
        (m) => m.BoatDetailComponent
      ),
  },
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: '**', redirectTo: 'login' },
];
