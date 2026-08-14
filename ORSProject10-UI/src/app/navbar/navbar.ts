import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, NavigationEnd, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Subscription } from 'rxjs';
import { filter } from 'rxjs/operators';
import { AuthService } from '../services/auth.service';
import { ORSAPI } from '../services/orsapi.config';

@Component({
  selector: 'app-navbar',
  imports: [CommonModule, RouterLink],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css'
})
export class NavbarComponent implements OnInit, OnDestroy {
  isLoginPage = false;
  logoError = false;
  private routerSub!: Subscription;

  constructor(private router: Router, private authService: AuthService) { }

  ngOnInit(): void {
    this.checkRoute(this.router.url);
    this.routerSub = this.router.events
      .pipe(filter(e => e instanceof NavigationEnd))
      .subscribe((e: any) => this.checkRoute(e.urlAfterRedirects));
  }

  ngOnDestroy(): void {
    this.routerSub?.unsubscribe();
  }

  private checkRoute(url: string): void {
    this.isLoginPage = url === '/login' || url === '/' || url === '';
  }

  onLogoError(): void {
    this.logoError = true;
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  get userName(): string {
    const userinfo = localStorage.getItem('user');
    if (!userinfo) return 'User';
    try {
      const parsed = JSON.parse(userinfo);
      return parsed?.firstName ? `${parsed.firstName} ${parsed.lastName ?? ''} (  ${parsed.login ?? ''}) `.trim() : userinfo;
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
