import { HttpClient, HttpParams } from '@angular/common/http'; // Added HttpParams
import { Injectable, inject } from '@angular/core';
import { Task } from './Task';
import { catchError, map, Observable, of, Subject, tap } from 'rxjs';
import { Basket } from './basket';

@Injectable({
  providedIn: 'root',
})
export class View {
  private readonly http = inject(HttpClient);
  private readonly basketApi = 'http://localhost:8080/basket';
  
  basketChange: Subject<void> = new Subject<void>();

  private log(message: string) {
    console.log(`BasketService: ${message}`);
  }

  /**
   * REMOVE TASK
   * Path: /basket/items/{taskId}
   */
  removeTaskFromBasket(id: number): Observable<any> {
    const url = `${this.basketApi}/items/${id}`;
    return this.http.delete(url, { withCredentials: true }).pipe(
      tap(() => {
        this.log(`deleted task id=${id}`);
        this.basketChange.next();
      }),
      catchError(this.handleError('delete task'))
    );
  }

  /**
   * ADD TASK
   * Uses HttpParams to safely encode characters like spaces or symbols
   */
  addTaskToBasket(name: string, description: string): Observable<any> {
    const url = `${this.basketApi}/items`;
    
    // Safely encode parameters
    const params = new HttpParams()
      .set('name', name)
      .set('description', description);

    return this.http.post(url, null, { 
      params, 
      withCredentials: true 
    }).pipe(
      tap(() => {
        this.log('added task to basket');
        this.basketChange.next();
      }),
      catchError(this.handleError('Add task failed'))
    );
  }

  /**
   * GET TASKS
   */
  getTaskFromBasket(): Observable<Task[]> {
    const url = `${this.basketApi}/items`;
    return this.http.get<Task[]>(url, { withCredentials: true }).pipe(
      tap(() => this.log('fetched basket tasks')),
      catchError(this.handleError<Task[]>('get tasks', []))
    );
  }

  /**
   * GET FULL BASKET OBJECT
   */
  getBasket(): Observable<Basket> {
    return this.getTaskFromBasket().pipe(
      map((tasks) => ({ tasks }))
    );
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(`${operation} failed:`, error);
      return of(result as T);
    };
  }
}