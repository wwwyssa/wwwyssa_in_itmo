import { Component, ChangeDetectorRef, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

interface AuthResponse {
  success: boolean;
  username: string;
  message: string;
  token?: string;
}

@Component({
  selector: 'app-auth',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.css']
})


export class AuthComponent {
  isLoginMode = true;
  username = '';
  password = '';
  errorMessage = '';
  successMessage = '';



  constructor(
    private http: HttpClient,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  toggleMode() {
    this.isLoginMode = !this.isLoginMode;
    this.errorMessage = '';
    this.successMessage = '';
    this.username = '';
    this.password = '';
  }

  onSubmit() {
    if (!this.username || !this.password) {
      this.errorMessage = 'Введите имя пользователя и пароль';
      return;
    }

    this.errorMessage = '';
    this.successMessage = '';

    const endpoint = this.isLoginMode ? '/auth/login' : '/auth/register';
    const body = {
      username: this.username,
      password: this.password
    };

    this.http.post<AuthResponse>(`http://localhost:8080${endpoint}`, body, { withCredentials: true })
      .subscribe({
        next: (response) => {

          if (response && response.success === true && response.token) {
            localStorage.setItem('token', response.token);
            localStorage.setItem('username', response.username);
            this.successMessage = response.message;


            this.router.navigate(['/']);

          } else {
            this.errorMessage = response?.message || 'Ошибка авторизации';
            this.password = '';
            this.cdr.detectChanges();
       }
        }
      });
  }
}
