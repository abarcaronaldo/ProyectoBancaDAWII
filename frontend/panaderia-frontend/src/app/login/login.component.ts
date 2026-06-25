import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../services/auth.service';
import { AuthLogin } from '../models/auth-login.model';
import { Router } from '@angular/router';


@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']

})
export class LoginComponent {

  credentials: AuthLogin = {
    username: '',
    password: ''
  };

  constructor(private authService: AuthService, private router: Router) {}

  login(): void {
    this.authService.login(this.credentials).subscribe({
      next: (response) => {
        const token = this.authService.extractToken(response);
        if (!token) {
          alert('No se recibió un token válido del backend.');
          return;
        }

        this.authService.saveToken(token);

        const user = this.authService.getUserFromToken();
        if (!user) {
          alert('El token recibido no contiene información de usuario válida.');
          return;
        }

        const role = this.authService.normalizeRole(user.role);
        if (role === 'ADMIN') {
          this.router.navigateByUrl('/admin/clientes');
        } else {
          this.router.navigateByUrl('/cliente/perfil');
        }
      },
      error: () => {
        alert('Credenciales incorrectas');
      }
    });
  }
}
