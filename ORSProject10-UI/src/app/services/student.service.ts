import { Injectable } from '@angular/core';
import { ORSAPI } from './orsapi.config';
import { ServiceLocator } from './service-locator';
import { BaseService } from './base.service';

export interface Student {
  id: number;
  firstName: string;
  lastName: string;
  dob?: string;
  mobileNumber: string;
  email: string;
  collegeId: number;
  collegeName: string;
  [key: string]: unknown;
}

@Injectable({ providedIn: 'root' })
export class StudentService extends BaseService {

  constructor(serviceLocator: ServiceLocator) {
    super(serviceLocator);
    this.url = ORSAPI.STUDENT_API;
    this.supportsPreload = true;
  }

}
