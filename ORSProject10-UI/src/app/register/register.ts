import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-register',
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './register.html',
  styleUrl: './register.css'
})
export class RegisterComponent {

  form: FormGroup;
  loading = false;
  showPassword = false;
  successMessage = '';
  errorMessage = '';
  errors: Record<string, string> = {};

  private readonly cdr = inject(ChangeDetectorRef);

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.form = this.fb.group({
      firstName:    ['', Validators.required],
      lastName:     ['', Validators.required],
      login:        ['', [Validators.required, Validators.email]],
      password:     ['', Validators.required],
      dob:          [''],
      mobileNumber: ['', Validators.required],
      gender:       ['Male']
    });
  }

  onSubmit(): void {
    this.form.markAllAsTouched();
    if (this.form.invalid || this.loading) return;

    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';
    this.errors = {};

    const v = this.form.value;
    this.authService.register(
      {
        firstName:    v.firstName,
        lastName:     v.lastName,
        login:        v.login,
        password:     v.password,
        dob:          v.dob || null,
        mobileNumber: v.mobileNumber,
        gender:       v.gender
      },
      () => {
        this.loading = false;
        this.successMessage = 'Registration successful! Redirecting to login…';
        this.cdr.markForCheck();
        setTimeout(() => this.router.navigate(['/login']), 1500);
      },
      (err: any) => {
        this.loading = false;
        this.errorMessage = err?.error?.message || err?.error?.detail || 'Registration failed. Please try again.';
        this.errors = err?.error?.errors ?? {};
        this.cdr.markForCheck();
      }
    );
  }
}
