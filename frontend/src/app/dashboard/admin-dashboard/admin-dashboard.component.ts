import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AnalyticsService } from '../../services/analytics.service';
import { JobService } from '../../services/job.service';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css']
})
export class AdminDashboardComponent implements OnInit {
  analytics: any = null;
  pendingJobs: any[] = [];
  allJobs: any[] = [];
  selectedJob: any = null;
  loading = true;

  constructor(
    private analyticsService: AnalyticsService,
    private jobService: JobService
  ) {}

  ngOnInit() {
    this.loadData();
  }

  loadData() {
    this.analyticsService.getDashboardAnalytics().subscribe({
      next: data => {
        this.analytics = data;
        this.loading = false;
      },
      error: () => this.loading = false
    });

    this.jobService.getJobs().subscribe({
      next: jobs => {
        this.allJobs = jobs;
        this.pendingJobs = jobs.filter(j => j.status === 'PENDING');
      }
    });
  }

  approveJob(id: number) {
    this.jobService.updateJobStatus(id, 'APPROVED').subscribe({
      next: () => {
        this.loadData();
      }
    });
  }

  rejectJob(id: number) {
    this.jobService.updateJobStatus(id, 'REJECTED').subscribe({
      next: () => {
        this.loadData();
      }
    });
  }

  openJobDetails(job: any) {
    this.selectedJob = job;
  }

  closeModal() {
    this.selectedJob = null;
  }
}
