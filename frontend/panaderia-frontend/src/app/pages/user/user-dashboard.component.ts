import { Component, ChangeDetectorRef, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';
import { AuthUser } from '../../models/auth-user.model';
import { ClienteService } from '../../services/cliente.service';
import { CuentaService } from '../../services/cuenta.service';
import { Cliente } from '../../models/cliente.model';
import { Cuenta } from '../../models/cuenta.model';

@Component({
  selector: 'app-user-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './user-dashboard.component.html',
  styleUrls: ['./user-dashboard.component.css'],
})
export class UserDashboardComponent implements OnInit {
  usuario: AuthUser | null = null;
  cliente: Cliente | null = null;
  cuentas: Cuenta[] = [];

  constructor(
    private authService: AuthService,
    private clienteService: ClienteService,
    private cuentaService: CuentaService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.usuario = this.authService.getUserFromToken();
    this.cargarDatosCliente();
  }

  cargarDatosCliente(): void {
  this.clienteService.miPerfil().subscribe({
    next: (cliente) => {
      this.cliente = cliente;
      
      this.cuentaService.porClienteId().subscribe({
        next: (cuentas) => {
          this.cuentas = cuentas;
          this.cdr.detectChanges();
        },
        error: () => this.cdr.detectChanges()
      });

      this.cdr.detectChanges();
    },
    error: () => this.cdr.detectChanges()
  });
}

  irAMiPerfil(): void {
    this.router.navigateByUrl('/cliente/perfil');
  }

  irAMisCuentas(): void {
    this.router.navigateByUrl('/cliente/cuentas');
    this.cdr.detectChanges();
  }

  irATransferencias(): void {
    this.router.navigateByUrl('/cliente/transacciones');
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}

