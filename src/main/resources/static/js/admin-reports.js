document.addEventListener('DOMContentLoaded', () => {
    requireAdminAuth();
    loadToday();
    loadMonthly();

    const periodForm = document.getElementById('periodForm');
    periodForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const start = document.getElementById('startDate').value;
        const end = document.getElementById('endDate').value;
        await loadReport(`/reports/financial?startDate=${start}&endDate=${end}`, 'period');
    });

    document.querySelectorAll('[data-download]').forEach(btn => {
        btn.addEventListener('click', () => {
            const kind = btn.getAttribute('data-download');
            downloadCsv(kind);
        });
    });
});

async function loadToday() { await loadReport('/reports/financial/today', 'today'); }
async function loadMonthly() { await loadReport('/reports/financial/monthly', 'month'); }

const reportCache = {};

async function loadReport(url, key) {
    const tableBody = document.querySelector(`#${key}Table tbody`);
    const totalSpan = document.getElementById(`${key}Total`);
    tableBody.innerHTML = '<tr><td colspan="6">Загрузка...</td></tr>';
    try {
        const data = await apiRequest(url);
        reportCache[key] = data;
        renderRows(tableBody, data);
        totalSpan.textContent = formatMoney(data.totalRevenue || 0);
    } catch (err) {
        tableBody.innerHTML = `<tr><td colspan="6" style="color:red;">${err.message}</td></tr>`;
        totalSpan.textContent = '—';
    }
}

function renderRows(tbody, data) {
    if (!data.orders || data.orders.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6">Нет данных</td></tr>';
        return;
    }
    tbody.innerHTML = '';
    data.orders.forEach(o => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${o.orderId ?? '—'}</td>
            <td>${o.clientName ?? '—'}</td>
            <td>${o.paymentMethod ?? '—'}</td>
            <td>${o.status ?? '—'}</td>
            <td>${o.orderDate ? formatDate(o.orderDate) : '—'}</td>
            <td>${formatMoney(o.amount || 0)}</td>
        `;
        tbody.appendChild(tr);
    });
}

function formatMoney(v) {
    return `${Number(v).toFixed(2)} ₽`;
}

function formatDate(dt) {
    return dt.replace('T', ' ').slice(0,16);
}

function downloadCsv(key) {
    const data = reportCache[key];
    if (!data || !data.orders || data.orders.length === 0) {
        showAlert('Нет данных для выгрузки', 'info');
        return;
    }
    const header = ['OrderId','Client','Method','Status','Date','Amount'];
    const rows = data.orders.map(o => [
        o.orderId ?? '',
        o.clientName ?? '',
        o.paymentMethod ?? '',
        o.status ?? '',
        o.orderDate ?? '',
        o.amount ?? ''
    ]);
    const csv = [header, ...rows].map(r => r.map(cell => `"${String(cell).replace(/"/g,'""')}"`).join(';')).join('\n');
    const blob = new Blob([csv], {type:'text/csv;charset=utf-8;'});
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `report_${key}.csv`;
    a.click();
    URL.revokeObjectURL(url);
}




