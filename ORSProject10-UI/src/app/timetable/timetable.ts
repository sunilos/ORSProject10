import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { TimeTableService, TimeTable } from '../services/timetable.service';
import { BaseComponent } from '../base/base.component';

@Component({
  selector: 'app-timetable',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './timetable.html',
  styleUrl: './timetable.css'
})
export class TimeTableComponent extends BaseComponent {

  protected override listUrl = '/timetables';
  override get title(): string { return this.isEditMode ? 'Edit Time Table' : 'Add Time Table'; }

  constructor(private timeTableService: TimeTableService) {
    super();
  }

  protected override buildForm(): FormGroup {
    return this.fb.group({
      exam_date: [null],
      exam_time: ['', Validators.required],
      subject_id: ['', Validators.required],
      subject_name: [''],
      course_id: ['', Validators.required],
      course_name: [''],
      semester: ['', Validators.required]
    });
  }

  protected override populateForm(t: any): void {
    this.form.patchValue({
      exam_date: t.exam_date,
      exam_time: t.exam_time,
      subject_id: t.subject_id,
      subject_name: t.subject_name,
      course_id: t.course_id,
      course_name: t.course_name,
      semester: t.semester
    });
  }

  onCourseChange(event: Event): void {
    const id = +(event.target as HTMLSelectElement).value;
    const course = this.preloadData?.courses?.find((c: any) => c.id === id);
    this.form.patchValue({ course_name: course?.value ?? '' });
  }

  onSubjectChange(event: Event): void {
    const id = +(event.target as HTMLSelectElement).value;
    const subject = this.preloadData?.subjects?.find((s: any) => s.id === id);
    this.form.patchValue({ subject_name: subject?.value ?? '' });
  }

  protected override getBody(): TimeTable {
    const v = this.form.value;
    return { id: this.entityId ?? 0, ...v };
  }

  protected override getService() { return this.timeTableService; }
}
