import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { AdminDashboardComponent } from './pages/admin/admin-dashboard.component';
import { UserDashboardComponent } from './pages/user/user-dashboard.component';
import { authGuard } from './guards/auth.guard';
import { roleGuard } from './guards/role.guard';
import { ClienteListComponent } from './cliente/list/cliente-list.component';
import { ClienteFormComponent } from './cliente/form/cliente-form.component';
import { CuentaListComponent } from './cuenta/list/cuenta-list.component';
import { CuentaFormComponent } from './cuenta/form/cuenta-form.component';
import { TransaccionListComponent } from './transaccion/list/transaccion-list.component';
import { TransaccionFormComponent } from './transaccion/form/transaccion-form.component';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },

  {
    path: 'admin',
    component: AdminDashboardComponent,
    canActivate: [authGuard, roleGuard],
    data: { role: 'ADMIN' },
    children: [
      { path: 'clientes', component: ClienteListComponent, runGuardsAndResolvers: 'always' },
      { path: 'clientes-form', component: ClienteFormComponent, runGuardsAndResolvers: 'always' },
      { path: 'clientes-form/:id', component: ClienteFormComponent, runGuardsAndResolvers: 'always' },
      { path: 'cuentas', component: CuentaListComponent, runGuardsAndResolvers: 'always' },
      { path: 'cuentas-form', component: CuentaFormComponent, runGuardsAndResolvers: 'always' },
      { path: 'transacciones', component: TransaccionListComponent, runGuardsAndResolvers: 'always' },
      { path: 'reporte', loadComponent: () => import('./pages/admin/reporte/reporte-movimientos.component').then(m => m.ReporteMovimientosComponent), runGuardsAndResolvers: 'always' }
    ]
  },

  {
    path: 'cliente',
    component: UserDashboardComponent,
    canActivate: [authGuard, roleGuard],
    data: { role: 'CLIENTE' },
    children: [
      { path: 'perfil', component: ClienteFormComponent, runGuardsAndResolvers: 'always' },
      { path: 'cuentas', component: CuentaListComponent, runGuardsAndResolvers: 'always' },
      { path: 'transacciones', component: TransaccionFormComponent, runGuardsAndResolvers: 'always' }
    ]
  },

  { path: '**', redirectTo: 'login' }
];
