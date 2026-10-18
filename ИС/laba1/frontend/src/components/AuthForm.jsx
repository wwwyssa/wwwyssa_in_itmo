import { useState } from 'react';
import { requestApi, setAuth } from '../api.js';
import { ErrorMessage, Field } from './Common.jsx';

export default function AuthForm() {
    const [mode, setMode] = useState('login');
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState(null);
    const [busy, setBusy] = useState(false);
    const registering = mode === 'register';
    async function submit(event) {
        event.preventDefault();
        if (busy) return;
        setBusy(true);
        setError(null);
        try {
            const auth = await requestApi('auth/' + mode, { method: 'POST', body: { username: username.trim(), password } });
            setPassword('');
            setAuth(auth);
        } catch (exception) {
            setError(exception);
        } finally {
            setBusy(false);
        }
    }
    return <main><section className="auth-card">
        <h1>{registering ? 'Регистрация' : 'Вход'}</h1>
        <p className="subheading">Войдите, чтобы просматривать квартиры и управлять ими.</p>
        <div className="button-row auth-tabs">{[['login', 'Вход'], ['register', 'Регистрация']].map(([value, label]) =>
            <button key={value} type="button" className={mode === value ? 'primary' : ''} disabled={busy}
                onClick={() => { setMode(value); setPassword(''); setError(null); }}>{label}</button>)}</div>
        <form id="auth-form" onSubmit={submit}>
            <Field label="Логин" name="username" value={username} onChange={event => setUsername(event.target.value)}
                autoComplete="username" minLength={3} maxLength={32} pattern="[A-Za-z0-9_]{3,32}" required disabled={busy}
                hint="От 3 до 32 латинских букв, цифр или символов _" />
            <Field label="Пароль" name="password" type="password" value={password} onChange={event => setPassword(event.target.value)}
                autoComplete={registering ? 'new-password' : 'current-password'} minLength={8} maxLength={128} required disabled={busy}
                hint="От 8 до 128 символов" />
            <ErrorMessage error={error} />
            <button className="primary" disabled={busy}>{busy ? 'Подождите…' : registering ? 'Зарегистрироваться' : 'Войти'}</button>
        </form>
    </section></main>;
}
