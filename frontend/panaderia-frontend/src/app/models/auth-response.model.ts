export interface AuthResponse {
  token: string;
  tokenType: string;
  expiresInMinutes?: number;
}
