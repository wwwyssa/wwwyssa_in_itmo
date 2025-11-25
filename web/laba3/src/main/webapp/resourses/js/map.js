let currentR = 3;

document.addEventListener("DOMContentLoaded", () => {
    redrawGraph();
});

function updateR(val) {
    currentR = parseFloat(val);
    redrawGraph();
}

function redrawGraph() {
    const canvas = document.getElementById('mapCanvas');
    if (!canvas) return;

    // --- ВАЖНО: Навешиваем обработчик клика при каждой перерисовке ---
    canvas.onclick = function(event) {
        handleCanvasClick(event, canvas);
    };

    const ctx = canvas.getContext('2d');
    const width = canvas.width;
    const height = canvas.height;
    const centerX = width / 2;
    const centerY = height / 2;
    const scale = 30;

    ctx.clearRect(0, 0, width, height);

    // 1. Рисуем Область (Синяя)
    // Используем логику отрисовки из вашего исходного кода
    ctx.fillStyle = '#4A90E2';
    ctx.beginPath();

    // Сектор (Четверть круга)
    ctx.moveTo(centerX, centerY);
    ctx.arc(centerX, centerY, (currentR / 2) * scale, -Math.PI / 2, 0, false);
    ctx.fill();

    // Треугольник
    ctx.beginPath();
    ctx.moveTo(centerX, centerY);
    ctx.lineTo(centerX - (currentR / 2) * scale, centerY);
    ctx.lineTo(centerX, centerY + currentR * scale);
    ctx.fill();

    // Прямоугольник
    ctx.beginPath();
    ctx.rect(centerX, centerY, currentR * scale, (currentR / 2) * scale);
    ctx.fill();
    ctx.closePath();

    // 2. Оси
    ctx.strokeStyle = 'black';
    ctx.lineWidth = 1;
    ctx.beginPath();
    ctx.moveTo(0, centerY);
    ctx.lineTo(width, centerY);
    ctx.moveTo(centerX, 0);
    ctx.lineTo(centerX, height);
    ctx.stroke();

    // 3. Деления осей
    ctx.font = '10px Arial';
    ctx.fillStyle = 'black';

    for (let i = -5; i <= 5; i++) {
        if (i === 0) continue;
        // Ось X
        const x = centerX + i * scale;
        ctx.beginPath();
        ctx.moveTo(x, centerY - 5);
        ctx.lineTo(x, centerY + 5);
        ctx.stroke();
        ctx.fillText(i, x - 3, centerY + 15);

        // Ось Y
        const y = centerY - i * scale;
        ctx.beginPath();
        ctx.moveTo(centerX - 5, y);
        ctx.lineTo(centerX + 5, y);
        ctx.stroke();
        ctx.fillText(i, centerX + 8, y + 3);
    }

    // 4. Отрисовка точек с сервера
    if (window.serverPoints) {
        window.serverPoints.forEach(p => {
            // Рисуем точку, только если её R совпадает с текущим (опционально)
            // Если нужно видеть все точки, уберите условие if
            if (Math.abs(p.r - currentR) > 0.01) return;

            const pX = centerX + p.x * scale;
            const pY = centerY - p.y * scale;

            ctx.beginPath();
            ctx.arc(pX, pY, 4, 0, 2 * Math.PI);
            ctx.fillStyle = p.hit ? 'green' : 'red';
            ctx.fill();
            ctx.strokeStyle = '#000';
            ctx.stroke();
        });
    }
}

function handleCanvasClick(event, canvas) {
    if (!currentR) {
        alert("Выберите R!");
        return;
    }

    const rect = canvas.getBoundingClientRect();
    const xCanvas = event.clientX - rect.left;
    const yCanvas = event.clientY - rect.top;

    const centerX = canvas.width / 2;
    const centerY = canvas.height / 2;
    const scale = 30;

    // Перевод координат экрана в координаты графика
    const x = (xCanvas - centerX) / scale;
    const y = (centerY - yCanvas) / scale;

    console.log(`Click: ${x.toFixed(3)}, ${y.toFixed(3)}`);

    // Заполнение скрытых полей JSF
    // (Ищем элементы по ID формы 'mainForm')
    const xInput = document.getElementById('mainForm:xCanvas');
    const yInput = document.getElementById('mainForm:yCanvas');
    const rInput = document.getElementById('mainForm:rCanvas');

    if (xInput && yInput && rInput) {
        xInput.value = x.toFixed(3);
        yInput.value = y.toFixed(3);
        rInput.value = currentR;

        // Вызов PrimeFaces RemoteCommand
        if (typeof addPointFromCanvas === 'function') {
            addPointFromCanvas();
        }
    } else {
        console.error("Скрытые поля формы не найдены. Проверьте main.xhtml");
    }
}