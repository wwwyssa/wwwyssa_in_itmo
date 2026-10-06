import { requestApi } from './api.js';
import { findElement, display, clearErrors, fieldError, formError, numberField, validForm, lockForm, openDialog } from './ui.js';
export function initFlatEditor({ selectReference, onSaved }) {
    const dialog = findElement('#flat-editor');
    const form = findElement('#flat-form');
    let id = null;
    let coordinates = null;
    let house = null;
    let openRequest = 0;
    function renderSelection() {
        const existing = form.elements.coordinateMode.value === 'existing';
        findElement('#new-coordinates').hidden = existing;
        findElement('#existing-coordinates').hidden = !existing;
        findElement('#selected-coordinates').textContent = coordinates ? `№${coordinates.id} · X: ${display(coordinates.x)} · Y: ${display(coordinates.y)}` : 'Координаты не выбраны';
        findElement('#selected-house').textContent = house ? `${house.name || 'Дом без названия'} · №${house.id} · ${house.year}, этажей: ${house.numberOfFloors}` : 'Без дома';
        findElement('#clear-house').hidden = !house;
    }
    form.addEventListener('change', event => {
        if (event.target.name === 'coordinateMode') {
            clearErrors(form);
            renderSelection();
        }
    });
    findElement('#choose-coordinates').addEventListener('click', () => selectReference('coordinates', item => {
        coordinates = item;
        clearErrors(form);
        renderSelection();
    }));
    findElement('#choose-house').addEventListener('click', () => selectReference('houses', item => {
        house = item;
        clearErrors(form);
        renderSelection();
    }));
    findElement('#clear-house').addEventListener('click', () => {
        house = null;
        renderSelection();
    });
    form.addEventListener('submit', async (event) => {
        event.preventDefault();
        if (form.dataset.busy === 'true') {
            return;
        }
        clearErrors(form);
        const name = form.elements.name.value;
        if (!name.trim()) {
            fieldError(form, 'name', 'Введите название квартиры.');
        }
        const body = {
            name,
            area: numberField(form, 'area', { integer: true, positive: true }),
            price: numberField(form, 'price', { positive: true }),
            numberOfRooms: numberField(form, 'numberOfRooms', { integer: true, positive: true, max: 11 }),
            timeToMetroOnFoot: numberField(form, 'timeToMetroOnFoot', { positive: true }),
            view: form.elements.view.value,
            transport: form.elements.transport.value || null,
            balcony: form.elements.balcony.value === '' ? null : form.elements.balcony.value === 'true',
            furniture: form.elements.furniture.value === '' ? null : form.elements.furniture.value === 'true',
            houseId: house?.id ?? null,
        };
        if (!body.view) {
            fieldError(form, 'view', 'Выберите вид из окна.');
        }
        if (form.elements.coordinateMode.value === 'existing') {
            if (!coordinates) {
                fieldError(form, 'coordinatesId', 'Выберите координаты.');
            } else {
                body.coordinatesId = coordinates.id;
            }
        } else {
            body.x = numberField(form, 'x');
            body.y = numberField(form, 'y', { max: 249 });
        }
        if (!validForm(form)) {
            return;
        }
        lockForm(form, true);
        try {
            await requestApi(id === null ? 'flats' : `flats/${id}`, { method: id === null ? 'POST' : 'PUT', body });
            dialog.close();
            await onSaved(id === null ? 'Квартира создана.' : 'Квартира сохранена.');
        } catch (error) {
            formError(form, error);
        } finally {
            lockForm(form, false);
        }
    });
    return {
        async open(flatId = null) {
            const request = ++openRequest;
            const flat = flatId === null ? null : await requestApi(`flats/${flatId}`);
            if (request !== openRequest) {
                return;
            }
            form.reset();
            clearErrors(form);
            id = flatId;
            coordinates = flat ? { id: flat.coordinatesId, x: flat.x, y: flat.y } : null;
            house = flat?.houseId == null ? null : { id: flat.houseId, name: flat.houseName, year: flat.houseYear, numberOfFloors: flat.houseNumberOfFloors };
            if (flat) {
                for (const name of ['name', 'area', 'price', 'numberOfRooms', 'timeToMetroOnFoot', 'view', 'transport', 'balcony', 'furniture']) {
                    form.elements.namedItem(name).value = flat[name] == null ? '' : String(flat[name]);
                }
                form.elements.coordinateMode.value = 'existing';
            }
            findElement('#flat-editor-title').textContent = flat ? `Редактировать квартиру №${flat.id}` : 'Новая квартира';
            findElement('button[type=submit]', form).textContent = flat ? 'Сохранить изменения' : 'Создать квартиру';
            renderSelection();
            openDialog(dialog);
            form.elements.name.focus();
        },
    };
}
