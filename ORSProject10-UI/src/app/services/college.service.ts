import { Injectable } from '@angular/core';
import { ORSAPI } from './orsapi.config';
import { ServiceLocator } from './service-locator';
import { BaseService } from './base.service';

export interface College {
  id: number;
  name: string;
  address?: string;
  city?: string;
  state?: string;
  phoneNumber?: string;
  [key: string]: unknown;
}

@Injectable({ providedIn: 'root' })
export class CollegeService extends BaseService {

  constructor(serviceLocator: ServiceLocator) {
    super(serviceLocator);
    this.url = ORSAPI.COLLEGE_API;
    this.supportsPreload = false;
  }
}
