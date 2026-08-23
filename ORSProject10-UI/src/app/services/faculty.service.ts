import { Injectable } from '@angular/core';
import { ORSAPI } from './orsapi.config';
import { ServiceLocator } from './service-locator';
import { BaseService } from './base.service';

export interface Faculty {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  mobileNo: string;
  address?: string;
  gender?: string;
  dob?: string;
  collegeId: number;
  collegeName: string;
  subjectId: number;
  subjectName: string;
  courseId: number;
  courseName: string;
  photo?: string;
  imageId?: string;
  [key: string]: unknown;
}

@Injectable({ providedIn: 'root' })
export class FacultyService extends BaseService {

  constructor(serviceLocator: ServiceLocator) {
    super(serviceLocator);
    this.url = ORSAPI.FACULTY_API;
  }

  uploadPhoto(facultyId: number, file: File, onSuccess: (data: any) => void, onError: (error: any) => void): void {
    const fd = new FormData();
    fd.append('file', file);
    this.serviceLocator.http.put<any>(`${ORSAPI.UPLOAD_FACULTY_PHOTO_API}/${facultyId}`, fd, onSuccess, onError);
  }
}
