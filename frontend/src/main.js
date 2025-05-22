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

const app = createApp(App);

app.use(router);

app.mount('#app');