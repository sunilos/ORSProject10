import { ChangeDetectorRef, Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { TimeTableService } from '../services/timetable.service';
import { BaseListComponent } from '../base/base-list.component';
import type { BaseService } from '../services/base.service';

@Component({
  selector: 'app-timetable-list',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './timetable-list.html',
  styleUrl: './timetable-list.css'
})
export class TimeTableListComponent extends BaseListComponent {

  protected override pageUrl = '/timetable';

  constructor(private fb: FormBuilder, private timeTableService: TimeTableService, router: Router, cdr: ChangeDetectorRef) {
    super(router, cdr);
    this.form = this.buildForm();
  }

  protected override buildForm(): FormGroup {
    return this.fb.group({
      course_name: [''],
      subject_name: [''],
      semester: ['']
    });
  }

  protected override getService(): BaseService { return this.timeTableService; }
}
