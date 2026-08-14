import { Injectable } from '@angular/core';
import { ORSAPI } from './orsapi.config';
import { ServiceLocator } from './service-locator';
import { BaseService } from './base.service';

export interface Course {
  id: number;
  name: string;
  description?: string;
  duration?: string;
  [key: string]: unknown;
}

@Injectable({ providedIn: 'root' })
export class CourseService extends BaseService {

  constructor(serviceLocator: ServiceLocator) {
    super(serviceLocator);
    this.url = ORSAPI.COURSE_API;
    this.supportsPreload = false;
  }
}
