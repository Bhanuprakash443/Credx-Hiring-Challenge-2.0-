import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { NotificationService } from '../services/notification.service';
import { AdminDashboardComponent } from './admin-dashboard/admin-dashboard.component';
import { CompanyDashboardComponent } from './company-dashboard/company-dashboard.component';
import { StudentDashboardComponent } from './student-dashboard/student-dashboard.component';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    AdminDashboardComponent,
    CompanyDashboardComponent,
    StudentDashboardComponent
  ],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  currentUser: any = null;
  notifications: any[] = [];
  unreadCount = 0;
  showNotifDropdown = false;

  constructor(
    private authService: AuthService,
    private notificationService: NotificationService,
    private router: Router
  ) {}

  ngOnInit() {
    this.currentUser = this.authService.currentUserValue;
    if (!this.currentUser) {
      this.router.navigate(['/login']);
      return;
    }

    this.notificationService.unreadCount$.subscribe(count => {
      this.unreadCount = count;
    });

    this.loadNotifications();
  }

  loadNotifications() {
    if (this.authService.isLoggedIn()) {
      this.notificationService.getNotifications().subscribe({
        next: data => {
          this.notifications = data;
        }
      });
    }
  }

  toggleNotifDropdown() {
    this.showNotifDropdown = !this.showNotifDropdown;
    if (this.showNotifDropdown) {
      this.loadNotifications();
    }
  }

  markAsRead(notif: any) {
    if (!notif.isRead) {
      this.notificationService.markAsRead(notif.id).subscribe({
        next: () => {
          notif.isRead = true;
          this.toggleNotifDropdown(); // Close or reload
        }
      });
    }
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
