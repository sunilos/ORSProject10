import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-change-password',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './change-password.html',
  styleUrl: './change-password.css'
})
export class ChangePasswordComponent {

  form: FormGroup;
  saving = false;
  successMessage = '';
  errorMessage = '';

  private readonly cdr = inject(ChangeDetectorRef);

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.form = this.fb.group({
      oldPassword:     ['', Validators.required],
      newPassword:     ['', [Validators.required, Validators.minLength(4)]],
      confirmPassword: ['', Validators.required]
    });
  }

  onSubmit(): void {
    this.form.markAllAsTouched();
    if (this.form.invalid || this.saving) return;

    const { oldPassword, newPassword, confirmPassword } = this.form.value;
    if (newPassword !== confirmPassword) {
      this.errorMessage = 'New passwords do not match.';
      return;
    }

    this.saving = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.authService.changePassword(
      { oldPassword, newPassword },
      () => {
        this.saving = false;
        this.successMessage = 'Password changed successfully. Redirecting…';
        this.cdr.markForCheck();
        setTimeout(() => this.router.navigate(['/welcome']), 1500);
      },
      (err: any) => {
        this.saving = false;
        this.errorMessage = err?.error?.message || err?.error?.detail || 'Failed to change password.';
        this.cdr.markForCheck();
      }
    );
  }

  goBack(): void {
    this.router.navigate(['/welcome']);
  }
}
