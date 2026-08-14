import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-welcome',
  imports: [RouterLink],
  templateUrl: './welcome.html',
  styleUrl: './welcome.css'
})
export class WelcomeComponent {
  constructor(private authService: AuthService, private router: Router) {}

  get userName(): string {
    const raw = localStorage.getItem('user');
    if (!raw) return 'User';
    try {
      const u = JSON.parse(raw);
      return u?.firstName ? `${u.firstName} ${u.lastName ?? ''}`.trim() : raw;
    } catch {
      return raw;
    }
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
