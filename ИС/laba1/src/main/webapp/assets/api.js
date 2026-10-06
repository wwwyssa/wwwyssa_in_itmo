const base = new URL('./api/', document.baseURI);
export function getAuth() {
    try {
        return JSON.parse(sessionStorage.getItem('flats.auth'));
    } catch {
        return null;
    }
}
export function setAuth(auth) {
    if (auth) {
        sessionStorage.setItem('flats.auth', JSON.stringify(auth));
    } else {
        sessionStorage.removeItem('flats.auth');
    }
    document.dispatchEvent(new CustomEvent('authchange'));
}
export class ApiError extends Error {
    constructor(message, status = 0) {
        super(message);
        this.status = status;
    }
}
export async function requestApi(path, { method = 'GET', body, signal } = {}) {
    const token = getAuth()?.token;
    const options = {
        method,
        signal,
        cache: 'no-store',
        headers: { Accept: 'application/json' },
    };
    if (token) {
        options.headers.Authorization = `Bearer ${token}`;
    }
    if (body !== undefined) {
        options.headers['Content-Type'] = 'application/json';
        options.body = JSON.stringify(body);
    }
    let response;
    try {
        response = await fetch(new URL(path, base), options);
    } catch (error) {
        if (error.name === 'AbortError') {
            throw error;
        }
        throw new ApiError('Не удалось связаться с сервером. Проверьте соединение и повторите запрос.');
    }
    let data;
    try {
        data = await response.json();
    } catch {
        throw new ApiError('Сервер вернул неожиданный ответ. Повторите запрос.', response.status);
    }
    if (!response.ok) {
        if (response.status === 401 && !path.startsWith('auth/') && token === getAuth()?.token) {
            setAuth(null);
        }
        throw new ApiError(data.message || 'Не удалось выполнить запрос.', response.status);
    }
    return data;
}
export function buildQueryParams(values) {
    const parameters = new URLSearchParams();
    for (const [key, value] of Object.entries(values)) {
        if (value !== undefined && value !== null) {
            parameters.set(key, value);
        }
    }
    return parameters.toString();
}
