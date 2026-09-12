document.addEventListener('DOMContentLoaded', function() {
    function updateClock() {
        const clock = document.getElementById("time");
        const now = new Date();
        const timeString = now.toLocaleTimeString("ru-RU");
        const dateString = now.toLocaleDateString("ru-RU");
        clock.textContent = `${dateString} ${timeString}`;
    }

    updateClock();
    setInterval(updateClock, 11000);
});