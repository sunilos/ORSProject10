import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';

import { AppComponent } from './app.component';
import { MarksheetComponent } from './marksheet/marksheet.component';
import { StudentComponent } from './student/student.component';
import { CollegeComponent } from './college/college.component';

import { AppRoutingModule } from './app-routing.module';
import { HttpClientModule } from '@angular/common/http';

import { HttpServiceService } from './http-service.service';
import { LoginComponent } from './login/login.component';
import { BasicsComponent } from './basics/basics.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { DataValidator } from './utility/data-validator';
import { ForgotPasswordComponent } from './login/forgotpassword.component';
import { SignUpComponent } from './login/signup.component';
import { MessageComponent } from './message/message.component';
import { CookieService } from 'ngx-cookie-service';
import { MessageListComponent } from './message/message-list.component';
import { MarksheetListComponent } from './marksheet/marksheet-list.component';
import { CollegeListComponent } from './college/college-list.component';
import { StudentListComponent } from './student/student-list.component';
import { UserComponent } from './user/user.component';
import { PageNotFoundComponent } from './page-not-found.component';
import { UserListComponent } from './user/user-list.component';
import { TopNavComponent } from './top-nav/top-nav.component';

@NgModule({
  declarations: [
    AppComponent,
    MarksheetComponent,
    MarksheetListComponent,
    StudentComponent,
    StudentListComponent,
    CollegeComponent,
    CollegeListComponent,
    LoginComponent,
    BasicsComponent,
    DashboardComponent,
    ForgotPasswordComponent,
    SignUpComponent,
    MessageComponent,
    MessageListComponent,
    UserComponent,
    UserListComponent,
    PageNotFoundComponent,
    TopNavComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [
    HttpServiceService,
    DataValidator,
    CookieService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { 
  
}
