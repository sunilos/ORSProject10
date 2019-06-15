import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})

export class LoginComponent implements OnInit {

  operation = '';

  forgotForm = {
    value :{}
  };

  user = {
    userId : '',
    password : ''
  };
  
  constructor() { 
  }

  /**
   * Initialize component
   */
  ngOnInit() {
    this.operation = 'SignIn';

    this.forgotForm = new FormGroup({
      userId: new FormControl("abc@gmail.com"),
      mobile: new FormControl("9999999999")
   });
  }

  /**
   * @param op Change operation 
   */
  changeOperation(op){
    this.operation = op;
  }


  /**
   * Sign In Operation
   */
  signIn(data){
    console.log('signIn',data);
  }

  /**
   * Forgot Password Operation 
   */
  forgotPassword(){
    console.log('forgotPassword',this.forgotForm.value);
  }

  /**
   * New user registration
   */
  signUp(){
    console.log('signup###',this.user);
  }

  getData(){
    var data = {userId:'TestId',password:'1234567890'};
    //this.forgotForm.value = data;
    this.user = data;
    
    //this.forgotForm.value.userId  = data
  }


}
