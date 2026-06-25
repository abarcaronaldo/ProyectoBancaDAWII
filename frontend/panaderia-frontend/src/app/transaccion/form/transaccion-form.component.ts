import { Component, ChangeDetectorRef, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { TransaccionService } from '../../services/transaccion.service';
import { OperacionRequest, TransferenciaRequest } from '../../models/transaccion.model';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-transaccion-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './transaccion-form.component.html',
  styleUrls: ['./transaccion-form.component.css']
})
export class TransaccionFormComponent implements OnInit {
  tipoOperacion: 'TRANSFERENCIA' | 'DEPOSITO' | 'RETIRO' = 'TRANSFERENCIA';
  transferencia: TransferenciaRequest = { cuentaOrigen: '', cuentaDestino: '', monto: 0 };
  operacion: OperacionRequest = { cuenta: '', monto: 0 };
  monto = 0;
  rolUsuario = '';
  deposito: OperacionRequest = { cuenta: '', monto: 0, descripcion: '' };
  retiro: OperacionRequest = { cuenta: '', monto: 0, descripcion: '' };

  constructor(
    private transaccionService: TransaccionService,
    private authService: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.rolUsuario = this.authService.normalizeRole(this.authService.getUserFromToken()?.role);
    this.tipoOperacion = this.rolUsuario === 'CLIENTE' ? 'TRANSFERENCIA' : 'DEPOSITO';
    this.cdr.detectChanges();
  }

  puedeMostrarOperacion(operacion: 'TRANSFERENCIA' | 'DEPOSITO' | 'RETIRO'): boolean {
    if (this.rolUsuario === 'CLIENTE') {
      return operacion === 'TRANSFERENCIA';
    }

    return operacion === 'DEPOSITO' || operacion === 'RETIRO';
  }

  enviar(): void {
    this.transferencia.monto = this.monto;
    this.operacion.monto = this.monto;

    if (this.tipoOperacion === 'TRANSFERENCIA') {
      this.transaccionService.transferir(this.transferencia).subscribe({
        next: () => {
          this.router.navigateByUrl('/cliente/cuentas');
          this.cdr.detectChanges();
        },
        error: () => this.cdr.detectChanges()
      });
    } else if (this.tipoOperacion === 'DEPOSITO') {
      this.transaccionService.depositar(this.deposito).subscribe({
        next: () => {
          this.router.navigateByUrl('/admin/cuentas');
          this.cdr.detectChanges();
        },
        error: () => this.cdr.detectChanges()
      });
    } else {
      this.transaccionService.retirar(this.retiro).subscribe({
        next: () => {
          this.router.navigateByUrl('/admin/cuentas');
          this.cdr.detectChanges();
        },
        error: () => this.cdr.detectChanges()
      });
    }
  }
}
