import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  loginForm: FormGroup;
  errorMsg = '';
  loading = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  onSubmit() {
    if (this.loginForm.invalid) return;
    this.loading = true;
    this.errorMsg = '';
    
    this.authService.login(this.loginForm.value).subscribe({
      next: () => {
        this.router.navigate(['/dashboard']);
      },
      error: err => {
        this.loading = false;
        this.errorMsg = typeof err.error === 'string' ? err.error : 'Login failed. Please check credentials.';
      }
    });
  }

  quickLogin(role: string) {
    let credentials = { username: '', password: 'password' };
    if (role === 'admin') {
      credentials = { username: 'admin', password: 'admin123' };
    } else if (role === 'student-rahul') {
      credentials = { username: 'rahul', password: 'password' };
    } else if (role === 'student-priya') {
      credentials = { username: 'priya', password: 'password' };
    } else if (role === 'student-amit') {
      credentials = { username: 'amit', password: 'password' };
    } else if (role === 'company-tech') {
      credentials = { username: 'techcorp', password: 'password' };
    } else if (role === 'company-innovate') {
      credentials = { username: 'innovatesoft', password: 'password' };
    }

    this.loading = true;
    this.errorMsg = '';
    this.authService.login(credentials).subscribe({
      next: () => {
        this.router.navigate(['/dashboard']);
      },
      error: err => {
        this.loading = false;
        this.errorMsg = typeof err.error === 'string' ? err.error : 'Quick Login failed.';
      }
    });
  }
}
