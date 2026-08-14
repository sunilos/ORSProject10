import { TestBed } from '@angular/core/testing';

import { ServiceLocator } from './service-locator';

describe('ServiceLocator', () => {
  let service: ServiceLocator;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ServiceLocator);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
