import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, catchError, map, Observable, of, Subject } from 'rxjs';

const URL = 'http://localhost:8080/user'
@Injectable({
  providedIn: 'root',
})
export class AuthenticationService {

  loggedIn$ = this.readStatus('checkToken').pipe(
    map(status => status === 200)
  );

  private readonly loginFailedSubject = new Subject<void>();
  readonly loginFailed$ = this.loginFailedSubject.asObservable();

  private readonly reloadTrigger = new BehaviorSubject<void>(undefined);
  readonly reload$ = this.reloadTrigger.asObservable();

  constructor(private client: HttpClient) {}

  private readStatus(path: string): Observable<number> {
    return this.client
      .get(`${URL}/${path}`, {
        observe: 'response',
        withCredentials: true,
      })
      .pipe(
        map(res => res.status),
        catchError((error: HttpErrorResponse) => of(error.status ?? 0))
      );
  }

  private reloadUser(): void {
    this.reloadTrigger.next();
  }

  login(username: string, password: string): void {
    this.client
      .post(`${URL}/authenticate`, { username, password }, {
        observe: 'response',
        withCredentials: true,
      })
      .subscribe({
        next: () => this.reloadUser(),
        error: () => {
          this.loginFailedSubject.next();
          this.reloadUser();
        },
      });
  }

  register(username: string, password: string): void {
    this.client
      .post(`${URL}/create`, { username, password }, {
        observe: 'response',
        withCredentials: true,
      })
      .subscribe({
        next: () => this.reloadUser(),
        error: () => this.reloadUser(),
      });
  }

  logout(): void {
    this.client
      .post(`${URL}/logout`, {}, {
        observe: 'response',
        withCredentials: true,
      })
      .subscribe({
        next: () => this.reloadUser(),
        error: () => this.reloadUser(),
      });
  }
}
