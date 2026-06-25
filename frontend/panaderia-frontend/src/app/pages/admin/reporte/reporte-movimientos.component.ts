import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TransaccionResponse } from '../../../models/transaccion.model';

@Component({
    selector: 'app-reporte-movimientos',
    standalone: true,
    imports: [CommonModule, FormsModule],
    templateUrl: './reporte-movimientos.component.html',
    styleUrls: ['./reporte-movimientos.component.css']
})
export class ReporteMovimientosComponent implements OnInit {

    fechaInicio: string = '';
    fechaFin: string = '';
    movimientos: TransaccionResponse[] = [];
    private historialBase: TransaccionResponse[] = [
        { id: 1, cuentaOrigen: '001-000123', cuentaDestino: '001-000456', monto: 250, tipo: 'TRANSFERENCIA', estado: 'EXITOSA' },
        { id: 2, cuentaOrigen: '001-000123', cuentaDestino: undefined, monto: 500, tipo: 'DEPOSITO', estado: 'EXITOSA' },
        { id: 3, cuentaOrigen: '001-000789', cuentaDestino: '001-000123', monto: 120, tipo: 'TRANSFERENCIA', estado: 'PENDIENTE' }
    ];

    constructor(private cdr: ChangeDetectorRef) { }

    ngOnInit(): void {
        this.movimientos = [...this.historialBase];
        this.cdr.detectChanges();
    }

    buscar(): void {
        if (!this.fechaInicio || !this.fechaFin) {
            this.movimientos = [...this.historialBase];
            this.cdr.detectChanges();
            return;
        }

        this.movimientos = [...this.historialBase];
        this.cdr.detectChanges();
    }
}
