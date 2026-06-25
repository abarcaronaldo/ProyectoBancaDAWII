import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { AuthLogin } from '../models/auth-login.model';
import { AuthResponse } from '../models/auth-response.model';
import { AuthUser } from '../models/auth-user.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly apiUrl = `${environment.apiUrl}/auth`;

  constructor(private http: HttpClient) {}

  login(credentials: AuthLogin): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, credentials);
  }

  saveToken(token: string): void {
    localStorage.setItem('token', token);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  extractToken(response: Partial<AuthResponse> | any): string | null {
    const token = response?.token ?? response?.accessToken ?? response?.jwt ?? response?.data?.token;
    return typeof token === 'string' && token.trim() ? token : null;
  }

  normalizeRole(role?: string | null): string {
    if (!role) {
      return '';
    }

    const normalized = String(role).trim().toUpperCase();
    return normalized.startsWith('ROLE_') ? normalized.replace(/^ROLE_/, '') : normalized;
  }

  getUserFromToken(): AuthUser | null {
    const token = this.getToken();
    if (!token) {
      return null;
    }

    try {
      const payload = this.decodeJwtPayload(token);
      return {
        id: Number(payload.userId ?? payload.id ?? 0),
        email: payload.email ?? payload.sub ?? payload.username ?? '',
        role: this.normalizeRole(payload.role ?? payload.roles?.[0] ?? payload.authorities?.[0])
      };
    } catch {
      return null;
    }
  }

  isAdmin(): boolean {
    const user = this.getUserFromToken();
    return this.normalizeRole(user?.role) === 'ADMIN';
  }

  logout(): void {
    localStorage.removeItem('token');
  }

  private decodeJwtPayload(token: string): any {
    const parts = token.split('.');
    if (parts.length < 2) {
      throw new Error('Token JWT inválido');
    }

    const base64Url = parts[1].replace(/-/g, '+').replace(/_/g, '/');
    const base64 = base64Url.padEnd(base64Url.length + ((4 - (base64Url.length % 4)) % 4), '=');
    const decoded = atob(base64);
    return JSON.parse(decoded);
  }
}
