import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-forget-password',
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './forget-password.html',
  styleUrl: './forget-password.css'
})
export class ForgetPasswordComponent {

  form: FormGroup;
  loading = false;
  successMessage = '';
  errorMessage = '';

  private readonly cdr = inject(ChangeDetectorRef);

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.form = this.fb.group({
      login: ['', [Validators.required, Validators.email]]
    });
  }

  onSubmit(): void {
    this.form.markAllAsTouched();
    if (this.form.invalid || this.loading) return;

    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.authService.forgotPassword(
      this.form.value.login,
      () => {
        this.loading = false;
        this.successMessage = 'Password reset email sent. Please check your inbox.';
        this.form.reset();
        this.cdr.markForCheck();
      },
      (err: any) => {
        this.loading = false;
        this.errorMessage = err?.error?.message || err?.error?.detail || 'Failed to send reset email. Please try again.';
        this.cdr.markForCheck();
      }
    );
  }
}
