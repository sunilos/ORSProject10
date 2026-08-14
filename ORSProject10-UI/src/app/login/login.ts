import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-login',
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class LoginComponent {
  loginForm: FormGroup;
  loading = false;
  errorMessage = '';
  showPassword = false;

  private readonly cdr = inject(ChangeDetectorRef);

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      loginId: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  onSubmit(): void {
    if (this.loginForm.invalid || this.loading) return;
    this.loading = true;
    this.errorMessage = '';
    const { loginId, password } = this.loginForm.value;
    this.authService.signin(
      { loginId, password },
      (res) => {
        localStorage.setItem('user', JSON.stringify(res.result.data));
        const u: any = JSON.parse(localStorage.getItem('user')!);
        console.log('Login successful:', u.firstName + ' ' + u.lastName + ' (' + u.login + ')');
        this.router.navigate(['/welcome']);
      },
      (res: any) => {
        this.loading = false;
        this.errorMessage = res?.['message'] || 'Login failed. Please check your credentials and try again.';
        this.cdr.markForCheck();
      }
    );
  }
}
