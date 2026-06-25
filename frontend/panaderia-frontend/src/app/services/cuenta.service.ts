import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Cuenta, CuentaCreateRequest } from '../models/cuenta.model';

@Injectable({ providedIn: 'root' })
export class CuentaService {
  private readonly apiUrl = `${environment.apiUrl}/cuentas`;

  constructor(private http: HttpClient) {}

  listar(): Observable<Cuenta[]> {
    return this.http.get<Cuenta[]>(this.apiUrl);
  }

  crear(cuenta: CuentaCreateRequest): Observable<Cuenta> {
    return this.http.post<Cuenta>(this.apiUrl, cuenta);
  }

  porClienteId(): Observable<Cuenta[]> {
  return this.http.get<Cuenta[]>(`${this.apiUrl}/cliente`);
}

  porNumero(numeroCuenta: string): Observable<Cuenta> {
    return this.http.get<Cuenta>(`${this.apiUrl}/numero/${encodeURIComponent(numeroCuenta)}`);
  }

  cambiarEstado(id: number, activo: boolean): Observable<Cuenta> {
    return this.http.patch<Cuenta>(`${this.apiUrl}/${id}/estado?activo=${activo}`, {});
  }

  actualizarSaldo(id: number, monto: number): Observable<Cuenta> {
    return this.http.put<Cuenta>(`${this.apiUrl}/${id}/saldo?monto=${monto}`, {});
  }
}
