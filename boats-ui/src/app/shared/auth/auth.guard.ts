import { inject, Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from './auth.service';
import { HttpClient } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
  http = inject(HttpClient);
  auth = inject(AuthService);
  router = inject(Router);

  canActivate() {
    if (this.auth.isLoggedIn()) return true;
    this.router.navigate(['/login']);
    return false;
  }

  isAuthenticated() {
    if (this.auth.isLoggedIn()) {
      this.router.navigate(['/home']);
      return false;
    }
    return true;
  }
}
