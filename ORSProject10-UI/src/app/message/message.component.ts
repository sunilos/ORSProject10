import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { HttpServiceService } from '../http-service.service';
import { Router, ActivatedRoute } from '@angular/router';
import { DataValidator } from '../utility/data-validator';
import { ServiceLocatorService } from '../service-locator.service';
import { BaseCtl } from '../base.component';


@Component({
  selector: 'app-message',
  templateUrl: './message.component.html',
  styleUrls: ['./message.component.css']
})

export class MessageComponent extends BaseCtl {

  constructor(public locator: ServiceLocatorService, public route: ActivatedRoute) {
    super(locator.endpoints.MESSAGE, locator, route);
  }

  validateForm(form) {
    let flag = true;
    let validator = this.serviceLocator.dataValidator;
    flag = flag && validator.isNotNullObject(form.type);
    flag = flag && validator.isNotNullObject(form.code);
    flag = flag && validator.isNotNullObject(form.subject);
    flag = flag && validator.isNotNullObject(form.body);
    return flag;
  }

  populateForm(form, data) {
    form.id = data.id;
    form.code = data.code;
    form.type = data.type;
    form.subject = data.subject;
    form.body = data.body;
    form.status = data.status;
    form.html = data.html;
    console.log('Populated Form', form);
  }
}
