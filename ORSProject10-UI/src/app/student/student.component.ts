import { Component, OnInit } from '@angular/core';
import { ServiceLocatorService } from '../service-locator.service';
import { ActivatedRoute } from '@angular/router';
import { BaseCtl } from '../base.component';

@Component({
  selector: 'app-student',
  templateUrl: './student.component.html',
  styleUrls: ['./student.component.css']
})
export class StudentComponent extends BaseCtl {

  constructor(public locator: ServiceLocatorService, public route: ActivatedRoute) {
    super(locator.endpoints.STUDENT, locator, route);
  }

  validateForm(form) {
    let flag = true;
    let validator = this.serviceLocator.dataValidator;
    flag = flag && validator.isNotNullObject(form.firstName);
    flag = flag && validator.isNotNullObject(form.lastName);
    flag = flag && validator.isNotNullObject(form.mobileNo);
    return flag;
  }

  populateForm(form, data) {
    form.id = data.id;
    form.collegeId = data.collegeId;
    form.email = data.email;
    form.firstName = data.firstName;
    form.lastName = data.lastName;
    form.mobileNo = data.mobileNo;
  }

  
}
