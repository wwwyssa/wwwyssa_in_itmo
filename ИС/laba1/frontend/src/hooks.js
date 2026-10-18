import { useEffect, useState } from 'react';
import { requestApi, setAuth } from './api.js';

export function useResource(path, revision = 0) {
    const [state, setState] = useState({ path, data: null, error: null, loading: true });
    useEffect(() => {
        const controller = new AbortController();
        setState(previous => ({ path, data: previous.path === path ? previous.data : null, error: null, loading: true }));
        requestApi(path, { signal: controller.signal }).then(
            data => { if (!controller.signal.aborted) setState({ path, data, error: null, loading: false }); },
            error => { if (!controller.signal.aborted) setState({ path, data: null, error, loading: false }); },
        );
        return () => controller.abort();
    }, [path, revision]);
    return state.path === path ? state : { data: null, error: null, loading: true };
}

export function useUpdates(token, refresh) {
    useEffect(() => {
        if (!token) return;
        let stopped = false;
        let socket;
        let timer;
        function connect() {
            const url = new URL('./updates', document.baseURI);
            url.protocol = url.protocol === 'https:' ? 'wss:' : 'ws:';
            socket = new WebSocket(url);
            socket.onopen = () => { if (!stopped) socket.send(token); };
            socket.onmessage = event => {
                if (!stopped && (event.data === 'ready' || event.data === 'changed')) refresh();
            };
            socket.onclose = event => {
                if (stopped) return;
                if (event.code === 1008) setAuth(null);
                else timer = setTimeout(connect, 3000);
            };
            socket.onerror = () => socket.close();
        }
        connect();
        return () => {
            stopped = true;
            clearTimeout(timer);
            socket?.close();
        };
    }, [token, refresh]);
}
