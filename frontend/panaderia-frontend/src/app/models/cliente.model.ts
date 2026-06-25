export interface Cliente {
  id?: number;
  dni: string;
  nombre: string;
  apellido: string;
  email: string;
  telefono: string;
  direccion: string;
  activo?: boolean;
  creadoEn?: string;
}

export interface ClienteRequest {
  dni: string;
  nombre: string;
  apellido: string;
  email: string;
  telefono: string;
  direccion: string;
}
