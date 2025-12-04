import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  username: string | null = '';

  constructor(private router: Router) {}

  ngOnInit() {
    const token = localStorage.getItem('token');
    this.username = localStorage.getItem('username');
    if (!token) {
      this.router.navigate(['/auth']);
    }
  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    this.router.navigate(['/auth']);
  }
}
