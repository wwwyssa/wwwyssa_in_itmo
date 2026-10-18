import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

const backend = process.env.BACKEND_URL || 'http://localhost:8080';

export default defineConfig({
    plugins: [react()],
    base: './',
    build: {
        outDir: '../build/frontend',
        emptyOutDir: true,
    },
    server: {
        proxy: {
            '/api': { target: backend, changeOrigin: true },
            '/updates': { target: backend, ws: true, changeOrigin: true },
        },
    },
});
