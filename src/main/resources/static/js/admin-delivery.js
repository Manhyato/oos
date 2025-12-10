// Админ-панель: управление доставкой
let currentOrderId = null;

async function assignCourier(orderId) {
    currentOrderId = orderId;
    
    try {
        // Получить список курьеров
        const couriers = await apiRequest('/delivery/couriers');
        
        const modal = document.getElementById('courierModal');
        const courierList = document.getElementById('courierList');
        
        courierList.innerHTML = '';
        
        if (couriers.length === 0) {
            courierList.innerHTML = '<p>Нет доступных курьеров</p>';
        } else {
            couriers.forEach(courier => {
                const btn = document.createElement('button');
                btn.className = 'btn btn-primary';
                btn.style.width = '100%';
                btn.style.marginBottom = '0.5rem';
                btn.textContent = `${courier.name} ${courier.available ? '(Свободен)' : '(Занят)'}`;
                btn.disabled = !courier.available;
                btn.onclick = () => confirmAssignCourier(orderId, courier.id);
                courierList.appendChild(btn);
            });
            
            // Добавить опцию автоматического назначения
            const autoBtn = document.createElement('button');
            autoBtn.className = 'btn btn-success';
            autoBtn.style.width = '100%';
            autoBtn.textContent = 'Автоматически (первый свободный)';
            autoBtn.onclick = () => confirmAssignCourier(orderId, null);
            courierList.appendChild(autoBtn);
        }
        
        modal.classList.remove('hidden');
    } catch (error) {
        showAlert('Ошибка при загрузке курьеров: ' + error.message, 'error');
    }
}

async function confirmAssignCourier(orderId, courierId) {
    try {
        const url = `/delivery/orders/${orderId}/assign${courierId ? '?courierId=' + courierId : ''}`;
        await apiRequest(url, { method: 'POST' });
        
        showAlert('Курьер успешно назначен!', 'success');
        closeCourierModal();
        
        // Перезагрузить страницу через секунду
        setTimeout(() => {
            window.location.reload();
        }, 1000);
    } catch (error) {
        showAlert('Ошибка при назначении курьера: ' + error.message, 'error');
    }
}

function closeCourierModal() {
    document.getElementById('courierModal').classList.add('hidden');
    currentOrderId = null;
}

document.addEventListener('DOMContentLoaded', () => {
    if (typeof requireAdminAuth === 'function') {
        requireAdminAuth();
    }
});

