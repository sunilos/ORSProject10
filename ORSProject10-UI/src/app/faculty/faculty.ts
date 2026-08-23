import { Component, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
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
  override get title(): string {
    return this.isEditMode ? 'Edit Faculty' : 'Add Faculty';
  }

  readonly genderOptions = ['Male', 'Female', 'Other'];

  photoPreview: string | null = null;
  photoUploading = false;

  /**
   * @param facultyService Service used for faculty CRUD and photo upload operations.
   */
  constructor(private facultyService: FacultyService) {
    super();
  }

  /**
   * Builds the reactive form group for a faculty record, including validators
   * for the required personal, college, course, and subject fields.
   */
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


  /**
   * Patches the form with an existing faculty record and refreshes the photo preview.
   * @param f Faculty data from router state or the `getById` API response.
   */
  protected override populateForm(f: any): void {
    this.form.patchValue({
      firstName: f.firstName, lastName: f.lastName, email: f.email,
      mobileNo: f.mobileNo, address: f.address ?? '',
      gender: f.gender ?? '', dob: this.toDateInputValue(f.dob),
      collegeId: f.collegeId, collegeName: f.collegeName,
      courseId: f.courseId, courseName: f.courseName,
      subjectId: f.subjectId, subjectName: f.subjectName,
      photo: f.photo ?? '',
      imageId: f.imageId ?? ''
    });
    this.revokePhotoPreview();
    this.photoPreview = null;
    if (f.imageId) {
      this.loadPhoto();
    }
  }

  /**
   * Sets `photoPreview` to the server URL for the form's current `imageId`.
   * No-op if `imageId` is not set.
   */
  private loadPhoto(): void {
    const imageId = this.form.get('imageId')?.value;
    if (!imageId) return;
    this.revokePhotoPreview();
    //make image URL to view  
    this.photoPreview = `${ORSAPI.GET_DOC_API}/${imageId}`;
    this.cdr.markForCheck();
  }

  /**
   * Releases the current `photoPreview` blob URL, if any, to avoid memory leaks.
   */
  private revokePhotoPreview(): void {
    //remove file view
    if (this.photoPreview?.startsWith('blob:')) {
      URL.revokeObjectURL(this.photoPreview);
    }
  }

  /**
   * Handles the photo `<input type="file">` change event: shows an instant local
   * preview, then uploads the file and swaps in the server-hosted image on success.
   * No-op in add-mode (no `entityId` yet) or if no file was selected.
   * @param event The file input's `change` event.
   */
  onPhotoSelected(event: Event): void {
    const file = (event.target as HTMLInputElement).files?.[0];
    if (!file || !this.entityId) return;
    this.revokePhotoPreview();

    //preview the image from file
    this.photoPreview = URL.createObjectURL(file);
    this.photoUploading = true;
    //notify image state change to Angular
    this.cdr.markForCheck();

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
        this.cdr.markForCheck();
      },
      () => {
        this.photoUploading = false;
        this.cdr.markForCheck();
      }
    );
  }

  /**
   * Angular lifecycle hook. Releases any outstanding photo preview blob URL.
   */
  ngOnDestroy(): void {
    this.revokePhotoPreview();
  }

  /**
   * Converts a date value (string, timestamp, etc.) to the `yyyy-MM-dd` format
   * expected by the `<input type="date">` control.
   * @param dob The raw date-of-birth value from the API.
   */
  private toDateInputValue(dob: unknown): string {
    if (!dob) return '';
    const d = new Date(dob as string | number);
    if (isNaN(d.getTime())) return '';
    const yyyy = d.getFullYear();
    const mm = String(d.getMonth() + 1).padStart(2, '0');
    const dd = String(d.getDate()).padStart(2, '0');
    return `${yyyy}-${mm}-${dd}`;
  }

  /**
   * Builds the request payload for save, converting `dob` from the date input's
   * string format to an epoch millisecond timestamp.
   */
  protected override getBody(): Faculty {
    const v = this.form.value;
    return {
      id: this.entityId ?? 0,
      ...v,
      dob: v.dob ? new Date(`${v.dob}T00:00:00`).getTime() : null
    };
  }

  /**
   * Returns the service instance responsible for faculty CRUD operations.
   */
  protected override getService() { return this.facultyService; }
}
