// -------------------- Базовые функции --------------------
const API_BASE = '/api';

async function apiRequest(url, options = {}) {
    try {
        const fetchOptions = {
            headers: { 'Content-Type': 'application/json', ...options.headers },
            ...options
        };
        if (fetchOptions.body && typeof fetchOptions.body === 'object') {
            fetchOptions.body = JSON.stringify(fetchOptions.body);
        }
        const response = await fetch(API_BASE + url, fetchOptions);
        if (!response.ok) {
            let errorMessage = 'Произошла ошибка';
            try { const error = await response.json(); errorMessage = error.message || errorMessage; } 
            catch (e) { errorMessage = `HTTP ${response.status}: ${response.statusText}`; }
            throw new Error(errorMessage);
        }
        const contentType = response.headers.get('content-type');
        if (contentType && contentType.includes('application/json')) return await response.json();
        return null;
    } catch (error) { console.error('API Error:', error); throw error; }
}

function showAlert(message, type = 'info') {
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${type}`;
    alertDiv.textContent = message;
    const container = document.querySelector('.container');
    container.insertBefore(alertDiv, container.firstChild);
    setTimeout(() => alertDiv.remove(), 5000);
}

// Простой хук для проверки админки на странице без импорта дополнительных модулей
function ensureAdminNav(buttonId = null) {
    if (typeof requireAdminAuth === 'function') {
        requireAdminAuth();
    }
    if (buttonId) {
        const btn = document.getElementById(buttonId);
        if (btn) btn.addEventListener('click', adminLogout);
    }
}

// -------------------- Модальное окно выбора курьера --------------------
const courierModal = document.getElementById('courierModal');
const courierListContainer = document.getElementById('courierList');
let currentOrderId = null;

async function assignCourier(orderId) {
    currentOrderId = orderId;
    courierListContainer.innerHTML = '<p>Загрузка курьеров...</p>';
    courierModal.classList.remove('hidden');

    try {
        // используем существующий endpoint
        const couriers = await apiRequest(`/delivery/couriers`);
        // фильтруем только доступных
        const availableCouriers = couriers.filter(c => c.available);

        if (!availableCouriers || availableCouriers.length === 0) {
            courierListContainer.innerHTML = '<p>Нет доступных курьеров</p>';
            return;
        }

        courierListContainer.innerHTML = '';
        availableCouriers.forEach(courier => {
            const btn = document.createElement('button');
            btn.className = 'btn btn-primary';
            btn.textContent = `${courier.name} (${courier.phone})`;
            btn.onclick = () => assignCourierToOrder(courier.id);
            courierListContainer.appendChild(btn);
        });
    } catch (err) {
        courierListContainer.innerHTML = `<p style="color:red;">Ошибка загрузки курьеров: ${err.message}</p>`;
    }
}

function closeCourierModal() {
    courierModal.classList.add('hidden');
    courierListContainer.innerHTML = '';
    currentOrderId = null;
}

async function assignCourierToOrder(courierId) {
    if (!currentOrderId) return;
    try {
        await apiRequest(`/delivery/orders/${currentOrderId}/assign?courierId=${courierId}`, {
            method: 'POST'
        });
        showAlert('Курьер успешно назначен', 'success');
        closeCourierModal();
        location.reload();
    } catch (err) {
        showAlert(`Ошибка назначения курьера: ${err.message}`, 'error');
    }
}

// Закрытие модалки при клике по фону
courierModal.addEventListener('click', (event) => {
    if (event.target === courierModal) closeCourierModal();
});

