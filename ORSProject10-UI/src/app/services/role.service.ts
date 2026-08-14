import { Injectable } from '@angular/core';
import { ORSAPI } from './orsapi.config';
import { ServiceLocator } from './service-locator';
import { BaseService } from './base.service';

export interface Role {
  id: number;
  name: string;
  description?: string;
  [key: string]: unknown;
}

@Injectable({ providedIn: 'root' })
export class RoleService extends BaseService {

  constructor(serviceLocator: ServiceLocator) {
    super(serviceLocator);
    this.url = ORSAPI.ROLE_API;
    this.supportsPreload = false;
  }
}
