import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject, interval, switchMap, startWith } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private apiUrl = '/api/notifications';
  private unreadCountSubject = new BehaviorSubject<number>(0);
  public unreadCount$ = this.unreadCountSubject.asObservable();

  constructor(private http: HttpClient, private authService: AuthService) {
    // Poll notifications every 10 seconds only if logged in
    this.authService.currentUser$.subscribe(user => {
      if (user) {
        this.startPolling();
      } else {
        this.unreadCountSubject.next(0);
      }
    });
  }

  private startPolling() {
    // Standard reactive polling
    interval(10000).pipe(
      startWith(0),
      switchMap(() => {
        if (this.authService.isLoggedIn()) {
          return this.getUnreadCount();
        }
        return [0];
      })
    ).subscribe({
      next: count => this.unreadCountSubject.next(count),
      error: () => {} // Suppress background errors
    });
  }

  getNotifications(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  getUnreadCount(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/unread-count`);
  }

  markAsRead(id: number): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/${id}/read`, {}).pipe(
      tap(() => {
        const currentCount = this.unreadCountSubject.value;
        if (currentCount > 0) {
          this.unreadCountSubject.next(currentCount - 1);
        }
      })
    );
  }
}

// Helper custom tap operator to update local subject
import { tap } from 'rxjs/operators';
