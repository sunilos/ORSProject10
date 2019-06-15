import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { HttpServiceService } from '../http-service.service';
import { Router } from '@angular/router';
import { DataValidator } from '../utility/data-validator';


@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html'
})

export class SignUpComponent implements OnInit {

  endpoint = "http://localhost:8080/Auth/signUp";

  form = {
    error: false,
    message: "",
    firstName: "",
    lastName: "",
    login :"",
    password : ""
  };

  inputerror = {};

  constructor(private httpService: HttpServiceService, private dataValidator: DataValidator, private router: Router) {
  }


  validate(){
    let flag = true;
    flag = flag && this.dataValidator.isNotNullObject(this.form.firstName);
    flag = flag && this.dataValidator.isNotNullObject(this.form.lastName);
    flag = flag && this.dataValidator.isNotNullObject(this.form.login);
    flag = flag && this.dataValidator.isNotNullObject(this.form.password);
    return flag;
  }

  /**
   * Initialize component
   */
  ngOnInit() {
  }

  submit() {
    var _self = this;
    this.httpService.post(this.endpoint, this.form,function (res) {

      console.log('MyResoonse', res);

      _self.form.message = '';
      _self.inputerror = {};

      if (res.result.message) {
        _self.form.message = res.result.message;
      }

      _self.form.error = !res.success;
      if (_self.form.error && res.result.inputerror) {
          _self.inputerror = res.result.inputerror;
      }
    });
  }
}
