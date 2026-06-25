export interface TransaccionResponse {
  id?: number;
  cuentaOrigen?: string;
  cuentaDestino?: string;
  monto: number;
  tipo: 'TRANSFERENCIA' | 'DEPOSITO' | 'RETIRO';
  estado?: 'EXITOSA' | 'PENDIENTE' | 'RECHAZADA';
  fechaTransaccion?: string;
}

export interface TransferenciaRequest {
  cuentaOrigen: string;
  cuentaDestino: string;
  monto: number;
}

export interface OperacionRequest {
  cuenta: string;
  monto: number;
  descripcion?: string;
}
