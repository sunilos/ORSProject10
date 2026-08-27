import { Injectable } from '@angular/core';
import { ORSAPI } from './orsapi.config';
import { ServiceLocator } from './service-locator';
import { BaseService } from './base.service';

export interface User {
  id: number;
  firstName: string;
  lastName: string;
  loginId: string;
  password?: string;
  dob?: string | number | null;
  roleId: number;
  roleName?: string;
  phone: string;
  gender?: string;
  photo?: string;
  [key: string]: unknown;
}

@Injectable({ providedIn: 'root' })
export class UserService extends BaseService {

  constructor(serviceLocator: ServiceLocator) {
    super(serviceLocator);
    this.url = ORSAPI.USER_API;
    this.supportsPreload = true;
  }

  uploadPhoto(userId: number, file: File, onSuccess: (data: any) => void, onError: (error: any) => void): void {
    const fd = new FormData();
    fd.append('file', file);
    this.serviceLocator.http.put<any>(`${ORSAPI.UPLOAD_PHOTO_API}/${userId}`, fd, onSuccess, onError);
  }
}
