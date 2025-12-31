import { RouterModule, Routes } from '@angular/router';
import { AuthencationDashboard } from './authencation-dashboard/authencation-dashboard';
import { ViewDashboard } from './view-dashboard/view-dashboard';
import { NgModule } from '@angular/core';

export const routes: Routes = [
    { path: '', redirectTo: 'login', pathMatch: 'full' }, // Auto-redirect to login
    { path: 'login', component: AuthencationDashboard },
    { path: 'view', component:  ViewDashboard}
  ];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports:[RouterModule]
})
export class AppRoutingModule{}