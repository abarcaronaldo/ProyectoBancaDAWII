import { Component, ChangeDetectorRef, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { ClienteService } from '../../services/cliente.service';
import { Cliente } from '../../models/cliente.model';

@Component({
  selector: 'app-cliente-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './cliente-list.component.html',
  styleUrls: ['./cliente-list.component.css']
})
export class ClienteListComponent implements OnInit {
  clientes: Cliente[] = [];
  searchTerm = '';

  constructor(
    private clienteService: ClienteService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.cargarClientes();
  }

  get filteredClientes(): Cliente[] {
    const term = this.searchTerm.trim().toLowerCase();
    if (!term) {
      return this.clientes;
    }

    return this.clientes.filter((cliente) => {
      const nombreCompleto = `${cliente.nombre ?? ''} ${cliente.apellido ?? ''}`.toLowerCase();
      return nombreCompleto.includes(term) || cliente.dni?.toLowerCase().includes(term);
    });
  }

  cargarClientes(): void {
    this.clienteService.listar().subscribe({
      next: (data) => {
        this.clientes = data;
        this.cdr.detectChanges();
      },
      error: () => this.cdr.detectChanges()
    });
  }

  editar(id?: number): void {
    this.router.navigateByUrl(`/admin/clientes-form/${id}`);
  }

  desactivar(id?: number): void {
    if (!id) {
      return;
    }

    this.clienteService.eliminar(id).subscribe(() => this.cargarClientes());
  }

  nuevo(): void {
    this.router.navigateByUrl('/admin/clientes-form');
  }
}
