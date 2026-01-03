import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewDashboard } from './view-dashboard';

describe('ViewDashboard', () => {
  let component: ViewDashboard;
  let fixture: ComponentFixture<ViewDashboard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ViewDashboard]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ViewDashboard);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
