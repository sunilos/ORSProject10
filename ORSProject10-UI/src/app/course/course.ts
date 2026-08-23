import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CourseService, Course } from '../services/course.service';
import { BaseComponent } from '../base/base.component';

@Component({
  selector: 'app-course',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './course.html',
  styleUrl: './course.css'
})
export class CourseComponent extends BaseComponent {

  protected override listUrl = '/courses';
  override get title(): string { return this.isEditMode ? 'Edit Course' : 'Add Course'; }

  constructor(private courseService: CourseService) {
    super();
  }

  protected override buildForm(): FormGroup {
    return this.fb.group({
      name: ['', Validators.required],
      description: [''],
      duration: ['']
    });
  }

  protected override populateForm(course: any): void {
    this.form.patchValue({ name: course.name, description: course.description ?? '', duration: course.duration ?? '' });
  }

  protected override getBody(): Course { return { id: this.entityId ?? 0, ...this.form.value }; }
  protected override getService() { return this.courseService; }
}
