import { Injectable } from '@angular/core';
import { ORSAPI } from './orsapi.config';
import { ServiceLocator } from './service-locator';
import { BaseService } from './base.service';

export interface Subject {
  id: number;
  name: string;
  description?: string;
  dob?: string;
  courseId: number;
  courseName: string;
  [key: string]: unknown;
}

@Injectable({ providedIn: 'root' })
export class SubjectService extends BaseService {

  constructor(serviceLocator: ServiceLocator) {
    super(serviceLocator);
    this.url = ORSAPI.SUBJECT_API;
  }
}
