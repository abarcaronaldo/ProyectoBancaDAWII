export interface Cuenta {
  id?: number;
  numeroCuenta: string;
  cci?: string;
  saldo: number;
  tipoCuenta: string;
  clienteId: number;
  activo?: boolean;
  fechaCreacion?: string;
}

export interface CuentaCreateRequest {
  clienteId: number;
  tipoCuenta: string;
}
