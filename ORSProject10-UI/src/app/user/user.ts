import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService, User } from '../services/user.service';
import { BaseComponent } from '../base/base.component';
import { ORSAPI } from '../services/orsapi.config';

@Component({
  selector: 'app-user',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './user.html',
  styleUrl: './user.css'
})
export class UserComponent extends BaseComponent {

  protected override listUrl = '/users';
  override get title(): string { return this.isEditMode ? 'Edit User' : 'Add User'; }

  readonly baseUrl = ORSAPI.baseUrl;

  get roleOptions(): { id: string; name: string }[] {
    const roleList = this.preloadData?.['roleList'] ?? {};
    return Object.entries(roleList).map(([id, name]) => ({ id, name: name as string }));
  }

  photoPreview: string | null = null;
  photoUploading = false;
  private readonly cdr2 = inject(ChangeDetectorRef);

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    router: Router,
    route: ActivatedRoute
  ) {
    super(router, route);
    this.form = this.buildForm();
  }

  override ngOnInit(): void {
    super.ngOnInit();
    if (!this.isEditMode) {
      this.form.get('password')?.setValidators(Validators.required);
      this.form.get('password')?.updateValueAndValidity();
    }
  }

  protected override buildForm(): FormGroup {
    return this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      loginId: ['', [Validators.required, Validators.email]],
      password: [''],
      dob: [''],
      roleId: ['', Validators.required],
      phone: ['', Validators.required],
      gender: ['M'],
      photo: ['']
    });
  }

  protected override populateForm(u: any): void {
    this.form.patchValue({
      firstName: u.firstName,
      lastName: u.lastName,
      loginId: u.loginId,
      dob: u.dob ?? '',
      roleId: u.roleId,
      phone: u.phone,
      gender: u.gender ?? 'M',
      photo: u.photo ?? ''
    });
    this.photoPreview = u.photo ? `${this.baseUrl}/media/${u.photo}` : null;
  }

  onPhotoSelected(event: Event): void {
    const file = (event.target as HTMLInputElement).files?.[0];
    if (!file || !this.entityId) return;
    this.photoPreview = URL.createObjectURL(file);
    this.photoUploading = true;
    this.cdr2.markForCheck();
    this.userService.uploadPhoto(this.entityId, file,
      (res: any) => {
        this.photoUploading = false;
        const filename = res?.photo ?? res?.filename ?? '';
        if (filename) {
          this.form.patchValue({ photo: filename });
          this.photoPreview = `${this.baseUrl}/media/${filename}`;
        }
        this.cdr2.markForCheck();
      },
      () => {
        this.photoUploading = false;
        this.cdr2.markForCheck();
      }
    );
  }

  protected override getBody(): User {
    const v = this.form.value;
    const body: User = {
      id: this.entityId ?? 0,
      firstName: v.firstName,
      lastName: v.lastName,
      loginId: v.loginId,
      dob: v.dob || null,
      roleId: v.roleId,
      phone: v.phone,
      gender: v.gender || 'M',
      photo: v.photo || ''
    };
    if (v.password) body['password'] = v.password;
    return body;
  }

  protected override getService() {
    return this.userService;
  }
}
