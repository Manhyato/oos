const categoriesUrl = "/admin/categories/api";
const pizzasUrl = "/pizzas";

document.addEventListener("DOMContentLoaded", () => {
    requireAdminAuth();
    loadCategories();
    loadPizzas();

    document.getElementById('pizzaForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        await addPizza();
    });
});

async function loadCategories() {
    try {
        const categories = await fetch(categoriesUrl).then(r => {
            if (!r.ok) throw new Error('Не удалось загрузить категории');
            return r.json();
        });
        const ul = document.getElementById("categories");
        const select = document.getElementById("pizzaCategory");
        ul.innerHTML = "";
        select.innerHTML = "";
        categories.forEach(category => {
            ul.innerHTML += `
                <li style="margin-bottom:0.5rem; display:flex; justify-content: space-between; align-items:center;">
                    <span>${category.name}</span>
                    <button class="btn btn-danger btn-small" onclick="deleteCategory(${category.id})">Удалить</button>
                </li>`;
            select.innerHTML += `<option value="${category.id}">${category.name}</option>`;
        });
    } catch (err) {
        showAlert("Ошибка загрузки категорий: " + err.message, "error");
    }
}

async function addCategory() {
    const nameField = document.getElementById("categoryName");
    const name = nameField.value.trim();
    if (!name) {
        showAlert("Введите название категории", "error");
        return;
    }
    try {
        await fetch(categoriesUrl, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ name })
        }).then(r => {
            if (!r.ok) throw new Error('Не удалось создать категорию');
        });
        nameField.value = "";
        await loadCategories();
        showAlert("Категория добавлена", "success");
    } catch (err) {
        showAlert(err.message || "Не удалось создать категорию", "error");
    }
}

async function loadPizzas() {
    try {
        const pizzas = await apiRequest(pizzasUrl);
        const tbody = document.getElementById("pizzasTable");
        if (!pizzas.length) {
            tbody.innerHTML = "<tr><td colspan='5'>Нет пицц</td></tr>";
            return;
        }
        tbody.innerHTML = "";
        pizzas.forEach(p => {
            tbody.innerHTML += `
                <tr>
                    <td>${p.id}</td>
                    <td>${p.name}</td>
                    <td>${p.category ? p.category.name : '—'}</td>
                    <td>${Number(p.price || 0).toFixed(2)} ₽</td>
                    <td><button class="btn btn-danger btn-small" onclick="deletePizza(${p.id})">Удалить</button></td>
                </tr>`;
        });
    } catch (err) {
        showAlert("Ошибка загрузки пицц: " + err.message, "error");
    }
}

async function addPizza() {
    const name = document.getElementById('pizzaName').value.trim();
    const price = parseFloat(document.getElementById('pizzaPrice').value);
    const categoryId = document.getElementById('pizzaCategory').value;

    if (!name) {
        showAlert("Название обязательно", "error");
        return;
    }
    if (!price || price <= 0) {
        showAlert("Цена должна быть больше 0", "error");
        return;
    }
    if (!categoryId) {
        showAlert("Выберите категорию", "error");
        return;
    }

    try {
        await apiRequest("/pizzas", {
            method: "POST",
            body: { name, price, categoryId }
        });
        document.getElementById('pizzaName').value = '';
        document.getElementById('pizzaPrice').value = '';
        await loadPizzas();
        showAlert("Пицца добавлена", "success");
    } catch (err) {
        showAlert(err.message || "Не удалось добавить пиццу", "error");
    }
}

async function deleteCategory(id) {
    if (!confirm("Удалить категорию?")) return;
    try {
        await fetch(`${categoriesUrl}/${id}`, { method: "DELETE" }).then(r => {
            if (!r.ok) throw new Error('Не удалось удалить категорию');
        });
        await loadCategories();
        await loadPizzas();
        showAlert("Категория удалена", "success");
    } catch (err) {
        showAlert(err.message || "Ошибка удаления категории", "error");
    }
}

async function deletePizza(id) {
    if (!confirm("Удалить пиццу?")) return;
    try {
        await apiRequest(`${pizzasUrl}/${id}`, { method: "DELETE" });
        await loadPizzas();
        showAlert("Пицца удалена", "success");
    } catch (err) {
        showAlert(err.message || "Ошибка удаления пиццы", "error");
    }
}

