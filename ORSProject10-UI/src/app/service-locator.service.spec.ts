import { TestBed } from '@angular/core/testing';

import { ServiceLocatorService } from './service-locator.service';

describe('ServiceLocatorService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ServiceLocatorService = TestBed.get(ServiceLocatorService);
    expect(service).toBeTruthy();
  });
});
