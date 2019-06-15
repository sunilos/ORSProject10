import { Component, OnInit } from '@angular/core';
import { BaseCtl } from '../base.component';
import { ServiceLocatorService } from '../service-locator.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-college',
  templateUrl: './college.component.html',
  styleUrls: ['./college.component.css']
})
export class CollegeComponent extends BaseCtl {

  constructor(public locator: ServiceLocatorService, public route: ActivatedRoute) {
    super(locator.endpoints.COLLEGE, locator, route);
  }


  validate() {
    return this.validateForm(this.form.data);
  }

  validateForm(form) {
    let flag = true;
    let validator = this.serviceLocator.dataValidator;
    flag = flag && validator.isNotNullObject(form.name);
    flag = flag && validator.isNotNullObject(form.phoneNo);
    return flag;
  }

  populateForm(form, data) {
    form.id = data.id;
    form.name = data.name;
    form.address = data.address;
    form.state = data.state;
    form.city = data.city;
    form.phoneNo = data.phoneNo;
    console.log('Populated Form', form);
  }
}
