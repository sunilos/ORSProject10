import { Component, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { UserService, User } from '../services/user.service';
import { BaseComponent } from '../base/base.component';
import { ORSAPI } from '../services/orsapi.config';

@Component({
  selector: 'app-user',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './user.html',
  styleUrl: './user.css'
})
export class UserComponent extends BaseComponent implements OnDestroy {

  protected override listUrl = '/users';
  override get title(): string { return this.isEditMode ? 'Edit User' : 'Add User'; }

  get roleOptions(): { id: string; name: string }[] {
    const roleList = this.preloadData?.['roleList'] ?? {};
    return Object.entries(roleList).map(([id, name]) => ({ id, name: name as string }));
  }

  photoPreview: string | null = null;
  photoUploading = false;

  constructor(private userService: UserService) {
    super();
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
      photo: [''],
      imageId: ['']
    });
  }

  protected override populateForm(u: any): void {
    this.form.patchValue({
      firstName: u.firstName,
      lastName: u.lastName,
      loginId: u.loginId,
      password: u.password ?? '',
      dob: this.toDateInputValue(u.dob),
      roleId: u.roleId,
      phone: u.phone,
      gender: u.gender ?? 'M',
      photo: u.photo ?? '',
      imageId: u.imageId ?? ''
    });
    this.revokePhotoPreview();
    this.photoPreview = null;
    if (u.imageId) {
      this.loadPhoto();
    }
  }

  private toDateInputValue(dob: unknown): string {
    if (!dob) return '';
    const date = new Date(dob as string | number);
    if (isNaN(date.getTime())) return '';
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  }

  private loadPhoto(): void {
    const imageId = this.form.get('imageId')?.value;
    if (!imageId) return;
    this.revokePhotoPreview();
    this.photoPreview = `${ORSAPI.GET_DOC_API}/${imageId}`;
    this.cdr.markForCheck();
  }

  private revokePhotoPreview(): void {
    if (this.photoPreview?.startsWith('blob:')) {
      URL.revokeObjectURL(this.photoPreview);
    }
  }

  onPhotoSelected(event: Event): void {
    const file = (event.target as HTMLInputElement).files?.[0];
    if (!file || !this.entityId) return;
    this.revokePhotoPreview();
    this.photoPreview = URL.createObjectURL(file);
    this.photoUploading = true;
    this.cdr.markForCheck();
    this.userService.uploadPhoto(this.entityId, file,
      (res: any) => {
        this.photoUploading = false;
        const filename = res?.photo ?? res?.filename ?? '';
        if (filename) {
          this.form.patchValue({ photo: filename });
        }
        const imageId = res?.imageId ?? '';
        if (imageId) {
          this.form.patchValue({ imageId });
          this.loadPhoto();
        }
        this.cdr.markForCheck();
      },
      () => {
        this.photoUploading = false;
        this.cdr.markForCheck();
      }
    );
  }

  ngOnDestroy(): void {
    this.revokePhotoPreview();
  }

  protected override getBody(): User {
    const v = this.form.value;
    const body: User = {
      id: this.entityId ?? 0,
      firstName: v.firstName,
      lastName: v.lastName,
      loginId: v.loginId,
      dob: v.dob ? new Date(`${v.dob}T00:00:00`).getTime() : null,
      roleId: v.roleId,
      phone: v.phone,
      gender: v.gender || 'M',
      photo: v.photo || '',
      imageId: v.imageId || ''
    };
    if (v.password) body['password'] = v.password;
    return body;
  }

  protected override getService() {
    return this.userService;
  }
}
