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
      examDate: [null],
      examTime: ['', Validators.required],
      courseId: ['', Validators.required],
      courseName: [''],
      subjectId: ['', Validators.required],
      subjectName: [''],
      semester: ['', Validators.required]
    });
  }

  protected override populateForm(t: any): void {
    this.form.patchValue({
      examDate: this.toDateInputValue(t.examDate),
      examTime: t.examTime,
      courseId: t.courseId, 
      courseName: t.courseName,
      subjectId: t.subjectId, 
      subjectName: t.subjectName,
      semester: t.semester
    });
  }

  private toDateInputValue(value: unknown): string {
    if (!value) return '';
    const date = new Date(value as string | number);
    if (isNaN(date.getTime())) return '';
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  }

  onCourseChange(event: Event): void {
    const id = +(event.target as HTMLSelectElement).value;
    const course = this.preloadData?.courseList?.find((c: any) => c.id === id);
    this.form.patchValue({ courseName: course?.value ?? '' });
  }

  onSubjectChange(event: Event): void {
    const id = +(event.target as HTMLSelectElement).value;
    const subject = this.preloadData?.subjectList?.find((s: any) => s.id === id);
    this.form.patchValue({ subjectName: subject?.value ?? '' });
  }

  protected override getBody(): TimeTable {
    const v = this.form.value;
    return {
      id: this.entityId ?? 0,
      ...v,
      examDate: v.examDate ? new Date(`${v.examDate}T00:00:00`).getTime() : null
    };
  }

  protected override getService() { return this.timeTableService; }
}
