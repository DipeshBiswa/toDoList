import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AuthencationDashboard } from './authencation-dashboard';

describe('AuthencationDashboard', () => {
  let component: AuthencationDashboard;
  let fixture: ComponentFixture<AuthencationDashboard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AuthencationDashboard]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AuthencationDashboard);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
