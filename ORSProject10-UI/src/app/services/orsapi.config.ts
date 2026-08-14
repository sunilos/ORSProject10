export interface ORSAPIConfig {
  baseUrl: string;
  LOGIN_API: string;
  TOKEN_REFRESH_API: string;
  CHANGE_PASSWORD_API: string;
  FORGOT_PASSWORD_API: string;
  REGISTER_API: string;
  USER_API: string;
  ROLE_API: string;
  COLLEGE_API: string;
  COURSE_API: string;
  STUDENT_API: string;
  SUBJECT_API: string;
  FACULTY_API: string;
  MARKSHEET_API: string;
  TIMETABLE_API: string;
  STUDENT_SEARCH_API: string;
  COLLEGE_SEARCH_API: string;
  UPLOAD_PHOTO_API: string;
}

const BASE = 'http://127.0.0.1:8080';
const CONTEXT = '';


export const ORSAPI: ORSAPIConfig = {

  baseUrl: BASE,

  TOKEN_REFRESH_API: `${BASE}/api/token/refresh`,
  LOGIN_API: `${BASE}${CONTEXT}/auth/login`,
  CHANGE_PASSWORD_API: `${BASE}${CONTEXT}/auth/change-password`,
  FORGOT_PASSWORD_API: `${BASE}${CONTEXT}/auth/forgot-password`,
  REGISTER_API: `${BASE}${CONTEXT}/auth/register`,
  UPLOAD_PHOTO_API: `${BASE}${CONTEXT}/auth/upload-photo`,

  USER_API: `${BASE}${CONTEXT}/user`,
  ROLE_API: `${BASE}${CONTEXT}/role`,
  COLLEGE_API: `${BASE}${CONTEXT}/college`,
  COLLEGE_SEARCH_API: `${BASE}${CONTEXT}/college/search/`,
  COURSE_API: `${BASE}${CONTEXT}/course`,
  STUDENT_API: `${BASE}${CONTEXT}/student`,
  STUDENT_SEARCH_API: `${BASE}${CONTEXT}/student/search/`,
  SUBJECT_API: `${BASE}${CONTEXT}/subject`,
  FACULTY_API: `${BASE}${CONTEXT}/faculty`,
  MARKSHEET_API: `${BASE}${CONTEXT}/marksheet`,
  TIMETABLE_API: `${BASE}${CONTEXT}/timetable`
};
