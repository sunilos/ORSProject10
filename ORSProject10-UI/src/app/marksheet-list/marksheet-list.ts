import { ChangeDetectorRef, Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { MarksheetService } from '../services/marksheet.service';
import { BaseListComponent } from '../base/base-list.component';
import type { BaseService } from '../services/base.service';

@Component({
  selector: 'app-marksheet-list',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './marksheet-list.html',
  styleUrl: './marksheet-list.css'
})
export class MarksheetListComponent extends BaseListComponent {

  protected override pageUrl = '/marksheet';

  constructor(private fb: FormBuilder, private marksheetService: MarksheetService, router: Router, cdr: ChangeDetectorRef) {
    super(router, cdr);
    this.form = this.buildForm();
  }

  protected override buildForm(): FormGroup {
    return this.fb.group({
      rollNo: [''],
      name: [''],
      year: ['']
    });
  }

  protected override getService(): BaseService { return this.marksheetService; }
}
