import { TestBed } from '@angular/core/testing';

import { View } from './view';

describe('View', () => {
  let service: View;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(View);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
