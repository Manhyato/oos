// Страница создания заказа
document.addEventListener('DOMContentLoaded', function() {
    loadCartSummary();
});

// Загрузить сводку корзины
function loadCartSummary() {
    const cart = JSON.parse(localStorage.getItem('cart')) || [];
    const summaryDiv = document.getElementById('orderSummary');
    
    if (cart.length === 0) {
        summaryDiv.innerHTML = '<div class="alert alert-error">Корзина пуста! <a href="/">Вернуться к меню</a></div>';
        document.getElementById('orderForm').style.display = 'none';
        return;
    }
    
    let total = 0;
    let html = '<h3>Ваш заказ:</h3><ul>';
    
    cart.forEach(item => {
        const itemTotal = item.price * item.quantity;
        total += itemTotal;
        html += `<li>${item.name} × ${item.quantity} = ${itemTotal.toFixed(2)} ₽</li>`;
    });
    
    html += `</ul><div class="cart-total">Итого: ${total.toFixed(2)} ₽</div>`;
    summaryDiv.innerHTML = html;
}

// Отправить заказ
async function submitOrder(event) {
    event.preventDefault();
    
    const cart = JSON.parse(localStorage.getItem('cart')) || [];
    if (cart.length === 0) {
        showAlert('Корзина пуста!', 'error');
        return;
    }
    
    const orderData = {
        clientName: document.getElementById('clientName').value,
        clientPhone: document.getElementById('clientPhone').value,
        address: document.getElementById('address').value,
        items: cart.map(item => ({
            pizzaId: item.pizzaId,
            quantity: item.quantity
        }))
    };
    
    const errorDiv = document.getElementById('errorMessage');
    errorDiv.classList.add('hidden');
    
    try {
        const order = await apiRequest('/orders', {
            method: 'POST',
            body: JSON.stringify(orderData)
        });
        
        // Очистить корзину
        localStorage.removeItem('cart');
        
        // Перейти на страницу оплаты
        window.location.href = `/orders/${order.id}/payment`;
    } catch (error) {
        errorDiv.textContent = error.message || 'Произошла ошибка при создании заказа';
        errorDiv.classList.remove('hidden');
    }
}

