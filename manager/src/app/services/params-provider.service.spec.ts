import { TestBed } from '@angular/core/testing';

import { ParamsProviderService } from './params-provider.service';

describe('ParamsProviderService', () => {
  let service: ParamsProviderService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ParamsProviderService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
