import { Injectable } from '@angular/core';
import { ORSAPI } from './orsapi.config';
import { ServiceLocator } from './service-locator';
import { BaseService } from './base.service';

export interface Marksheet {
  id: number;
  rollNumber: string;
  name: string;
  physics: number;
  chemistry: number;
  maths: number;
  year: number;
  student_id: number;
  [key: string]: unknown;
}

@Injectable({ providedIn: 'root' })
export class MarksheetService extends BaseService {

  constructor(serviceLocator: ServiceLocator) {
    super(serviceLocator);
    this.url = ORSAPI.MARKSHEET_API;
    this.supportsPreload = true;
  }
}
