import { Injectable } from '@angular/core';
import { ServiceLocator } from './service-locator';

@Injectable()
export class BaseService {

  protected url: string = '';

  protected supportsPreload = true;

  constructor(protected serviceLocator: ServiceLocator) { }

  /**
   * Fetches the entity at the base `url` (e.g. a list endpoint).
   * @param onSuccess - Called with the parsed response body.
   * @param onError - Called with the error if the request fails.
   */
  get<T>(onSuccess: (data: T) => void, onError: (error: any) => void): void {
    this.serviceLocator.http.get<T>(this.url, onSuccess, onError);
  }

  /**
   * Fetches a single entity by id.
   * @param id - Identifier of the entity to fetch.
   * @param onSuccess - Called with the parsed response body.
   * @param onError - Called with the error if the request fails.
   */
  getById<T>(id: number, onSuccess: (data: T) => void, onError: (error: any) => void): void {
    this.serviceLocator.http.get<T>(`${this.url}${id}/`, onSuccess, onError);
  }

  /**
   * Creates a new entity.
   * @param body - Entity payload to create.
   * @param onSuccess - Called with the parsed response body.
   * @param onError - Called with the error if the request fails.
   */
  add<T>(body: unknown, onSuccess: (data: T) => void, onError: (error: any) => void): void {
    this.serviceLocator.http.post<T>(this.url, body, onSuccess, onError);
  }

  /**
   * Searches entities and returns a JSON result set.
   * @param body - Search criteria, including pagination fields.
   * @param onSuccess - Called with the parsed response body.
   * @param onError - Called with the error if the request fails.
   */
  search<T>(body: unknown, onSuccess: (data: T) => void, onError: (error: any) => void): void {
    this.serviceLocator.http.post<T>(`${this.url}/search`, body, onSuccess, onError);
  }

  /**
   * Runs the same search as {@link search} but expects a binary report (e.g. PDF/DOC) instead of JSON.
   * @param body - Search criteria, including the requested `reportType`.
   * @param onSuccess - Called with the response file as a Blob.
   * @param onError - Called with the error if the request fails.
   */
  searchReport(body: unknown, onSuccess: (data: Blob) => void, onError: (error: any) => void): void {
    this.serviceLocator.http.postBlob(`${this.url}/report`, body, onSuccess, onError);
  }

  /**
   * Updates an existing entity by id.
   * @param id - Identifier of the entity to update.
   * @param body - Updated entity payload.
   * @param onSuccess - Called with the parsed response body.
   * @param onError - Called with the error if the request fails.
   */
  update<T>(id: number, body: unknown, onSuccess: (data: T) => void, onError: (error: any) => void): void {
    this.serviceLocator.http.put<T>(`${this.url}/${id}`, body, onSuccess, onError);
  }

  /**
   * Deletes an entity by id.
   * @param id - Identifier of the entity to delete.
   * @param onSuccess - Called with the parsed response body.
   * @param onError - Called with the error if the request fails.
   */
  delete<T>(id: number, onSuccess: (data: T) => void, onError: (error: any) => void): void {
    this.serviceLocator.http.delete<T>(`${this.url}/${id}`, onSuccess, onError);
  }

  /**
   * Loads dropdown/reference data used to populate the entity's form, if supported.
   * @param onSuccess - Called with the parsed response body.
   * @param onError - Called with the error if the request fails.
   */
  preload<T>(onSuccess: (data: T) => void, onError: (error: any) => void): void {
    console.log('calling base service preload' + `${this.url}/preload`);
    if (!this.supportsPreload) return;
    this.serviceLocator.http.get<T>(`${this.url}/preload`, onSuccess, onError);
  }
}
