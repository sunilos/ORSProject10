import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class HttpClientService {

  constructor(private http: HttpClient) { }

  /**
   * Sends a GET request and resolves with a JSON body of type T.
   * @param url - Endpoint to request.
   * @param onSuccess - Called with the parsed response body.
   * @param onError - Called with the error if the request fails.
   * @param params - Optional query string parameters.
   */
  get<T>(url: string, onSuccess: (data: T) => void, onError: (error: unknown) => void, params?: Record<string, string>): void {
    const httpParams = params ? new HttpParams({ fromObject: params }) : undefined;
    this.http.get<T>(url, { params: httpParams }).subscribe({ next: onSuccess, error: onError });
  }

  /**
   * Sends a POST request with a JSON body and resolves with a JSON response of type T.
   * @param url - Endpoint to request.
   * @param body - Request payload.
   * @param onSuccess - Called with the parsed response body.
   * @param onError - Called with the error if the request fails.
   */
  post<T>(url: string, body: unknown, onSuccess: (data: T) => void, onError: (error: unknown) => void): void {
    this.http.post<T>(url, body).subscribe({ next: onSuccess, error: onError });
  }

  /**
   * Sends a POST request and resolves with the raw binary response as a Blob.
   * Use this for endpoints that return a file (e.g. PDF/DOC reports) instead of JSON.
   * @param url - Endpoint to request.
   * @param body - Request payload.
   * @param onSuccess - Called with the response Blob.
   * @param onError - Called with the error if the request fails.
   */
  postBlob(url: string, body: unknown, onSuccess: (data: Blob) => void, onError: (error: unknown) => void): void {
    this.http.post(url, body, { responseType: 'blob' }).subscribe({ next: onSuccess, error: onError });
  }

  /**
   * Sends a PUT request with a JSON body and resolves with a JSON response of type T.
   * @param url - Endpoint to request.
   * @param body - Request payload.
   * @param onSuccess - Called with the parsed response body.
   * @param onError - Called with the error if the request fails.
   */
  put<T>(url: string, body: unknown, onSuccess: (data: T) => void, onError: (error: unknown) => void): void {
    this.http.put<T>(url, body).subscribe({ next: onSuccess, error: onError });
  }

  /**
   * Sends a DELETE request and resolves with a JSON response of type T.
   * @param url - Endpoint to request.
   * @param onSuccess - Called with the parsed response body.
   * @param onError - Called with the error if the request fails.
   */
  delete<T>(url: string, onSuccess: (data: T) => void, onError: (error: unknown) => void): void {
    this.http.delete<T>(url).subscribe({ next: onSuccess, error: onError });
  }

  /**
   * Sends a GET request with query string parameters and resolves with a JSON response of type T.
   * @param url - Endpoint to request.
   * @param params - Query string parameters.
   * @param onSuccess - Called with the parsed response body.
   * @param onError - Called with the error if the request fails.
   */
  search<T>(url: string, params: Record<string, string>, onSuccess: (data: T) => void, onError: (error: unknown) => void): void {
    const httpParams = new HttpParams({ fromObject: params });
    this.http.get<T>(url, { params: httpParams }).subscribe({ next: onSuccess, error: onError });
  }
}
