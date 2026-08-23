import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { SubjectService, Subject } from '../services/subject.service';
import { BaseComponent } from '../base/base.component';

@Component({
  selector: 'app-subject',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './subject.html',
  styleUrl: './subject.css'
})
export class SubjectComponent extends BaseComponent {

  protected override listUrl = '/subjects';
  override get title(): string { return this.isEditMode ? 'Edit Subject' : 'Add Subject'; }

  constructor(private subjectService: SubjectService) {
    super();
  }

  protected override buildForm(): FormGroup {
    return this.fb.group({
      name: ['', Validators.required],
      description: [''],
      courseId: ['', Validators.required],
      courseName: ['']
    });
  }

  protected override populateForm(s: any): void {
    this.form.patchValue({
      name: s.name,
      description: s.description ?? '',
      courseId: s.courseId,
      courseName: s.courseName
    });
  }

  protected override getBody(): Subject {
    const v = this.form.value;
    return { id: this.entityId ?? 0, ...v };
  }
  protected override getService() { 
    return this.subjectService; 
  }
}
