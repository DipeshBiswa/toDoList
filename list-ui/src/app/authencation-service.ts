import { HttpClient, HttpErrorResponse, HttpParams, HttpHeaders } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, catchError, map, Observable, of, Subject, tap } from 'rxjs';

const URL = 'http://localhost:8080/user';

@Injectable({
  providedIn: 'root',
})
export class AuthenticationService {
  private readonly client = inject(HttpClient);
  private readonly router = inject(Router);

  // Checks login status by calling checkToken (which looks at cookies)
  readonly loggedIn$ = this.readStatus('checkToken').pipe(
    map(status => status === 200)
  );

  private readonly loginFailedSubject = new Subject<void>();
  readonly loginFailed$ = this.loginFailedSubject.asObservable();

  private readonly reloadTrigger = new BehaviorSubject<void>(undefined);
  readonly reload$ = this.reloadTrigger.asObservable();

  /**
   * Helper to check status endpoints like /checkToken or /me
   * No parameters needed because Java uses @CookieValue
   */
  private readStatus(path: string): Observable<number> {
    return this.client
      .get(`${URL}/${path}`, {
        observe: 'response',
        withCredentials: true, // Sends the sessionToken cookie automatically
      })
      .pipe(
        map(res => res.status),
        catchError((error: HttpErrorResponse) => of(error.status ?? 0))
      );
  }

  private reloadUser(): void {
    this.reloadTrigger.next();
  }

  /**
   * LOGIN
   * Sends credentials as form-data (application/x-www-form-urlencoded)
   * which matches Spring's @RequestParam.
   */
  login(username: string, password: string): Observable<any> {
    const body = new HttpParams()
      .set('username', username)
      .set('password', password);
    
    return this.client.post(`${URL}/authenticate`, body, {
      observe: 'response',
      withCredentials: true,
    }).pipe(
      tap({
        next: () => this.reloadUser(),
        error: () => {
          this.loginFailedSubject.next();
          this.reloadUser();
        }
      })
    );
  }

  /**
   * REGISTER
   */
  register(username: string, password: string): Observable<any> {
    const body = new HttpParams()
      .set('username', username)
      .set('password', password);
    
    return this.client.post(`${URL}/create`, body, {
      observe: 'response',
      withCredentials: true,
    }).pipe(
      tap({
        next: () => this.reloadUser(),
        error: () => this.reloadUser(),
      })
    );
  }

  /**
   * LOGOUT
   * No parameters needed in the call because Java uses @CookieValue.
   */
  logout(): Observable<any> {
    return this.client.post(`${URL}/logout`, null, {
      observe: 'response',
      withCredentials: true, // Browser sends cookie; Java @CookieValue receives it
    }).pipe(
      tap({
        next: () => {
          this.reloadUser();
          this.router.navigate(['/login']);
        },
        error: () => this.reloadUser(),
      })
    );
  }
}