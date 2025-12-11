document.addEventListener('DOMContentLoaded', () => {
    // защита входа
    requireAdminAuth();

    document.querySelectorAll('[data-order-id][data-status-select]').forEach(select => {
        select.addEventListener('change', async (e) => {
            const orderId = e.target.getAttribute('data-order-id');
            const newStatus = e.target.value;
            const row = e.target.closest('tr');
            const statusCell = row.querySelector('.order-status-text');
            const paidBadge = row.querySelector('.paid-badge');

            try {
                const updated = await apiRequest(`/orders/${orderId}/status?status=${newStatus}`, { method: 'PATCH' });
                statusCell.textContent = updated.status;
                if (updated.paid && paidBadge) {
                    paidBadge.classList.remove('status-new');
                    paidBadge.classList.add('status-paid');
                    paidBadge.textContent = 'Да';
                }
                showAlert('Статус обновлён', 'success');
            } catch (err) {
                showAlert(err.message || 'Не удалось обновить статус', 'error');
                // откат UI
                e.target.value = statusCell.textContent;
            }
        });
    });
});




