import { requestApi, buildQueryParams, getAuth, setAuth } from './api.js';
import { findElement, renderTable, renderPagination, flatDetails, flatColumns, referenceColumns, clearErrors, formError, numberField, validForm, lockForm, openDialog, setupDialogs, errorText } from './ui.js';
import { initFlatEditor } from './flat-editor.js';
import { initReferences } from './references.js';
import { initOperations } from './operations.js';
const sections = {
    flats: { title: 'Квартиры', subtitle: 'Все квартиры и их характеристики', create: 'Добавить квартиру', columns: flatColumns },
    houses: { title: 'Дома', subtitle: 'Дома, которые можно связать с квартирой', create: 'Добавить дом', columns: referenceColumns.houses },
    coordinates: { title: 'Координаты', subtitle: 'Координаты, которые можно использовать повторно', create: 'Добавить координаты', columns: referenceColumns.coordinates },
};
const states = {};
for (const kind of Object.keys(sections)) {
    states[kind] = { page: 0, size: 10, sort: 'id', direction: 'asc', filters: {}, controller: null };
}
let current = 'flats';
let detailId = null;
let detailRequest = 0;
let deleting = null;
let lookupKind = 'flats';
setupDialogs();
const references = initReferences({ onSaved });
const editor = initFlatEditor({ selectReference: references.openPicker, onSaved });
initOperations({ selectHouse: references.openPicker, onView: openFlat, onEdit: id => editor.open(id), onError: error => notice(errorText(error)) });
function notice(message) {
    const container = findElement('#notice');
    container.replaceChildren();
    const text = document.createElement('span');
    text.textContent = message;
    const close = document.createElement('button');
    close.type = 'button';
    close.textContent = '×';
    close.setAttribute('aria-label', 'Скрыть сообщение');
    close.addEventListener('click', () => {
        container.hidden = true;
    });
    container.append(text, close);
    container.hidden = false;
}
async function onSaved(message) {
    notice(message);
    if (sections[current]) {
        await loadList(current);
    }
}
for (const [kind, config] of Object.entries(sections)) {
    const root = findElement(`#${kind}-section`);
    let filters = '';
    if (kind === 'flats') {
        filters = '<form class="filter-bar" data-filters><label class="field">Название<input name="name" placeholder="Часть названия" autocomplete="off"></label><label class="field">Дом<input name="houseName" placeholder="Часть названия дома" autocomplete="off"></label><label class="field">Число комнат<input name="numberOfRooms" type="number" min="1" max="11" step="1"></label><label class="field">Пешком до метро<input name="timeToMetroOnFoot" type="number" min="0" step="any"></label><div class="button-row"><button type="submit">Найти</button><button type="reset">Сбросить</button></div></form>';
    }
    if (kind === 'houses') {
        filters = '<form class="filter-bar compact" data-filters><label class="field">Название дома<input name="name" placeholder="Часть названия" autocomplete="off"></label><div class="button-row"><button type="submit">Найти</button><button type="reset">Сбросить</button></div></form>';
    }
    root.innerHTML = `<header class="page-heading"><div><h1>${config.title}</h1><p class="subheading">${config.subtitle}</p></div><div class="button-row"><button type="button" data-refresh>Обновить</button><button type="button" data-lookup>Найти по ID</button><button type="button" class="primary" data-create>${config.create}</button></div></header><div class="panel">${filters}<p class="table-error" role="alert" hidden></p><div class="table-scroll" data-table aria-label="${config.title}">${renderTable([], config.columns, { empty: 'Загрузка…' })}</div><div class="pagination" data-pagination></div></div>`;
    root.addEventListener('click', async (event) => {
        const button = event.target.closest('button');
        if (!button || button.disabled) {
            return;
        }
        const state = states[kind];
        if (button.hasAttribute('data-refresh')) {
            await loadList(kind);
            return;
        }
        if (button.hasAttribute('data-create')) {
            if (kind === 'flats') {
                await editor.open();
            } else {
                references.openCreate(kind);
            }
            return;
        }
        if (button.hasAttribute('data-lookup')) {
            lookupKind = kind;
            findElement('#lookup-form').reset();
            clearErrors(findElement('#lookup-form'));
            findElement('#lookup-title').textContent = kind === 'flats' ? 'Найти квартиру по ID' : kind === 'houses' ? 'Найти дом по ID' : 'Найти координаты по ID';
            openDialog(findElement('#lookup-dialog'));
            findElement('#lookup-form input').focus();
            return;
        }
        if (button.dataset.sort) {
            state.direction = state.sort === button.dataset.sort && state.direction === 'asc' ? 'desc' : 'asc';
            state.sort = button.dataset.sort;
            state.page = 0;
            await loadList(kind);
            return;
        }
        if (button.dataset.page) {
            state.page += button.dataset.page === 'next' ? 1 : -1;
            await loadList(kind);
            return;
        }
        if (button.dataset.action) {
            button.disabled = true;
            try {
                const id = Number(button.dataset.id);
                if (kind !== 'flats') {
                    await references.openView(kind, id);
                } else if (button.dataset.action === 'edit') {
                    await editor.open(id);
                } else {
                    await openFlat(id);
                }
            } catch (error) {
                notice(errorText(error));
            } finally {
                button.disabled = false;
            }
        }
    });
    root.addEventListener('change', event => {
        if (event.target.hasAttribute('data-page-size')) {
            states[kind].size = Number(event.target.value);
            states[kind].page = 0;
            loadList(kind);
        }
    });
    const form = findElement('[data-filters]', root);
    if (form) {
        form.addEventListener('submit', event => {
            event.preventDefault();
            states[kind].filters = Object.fromEntries(
                [...new FormData(form)].filter(([, value]) => value !== '')
            );
            states[kind].page = 0;
            loadList(kind);
        });
        form.addEventListener('reset', () => {
            states[kind].filters = {};
            states[kind].page = 0;
            loadList(kind);
        });
    }
}
async function loadList(kind, allowClamp = true) {
    if (!getAuth()) {
        return;
    }
    const root = findElement(`#${kind}-section`);
    const state = states[kind];
    state.controller?.abort();
    const controller = new AbortController();
    state.controller = controller;
    const output = findElement('[data-table]', root);
    const errorOutput = findElement('.table-error', root);
    errorOutput.hidden = true;
    output.setAttribute('aria-busy', 'true');
    findElement('[data-refresh]', root).disabled = true;
    findElement('[data-pagination]', root).querySelectorAll('button,select').forEach(element => {
        element.disabled = true;
    });
    try {
        const page = await requestApi(`${kind}?${buildQueryParams({ page: state.page, size: state.size, sort: state.sort, direction: state.direction, ...state.filters })}`, { signal: controller.signal });
        if (state.controller !== controller) {
            return;
        }
        if (allowClamp && state.page > 0 && state.page >= page.totalPages) {
            state.page = Math.max(0, page.totalPages - 1);
            await loadList(kind, false);
            return;
        }
        output.innerHTML = renderTable(page.items, sections[kind].columns, { sort: state.sort, direction: state.direction, actions: kind === 'flats' ? 'flat' : 'view', empty: Object.values(state.filters).some(Boolean) ? 'Ничего не найдено. Измените фильтры или сбросьте их.' : `Пока нет записей. ${sections[kind].create}.` });
        findElement('[data-pagination]', root).innerHTML = renderPagination(page);
    } catch (error) {
        if (error.name === 'AbortError' || state.controller !== controller) {
            return;
        }
        errorOutput.textContent = `${errorText(error)} Нажмите «Обновить», чтобы повторить запрос.`;
        errorOutput.hidden = false;
        output.innerHTML = renderTable([], sections[kind].columns, { empty: 'Не удалось загрузить записи.' });
        findElement('[data-pagination]', root).innerHTML = '';
    } finally {
        if (state.controller === controller) {
            output.setAttribute('aria-busy', 'false');
            findElement('[data-refresh]', root).disabled = false;
        }
    }
}
async function openFlat(id) {
    const request = ++detailRequest;
    const flat = await requestApi(`flats/${id}`);
    if (request !== detailRequest) {
        return;
    }
    detailId = flat.id;
    findElement('#flat-detail-title').textContent = `Квартира №${flat.id}`;
    findElement('#flat-detail-body').innerHTML = flatDetails(flat);
    openDialog(findElement('#flat-detail'));
}
findElement('#detail-edit').addEventListener('click', async (event) => {
    event.target.disabled = true;
    try {
        await editor.open(detailId);
        findElement('#flat-detail').close();
    } catch (error) {
        notice(errorText(error));
    } finally {
        event.target.disabled = false;
    }
});
findElement('#detail-delete').addEventListener('click', async (event) => {
    event.target.disabled = true;
    try {
        deleting = await requestApi(`flats/${detailId}`);
        findElement('#delete-description').textContent = `Квартира «${deleting.name}», ID ${deleting.id}, будет удалена.`;
        clearErrors(findElement('#delete-form'));
        openDialog(findElement('#delete-dialog'));
    } catch (error) {
        notice(errorText(error));
    } finally {
        event.target.disabled = false;
    }
});
findElement('#delete-form').addEventListener('submit', async (event) => {
    event.preventDefault();
    const form = event.target;
    if (form.dataset.busy === 'true') {
        return;
    }
    clearErrors(form);
    lockForm(form, true);
    try {
        await requestApi(`flats/${deleting.id}`, { method: 'DELETE' });
        findElement('#delete-dialog').close();
        findElement('#flat-detail').close();
        await onSaved('Квартира удалена.');
    } catch (error) {
        formError(form, error);
    } finally {
        lockForm(form, false);
    }
});
findElement('#lookup-form').addEventListener('submit', async (event) => {
    event.preventDefault();
    const form = event.target;
    if (form.dataset.busy === 'true') {
        return;
    }
    clearErrors(form);
    const id = numberField(form, 'id', { integer: true, positive: true, max: lookupKind === 'flats' ? 2147483647 : Number.MAX_SAFE_INTEGER });
    if (!validForm(form)) {
        return;
    }
    lockForm(form, true);
    try {
        if (lookupKind === 'flats') {
            await openFlat(id);
        } else {
            await references.openView(lookupKind, id);
        }
        findElement('#lookup-dialog').close();
    } catch (error) {
        formError(form, error);
    } finally {
        lockForm(form, false);
    }
});
function route() {
    const name = location.hash.slice(1);
    current = Object.hasOwn(sections, name) || name === 'operations' ? name : 'flats';
    document.querySelectorAll('[data-section]').forEach(section => {
        section.hidden = section.dataset.section !== current;
    });
    document.querySelectorAll('[data-nav]').forEach(link => {
        if (link.dataset.nav === current) {
            link.setAttribute('aria-current', 'page');
        } else {
            link.removeAttribute('aria-current');
        }
    });
    document.title = `${sections[current]?.title || 'Операции'} · Квартиры`;
    if (getAuth() && sections[current]) {
        loadList(current);
    }
}
window.addEventListener('hashchange', route);
document.querySelectorAll('[data-nav],.brand').forEach(link => link.addEventListener('click', () => {
    if (link.hash === location.hash && sections[current]) {
        loadList(current);
    }
}));
route();
let updatesSocket = null;
let reconnectTimer;
function connectUpdates() {
    const auth = getAuth();
    if (!auth) {
        return;
    }
    const url = new URL('./updates', document.baseURI);
    url.protocol = url.protocol === 'https:' ? 'wss:' : 'ws:';
    const socket = new WebSocket(url);
    updatesSocket = socket;
    function refreshList() {
        if (sections[current]) {
            loadList(current);
        }
    }
    socket.onopen = () => socket.send(auth.token);
    socket.onmessage = event => {
        if (updatesSocket === socket && (event.data === 'ready' || event.data === 'changed')) {
            refreshList();
        }
    };
    socket.onclose = event => {
        if (updatesSocket !== socket) {
            return;
        }
        updatesSocket = null;
        if (event.code === 1008) {
            setAuth(null);
        } else if (getAuth()) {
            reconnectTimer = setTimeout(connectUpdates, 3000);
        }
    };
    socket.onerror = () => socket.close();
}
document.addEventListener('authchange', () => {
    clearTimeout(reconnectTimer);
    const previousSocket = updatesSocket;
    updatesSocket = null;
    previousSocket?.close();
    detailRequest++;
    for (const state of Object.values(states)) {
        state.controller?.abort();
    }
    if (getAuth()) {
        route();
        connectUpdates();
    }
});
connectUpdates();
