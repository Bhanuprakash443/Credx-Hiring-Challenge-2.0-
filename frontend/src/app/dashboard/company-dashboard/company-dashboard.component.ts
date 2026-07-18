import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormsModule } from '@angular/forms';
import { JobService } from '../../services/job.service';
import { ApplicationService } from '../../services/application.service';

@Component({
  selector: 'app-company-dashboard',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule],
  templateUrl: './company-dashboard.component.html',
  styleUrls: ['./company-dashboard.component.css']
})
export class CompanyDashboardComponent implements OnInit {
  jobs: any[] = [];
  applications: any[] = [];
  jobForm: FormGroup;
  showPostModal = false;
  showStatusModal = false;
  selectedApplication: any = null;
  statusNotes = '';
  targetStatus = '';
  loading = true;

  constructor(
    private fb: FormBuilder,
    private jobService: JobService,
    private applicationService: ApplicationService
  ) {
    this.jobForm = this.fb.group({
      title: ['', Validators.required],
      description: ['', Validators.required],
      requirements: [''],
      minCgpa: [7.0, [Validators.required, Validators.min(0), Validators.max(10)]],
      salary: ['', Validators.required],
      location: ['', Validators.required],
      deadline: ['', Validators.required]
    });
  }

  ngOnInit() {
    this.loadData();
  }

  loadData() {
    this.loading = true;
    this.jobService.getJobs().subscribe({
      next: jobs => {
        this.jobs = jobs;
        this.loading = false;
      },
      error: () => this.loading = false
    });

    this.applicationService.getApplications().subscribe({
      next: apps => {
        this.applications = apps;
      }
    });
  }

  openPostModal() {
    this.jobForm.reset({
      minCgpa: 7.0
    });
    this.showPostModal = true;
  }

  closePostModal() {
    this.showPostModal = false;
  }

  onSubmitJob() {
    if (this.jobForm.invalid) return;
    this.jobService.createJob(this.jobForm.value).subscribe({
      next: () => {
        this.closePostModal();
        this.loadData();
      }
    });
  }

  closeJob(id: number) {
    this.jobService.updateJobStatus(id, 'CLOSED').subscribe({
      next: () => {
        this.loadData();
      }
    });
  }

  openStatusModal(app: any, status: string) {
    this.selectedApplication = app;
    this.targetStatus = status;
    this.statusNotes = app.notes || '';
    this.showStatusModal = true;
  }

  closeStatusModal() {
    this.selectedApplication = null;
    this.targetStatus = '';
    this.statusNotes = '';
    this.showStatusModal = false;
  }

  submitStatusUpdate() {
    if (!this.selectedApplication) return;
    this.applicationService.updateStatus(
      this.selectedApplication.id,
      this.targetStatus,
      this.statusNotes
    ).subscribe({
      next: () => {
        this.closeStatusModal();
        this.loadData();
      }
    });
  }
}
