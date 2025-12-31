import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { User } from "./user/user";
import { AuthencationDashboard } from "./authencation-dashboard/authencation-dashboard";
import { ViewDashboard } from "./view-dashboard/view-dashboard";

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('list-ui');
}
