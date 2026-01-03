import { TestBed } from '@angular/core/testing';

import { AuthenticationService } from './authencation-service';

describe('AuthencationService', () => {
  let service: AuthenticationService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AuthenticationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
