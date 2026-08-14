import { ChangeDetectorRef, Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { SubjectService } from '../services/subject.service';
import { BaseListComponent } from '../base/base-list.component';
import type { BaseService } from '../services/base.service';

@Component({
  selector: 'app-subject-list',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './subject-list.html',
  styleUrl: './subject-list.css'
})
export class SubjectListComponent extends BaseListComponent {

  protected override pageUrl = '/subject';

  constructor(private fb: FormBuilder, private subjectService: SubjectService, router: Router, cdr: ChangeDetectorRef) {
    super(router, cdr);
    this.form = this.buildForm();
  }

  protected override buildForm(): FormGroup {
    return this.fb.group({
      name: ['', Validators.required],
      description: [''],
      course_ID: ['', Validators.required],
      courseName: ['']
    });
  }

  protected override getService(): BaseService { return this.subjectService; }
}
