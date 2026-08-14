import { ChangeDetectorRef, Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CollegeService } from '../services/college.service';
import { BaseListComponent } from '../base/base-list.component';
import type { BaseService } from '../services/base.service';

@Component({
  selector: 'app-college-list',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './college-list.html',
  styleUrl: './college-list.css'
})
export class CollegeListComponent extends BaseListComponent {

  protected override pageUrl = '/college';

  constructor(private fb: FormBuilder, private collegeService: CollegeService, router: Router, cdr: ChangeDetectorRef) {
    super(router, cdr);
    this.form = this.buildForm();
  }

  protected override buildForm(): FormGroup {
    return this.fb.group({
      name: [''],
      city: [''],
      state: ['']
    });
  }

  protected override getService(): BaseService { return this.collegeService; }
}
