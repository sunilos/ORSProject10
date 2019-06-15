import { OnInit } from '@angular/core';
import { ServiceLocatorService } from './service-locator.service';
import { HttpServiceService } from './http-service.service';
import { ActivatedRoute } from '@angular/router';
import { BaseCtl } from './base.component';

export class BaseListCtl extends BaseCtl {

  constructor(public endpoint, public locator: ServiceLocatorService, public route: ActivatedRoute) {
    super(endpoint, locator, route);
  }

  /**
   * Initialize component
   */
  ngOnInit() {
    this.preload();
    this.search();
  }

  display() {
    this.search();
  }

  submit() {
    this.search();
  }

  delete(id){
    super.delete(id,function(){
      this.search();
    });
  }


  next() {
    this.form.pageNo++;
    this.display();
  }

  previous() {
    if (this.form.pageNo > 0) {
      this.form.pageNo--
      this.display();
    }
  }


}
