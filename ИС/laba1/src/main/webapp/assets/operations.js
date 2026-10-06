import { requestApi, buildQueryParams } from './api.js';
import { findElement, escapeHtml, display, renderTable, renderDetails, flatColumns, clearErrors, fieldError, formError, numberField, validForm, lockForm } from './ui.js';
const operations = [
    { key: 'max-area', title: 'Максимальная площадь', description: 'Найти квартиру с самой большой площадью.' },
    { key: 'count-by-house', title: 'Количество квартир в доме', description: 'Посчитать квартиры, связанные с выбранным домом. Можно выбрать квартиры без дома.' },
    { key: 'name-prefix', title: 'Поиск по началу названия', description: 'Найти квартиры, название которых начинается с указанного текста. Регистр учитывается; пустая строка найдёт все квартиры.' },
    { key: 'most-expensive-without-balcony', title: 'Самая дорогая без балкона', description: 'Найти самую дорогую квартиру, у которой точно нет балкона.' },
    { key: 'most-expensive-of-three', title: 'Самая дорогая из трёх', description: 'Укажите три разных ID квартир, чтобы выбрать из них самую дорогую.' },
];
export function initOperations({ selectHouse, onView, onEdit, onError }) {
    const root = findElement('#operations-section');
    root.innerHTML = '<header class="page-heading"><div><h1>Специальные операции</h1><p class="subheading">Поиск и сравнение квартир</p></div></header><div class="operations-layout"><div class="operations-menu" aria-label="Выбор операции"></div><div class="panel operation-panel" id="operation-panel"></div></div>';
    let active = operations[0];
    let house = null;
    let generation = 0;
    findElement('.operations-menu', root).innerHTML = operations.map(operation => `<button type="button" class="operation-choice" data-operation="${operation.key}" aria-pressed="false">${escapeHtml(operation.title)}</button>`).join('');
    function render() {
        generation++;
        house = null;
        root.querySelectorAll('[data-operation]').forEach(button => {
            button.setAttribute('aria-pressed', String(button.dataset.operation === active.key));
        });
        let fields = '';
        if (active.key === 'name-prefix') {
            fields = '<label class="field">Начало названия<input name="prefix" autocomplete="off" placeholder="Например, Квартира"></label>';
        }
        if (active.key === 'most-expensive-of-three') {
            fields = `<div class="form-grid">${[1, 2, 3].map(index => `<label class="field">ID квартиры ${index}<input name="id${index}" type="number" min="1" max="2147483647" step="1" required><span class="field-error" data-error-for="id${index}"></span></label>`).join('')}</div>`;
        }
        if (active.key === 'count-by-house') {
            fields = '<div class="selection-row full"><span id="operation-house">Квартиры без дома</span><div class="button-row"><button type="button" id="operation-choose-house">Выбрать дом</button><button type="button" id="operation-clear-house" hidden>Убрать</button></div></div>';
        }
        findElement('#operation-panel').innerHTML = `<h2>${escapeHtml(active.title)}</h2><p class="hint">${escapeHtml(active.description)}</p><form class="operation-form" novalidate><p class="form-error" role="alert" hidden></p>${fields}<button type="submit" class="primary">Выполнить</button></form><div id="operation-result" class="operation-result" role="status" hidden></div>`;
    }
    root.addEventListener('click', async (event) => {
        const choice = event.target.closest('[data-operation]');
        if (choice) {
            active = operations.find(item => item.key === choice.dataset.operation);
            render();
            return;
        }
        if (event.target.closest('#operation-choose-house')) {
            selectHouse('houses', item => {
                house = item;
                findElement('#operation-house').textContent = `${item.name || 'Дом без названия'} · №${item.id}`;
                findElement('#operation-clear-house').hidden = false;
            });
        }
        if (event.target.closest('#operation-clear-house')) {
            house = null;
            findElement('#operation-house').textContent = 'Квартиры без дома';
            findElement('#operation-clear-house').hidden = true;
        }
        const button = event.target.closest('[data-action]');
        if (button) {
            button.disabled = true;
            try {
                await (button.dataset.action === 'edit' ? onEdit : onView)(Number(button.dataset.id));
            } catch (error) {
                onError(error);
            } finally {
                button.disabled = false;
            }
        }
    });
    root.addEventListener('submit', async (event) => {
        event.preventDefault();
        const form = event.target;
        if (form.dataset.busy === 'true') {
            return;
        }
        clearErrors(form);
        const params = {};
        if (active.key === 'count-by-house') {
            params.houseId = house?.id ?? null;
        }
        if (active.key === 'name-prefix') {
            params.prefix = form.elements.prefix.value;
        }
        if (active.key === 'most-expensive-of-three') {
            for (const name of ['id1', 'id2', 'id3'])
                params[name] = numberField(form, name, { integer: true, positive: true, max: 2147483647 });
            if (validForm(form) && new Set(Object.values(params)).size !== 3) {
                fieldError(form, 'id3', 'Укажите три разных ID.');
            }
        }
        if (!validForm(form)) {
            return;
        }
        const operation = active;
        const requestGeneration = generation;
        const output = findElement('#operation-result');
        output.hidden = true;
        lockForm(form, true);
        try {
            const result = await requestApi(`flats/operations/${operation.key}?${buildQueryParams(params)}`);
            if (requestGeneration !== generation) {
                return;
            }
            if (typeof result === 'number') {
                output.innerHTML = `<div class="result-count">${escapeHtml(display(result))}</div><p class="hint">Квартир ${house ? 'в выбранном доме' : 'без дома'}</p>`;
            } else if (Array.isArray(result)) {
                output.innerHTML = `<h3>Найдено: ${escapeHtml(display(result.length))}</h3><div class="table-scroll result-table">${renderTable(result, flatColumns, { actions: 'flat', empty: 'Квартир с таким началом названия нет.' })}</div>`;
            } else {
                output.innerHTML = `<h3>Найдена квартира</h3>${renderDetails(result, flatColumns.filter(([key]) => ['id', 'name', 'area', 'price', 'balcony', 'houseName'].includes(key)))}<div class="button-row result-actions"><button type="button" data-action="view" data-id="${escapeHtml(result.id)}">Открыть квартиру</button><button type="button" data-action="edit" data-id="${escapeHtml(result.id)}">Редактировать</button></div>`;
            }
            output.hidden = false;
        } catch (error) {
            if (requestGeneration !== generation) {
                return;
            }
            if (error.status === 404 && operation.key !== 'most-expensive-of-three') {
                output.textContent = 'Подходящих квартир пока нет.';
                output.hidden = false;
            } else {
                formError(form, error);
            }
        } finally {
            lockForm(form, false);
        }
    });
    render();
}
