import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  username: string | null = '';
  canvas: HTMLCanvasElement | null = null;
  ctx: CanvasRenderingContext2D | null = null;
  R = 100;
  x: number | null = null;
  y: number = 0;
  rValue: number | null = null;
  xValues = [-3, -2, -1, 0, 1, 2, 3, 4, 5];
  rValues = [0, 1, 2, 3, 4, 5];
  points: Array<{ x: number; y: number; r: number; hit: boolean }> = [];

  constructor(private router: Router, private http: HttpClient) {}

  ngOnInit() {
    const token = localStorage.getItem('token');
    this.username = localStorage.getItem('username');
    if (!token) {
      this.router.navigate(['/auth']);
      return;
    }
    
    setTimeout(() => {
      this.initCanvas();
      this.loadPoints();
    }, 100);
  }

  loadPoints() {
    const token = localStorage.getItem('token');
    const headers: any = {};
    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    }

    this.http.get('http://localhost:8080/api/points', { headers }).subscribe({
      next: (response: any) => {
        console.log('Points loaded:', response);
        this.points = response.map((point: any) => ({
          x: point.x,
          y: point.y,
          r: point.r,
          hit: point.hit
        }));
        this.drawGraph();
      },
      error: (error) => {
        console.error('Error loading points:', error);
      }
    });
  }

  initCanvas() {
    this.canvas = document.getElementById('canvas') as HTMLCanvasElement;
    if (this.canvas) {
      this.ctx = this.canvas.getContext('2d');
      this.canvas.addEventListener('click', (event) => this.onCanvasClick(event));
      this.drawGraph();
    }
  }

  onCanvasClick(event: MouseEvent) {
    if (!this.canvas || this.rValue === null) {
      alert('Выберите значение R перед кликом на график');
      return;
    }

    const rect = this.canvas.getBoundingClientRect();
    const clickX = event.clientX - rect.left;
    const clickY = event.clientY - rect.top;

    const centerX = this.canvas.width / 2;
    const centerY = this.canvas.height / 2;

    // Преобразуем экранные координаты в координаты графика
    const graphX = (clickX - centerX) / 60;
    const graphY = (centerY - clickY) / 60;

    console.log(`Clicked at: x=${graphX.toFixed(2)}, y=${graphY.toFixed(2)}, r=${this.rValue}`);

    // Отправляем точку на сервер
    this.savePointFromClick(graphX, graphY);
  }

  savePointFromClick(x: number, y: number) {
    if (this.rValue === null) {
      return;
    }

    const payload = {
      x: parseFloat(x.toFixed(2)),
      y: parseFloat(y.toFixed(2)),
      r: this.rValue
    };

    const token = localStorage.getItem('token');
    const headers: any = {};
    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    }

    this.http.post('http://localhost:8080/api/points', payload, { headers }).subscribe({
      next: (response: any) => {
        console.log('Point saved:', response);
        this.points.push({
          x: response.x,
          y: response.y,
          r: response.r,
          hit: response.hit
        });
        this.drawGraph();
      },
      error: (error) => {
        console.error('Error saving point:', error);
        alert('Ошибка при сохранении точки: ' + (error.error?.message || error.message));
      }
    });
  }

  drawGraph() {
    if (!this.ctx || !this.canvas) return;

    const w = this.canvas.width;
    const h = this.canvas.height;
    const centerX = w / 2;
    const centerY = h / 2;

    this.ctx.fillStyle = 'white';
    this.ctx.fillRect(0, 0, w, h);

    this.drawAxes(centerX, centerY);

    this.ctx.fillStyle = 'rgba(100, 150, 255, 0.3)';
    this.ctx.strokeStyle = 'blue';
    this.ctx.lineWidth = 2;

    this.drawCircleQuarter(centerX, centerY);

    this.drawRectangle(centerX, centerY);

    this.drawTriangle(centerX, centerY);
    
    // Рисуем все сохраненные точки
    this.drawPoints(centerX, centerY);
  }
  
  drawPoints(centerX: number, centerY: number) {
    if (!this.ctx) return;
    
    this.points.forEach(point => {
      if (point.r != this.rValue) {
        return;
      }
      const screenX = centerX + point.x * 60;
      const screenY = centerY - point.y * 60;
      
      this.ctx!.fillStyle = point.hit ? 'green' : 'red';
      this.ctx!.beginPath();
      this.ctx!.arc(screenX, screenY, 5, 0, Math.PI * 2);
      this.ctx!.fill();
      this.ctx!.strokeStyle = 'darkblue';
      this.ctx!.lineWidth = 2;
      this.ctx!.stroke();
    });
  }

  drawAxes(centerX: number, centerY: number) {
    if (!this.ctx) return;

    this.ctx.strokeStyle = '#333';
    this.ctx.lineWidth = 1;
    this.ctx.beginPath();
    this.ctx.moveTo(0, centerY);
    this.ctx.lineTo(this.canvas!.width, centerY);
    this.ctx.stroke();

    this.ctx.beginPath();
    this.ctx.moveTo(centerX, 0);
    this.ctx.lineTo(centerX, this.canvas!.height);
    this.ctx.stroke();

    this.ctx.strokeStyle = '#333';
    this.ctx.fillStyle = '#333';
    this.ctx.font = '12px Arial';
    this.ctx.fillText('X', this.canvas!.width - 20, centerY + 15);
    this.ctx.fillText('Y', centerX + 10, 15);


    this.ctx.beginPath();
    this.ctx.moveTo(centerX, centerY - this. R);
    this.ctx.lineTo(centerX - 10, centerY - this.R);
    this.ctx.lineTo(centerX + 10, centerY - this.R);
    this.ctx.stroke();
    this.ctx.fill();

    this.ctx.fillText('R', centerX + this.R, centerY  + 20);
    this.ctx.fillText('R', centerX + 5, centerY - this.R);

    this.ctx.beginPath();
    this.ctx.moveTo(centerX + this.R, centerY);
    this.ctx.lineTo(centerX + this.R, centerY - 10);
    this.ctx.lineTo(centerX + this.R, centerY + 10);
    this.ctx.stroke();
    this.ctx.fill();
  }

  drawCircleQuarter(centerX: number, centerY: number) {
    if (!this.ctx) return;

    this.ctx.beginPath();
    this.ctx.moveTo(centerX, centerY);
    this.ctx.arc(centerX, centerY, this.R, -Math.PI / 2, 0, false);
    this.ctx.closePath();
    this.ctx.fill();
  }

  drawRectangle(centerX: number, centerY: number) {
    if (!this.ctx) return;
    this.ctx.fillRect(centerX, centerY, this.R, this.R);
  }

  drawTriangle(centerX: number, centerY: number) {
    if (!this.ctx) return;

    const p1 = [centerX, centerY];
    const p2 = [centerX - this.R, centerY];
    const p3 = [centerX, centerY + this.R];

    this.ctx.beginPath();
    this.ctx.moveTo(p1[0], p1[1]);
    this.ctx.lineTo(p2[0], p2[1]);
    this.ctx.lineTo(p3[0], p3[1]);
    this.ctx.closePath();
    this.ctx.fill();
  }

  selectX(value: number) {
    this.x = value;
  }

  selectR(value: number) {
    this.rValue = value;
    this.R = value * 60;
    this.drawGraph();
  }

  submitPoint() {
    if (this.x === null || this.rValue === null) {
      alert('Выберите значения X и R');
      return;
    }
    if (this.y < -3 || this.y > 3) {
      alert('Y должен быть в диапазоне от -3 до 3');
      return;
    }
    
    this.savePointFromClick(this.x, this.y);
  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    this.router.navigate(['/auth']);
  }
}

