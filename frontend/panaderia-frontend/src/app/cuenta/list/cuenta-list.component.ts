import { Component, ChangeDetectorRef, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { CuentaService } from '../../services/cuenta.service';
import { Cuenta } from '../../models/cuenta.model';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-cuenta-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './cuenta-list.component.html',
  styleUrls: ['./cuenta-list.component.css']
})
export class CuentaListComponent implements OnInit {
  cuentas: Cuenta[] = [];
  cuentaDetalle: Cuenta | null = null;
  searchTerm = '';
  esVistaCliente = false;

  constructor(
    private cuentaService: CuentaService,
    private authService: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.esVistaCliente = this.router.url.includes('/cliente/cuentas');
    if (this.esVistaCliente) {
      this.cargarCuentaCliente();
    } else {
      this.cargarCuentas();
    }
  }

  get filteredCuentas(): Cuenta[] {
    const term = this.searchTerm.trim().toLowerCase();
    if (!term) {
      return this.cuentas;
    }

    return this.cuentas.filter((cuenta) => cuenta.numeroCuenta?.toLowerCase().includes(term));
  }

  cargarCuentas(): void {
    this.cuentaService.listar().subscribe({
      next: (data) => {
        this.cuentas = data;
        this.cdr.detectChanges();
      },
      error: () => this.cdr.detectChanges()
    });
  }

  cargarCuentaCliente(): void {
    const usuario = this.authService.getUserFromToken();

    this.cuentaService.porClienteId().subscribe({
      next: (cuentas) => {
        this.cuentaDetalle = cuentas[0] ?? null;
        this.cdr.detectChanges();
      },
      error: () => this.cdr.detectChanges()
    });
  }

  alternarEstado(cuenta: Cuenta): void {
    if (!cuenta.id) {
      return;
    }

    this.cuentaService.cambiarEstado(cuenta.id, !(cuenta.activo ?? true)).subscribe({
      next: () => this.cargarCuentas(),
      error: () => this.cdr.detectChanges()
    });
  }
}
