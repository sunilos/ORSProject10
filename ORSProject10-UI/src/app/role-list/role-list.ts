import { ChangeDetectorRef, Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { RoleService } from '../services/role.service';
import { BaseListComponent } from '../base/base-list.component';
import type { BaseService } from '../services/base.service';

@Component({
  selector: 'app-role-list',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './role-list.html',
  styleUrl: './role-list.css'
})
export class RoleListComponent extends BaseListComponent {

  protected override pageUrl = '/role';

  constructor(private fb: FormBuilder, private roleService: RoleService, router: Router, cdr: ChangeDetectorRef) {
    super(router, cdr);
    this.form = this.buildForm();
  }

  protected override buildForm(): FormGroup {
    return this.fb.group({
      name: [''],
      description: ['']
    });
  }

  protected override getService(): BaseService { return this.roleService; }
}
