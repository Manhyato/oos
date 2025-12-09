// Управление корзиной
let cart = JSON.parse(localStorage.getItem('cart')) || [];

// Инициализация корзины при загрузке страницы
document.addEventListener('DOMContentLoaded', function() {
    updateCartDisplay();
    updateCartCount();
    
    // Добавляем обработчики для кнопок "Добавить в корзину"
    document.querySelectorAll('.add-to-cart-btn').forEach(btn => {
        btn.addEventListener('click', function() {
            const pizzaId = parseInt(this.getAttribute('data-pizza-id'));
            const pizzaName = this.getAttribute('data-pizza-name');
            const pizzaPrice = parseFloat(this.getAttribute('data-pizza-price'));
            addToCart(pizzaId, pizzaName, pizzaPrice);
        });
    });
});

// Добавить пиццу в корзину
function addToCart(pizzaId, pizzaName, price) {
    const existingItem = cart.find(item => item.pizzaId === pizzaId);
    
    if (existingItem) {
        existingItem.quantity++;
    } else {
        cart.push({
            pizzaId: pizzaId,
            name: pizzaName,
            price: price,
            quantity: 1
        });
    }
    
    saveCart();
    updateCartDisplay();
    updateCartCount();
    showAlert('Пицца добавлена в корзину!', 'success');
    
    // Автоматически открыть корзину при добавлении товара
    const cartDiv = document.getElementById('cart');
    if (cartDiv) {
        cartDiv.classList.remove('hidden');
    }
}

// Переключить видимость корзины
function toggleCart() {
    const cartDiv = document.getElementById('cart');
    if (cartDiv) {
        if (cart.length === 0) {
            showAlert('Корзина пуста!', 'info');
            return;
        }
        cartDiv.classList.toggle('hidden');
    }
}

// Закрыть корзину при клике вне её области
document.addEventListener('click', function(event) {
    const cartDiv = document.getElementById('cart');
    const cartButton = document.getElementById('cartButton');
    
    if (cartDiv && !cartDiv.contains(event.target) && 
        cartButton && !cartButton.contains(event.target) &&
        !event.target.closest('.add-to-cart-btn')) {
        cartDiv.classList.add('hidden');
    }
});

// Обновить счётчик корзины в навигации
function updateCartCount() {
    const cartCount = document.getElementById('cartCount');
    if (cartCount) {
        const totalItems = cart.reduce((sum, item) => sum + item.quantity, 0);
        if (totalItems > 0) {
            cartCount.textContent = totalItems;
            cartCount.style.display = 'inline-block';
        } else {
            cartCount.style.display = 'none';
        }
    }
}

// Обновить отображение корзины
function updateCartDisplay() {
    const cartDiv = document.getElementById('cart');
    const cartItemsDiv = document.getElementById('cartItems');
    const cartTotalDiv = document.getElementById('cartTotal');
    
    if (!cartDiv || !cartItemsDiv || !cartTotalDiv) return;
    
    let total = 0;
    cartItemsDiv.innerHTML = '';
    
    if (cart.length === 0) {
        cartItemsDiv.innerHTML = '<p style="text-align: center; color: #718096;">Корзина пуста</p>';
        cartTotalDiv.textContent = 'Итого: 0 ₽';
        return;
    }
    
    cart.forEach((item, index) => {
        const itemDiv = document.createElement('div');
        itemDiv.className = 'cart-item';
        
        const itemTotal = item.price * item.quantity;
        total += itemTotal;
        
        itemDiv.innerHTML = `
            <div>
                <strong>${escapeHtml(item.name)}</strong><br>
                <small>${item.price} ₽ × ${item.quantity}</small>
            </div>
            <div>
                <strong>${itemTotal.toFixed(2)} ₽</strong>
                <button onclick="removeFromCart(${index})" class="btn btn-danger" style="padding: 0.25rem 0.5rem; margin-left: 0.5rem;">×</button>
            </div>
        `;
        
        cartItemsDiv.appendChild(itemDiv);
    });
    
    cartTotalDiv.textContent = `Итого: ${total.toFixed(2)} ₽`;
    updateCartCount();
}

// Экранирование HTML для безопасности
function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// Удалить из корзины
function removeFromCart(index) {
    cart.splice(index, 1);
    saveCart();
    updateCartDisplay();
    updateCartCount();
}

// Сохранить корзину в localStorage
function saveCart() {
    localStorage.setItem('cart', JSON.stringify(cart));
}

// Оформить заказ
function checkout() {
    if (cart.length === 0) {
        showAlert('Корзина пуста!', 'error');
        return;
    }
    
    // Сохраняем корзину и переходим на страницу оформления заказа
    window.location.href = '/orders/create';
}

