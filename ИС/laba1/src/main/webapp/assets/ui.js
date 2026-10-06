export const findElement = (selector, root = document) => root.querySelector(selector);
export const escapeHtml = value => String(value ?? '').replace(/[&<>"']/g, character => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' })[character]);
const numbers = new Intl.NumberFormat('ru-RU', { maximumFractionDigits: 8 });
const dates = new Intl.DateTimeFormat('ru-RU', { dateStyle: 'short', timeStyle: 'short' });
export function display(value, key = '') {
    if (value === null || value === undefined) {
        return '—';
    }
    if (typeof value === 'boolean') {
        return value ? 'Да' : 'Нет';
    }
    if (typeof value === 'number' && ['id', 'year', 'houseYear', 'coordinatesId', 'houseId'].includes(key)) {
        return String(value);
    }
    if (key === 'creationDate') {
        const date = new Date(value);
        return Number.isNaN(date.valueOf()) ? String(value) : dates.format(date);
    }
    return typeof value === 'number' ? numbers.format(value) : String(value);
}
export const flatColumns = [
    ['id', 'ID'], ['name', 'Название'], ['area', 'Площадь'], ['price', 'Цена'], ['numberOfRooms', 'Комнаты'],
    ['timeToMetroOnFoot', 'До метро'], ['balcony', 'Балкон'], ['furniture', 'Мебель'], ['view', 'Вид'],
    ['transport', 'Транспорт'], ['coordinatesId', 'ID координат'], ['x', 'X'], ['y', 'Y'],
    ['houseId', 'ID дома'], ['houseName', 'Дом'], ['houseYear', 'Год дома'], ['houseNumberOfFloors', 'Этажи дома'],
    ['creationDate', 'Создана'],
];
export const referenceColumns = {
    houses: [['id', 'ID'], ['name', 'Название'], ['year', 'Год'], ['numberOfFloors', 'Этажи']],
    coordinates: [['id', 'ID'], ['x', 'X'], ['y', 'Y']],
};
export function renderTable(items, columns, { sort, direction, actions = 'view', empty = 'Пока нет записей.' } = {}) {
    const headings = columns.map(([key, label]) => {
        const active = sort === key;
        const order = active ? (direction === 'asc' ? 'ascending' : 'descending') : 'none';
        if (!sort) {
            return `<th>${escapeHtml(label)}</th>`;
        }
        const symbol = active ? (direction === 'asc' ? '↑' : '↓') : '';
        return `<th aria-sort="${order}">
      <button class="sort-button" type="button" data-sort="${key}">
        ${escapeHtml(label)}<span class="sort-symbol" aria-hidden="true">${symbol}</span>
      </button>
    </th>`;
    }).join('');
    let rows = `<tr><td class="empty-state" colspan="${columns.length + 1}">${escapeHtml(empty)}</td></tr>`;
    if (items.length > 0) {
        rows = items.map(item => {
            const cells = columns.map(([key]) => {
                const cellClass = key === 'name' || key === 'houseName' ? ' class="name-cell"' : '';
                const value = escapeHtml(display(item[key], key));
                return `<td${cellClass} title="${value}">${value}</td>`;
            }).join('');
            const id = escapeHtml(item.id);
            let buttons = `<button type="button" class="table-action" data-action="view" data-id="${id}">Открыть</button>`;
            if (actions === 'pick') {
                buttons = `<button type="button" class="table-action" data-action="pick" data-id="${id}">Выбрать</button>`;
            } else if (actions === 'flat') {
                buttons += `<button type="button" class="table-action" data-action="edit" data-id="${id}">Изменить</button>`;
            }
            return `<tr>${cells}<td class="actions-cell"><div class="button-row">${buttons}</div></td></tr>`;
        }).join('');
    }
    return `<table><thead><tr>${headings}<th class="actions-cell">Действия</th></tr></thead><tbody>${rows}</tbody></table>`;
}
export function renderPagination(page, { picker = false } = {}) {
    const start = page.totalElements ? page.page * page.size + 1 : 0;
    const end = Math.min((page.page + 1) * page.size, page.totalElements);
    return `<span class="pagination-info">${display(start)}–${display(end)} из ${display(page.totalElements)}</span><div class="page-controls">${picker ? '' : `<label class="page-size">На странице <select data-page-size aria-label="Записей на странице">${[10, 20, 50].map(size => `<option value="${size}"${size === page.size ? ' selected' : ''}>${size}</option>`).join('')}</select></label>`}<button type="button" data-page="prev"${page.page === 0 ? ' disabled' : ''}>Назад</button><span>${page.totalPages ? page.page + 1 : 0} / ${page.totalPages}</span><button type="button" data-page="next"${page.page + 1 >= page.totalPages ? ' disabled' : ''}>Далее</button></div>`;
}
export function renderDetails(items, columns) {
    return `<dl class="details">${columns.map(([key, label]) => `<div><dt>${escapeHtml(label)}</dt><dd>${escapeHtml(display(items[key], key))}</dd></div>`).join('')}</dl>`;
}
export function flatDetails(flat) {
    const relatedFields = ['coordinatesId', 'x', 'y', 'houseId', 'houseName', 'houseYear', 'houseNumberOfFloors'];
    const apartment = renderDetails(flat, flatColumns.filter(([key]) => !relatedFields.includes(key)));
    const coordinates = renderDetails(flat, [['coordinatesId', 'ID'], ['x', 'X'], ['y', 'Y']]);
    let house = '<p class="hint">Дом не указан.</p>';
    if (flat.houseId !== null) {
        house = renderDetails(flat, [['houseId', 'ID'], ['houseName', 'Название'], ['houseYear', 'Год'], ['houseNumberOfFloors', 'Этажи']]);
    }
    return `<section class="detail-group"><h3>Квартира</h3>${apartment}</section>
    <section class="detail-group"><h3>Координаты</h3>${coordinates}</section>
    <section class="detail-group"><h3>Дом</h3>${house}</section>`;
}
const messages = {
    'name must not be blank': 'Введите название квартиры.', 'area must be positive': 'Площадь должна быть больше нуля.',
    'price must be a finite positive number': 'Цена должна быть конечным числом больше нуля.',
    'timeToMetroOnFoot must be a finite positive number': 'Время до метро должно быть конечным числом больше нуля.',
    'numberOfRooms must be between 1 and 11': 'Число комнат должно быть от 1 до 11.', 'view is required': 'Выберите вид из окна.',
    'coordinatesId does not exist': 'Выбранные координаты больше не существуют. Выберите другие.',
    'houseId does not exist': 'Выбранный дом больше не существует. Выберите другой.',
    'flat not found': 'Квартира не найдена. Возможно, её уже удалили.', 'house not found': 'Дом не найден.',
    'coordinates not found': 'Координаты не найдены.', 'house year must be positive': 'Год должен быть больше нуля.',
    'numberOfFloors must be positive': 'Число этажей должно быть больше нуля.',
    'x and y must be finite; y must be at most 249': 'Введите числовые координаты. Y не должен превышать 249.',
};
export function errorText(error) {
    if (error.status >= 500) {
        return 'Ошибка БД';
    }
    return messages[error.message] || error.message;
}
export function clearErrors(form) {
    form.querySelectorAll('[aria-invalid]').forEach(element => element.removeAttribute('aria-invalid'));
    form.querySelectorAll('.field-error').forEach(element => {
        element.textContent = '';
    });
    const message = findElement('.form-error', form);
    if (message) {
        message.hidden = true;
        message.textContent = '';
    }
}
export function fieldError(form, name, message) {
    const element = form.elements.namedItem(name);
    if (element instanceof HTMLElement) {
        element.setAttribute('aria-invalid', 'true');
    }
    const output = form.querySelector(`[data-error-for="${name}"]`);
    if (output) {
        output.textContent = message;
    }
}
export function formError(form, error) {
    const output = findElement('.form-error', form);
    output.textContent = errorText(error);
    output.hidden = false;
    for (const name of ['coordinatesId', 'houseId', 'numberOfRooms', 'timeToMetroOnFoot', 'area', 'price', 'view', 'name', 'year', 'numberOfFloors']) {
        if (error.message.includes(name)) {
            fieldError(form, name, errorText(error));
            break;
        }
    }
    output.scrollIntoView({ block: 'nearest' });
}
export function numberField(form, name, { integer = false, positive = false, max = Infinity, required = true } = {}) {
    const input = form.elements.namedItem(name);
    const raw = input.value.trim();
    if (!raw && !required && !input.validity.badInput) {
        return null;
    }
    let message = '';
    const value = Number(raw);
    if (!raw || !Number.isFinite(value) || input.validity.badInput) {
        message = 'Введите число.';
    } else if (integer && !Number.isSafeInteger(value)) {
        message = 'Введите целое число допустимого размера.';
    } else if (positive && value <= 0) {
        message = 'Значение должно быть больше нуля.';
    } else if (value > max) {
        message = `Значение не должно превышать ${display(max)}.`;
    }
    if (message) {
        fieldError(form, name, message);
        return undefined;
    }
    return value;
}
export function validForm(form) {
    const first = form.querySelector('[aria-invalid=true]');
    if (first) {
        first.focus();
        return false;
    }
    return ![...form.querySelectorAll('.field-error')].some(element => element.textContent);
}
export function lockForm(form, busy) {
    form.dataset.busy = String(busy);
    form.closest('dialog')?.setAttribute('aria-busy', String(busy));
    form.querySelectorAll('button,input,select').forEach(element => {
        if (busy) {
            element.dataset.wasDisabled = String(element.disabled);
            element.disabled = true;
        } else {
            element.disabled = element.dataset.wasDisabled === 'true';
            delete element.dataset.wasDisabled;
        }
    });
}
export function openDialog(dialog) {
    if (!dialog.open) {
        dialog.showModal();
    }
}
export function setupDialogs() {
    document.addEventListener('click', event => {
        const button = event.target.closest('[data-close]');
        if (!button) {
            return;
        }
        const dialog = button.closest('dialog');
        if (!dialog.querySelector('form[data-busy=true]')) {
            dialog.close();
        }
    });
    document.querySelectorAll('dialog').forEach(dialog => {
        dialog.addEventListener('cancel', event => {
            if (dialog.querySelector('form[data-busy=true]')) {
                event.preventDefault();
            }
        });
        dialog.addEventListener('close', () => {
            dialog.querySelectorAll('form').forEach(clearErrors);
        });
    });
}
