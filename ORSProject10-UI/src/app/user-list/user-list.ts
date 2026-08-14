import { ChangeDetectorRef, Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../services/user.service';
import { BaseListComponent } from '../base/base-list.component';
import type { BaseService } from '../services/base.service';
import { ORSAPI } from '../services/orsapi.config';

@Component({
  selector: 'app-user-list',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './user-list.html',
  styleUrl: './user-list.css'
})
export class UserListComponent extends BaseListComponent {

  protected override pageUrl = '/user';
  readonly baseUrl = ORSAPI.baseUrl;

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    router: Router,
    cdr: ChangeDetectorRef
  ) {
    super(router, cdr);
    this.form = this.buildForm();
  }

  protected override buildForm(): FormGroup {
    return this.fb.group({
      firstName: [''],
      lastName:  [''],
      loginId:   [''],
      phone:     ['']
    });
  }

  protected override getService(): BaseService { return this.userService; }
}
