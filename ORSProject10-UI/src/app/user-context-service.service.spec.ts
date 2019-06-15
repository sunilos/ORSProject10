import { TestBed } from '@angular/core/testing';

import { UserContextServiceService } from './user-context-service.service';

describe('UserContextServiceService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: UserContextServiceService = TestBed.get(UserContextServiceService);
    expect(service).toBeTruthy();
  });
});
