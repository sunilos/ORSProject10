import { ChangeDetectorRef, Component, inject, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { FacultyService, Faculty } from '../services/faculty.service';
import { BaseComponent } from '../base/base.component';
import { ORSAPI } from '../services/orsapi.config';

@Component({
  selector: 'app-faculty',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './faculty.html',
  styleUrl: './faculty.css'
})
export class FacultyComponent extends BaseComponent implements OnDestroy {

  protected override listUrl = '/faculty';
  override get title(): string { return this.isEditMode ? 'Edit Faculty' : 'Add Faculty'; }

  readonly genderOptions = ['Male', 'Female', 'Other'];

  photoPreview: string | null = null;
  photoUploading = false;
  private readonly photoCdr = inject(ChangeDetectorRef);

  constructor(
    private fb: FormBuilder,
    private facultyService: FacultyService,
    router: Router,
    route: ActivatedRoute
  ) {
    super(router, route);
    this.form = this.buildForm();
  }

  protected override buildForm(): FormGroup {
    return this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      mobileNo: ['', [Validators.required, Validators.pattern(/^[6-9]\d{9}$/)]],
      address: [''],
      gender: [''],
      dob: [''],
      collegeId: ['', Validators.required],
      collegeName: [''],
      courseId: ['', Validators.required],
      courseName: [''],
      subjectId: ['', Validators.required],
      subjectName: [''],
      photo: [''],
      imageId: ['']
    });
  }


  protected override populateForm(f: any): void {
    this.form.patchValue({
      firstName: f.firstName, lastName: f.lastName, email: f.email,
      mobileNo: f.mobileNo, address: f.address ?? '',
      gender: f.gender ?? '', dob: this.toDateInputValue(f.dob),
      collegeId: f.collegeId, collegeName: f.collegeName,
      courseId: f.courseId, courseName: f.courseName,
      subjectId: f.subjectId, subjectName: f.subjectName,
      photo: f.photo ?? '', imageId: f.imageId ?? ''
    });
    this.revokePhotoPreview();
    this.photoPreview = null;
    if (f.imageId) {
      this.loadPhoto();
    }
  }

  private loadPhoto(): void {
    const imageId = this.form.get('imageId')?.value;
    if (!imageId) return;
    this.revokePhotoPreview();
    this.photoPreview = `${ORSAPI.GET_DOC_API}/${imageId}`;
    this.photoCdr.markForCheck();
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
    this.photoCdr.markForCheck();
    this.facultyService.uploadPhoto(this.entityId, file,
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
        this.photoCdr.markForCheck();
      },
      () => {
        this.photoUploading = false;
        this.photoCdr.markForCheck();
      }
    );
  }

  ngOnDestroy(): void {
    this.revokePhotoPreview();
  }

  private toDateInputValue(dob: unknown): string {
    if (!dob) return '';
    const d = new Date(dob as string | number);
    if (isNaN(d.getTime())) return '';
    const yyyy = d.getFullYear();
    const mm = String(d.getMonth() + 1).padStart(2, '0');
    const dd = String(d.getDate()).padStart(2, '0');
    return `${yyyy}-${mm}-${dd}`;
  }

  onCollegeChange(event: Event): void {
    const key = (event.target as HTMLSelectElement).value;
    const college = this.preloadData?.collegeList?.find((c: any) => c.key === key);
    this.form.patchValue({ collegeName: college?.value ?? '' });
  }

  onCourseChange(event: Event): void {
    const key = (event.target as HTMLSelectElement).value;
    const course = this.preloadData?.courseList?.find((c: any) => c.key === key);
    this.form.patchValue({ courseName: course?.value ?? '' });
  }

  onSubjectChange(event: Event): void {
    const key = (event.target as HTMLSelectElement).value;
    const subject = this.preloadData?.subjectList?.find((s: any) => s.key === key);
    this.form.patchValue({ subjectName: subject?.value ?? '' });
  }

  protected override getBody(): Faculty {
    const v = this.form.value;
    return { id: this.entityId ?? 0, ...v, dob: v.dob ? new Date(`${v.dob}T00:00:00`).getTime() : null };
  }
  protected override getService() { return this.facultyService; }
}
