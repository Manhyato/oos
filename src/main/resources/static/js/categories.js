console.log("JS loaded: categories.js");

const categoriesUrl = "/admin/categories/api";

/**
 * Загружаем категории и заполняем список
 */
function loadCategories() {
    fetch(categoriesUrl)
        .then(response => response.json())
        .then(data => {
            console.log("Loaded categories:", data);
            const ul = document.getElementById("categories");
            ul.innerHTML = "";

            data.forEach(category => {
                ul.innerHTML += `
                    <li>
                        ${category.name}
                        <button onclick="deleteCategory(${category.id})">Удалить</button>
                    </li>`;
            });
        })
        .catch(err => console.error("Error loading categories:", err));
}

/**
 * Добавляем новую категорию
 */
function addCategory() {
    const nameField = document.getElementById("categoryName");
    const name = nameField.value.trim();

    if (!name) {
        alert("Введите название категории!");
        return;
    }

    fetch(categoriesUrl, {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({name: name})
    })
        .then(() => {
            nameField.value = "";
            loadCategories();
        })
        .catch(err => console.error("Error adding category:", err));
}

/**
 * Удаление категории по ID
 */
function deleteCategory(id) {
    fetch(`${categoriesUrl}/${id}`, {
        method: "DELETE"
    })
        .then(() => loadCategories())
        .catch(err => console.error("Error deleting category:", err));
}

// При загрузке страницы сразу загружаем категории
document.addEventListener("DOMContentLoaded", loadCategories);
