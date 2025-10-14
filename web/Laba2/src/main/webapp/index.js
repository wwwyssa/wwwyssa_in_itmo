"use strict";

const state = {
    x: NaN,
    y: NaN,
    r: 3.0,
};

document.addEventListener('DOMContentLoaded', function() {
    initializeApp();
});

function initializeApp() {
    const canvas = document.getElementById("graphCanvas");
    const ctx = canvas.getContext("2d");

    const rInput = document.getElementById("r");
    if (rInput && !rInput.value) {
        rInput.value = "3";
        state.r = 3.0;
    }

    drawGraph(canvas, ctx);
    setupEventListeners(canvas, ctx);
}

function setupEventListeners(canvas, ctx) {
    const xSelect = document.getElementById("x");
    if (xSelect) {
        xSelect.addEventListener("change", (ev) => {
            const value = ev.target.value;
            if (value === "") {
                state.x = NaN;
            } else {
                const xValue = parseFloat(value);
                state.x = xValue;
                hideError();
            }
        });
    }

    const yInput = document.getElementById("y");
    if (yInput) {
        yInput.addEventListener("change", (ev) => {
            const value = ev.target.value.replace(',', '.');

            if (!/^[-+]?\d*([.]\d+)?$/.test(value)) {
                showError("Y должен быть числом! Дробные числа вводить через точку");
                ev.target.value = "";
                state.y = NaN;
            } else {
                const yValue = parseFloat(value);
                if (yValue < -5 || yValue > 5) {
                    showError("Y должен быть от -5 до 5");
                    ev.target.value = "";
                    state.y = NaN;
                } else {
                    state.y = yValue;
                    hideError();
                }
            }
        });
    }

    const rInput = document.getElementById("r");
    if (rInput) {
        rInput.addEventListener("change", (ev) => {
            const value = ev.target.value.replace(',', '.');

            if (!/^[-+]?\d*([.]\d+)?$/.test(value)) {
                showError("R должен быть числом! Дробные числа вводить через точку");
                ev.target.value = "";
                state.r = NaN;
            } else {
                const rValue = parseFloat(value);
                const allowedR = [1, 2, 3, 4, 5];
                if (!allowedR.includes(rValue)) {
                    showError("R должен быть одним из значений: 1, 2, 3, 4, 5");
                    ev.target.value = "";
                    state.r = NaN;
                } else {
                    state.r = rValue;
                    hideError();
                    drawGraph(canvas, ctx);
                }
            }
        });
    }


    const form = document.getElementById("pointForm");
    if (form) {
        form.addEventListener("submit", async function (ev) {
            ev.preventDefault();
            await submitForm();
        });
    }

    if (canvas) {
        canvas.addEventListener('click', (ev) => {
            handleCanvasClick(ev, canvas, ctx);
        });
    }
}

function handleCanvasClick(ev, canvas, ctx) {
    if (isNaN(state.r) || state.r < 1 || state.r > 5) {
        showError("Сначала установите радиус R (от 1 до 5)");
        return;
    }

    const rect = canvas.getBoundingClientRect();
    const x = ev.clientX - rect.left;
    const y = ev.clientY - rect.top;

    const scale = 50;
    const graphX = ((x - canvas.width / 2) / scale);
    const graphY = ((canvas.height / 2 - y) / scale);

    const roundedX = Math.round(graphX * 100) / 100;
    const roundedY = Math.round(graphY * 100) / 100;

    state.x = roundedX;
    state.y = roundedY;

    const xSelect = document.getElementById("x");
    const yInput = document.getElementById("y");

    if (xSelect) {
        xSelect.value = state.x.toFixed(2);
    }

    if (yInput) {
        yInput.value = state.y.toFixed(2);
    }

    submitForm();
}

async function submitForm() {
    if (!validateState(state)) {
        return;
    }

    try {
        const params = new URLSearchParams();
        params.append('x', state.x.toString());
        params.append('y', state.y.toString());
        params.append('r', state.r.toString());

        const response = await fetch("controller", {
            method: "POST",
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: params
        });

        if (response.ok) {
            window.location.href = "result.jsp";
        } else {
            showError("Ошибка сервера");
        }
    } catch (error) {
        showError("Ошибка соединения с сервером");
    }
}

function validateState(state) {
    hideError();

    if (isNaN(state.x) || state.x < -5 || state.x > 5) {
        showError("X должен быть от -5 до 5");
        return false;
    }

    if (isNaN(state.y) || state.y < -5 || state.y > 5) {
        showError("Y должен быть от -5 до 5");
        return false;
    }

    if (isNaN(state.r) || state.r < 1 || state.r > 5) {
        showError("R должен быть от 1 до 5");
        return false;
    }

    const allowedR = [1, 2, 3, 4, 5];
    if (!allowedR.includes(state.r)) {
        showError("R должен быть одним из значений: 1, 2, 3, 4, 5");
        return false;
    }

    return true;
}

function showError(message) {
    const errorDiv = document.getElementById("error");
    if (errorDiv) {
        errorDiv.textContent = message;
        errorDiv.style.display = "block";
        return;
    }

    const notification = document.getElementById("notification");
    if (notification) {
        notification.textContent = message;
        notification.className = "notification error";
        notification.style.display = 'block';

        return;
    }
ч
    alert(message);
}

function hideError() {
    const errorDiv = document.getElementById("error");
    if (errorDiv) {
        errorDiv.style.display = "none";
    }

    const notification = document.getElementById("notification");
    if (notification) {
        notification.style.display = "none";
    }
}

function drawGraph(canvas, ctx) {
    if (!canvas || !ctx) {
        return;
    }

    const width = canvas.width;
    const height = canvas.height;
    const r = isNaN(state.r) ? 3 : state.r;
    const scale = 50;

    ctx.clearRect(0, 0, width, height);
    ctx.fillStyle = "#ffffff";
    ctx.fillRect(0, 0, width, height);

    drawGridAndAxes(ctx, width, height, r, scale);
    drawHitArea(ctx, width, height, r, scale);
    drawPoints(ctx, width, height, r, scale);
}

function drawPoints(ctx, width, height, r, scale) {

    if (typeof sessionPoints === 'undefined' || !sessionPoints.length) {
        return;
    }

    const pointsToDraw = sessionPoints.filter(point => point.r === r);

    pointsToDraw.forEach(point => {
        const canvasX = width / 2 + (point.x * scale);
        const canvasY = height / 2 - (point.y * scale);


        if (canvasX >= 0 && canvasX <= width && canvasY >= 0 && canvasY <= height) {

            ctx.fillStyle = point.inside ? "green" : "red";
            ctx.beginPath();
            ctx.arc(canvasX, canvasY, 5, 0, 2 * Math.PI);
            ctx.fill();
        }
    });
}

function drawGridAndAxes(ctx, width, height, r, scale) {
    ctx.strokeStyle = "#e0e0e0";
    ctx.lineWidth = 1;

    for (let x = width/2; x >= 0; x -= scale) {
        ctx.beginPath();
        ctx.moveTo(x, 0);
        ctx.lineTo(x, height);
        ctx.stroke();
    }
    for (let x = width/2; x <= width; x += scale) {
        ctx.beginPath();
        ctx.moveTo(x, 0);
        ctx.lineTo(x, height);
        ctx.stroke();
    }

    for (let y = height/2; y >= 0; y -= scale) {
        ctx.beginPath();
        ctx.moveTo(0, y);
        ctx.lineTo(width, y);
        ctx.stroke();
    }
    for (let y = height/2; y <= height; y += scale) {
        ctx.beginPath();
        ctx.moveTo(0, y);
        ctx.lineTo(width, y);
        ctx.stroke();
    }

    ctx.strokeStyle = "#000000";
    ctx.lineWidth = 2;

    ctx.beginPath();
    ctx.moveTo(width/2, 0);
    ctx.lineTo(width/2, height);

    ctx.moveTo(0, height/2);
    ctx.lineTo(width, height/2);
    ctx.stroke();

    ctx.beginPath();

    ctx.moveTo(width/2, 0);
    ctx.lineTo(width/2 - 5, 10);
    ctx.moveTo(width/2, 0);
    ctx.lineTo(width/2 + 5, 10);
    // Стрелка X
    ctx.moveTo(width, height/2);
    ctx.lineTo(width - 10, height/2 - 5);
    ctx.moveTo(width, height/2);
    ctx.lineTo(width - 10, height/2 + 5);
    ctx.stroke();

    ctx.fillStyle = "#000000";
    ctx.font = "12px Arial";
    ctx.textAlign = "center";
    ctx.textBaseline = "middle";

    ctx.fillText("x", width - 10, height/2 - 10);

    ctx.fillText("y", width/2 + 10, 10);

    ctx.textAlign = "center";
    ctx.textBaseline = "top";

    for (let i = -4; i <= 4; i++) {
        if (i === 0) continue;
        const x = width/2 + i * scale;
        ctx.fillText(i.toString(), x, height/2 + 5);
    }

    ctx.textAlign = "right";
    ctx.textBaseline = "middle";
    for (let i = -4; i <= 4; i++) {
        if (i === 0) continue;
        const y = height/2 - i * scale;
        ctx.fillText(i.toString(), width/2 - 5, y);
    }

    ctx.textAlign = "left";
    ctx.fillText(`R = ${r}`, 10, 20);
}

function drawHitArea(ctx, width, height, r, scale) {
    ctx.fillStyle = "rgba(164,120,100,0.53)";
    ctx.strokeStyle = "#A47864";
    ctx.lineWidth = 1;

    ctx.beginPath();
    ctx.rect(width/2 - r * scale, height/2 - (r * scale / 2), r * scale, r * scale / 2);
    ctx.fill();
    ctx.stroke();

    // 2.
    ctx.beginPath();
    ctx.moveTo(width/2, height/2);
    ctx.lineTo(width/2 + (r * scale / 2), height/2);
    ctx.lineTo(width/2, height/2 + (r * scale / 2));
    ctx.closePath();
    ctx.fill();
    ctx.stroke();

    // 3.
    ctx.beginPath();
    ctx.arc(width/2, height/2, r * scale / 2,  Math.PI / 2, Math.PI, false);
    ctx.lineTo(width/2, height/2);
    ctx.closePath();
    ctx.fill();
    ctx.stroke();
}