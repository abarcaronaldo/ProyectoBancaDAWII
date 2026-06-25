import { Component, ChangeDetectorRef, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ClienteService } from '../../services/cliente.service';
import { ClienteRequest } from '../../models/cliente.model';

@Component({
  selector: 'app-cliente-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './cliente-form.component.html',
  styleUrls: ['./cliente-form.component.css']
})
export class ClienteFormComponent implements OnInit {
  cliente: ClienteRequest = {
    dni: '',
    nombre: '',
    apellido: '',
    email: '',
    telefono: '',
    direccion: ''
  };

  clienteId?: number;
  modoLectura = false;

  constructor(
    private clienteService: ClienteService,
    private router: Router,
    private route: ActivatedRoute,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.modoLectura = this.router.url.includes('/cliente/perfil');

    this.route.params.subscribe((params) => {
      if (params['id']) {
        this.clienteId = +params['id'];
        this.cargarCliente(this.clienteId);
      } else if (this.modoLectura) {
        this.cargarPerfil();
      }
    });
  }

  private cargarCliente(id: number): void {
    this.clienteService.obtenerPorId(id).subscribe({
      next: (data) => {
        this.cliente = {
          dni: data.dni,
          nombre: data.nombre,
          apellido: data.apellido,
          email: data.email,
          telefono: data.telefono,
          direccion: data.direccion
        };
        this.cdr.detectChanges();
      },
      error: () => this.cdr.detectChanges()
    });
  }

  private cargarPerfil(): void {
    this.clienteService.miPerfil().subscribe({
      next: (data) => {
        this.cliente = {
          dni: data.dni,
          nombre: data.nombre,
          apellido: data.apellido,
          email: data.email,
          telefono: data.telefono,
          direccion: data.direccion
        };
        this.cdr.detectChanges();
      },
      error: () => this.cdr.detectChanges()
    });
  }

  guardar(): void {
    if (this.clienteId) {
      this.clienteService.actualizar(this.clienteId, this.cliente).subscribe({
        next: () => this.router.navigate(['/admin/clientes']),
        error: () => this.cdr.detectChanges()
      });
    }
  }
}
