import { Component, OnInit } from '@angular/core';
import { BaseCtl } from '../base.component';
import { ServiceLocatorService } from '../service-locator.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent extends BaseCtl {

  constructor(public locator: ServiceLocatorService, public route: ActivatedRoute) {
    super(locator.endpoints.USER, locator, route);
  }

  validateForm(form) {
    let flag = true;
    let validator = this.serviceLocator.dataValidator;
    flag = flag && validator.isNotNullObject(form.firstName);
    flag = flag && validator.isNotNullObject(form.lastName);
    flag = flag && validator.isNotNullObject(form.loginId);
    flag = flag && validator.isNotNullObject(form.password);
    return flag;
  }

  populateForm(form, data) {
    form.id = data.id;
    form.firstName = data.firstName;
    form.lastName = data.lastName;
    form.email  = data.email;
    form.loginId = data.loginId;
    form.password = data.password;
    form.gender = data.gender;
    form.alternateMobile = data.alternateMobile;
    form.roleId = data.roleId;
    form.status = data.status;
    console.log('Populated Form', form);
  }

  test(){
  
  }

}
