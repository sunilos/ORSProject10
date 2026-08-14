import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { StudentService, Student } from '../services/student.service';
import { College } from '../services/college.service';
import { BaseComponent } from '../base/base.component';

@Component({
  selector: 'app-student',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './student.html',
  styleUrl: './student.css'
})
export class StudentComponent extends BaseComponent {

  protected override listUrl = '/students';
  override get title(): string { return this.isEditMode ? 'Edit Student' : 'Add Student'; }

  colleges: College[] = [];

  constructor(
    private fb: FormBuilder,
    private studentService: StudentService,
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
      dob: [''],
      mobileNo: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      collegeId: ['', Validators.required],
      collegeName: ['']
    });
  }

  protected override populateForm(s: any): void {
    this.form.patchValue({
      firstName: s.firstName,
      lastName: s.lastName,
      dob: this.toDateInputValue(s.dob),
      mobileNo: s.mobileNo,
      email: s.email,
      collegeId: s.collegeId,
      collegeName: s.collegeName
    });
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
    const id = +(event.target as HTMLSelectElement).value;
    const college = this.colleges.find(c => c.id === id);
    this.form.patchValue({ collegeName: college?.name ?? '' });
  }

  protected override getBody(): Student {
    const v = this.form.value;
    return { id: this.entityId ?? 0, ...v, dob: v.dob ? new Date(`${v.dob}T00:00:00`).getTime() : null };
  }
  protected override getService() { return this.studentService; }
}
