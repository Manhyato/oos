const ADMIN_LOGIN = 'admin';
const ADMIN_PASSWORD = 'pizza2025';
const ADMIN_FLAG_KEY = 'oosAdminAuth';

function isAdminAuthenticated() {
    return sessionStorage.getItem(ADMIN_FLAG_KEY) === 'true';
}

function requireAdminAuth() {
    if (!isAdminAuthenticated()) {
        window.location.href = '/admin/login';
    }
}

function adminLogout() {
    sessionStorage.removeItem(ADMIN_FLAG_KEY);
    window.location.href = '/admin/login';
}

document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('adminLoginForm');
    if (loginForm) {
        loginForm.addEventListener('submit', (e) => {
            e.preventDefault();
            const login = document.getElementById('login').value.trim();
            const password = document.getElementById('password').value;
            const errorDiv = document.getElementById('loginError');
            errorDiv.classList.add('hidden');

            if (login === ADMIN_LOGIN && password === ADMIN_PASSWORD) {
                sessionStorage.setItem(ADMIN_FLAG_KEY, 'true');
                window.location.href = '/admin/orders';
            } else {
                errorDiv.textContent = 'Неверный логин или пароль';
                errorDiv.classList.remove('hidden');
            }
        });
    }
});

