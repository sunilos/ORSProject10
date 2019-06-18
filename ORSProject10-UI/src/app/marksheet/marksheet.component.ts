import { Component, OnInit } from '@angular/core';
import { HttpServiceService } from '../http-service.service';
import { SearchResponse, RecordResponse } from '../response';
import { BaseCtl } from '../base.component';
import { ActivatedRoute } from '@angular/router';
import { ServiceLocatorService } from '../service-locator.service';

@Component({
  selector: 'app-marksheet',
  templateUrl: './marksheet.component.html',
  styleUrls: ['./marksheet.component.css']
})

export class MarksheetComponent extends BaseCtl {

  constructor(public locator: ServiceLocatorService, public route: ActivatedRoute) {
    super(locator.endpoints.MARKSHEET, locator, route);

  }

  validate() {
    return this.validateForm(this.form.data);
  }

  validateForm(form) {
    let flag = true;
    let validator = this.serviceLocator.dataValidator;
    flag = flag && validator.isNotNullObject(form.rollNo);
    flag = flag && validator.isNotNullObject(form.name);
    flag = flag && validator.isNotNullObject(form.physics);
    flag = flag && validator.isNotNullObject(form.chemistry);
    flag = flag && validator.isNotNullObject(form.maths);
    return flag;
  }

  populateForm(form, data) {
    form.id = data.id;
    form.rollNo = data.rollNo;
    form.name = data.name;
    form.physics = data.physics;
    form.chemistry = data.chemistry;
    form.maths = data.maths;
    console.log('Populated Form', form);
  }

}


