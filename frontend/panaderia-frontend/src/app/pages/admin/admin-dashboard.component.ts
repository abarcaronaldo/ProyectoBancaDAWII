import { Component, ChangeDetectorRef, OnInit } from '@angular/core';
import { RouterModule, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';
import { AuthUser } from '../../models/auth-user.model';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css'],
})
export class AdminDashboardComponent implements OnInit {
  usuario: AuthUser | null = null;

  constructor(private authService: AuthService, private router: Router, private cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.usuario = this.authService.getUserFromToken();
    this.cdr.detectChanges();
  }

  irAClientes(): void {
    this.router.navigateByUrl('/admin/clientes');
  }

  irACuentas(): void {
    this.router.navigateByUrl('/admin/cuentas');
  }

  irATransacciones(): void {
    this.router.navigateByUrl('/admin/transacciones');
  }

  irAReporte(): void {
    this.router.navigateByUrl('/admin/reporte');
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}

