import { ChangeDetectorRef, Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CourseService } from '../services/course.service';
import { BaseListComponent } from '../base/base-list.component';
import { BaseService } from '../services/base.service';

@Component({
  selector: 'app-course-list',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './course-list.html',
  styleUrl: './course-list.css'
})
export class CourseListComponent extends BaseListComponent {

  protected override pageUrl = '/course';

  constructor(private fb: FormBuilder, private courseService: CourseService, router: Router, cdr: ChangeDetectorRef) {
    super(router, cdr);
    this.form = this.buildForm();
  }

  protected override buildForm(): FormGroup {
    return this.fb.group({
      name: [''],
      description: [''],
      duration: ['']
    });
  }

  protected override getService(): BaseService { return this.courseService; }
}
