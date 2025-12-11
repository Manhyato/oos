// Админ-панель: управление доставкой
let currentOrderId = null;

async function assignCourier(orderId) {
    currentOrderId = orderId;
    
    try {
        // Получить список курьеров - обновляем каждый раз для актуальных данных
        const couriers = await apiRequest('/delivery/couriers');
        
        const modal = document.getElementById('courierModal');
        const courierList = document.getElementById('courierList');
        
        courierList.innerHTML = '';
        
        if (!couriers || couriers.length === 0) {
            courierList.innerHTML = '<p>Нет доступных курьеров</p>';
        } else {
            // Фильтруем только свободных курьеров для отображения
            const availableCouriers = couriers.filter(c => c.available === true);
            
            if (availableCouriers.length === 0) {
                courierList.innerHTML = '<p>Нет свободных курьеров. Все курьеры заняты.</p>';
            } else {
                availableCouriers.forEach(courier => {
                    const btn = document.createElement('button');
                    btn.className = 'btn btn-primary';
                    btn.style.width = '100%';
                    btn.style.marginBottom = '0.5rem';
                    btn.textContent = `${courier.name} (${courier.phone}) - Свободен`;
                    btn.onclick = () => confirmAssignCourier(orderId, courier.id);
                    courierList.appendChild(btn);
                });
            }
            
            // Добавить опцию автоматического назначения (только если есть свободные)
            if (availableCouriers.length > 0) {
                const autoBtn = document.createElement('button');
                autoBtn.className = 'btn btn-success';
                autoBtn.style.width = '100%';
                autoBtn.textContent = 'Автоматически (первый свободный)';
                autoBtn.onclick = () => confirmAssignCourier(orderId, null);
                courierList.appendChild(autoBtn);
            }
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
        
        // Перезагрузить страницу через секунду для обновления данных
        setTimeout(() => {
            window.location.reload();
        }, 1000);
    } catch (error) {
        showAlert('Ошибка при назначении курьера: ' + error.message, 'error');
        // Закрываем модалку при ошибке, чтобы пользователь мог попробовать снова
        closeCourierModal();
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

