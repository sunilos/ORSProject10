import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { RoleService, Role } from '../services/role.service';
import { BaseComponent } from '../base/base.component';

@Component({
  selector: 'app-role',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './role.html',
  styleUrl: './role.css'
})
export class RoleComponent extends BaseComponent {

  protected override listUrl = '/roles';
  override get title(): string { 
    return this.isEditMode ? 'Edit Role' : 'Add Role'; 
  }

  constructor(private roleService: RoleService) {
    super();
  }

  protected override buildForm(): FormGroup {
    return this.fb.group({
      name: ['', Validators.required],
      description: ['']
    });
  }

  protected override populateForm(role: any): void {
    this.form.patchValue(
      { 
        name: role.name, 
        description: role.description ?? '' 
      });
  }

  protected override getBody(): Role { 
    return { 
      id: this.entityId ?? 0, ...this.form.value 
    }; 
  }
  protected override getService() { 
    return this.roleService; 
  }
}
