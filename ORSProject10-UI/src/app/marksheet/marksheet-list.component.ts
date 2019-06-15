import { Component, OnInit } from '@angular/core';
import { BaseListCtl } from '../base-list.component';
import { ServiceLocatorService } from '../service-locator.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-marksheet-list',
  templateUrl: './marksheet-list.component.html',
  styleUrls: ['./marksheet.component.css']
})

export class MarksheetListComponent extends BaseListCtl {
  constructor(public locator: ServiceLocatorService, public route: ActivatedRoute) {
    super(locator.endpoints.MARKSHEET, locator, route);
  }

}


