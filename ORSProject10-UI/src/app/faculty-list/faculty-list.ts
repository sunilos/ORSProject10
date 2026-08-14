import { ChangeDetectorRef, Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { FacultyService } from '../services/faculty.service';
import { BaseListComponent } from '../base/base-list.component';
import type { BaseService } from '../services/base.service';

@Component({
  selector: 'app-faculty-list',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './faculty-list.html',
  styleUrl: './faculty-list.css'
})
export class FacultyListComponent extends BaseListComponent {

  protected override pageUrl = '/faculty';

  constructor(private fb: FormBuilder, private facultyService: FacultyService, router: Router, cdr: ChangeDetectorRef) {
    super(router, cdr);
    this.form = this.buildForm();
  }

  protected override buildForm(): FormGroup {
    return this.fb.group({
      firstName: [''],
      lastName: [''],
      email: [''],
      collegeName: [''],
      courseName: ['']
    });
  }

  protected override getService(): BaseService { return this.facultyService; }
}
