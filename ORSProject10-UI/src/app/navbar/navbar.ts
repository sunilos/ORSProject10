import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../services/auth.service';
import { ORSAPI } from '../services/orsapi.config';

@Component({
  selector: 'app-navbar',
  imports: [CommonModule, RouterLink],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css'
})
export class NavbarComponent {
  logoError = false;

  constructor(private router: Router, private authService: AuthService) { }

  get isLoggedIn(): boolean {
    return this.authService.isLoggedIn();
  }

  onLogoError(): void {
    this.logoError = true;
  }

  logout(): void {
    localStorage.clear();
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  get userName(): string {
    const userinfo = localStorage.getItem('user');
    if (!userinfo) return 'User';
    try {
      const parsed = JSON.parse(userinfo);
      return parsed?.firstName ? `${parsed.firstName} ${parsed.lastName ?? ''} (${parsed.roleName ?? ''})`.trim() : userinfo;
    } catch {
      return userinfo;
    }
  }

  get userPhoto(): string {
    const userinfo = localStorage.getItem('user');
    if (!userinfo) return '';
    try {
      const parsed = JSON.parse(userinfo);
      if (!parsed?.photo) return '';
      return `${ORSAPI.baseUrl}/media/${parsed.photo}`;
    } catch {
      return '';
    }
  }

}
