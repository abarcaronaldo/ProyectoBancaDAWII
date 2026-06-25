import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { OperacionRequest, TransaccionResponse, TransferenciaRequest } from '../models/transaccion.model';

@Injectable({ providedIn: 'root' })
export class TransaccionService {
  private readonly apiUrl = `${environment.apiUrl}/transacciones`;

  constructor(private http: HttpClient) {}

  transferir(request: TransferenciaRequest): Observable<TransaccionResponse> {
    return this.http.post<TransaccionResponse>(`${this.apiUrl}/transferencia`, request);
  }

  depositar(request: OperacionRequest): Observable<TransaccionResponse> {
    return this.http.post<TransaccionResponse>(`${this.apiUrl}/deposito`, request);
  }

  retirar(request: OperacionRequest): Observable<TransaccionResponse> {
    return this.http.post<TransaccionResponse>(`${this.apiUrl}/retiro`, request);
  }
}
