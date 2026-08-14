import { Injectable } from '@angular/core';
import { ORSAPI } from './orsapi.config';
import { ServiceLocator } from './service-locator';
import { BaseService } from './base.service';

export interface TimeTable {
  id: number;
  exam_date: string | null;
  exam_time: string;
  subject_id: number;
  subject_name: string;
  course_id: number;
  course_name: string;
  semester: string;
  [key: string]: unknown;
}

@Injectable({ providedIn: 'root' })
export class TimeTableService extends BaseService {

  constructor(serviceLocator: ServiceLocator) {
    super(serviceLocator);
    this.url = ORSAPI.TIMETABLE_API;
    this.supportsPreload = true;
  }
}
