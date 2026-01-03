import { Component, OnInit, OnDestroy } from '@angular/core';
import { Basket } from '../basket';
import { View } from '../view';
import { ModalComponent } from '../modal/modal';
import { CommonModule } from '@angular/common';
import { AuthenticationService } from '../authencation-service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-view-dashboard',
  standalone: true,
  imports: [CommonModule, ModalComponent],
  templateUrl: './view-dashboard.html',
  styleUrl: './view-dashboard.css',
})
export class ViewDashboard implements OnInit, OnDestroy {
  basket: Basket | null = null;
  showModal = false;
  private basketSub!: Subscription;

  constructor(private viewService: View, private authService: AuthenticationService) {}

  ngOnInit() {
    // Initial load
    this.loadBasket();

    // Listen for changes (Adds/Deletes) and refresh the list automatically
    this.basketSub = this.viewService.basketChange.subscribe(() => {
      this.loadBasket();
    });
  }

  loadBasket() {
    this.viewService.getBasket().subscribe({
      next: (data) => {
        this.basket = data;
        console.log('Basket updated:', this.basket);
      },
      error: (err) => console.error('Could not load basket:', err)
    });
  }

  createTask(name: string, description: string) {
    if (!name || !description) return;
    
    this.viewService.addTaskToBasket(name, description).subscribe({
      next: () => {
        this.showModal = false; // Close the modal
        // Note: loadBasket() is called automatically via the basketChange subscription!
      },
      error: (err) => console.error('Error adding task:', err)
    });
  }

  deleteTask(id: number) {
    this.viewService.removeTaskFromBasket(id).subscribe();
  }

  logout() {
    this.authService.logout().subscribe();
  }

  ngOnDestroy() {
    if (this.basketSub) {
      this.basketSub.unsubscribe();
    }
  }
}