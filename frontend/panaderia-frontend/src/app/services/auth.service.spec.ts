import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { AuthService } from './auth.service';

describe('AuthService', () => {
  let service: AuthService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    service = TestBed.inject(AuthService);
    localStorage.clear();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should normalize ROLE_ADMIN claims from JWT payload', () => {
    const payload = btoa(JSON.stringify({ sub: 'admin', userId: 1, role: 'ROLE_ADMIN' }));
    localStorage.setItem('token', `header.${payload}.signature`);

    const user = service.getUserFromToken();

    expect(user?.role).toBe('ADMIN');
    expect(service.isAdmin()).toBeTrue();
  });
});
