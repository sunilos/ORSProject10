import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';


@Injectable()

export class HttpServiceService {

  sessionid = '1BD5F72D01F0164ADBDE1776CD6SUNIL';

  setSessionId(sid) {
    this.sessionid = sid;
    console.log('Session Id', this.sessionid);

  }

  constructor(private httpClient: HttpClient) {

  }

  getHeader() {
    let sesCookiee = 'JSESSIONID=' + this.sessionid;
    //'cookie' :   sesCookiee,
    let httpOptions = {
      headers: new HttpHeaders({
        "withCredentials": "true"
      })
    };
    console.log(httpOptions);
    return httpOptions;
  }

  get(endpoint, callback) {
    return this.httpClient.get(endpoint, this.getHeader()).subscribe((data) => {
      console.log(data);
      callback(data);
    });;
  }


  post(endpoint, bean, callback) {
    return this.httpClient.post(endpoint, bean, this.getHeader()).subscribe((data) => {
      console.log(data);
      callback(data);
    }, error => {
      console.log('ORS Error', error);
    });
  }

}
