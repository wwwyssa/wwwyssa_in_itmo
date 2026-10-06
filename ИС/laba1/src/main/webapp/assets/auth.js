import { requestApi, getAuth, setAuth } from './api.js';

const form = document.querySelector('#auth-form');
const error = document.querySelector('#auth-error');
const modes = document.querySelectorAll('[data-auth-mode]');
let mode = 'login';
let expiryTimer;

function selectMode(value) {
    mode = value;
    const registering = mode === 'register';
    document.querySelector('#auth-title').textContent = registering ? 'Регистрация' : 'Вход';
    form.querySelector('[type=submit]').textContent = registering ? 'Зарегистрироваться' : 'Войти';
    form.elements.password.autocomplete = registering ? 'new-password' : 'current-password';
    modes.forEach(button => button.classList.toggle('primary', button.dataset.authMode === mode));
    error.hidden = true;
    form.elements.password.value = '';
}
modes.forEach(button => button.addEventListener('click', () => selectMode(button.dataset.authMode)));

form.addEventListener('submit', async event => {
    event.preventDefault();
    if (form.dataset.busy === 'true' || !form.reportValidity()) {
        return;
    }
    form.dataset.busy = 'true';
    error.hidden = true;
    form.querySelectorAll('button').forEach(button => button.disabled = true);
    modes.forEach(button => button.disabled = true);
    try {
        const auth = await requestApi(`auth/${mode}`, {
            method: 'POST',
            body: { username: form.elements.username.value.trim(), password: form.elements.password.value },
        });
        form.reset();
        setAuth(auth);
    } catch (exception) {
        error.textContent = exception.message;
        error.hidden = false;
    } finally {
        form.dataset.busy = 'false';
        form.querySelectorAll('button').forEach(button => button.disabled = false);
        modes.forEach(button => button.disabled = false);
    }
});

document.querySelector('#logout').addEventListener('click', () => setAuth(null));

async function showSession() {
    clearTimeout(expiryTimer);
    const auth = getAuth();
    if (auth && (typeof auth.token !== 'string' || typeof auth.username !== 'string'
            || !Number.isFinite(auth.expiresAt) || auth.expiresAt * 1000 <= Date.now())) {
        setAuth(null);
        return;
    }
    const loggedIn = Boolean(auth);
    document.querySelector('#auth-screen').hidden = loggedIn;
    document.querySelector('#app-content').hidden = !loggedIn;
    document.querySelector('#app-nav').hidden = !loggedIn;
    document.querySelector('#user-menu').hidden = !loggedIn;
    document.querySelector('#current-user').textContent = auth?.username || '';
    if (loggedIn) {
        expiryTimer = setTimeout(() => setAuth(null), auth.expiresAt * 1000 - Date.now());
        try {
            await import('./app.js');
        } catch {
            setAuth(null);
            error.textContent = 'Не удалось загрузить приложение. Обновите страницу.';
            error.hidden = false;
        }
    } else {
        document.querySelectorAll('dialog[open]').forEach(dialog => dialog.close());
        document.title = 'Вход · Квартиры';
        form.elements.password.value = '';
    }
}
document.addEventListener('authchange', showSession);
showSession();
