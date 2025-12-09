// Страница отслеживания заказа
async function trackOrder() {
    const orderId = document.getElementById('orderId').value;
    
    if (!orderId) {
        showAlert('Введите номер заказа', 'error');
        return;
    }
    
    const infoDiv = document.getElementById('orderInfo');
    infoDiv.innerHTML = '<p>Загрузка...</p>';
    
    try {
        const status = await apiRequest(`/delivery/orders/${orderId}/status`);
        
        const statusMap = {
            'NEW': { text: 'Новый', class: 'status-new' },
            'PAID': { text: 'Оплачен', class: 'status-paid' },
            'DELIVERY_ASSIGNED': { text: 'Курьер назначен', class: 'status-delivering' },
            'DELIVERING': { text: 'В доставке', class: 'status-delivering' },
            'DELIVERED': { text: 'Доставлен', class: 'status-delivered' }
        };
        
        const statusInfo = statusMap[status.status] || { text: status.status, class: 'status-new' };
        
        infoDiv.innerHTML = `
            <div class="card">
                <h3>Заказ #${status.orderId}</h3>
                <p><strong>Статус:</strong> 
                    <span class="status-badge ${statusInfo.class}">${statusInfo.text}</span>
                </p>
                ${status.courierName ? `<p><strong>Курьер:</strong> ${status.courierName}</p>` : ''}
                <a href="/orders/${status.orderId}" class="btn btn-primary mt-2">Подробнее</a>
            </div>
        `;
    } catch (error) {
        infoDiv.innerHTML = `<div class="alert alert-error">${error.message || 'Заказ не найден'}</div>`;
    }
}

