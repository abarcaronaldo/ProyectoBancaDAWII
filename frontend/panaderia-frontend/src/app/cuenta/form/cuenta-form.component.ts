import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CuentaService } from '../../services/cuenta.service';
import { CuentaCreateRequest } from '../../models/cuenta.model';

@Component({
  selector: 'app-cuenta-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './cuenta-form.component.html',
  styleUrls: ['./cuenta-form.component.css']
})
export class CuentaFormComponent {
  cuenta: CuentaCreateRequest = {
    clienteId: 0,
    tipoCuenta: 'AHORROS'
  };

  constructor(private cuentaService: CuentaService, private router: Router) {}

  guardar(): void {
    this.cuentaService.crear(this.cuenta).subscribe({
      next: () => this.router.navigateByUrl('/admin/cuentas'),
      error: (err) => console.error(err)
    });
  }
}
