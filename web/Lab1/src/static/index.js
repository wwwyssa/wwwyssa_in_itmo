"use strict";

const state = {
    x: NaN,
    y: NaN,
    r: 1.0,
};

const table = document.getElementById("result-table");
const tbody = table.querySelector("tbody");
const error = document.getElementById("error");
const canvas = document.getElementById("graph");
const ctx = canvas.getContext("2d");

// Draw initial graph
drawGraph();

document.getElementById("x").addEventListener("change", (ev) => {
    const input = ev.target;
    const value = input.value;

    if (!/^[-+]?\d*([,]\d+)?$/.test(value)) {
        showError("X должен быть числом! Дробные числа вводить через запятую");
        input.value = "";
        state.x = NaN;
    } else {
        state.x = parseFloat(value);
        error.hidden = true;
    }
});


const yButtonsContainer = document.getElementById('y-buttons');
const yValues = [-3, -2, -1, 0, 1, 2, 3, 4, 5];

yValues.forEach(value => {
    const button = document.createElement('button');
    button.type = 'button';
    button.textContent = value;
    button.classList.add('y-button');
    button.addEventListener('click', () => {
        document.querySelectorAll('.y-button').forEach(btn => {
            btn.classList.remove('selected');
        });
        button.classList.add('selected');
        state.y = value;
        error.hidden = true;
    });
    yButtonsContainer.appendChild(button);
});

// Создаем radio кнопки для R
const rRadioContainer = document.getElementById('r-radio');
const rValues = [1, 1.5, 2, 2.5, 3];

rValues.forEach(value => {
    const label = document.createElement('label');
    const radio = document.createElement('input');
    radio.type = 'radio';
    radio.name = 'r';
    radio.value = value;

    if (value === state.r) {
        radio.checked = true;
    }

    radio.addEventListener('change', (ev) => {
        state.r = parseFloat(ev.target.value);
        error.hidden = true;
        drawGraph(); // Перерисовываем график при изменении R
    });

    label.appendChild(radio);
    label.appendChild(document.createTextNode(value));
    rRadioContainer.appendChild(label);
});

const validateState = (state) => {
    error.hidden = true;

    if (isNaN(state.x) || state.x < -5 || state.x > 5) {
        showError("X Должен быть от -5 до 5");
        return false;
    }

    if (isNaN(state.y)) {
        showError("Пж выбери Y");
        return false;
    }

    if (isNaN(state.r) || state.r < 1 || state.r > 3) {
        showError("Пж выбери R");
        return false;
    }

    return true;
}

function showError(message) {
    const modal = document.getElementById("error-modal");
    const modalMessage = document.getElementById("modal-error-message");
    const closeButton = modal.querySelector(".close-button");

    modalMessage.textContent = message;
    modal.style.display = "block";

    closeButton.onclick = () => {
        modal.style.display = "none";
    };

    window.onclick = (event) => {
        if (event.target === modal) {
            modal.style.display = "none";
        }
    };
}

document.getElementById("data-form").addEventListener("submit", async function (ev) {
    ev.preventDefault();

    if (!validateState(state)) return;

    const newRow = tbody.insertRow(0);

    const rowX = newRow.insertCell(0);
    const rowY = newRow.insertCell(1);
    const rowR = newRow.insertCell(2);
    const rowTime = newRow.insertCell(3);
    const rowExecTime = newRow.insertCell(4);
    const rowResult = newRow.insertCell(5);

    try {
        // Используем URLSearchParams вместо FormData для лучшей совместимости с сервером
        const formData = new URLSearchParams();
        formData.append('x', state.x);
        formData.append('y', state.y);
        formData.append('r', state.r);

        const response = await fetch("/fcgi-bin/Laba1.jar", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
            },
            body: formData
        });

        const results = {
            x: state.x,
            y: state.y,
            r: state.r,
            execTime: "",
            time: "",
            result: "",
        };

        if (response.ok) {
            const result = await response.json();
            results.time = new Date().toLocaleString();
            results.execTime = `${result.time} ns`;
            results.result = result.result.toString();
        } else if (response.status === 400) {
            const result = await response.json();
            results.time = new Date().toLocaleString();
            results.execTime = "N/A";
            results.result = `error: ${result.reason}`;
            rowResult.classList.add("error");
        } else {
            results.time = "N/A";
            results.execTime = "N/A";
            results.result = "server error";
            rowResult.classList.add("error");
        }

        const prevResults = JSON.parse(localStorage.getItem("results") || "[]");
        localStorage.setItem("results", JSON.stringify([...prevResults, results]));

        rowX.innerText = results.x.toString();
        rowY.innerText = results.y.toString();
        rowR.innerText = results.r.toString();
        rowTime.innerText = results.time;
        rowExecTime.innerText = results.execTime;
        rowResult.innerText = results.result;

        if (results.result === "true") {
            rowResult.innerText = "hit";
            rowResult.classList.add("hit");
        } else if (results.result === "false") {
            rowResult.innerText = "miss";
            rowResult.classList.add("miss");
        }

    } catch (error) {
        console.error("Fetch error:", error);

        const results = {
            x: state.x,
            y: state.y,
            r: state.r,
            execTime: "N/A",
            time: "N/A",
            result: "connection error"
        };

        const prevResults = JSON.parse(localStorage.getItem("results") || "[]");
        localStorage.setItem("results", JSON.stringify([...prevResults, results]));

        rowX.innerText = results.x.toString();
        rowY.innerText = results.y.toString();
        rowR.innerText = results.r.toString();
        rowTime.innerText = results.time;
        rowExecTime.innerText = results.execTime;
        rowResult.innerText = results.result;
        rowResult.classList.add("error");
    }
});

// Draw graph function
function drawGraph() {
    const width = canvas.width;
    const height = canvas.height;
    const r = state.r;
    const scale = 50;

    // Clear canvas
    ctx.clearRect(0, 0, width, height);

    ctx.fillStyle = "#000000";
    ctx.fillRect(0, 0, width, height);

    ctx.beginPath();
    ctx.strokeStyle = "#e1e1e1";
    ctx.lineWidth = 2;

    // Y axis
    ctx.moveTo(width/2, 0);
    ctx.lineTo(width/2, height);

    // X axis
    ctx.moveTo(0, height/2);
    ctx.lineTo(width, height/2);

    // Arrow for Y axis
    ctx.moveTo(width/2, 0);
    ctx.lineTo(width/2 - 5, 10);
    ctx.moveTo(width/2, 0);
    ctx.lineTo(width/2 + 5, 10);

    // Arrow for X axis
    ctx.moveTo(width, height/2);
    ctx.lineTo(width - 10, height/2 - 5);
    ctx.moveTo(width, height/2);
    ctx.lineTo(width - 10, height/2 + 5);

    ctx.stroke();

    // Draw labels
    ctx.fillStyle = "#e1e1e1";
    ctx.font = "12px monospace";

    // X axis labels
    ctx.fillText(`-R`, width/2 - (r * scale), height/2 + 15);
    ctx.fillText(`-R/2`, width/2 - (r * scale / 2), height/2 + 15);
    ctx.fillText(`R/2`, width/2 + (r * scale / 2), height/2 + 15);
    ctx.fillText(`R`, width/2 + (r * scale), height/2 + 15);
    ctx.fillText("x", width - 10, height/2 - 10);

    // Y axis labels
    ctx.fillText(`R`, width/2 + 5, height/2 - (r * scale));
    ctx.fillText(`R/2`, width/2 + 5, height/2 - (r * scale / 2));
    ctx.fillText(`-R/2`, width/2 + 5, height/2 + (r * scale / 2));
    ctx.fillText(`-R`, width/2 + 5, height/2 + (r * scale));
    ctx.fillText("y", width/2 + 10, 10);

    // Draw shape with the correct configuration
    ctx.fillStyle = "rgba(255,12,0,0.5)";

    ctx.beginPath();
    ctx.moveTo(width/2, height/2); // (0,0)
    ctx.lineTo(width/2 - (r * scale / 2), height/2); // (- R/2, 0)
    ctx.lineTo(width/2, height/2 - (r * scale / 2)); // (0, R/2)
    ctx.closePath();
    ctx.fill();

    ctx.beginPath();
    ctx.moveTo(width/2, height/2); // center
    ctx.arc(width/2, height/2, r * scale / 2, Math.PI / 2, Math.PI, false);
    ctx.lineTo(width/2, height/2);
    ctx.closePath();
    ctx.fill();

    ctx.beginPath();
    ctx.moveTo(width/2, height/2); // (0,0)
    ctx.lineTo(width/2 + (r * scale), height/2); // (-R, 0)
    ctx.lineTo(width/2 + (r * scale), height/2 + (r * scale) / 2); // (-R, -R)
    ctx.lineTo(width/2, height/2 + (r * scale) / 2); // (0, -R)
    ctx.closePath();
    ctx.fill();
}

const prevResults = JSON.parse(localStorage.getItem("results") || "[]");
prevResults.forEach(result => {
    const newRow = tbody.insertRow(-1); // Вставляем в конец

    const rowX = newRow.insertCell(0);
    const rowY = newRow.insertCell(1);
    const rowR = newRow.insertCell(2);
    const rowTime = newRow.insertCell(3);
    const rowExecTime = newRow.insertCell(4);
    const rowResult = newRow.insertCell(5);

    rowX.innerText = result.x.toString();
    rowY.innerText = result.y.toString();
    rowR.innerText = result.r.toString();
    rowTime.innerText = result.time;
    rowExecTime.innerText = result.execTime;

    if (result.result === "true") {
        rowResult.innerText = "hit";
        rowResult.classList.add("hit");
    } else if (result.result === "false") {
        rowResult.innerText = "miss";
        rowResult.classList.add("miss");
    } else {
        rowResult.innerText = result.result;
        if (result.result.includes("error")) {
            rowResult.classList.add("error");
        }
    }
});
