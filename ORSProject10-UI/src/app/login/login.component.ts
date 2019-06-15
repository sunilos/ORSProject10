import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { HttpServiceService } from '../http-service.service';
import { Router } from '@angular/router';
import { DataValidator } from '../utility/data-validator';
import { CookieService } from 'ngx-cookie-service';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})

export class LoginComponent implements OnInit {

  endpoint = "http://localhost:8080/Auth";

  form = {
    error: false,
    message: "",
    loginId: 'superuser@nenosystems.com',
    password: 'pass1234'
  };

  inputerror = {};
  message = '';

  constructor(private httpService: HttpServiceService, private dataValidator: DataValidator, private router: Router,
    private cookieService: CookieService) {
  }

  /**
   * Initialize component
   */
  ngOnInit() {
  }

  validate() {
    let flag = true;
    flag = flag && this.dataValidator.isNotNull(this.form.loginId);
    flag = flag && this.dataValidator.isNotNull(this.form.password);
    return flag;
  }

  signIn() {
    var _self = this;
    console.log('signIn', this.form);
    this.httpService.post(this.endpoint + "/login", this.form, function (res) {

      console.log('MyResoonse', res);

      _self.form.message = '';
      _self.inputerror = {};

      if (_self.dataValidator.isNotNullObject(res.result.message)) {
        _self.form.message = res.result.message;
      }

      _self.form.error = !res.success;

      if (_self.dataValidator.isNotNullObject(res.result.inputerror)) {
        _self.inputerror = res.result.inputerror;
      }

      if (_self.dataValidator.isTrue(res.success)) {
        _self.httpService.setSessionId(res.result.jsessionid);
        _self.cookieService.set('JSESSIONID',res.result.jsessionid);
        _self.router.navigateByUrl('/dashboard');
      }

    });
  }

}
