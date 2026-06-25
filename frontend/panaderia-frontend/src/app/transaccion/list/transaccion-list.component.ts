import { ChangeDetectorRef, Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { TransaccionService } from '../../services/transaccion.service';
import { OperacionRequest } from '../../models/transaccion.model';

@Component({
  selector: 'app-transaccion-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './transaccion-list.component.html',
  styleUrls: ['./transaccion-list.component.css']
})
export class TransaccionListComponent {
  deposito: OperacionRequest = { cuenta: '', monto: 0, descripcion: '' };
  retiro: OperacionRequest = { cuenta: '', monto: 0, descripcion: '' };

  constructor(private transaccionService: TransaccionService, private cdr: ChangeDetectorRef) {}

  registrarDeposito(): void {
    this.transaccionService.depositar(this.deposito).subscribe({
      next: () => {
        this.deposito = { cuenta: '', monto: 0, descripcion: '' };
        this.cdr.detectChanges();
      },
      error: () => this.cdr.detectChanges()
    });
  }

  registrarRetiro(): void {
    this.transaccionService.retirar(this.retiro).subscribe({
      next: () => {
        this.retiro = { cuenta: '', monto: 0, descripcion: '' };
        this.cdr.detectChanges();
      },
      error: () => this.cdr.detectChanges()
    });
  }
}
