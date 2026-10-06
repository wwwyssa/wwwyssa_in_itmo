import { requestApi, buildQueryParams } from './api.js';
import { findElement, renderTable, renderPagination, renderDetails, referenceColumns, clearErrors, formError, numberField, validForm, lockForm, openDialog, errorText } from './ui.js';
export function initReferences({ onSaved }) {
    const editor = findElement('#reference-editor');
    const form = findElement('#reference-form');
    const picker = findElement('#reference-picker');
    let editing = null;
    let picking = null;
    let viewRequest = 0;
    function openCreate(kind, callback = null) {
        editing = { kind, callback };
        findElement('#reference-editor-title').textContent = kind === 'houses' ? 'Новый дом' : 'Новые координаты';
        findElement('#reference-fields').innerHTML = kind === 'houses'
            ? '<label class="field">Название<input name="name" autocomplete="off"><span class="hint">Можно оставить пустым</span></label><label class="field">Год<input name="year" type="number" min="1" step="1" required><span class="field-error" data-error-for="year"></span></label><label class="field">Число этажей<input name="numberOfFloors" type="number" min="1" max="2147483647" step="1" required><span class="field-error" data-error-for="numberOfFloors"></span></label>'
            : '<label class="field">X<input name="x" type="number" step="any" required><span class="field-error" data-error-for="x"></span></label><label class="field">Y<input name="y" type="number" max="249" step="any" required><span class="field-error" data-error-for="y"></span><span class="hint">Не больше 249</span></label>';
        form.reset();
        clearErrors(form);
        openDialog(editor);
        findElement('input', form).focus();
    }
    form.addEventListener('submit', async (event) => {
        event.preventDefault();
        if (form.dataset.busy === 'true') {
            return;
        }
        clearErrors(form);
        const { kind, callback } = editing;
        const body = kind === 'houses'
            ? { name: form.elements.name.value, year: numberField(form, 'year', { integer: true, positive: true }), numberOfFloors: numberField(form, 'numberOfFloors', { integer: true, positive: true, max: 2147483647 }) }
            : { x: numberField(form, 'x'), y: numberField(form, 'y', { max: 249 }) };
        if (!validForm(form)) {
            return;
        }
        lockForm(form, true);
        try {
            const item = await requestApi(kind, { method: 'POST', body });
            editor.close();
            if (callback) {
                callback(item);
            }
            await onSaved(kind === 'houses' ? 'Дом создан.' : 'Координаты созданы.');
        } catch (error) {
            formError(form, error);
        } finally {
            lockForm(form, false);
        }
    });
    async function loadPicker() {
        const state = picking;
        state.controller?.abort();
        const controller = new AbortController();
        state.controller = controller;
        findElement('#picker-error').hidden = true;
        findElement('#picker-table').setAttribute('aria-busy', 'true');
        findElement('#picker-pagination').innerHTML = '';
        try {
            let page;
            if (state.kind === 'coordinates' && state.search) {
                if (!/^\d+$/.test(state.search) || !Number.isSafeInteger(Number(state.search)) || Number(state.search) <= 0) {
                    throw new Error('Введите положительный целый ID.');
                }
                const item = await requestApi(`coordinates/${state.search}`, { signal: controller.signal });
                page = { items: [item], page: 0, size: 5, totalElements: 1, totalPages: 1 };
            } else {
                page = await requestApi(`${state.kind}?${buildQueryParams({ page: state.page, size: 5, sort: 'id', direction: 'desc', ...(state.kind === 'houses' ? { name: state.search } : {}) })}`, { signal: controller.signal });
            }
            if (picking !== state || state.controller !== controller) {
                return;
            }
            state.items = page.items;
            findElement('#picker-table').innerHTML = renderTable(page.items, referenceColumns[state.kind], { actions: 'pick', empty: state.search ? 'Ничего не найдено. Измените условия поиска.' : 'Пока нет записей. Можно создать новый объект.' });
            findElement('#picker-pagination').innerHTML = renderPagination(page, { picker: true });
        } catch (error) {
            if (error.name === 'AbortError' || picking !== state || state.controller !== controller) {
                return;
            }
            findElement('#picker-error').textContent = errorText(error);
            findElement('#picker-error').hidden = false;
            findElement('#picker-table').innerHTML = renderTable([], referenceColumns[state.kind], { actions: 'pick', empty: 'Не удалось загрузить записи.' });
        } finally {
            if (picking === state && state.controller === controller) {
                findElement('#picker-table').setAttribute('aria-busy', 'false');
            }
        }
    }
    function select(item) {
        const callback = picking.callback;
        picker.close();
        callback(item);
    }
    findElement('#picker-filter').addEventListener('submit', event => {
        event.preventDefault();
        picking.search = event.target.elements.search.value.trim();
        picking.page = 0;
        loadPicker();
    });
    findElement('#picker-reset').addEventListener('click', () => {
        findElement('#picker-filter').reset();
        picking.search = '';
        picking.page = 0;
        loadPicker();
    });
    findElement('#picker-table').addEventListener('click', event => {
        const button = event.target.closest('[data-action=pick]');
        if (button) {
            select(picking.items.find(item => String(item.id) === button.dataset.id));
        }
    });
    findElement('#picker-pagination').addEventListener('click', event => {
        const button = event.target.closest('[data-page]');
        if (!button || button.disabled) {
            return;
        }
        picking.page += button.dataset.page === 'next' ? 1 : -1;
        loadPicker();
    });
    findElement('#picker-create').addEventListener('click', () => openCreate(picking.kind, select));
    picker.addEventListener('close', () => picking?.controller?.abort());
    return {
        openCreate,
        openPicker(kind, callback) {
            picking?.controller?.abort();
            picking = { kind, callback, page: 0, search: '', items: [] };
            findElement('#reference-picker-title').textContent = kind === 'houses' ? 'Выбрать дом' : 'Выбрать координаты';
            findElement('#picker-filter-label').textContent = kind === 'houses' ? 'Название дома' : 'ID координат';
            findElement('#picker-filter').reset();
            findElement('#picker-filter input').placeholder = kind === 'houses' ? 'Поиск по части названия' : 'Все записи или конкретный ID';
            findElement('#picker-table').innerHTML = renderTable([], referenceColumns[kind], { actions: 'pick', empty: 'Загрузка…' });
            findElement('#picker-create').textContent = kind === 'houses' ? 'Создать дом' : 'Создать координаты';
            openDialog(picker);
            findElement('#picker-filter input').focus();
            loadPicker();
        },
        async openView(kind, id) {
            const request = ++viewRequest;
            const item = await requestApi(`${kind}/${id}`);
            if (request !== viewRequest) {
                return;
            }
            findElement('#reference-detail-title').textContent = kind === 'houses' ? `Дом №${item.id}` : `Координаты №${item.id}`;
            findElement('#reference-detail-body').innerHTML = renderDetails(item, referenceColumns[kind]);
            openDialog(findElement('#reference-detail'));
        },
    };
}
