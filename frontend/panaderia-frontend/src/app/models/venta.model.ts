export interface Venta {
  id?: number; 
  fecha: Date,
  cliente: string,
  idProducto?: number;
  idUsuario: number;
  cantidad: number;
  total: number;
}