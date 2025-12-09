// Страница оплаты заказа
async function processPayment(event) {
    event.preventDefault();
    
    if (isPaid) {
        showAlert('Заказ уже оплачен!', 'error');
        return;
    }
    
    const paymentMethod = document.getElementById('paymentMethod').value;
    if (!paymentMethod) {
        showAlert('Выберите способ оплаты', 'error');
        return;
    }
    
    const errorDiv = document.getElementById('errorMessage');
    const successDiv = document.getElementById('successMessage');
    errorDiv.classList.add('hidden');
    successDiv.classList.add('hidden');
    
    try {
        const payment = await apiRequest(`/payments/order/${orderId}`, {
            method: 'POST',
            body: JSON.stringify({
                method: paymentMethod
            })
        });
        
        successDiv.textContent = 'Заказ успешно оплачен! Перенаправление...';
        successDiv.classList.remove('hidden');
        
        // Перейти на страницу деталей заказа через 2 секунды
        setTimeout(() => {
            window.location.href = `/orders/${orderId}`;
        }, 2000);
    } catch (error) {
        errorDiv.textContent = error.message || 'Произошла ошибка при оплате';
        errorDiv.classList.remove('hidden');
    }
}

