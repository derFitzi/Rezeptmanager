import { createApp } from 'vue';
import App from './App.vue';
import router from './router';

if (typeof process === 'undefined') {
    global.process = {
        env: {
            NODE_ENV: 'development'
        }
    };
}

// globale Fehlererkennung
window.addEventListener('unhandledrejection', event => {
    console.error('Unhandled promise rejection:', event.reason);
    event.preventDefault();
});

const app = createApp(App);

app.use(router);

app.mount('#app');