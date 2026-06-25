import { inject } from '@angular/core';
import { CanActivateFn, Router, ActivatedRouteSnapshot } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const roleGuard: CanActivateFn = (route: ActivatedRouteSnapshot) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const user = authService.getUserFromToken();

  if (!user) {
    router.navigate(['/login']);
    return false;
  }

  const requiredRole = route.data['role'];
  const normalizedUserRole = authService.normalizeRole(user.role);
  const normalizedRequiredRole = authService.normalizeRole(String(requiredRole || ''));

  if (normalizedUserRole !== normalizedRequiredRole) {
    if (normalizedUserRole === 'ADMIN') {
      router.navigate(['/admin/clientes']);
    } else {
      router.navigate(['/cliente/perfil']);
    }
    return false;
  }

  return true;
};
