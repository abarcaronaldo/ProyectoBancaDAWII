import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Venta } from '../models/venta.model';
import { VentaDetalle } from '../models/ventadetalle.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class VentaService {

  private apiUrl = `${environment.apiUrl}/ventas`;

  constructor(private http: HttpClient) { }

  //listar ventas
  listar(): Observable<VentaDetalle[]> {
    return this.http.get<VentaDetalle[]>(this.apiUrl);
  }

  //crear venta
  crear(venta: Venta): Observable<Venta> {
    return this.http.post<Venta>(this.apiUrl, venta);
  }

  //actualizar venta
  actualizar(id: number, venta: Venta): Observable<Venta> {
    return this.http.put<Venta>(`${this.apiUrl}/${id}`, venta);
  }

  //eliminar venta
  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  //reporte movimientos
  reporte(inicio: string, fin: string): Observable<import('../models/movimiento.model').Movimiento[]> {
    return this.http.get<import('../models/movimiento.model').Movimiento[]>(`${this.apiUrl}/reporte?inicio=${inicio}&fin=${fin}`);
  }
}
