import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CollegeService, College } from '../services/college.service';
import { BaseComponent } from '../base/base.component';

@Component({
  selector: 'app-college',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './college.html',
  styleUrl: './college.css'
})
export class CollegeComponent extends BaseComponent {

  protected override listUrl = '/colleges';
  override get title(): string { return this.isEditMode ? 'Edit College' : 'Add College'; }

  constructor(private collegeService: CollegeService) {
    super();
  }

  protected override buildForm(): FormGroup {
    return this.fb.group({
      name: ['', Validators.required],
      address: [''],
      city: [''],
      state: [''],
      phoneNo: ['']
    });
  }

  protected override populateForm(college: any): void {
    this.form.patchValue({
      name: college['name'],
      address: college['address'] ?? '',
      city: college['city'] ?? '',
      state: college['state'] ?? '',
      phoneNo: college['phoneNo'] ?? ''
    });
  }

  protected override getBody(): College {
    return { id: this.entityId ?? 0, ...this.form.value };
  }


  protected override getService(): CollegeService {
    return this.collegeService;
  }
}
