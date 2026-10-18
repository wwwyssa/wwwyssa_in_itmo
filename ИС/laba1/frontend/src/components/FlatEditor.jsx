import { useEffect, useState } from 'react';
import { requestApi } from '../api.js';
import { errorText, display } from '../format.js';
import { focusError, numberValue } from '../validation.js';
import { ErrorMessage, Field, Modal } from './Common.jsx';

const empty = { name: '', area: '', price: '', numberOfRooms: '', timeToMetroOnFoot: '',
    view: '', transport: '', balcony: '', furniture: '', x: '', y: '', coordinateMode: 'new' };
const fields = [
    ['area', 'Площадь', { integer: true, positive: true }, { min: 1, step: 1 }],
    ['price', 'Цена', { positive: true }, { min: 0, step: 'any' }],
    ['numberOfRooms', 'Число комнат', { integer: true, positive: true, max: 11 }, { min: 1, max: 11, step: 1 }],
    ['timeToMetroOnFoot', 'Пешком до метро', { positive: true }, { min: 0, step: 'any' }],
];

export default function FlatEditor({ id, onClose, onSaved, onSelect }) {
    const [values, setValues] = useState(empty);
    const [coordinates, setCoordinates] = useState(null);
    const [house, setHouse] = useState(null);
    const [errors, setErrors] = useState({});
    const [error, setError] = useState(null);
    const [busy, setBusy] = useState(false);
    const [loading, setLoading] = useState(id != null);
    useEffect(() => {
        if (id == null) return;
        const controller = new AbortController();
        requestApi('flats/' + id, { signal: controller.signal }).then(flat => {
            if (controller.signal.aborted) return;
            const next = { ...empty, coordinateMode: 'existing' };
            for (const name of Object.keys(empty)) {
                if (Object.hasOwn(flat, name)) next[name] = flat[name] == null ? '' : String(flat[name]);
            }
            setValues(next);
            setCoordinates({ id: flat.coordinatesId, x: flat.x, y: flat.y });
            setHouse(flat.houseId == null ? null : { id: flat.houseId, name: flat.houseName, year: flat.houseYear, numberOfFloors: flat.houseNumberOfFloors });
        }).catch(exception => { if (!controller.signal.aborted) setError(exception); })
            .finally(() => { if (!controller.signal.aborted) setLoading(false); });
        return () => controller.abort();
    }, [id]);
    const change = event => {
        const { name, value } = event.target;
        setValues(previous => ({ ...previous, [name]: value }));
        setErrors(previous => ({ ...previous, [name]: undefined }));
    };
    const input = name => ({ name, value: values[name], onChange: change, error: errors[name], disabled: busy || loading });
    async function submit(event) {
        event.preventDefault();
        if (busy || loading) return;
        const found = {};
        if (!values.name.trim()) found.name = 'Введите название квартиры.';
        if (!values.view) found.view = 'Выберите вид из окна.';
        const body = { name: values.name, view: values.view, transport: values.transport || null,
            balcony: values.balcony === '' ? null : values.balcony === 'true',
            furniture: values.furniture === '' ? null : values.furniture === 'true', houseId: house?.id ?? null };
        for (const [name, , rules] of fields) body[name] = numberValue(values[name], name, found, rules);
        if (values.coordinateMode === 'existing') {
            if (!coordinates) found.coordinatesId = 'Выберите координаты.';
            else body.coordinatesId = coordinates.id;
        } else {
            body.x = numberValue(values.x, 'x', found);
            body.y = numberValue(values.y, 'y', found, { max: 249 });
        }
        setErrors(found);
        setError(null);
        if (Object.keys(found).length) { focusError(event.currentTarget, found); return; }
        setBusy(true);
        try {
            await requestApi(id == null ? 'flats' : 'flats/' + id, { method: id == null ? 'POST' : 'PUT', body });
            onSaved(id == null ? 'Квартира создана.' : 'Квартира сохранена.');
            onClose();
        } catch (exception) {
            setError(exception);
            const field = Object.keys(body).find(name => exception.message.includes(name));
            if (field) setErrors({ [field]: errorText(exception) });
        } finally { setBusy(false); }
    }
    return <Modal title={id == null ? 'Новая квартира' : 'Редактировать квартиру №' + id} onClose={onClose} busy={busy}>
        {loading ? <p className="dialog-body">Загрузка…</p> : error && id != null && values.name === '' ?
            <div className="dialog-body"><ErrorMessage error={error} /></div> :
            <form noValidate onSubmit={submit}>
                <div className="dialog-body">
                    <ErrorMessage error={error} />
                    <div className="form-grid">
                        <Field label="Название" {...input('name')} full autoComplete="off" required />
                        {fields.map(([name, label, , attrs]) => <Field key={name} label={label} type="number" {...attrs} {...input(name)} required />)}
                        <Field label="Вид из окна" {...input('view')} required>
                            <option value="">Выберите вид</option>{[['STREET', 'Улица'], ['YARD', 'Двор'], ['PARK', 'Парк'], ['BAD', 'Плохой'], ['TERRIBLE', 'Очень плохой']].map(([value, label]) => <option key={value} value={value}>{label} — {value}</option>)}
                        </Field>
                        <Field label="Транспорт" {...input('transport')}><option value="">Не указан</option>
                            {['FEW', 'LITTLE', 'NORMAL'].map(value => <option key={value}>{value}</option>)}</Field>
                        {['balcony', 'furniture'].map(name => <Field key={name} label={name === 'balcony' ? 'Балкон' : 'Мебель'} {...input(name)}>
                            <option value="">Не указано</option><option value="true">Есть</option><option value="false">Нет</option>
                        </Field>)}
                    </div>
                    <fieldset className="form-section" disabled={busy}>
                        <legend>Координаты</legend>
                        <div className="radio-row">{[['new', 'Новые'], ['existing', 'Существующие']].map(([value, label]) =>
                            <label key={value}><input type="radio" name="coordinateMode" value={value} checked={values.coordinateMode === value} onChange={change} />{label}</label>)}</div>
                        {values.coordinateMode === 'new' ? <div className="form-grid">
                            <Field label="X" type="number" step="any" {...input('x')} />
                            <Field label="Y" type="number" step="any" max={249} hint="Не больше 249" {...input('y')} />
                        </div> : <>
                            <div className="selection-row"><span>{coordinates ? '№' + coordinates.id + ' · X: ' + display(coordinates.x) + ' · Y: ' + display(coordinates.y) : 'Координаты не выбраны'}</span>
                                <button type="button" name="coordinatesId" onClick={() => onSelect('coordinates', item => { setCoordinates(item); setErrors({}); })}>Выбрать</button></div>
                            {errors.coordinatesId && <span className="field-error">{errors.coordinatesId}</span>}
                        </>}
                    </fieldset>
                    <fieldset className="form-section" disabled={busy}>
                        <legend>Дом <span className="optional">необязательно</span></legend>
                        <div className="selection-row"><span>{house ? (house.name || 'Дом без названия') + ' · №' + house.id + ' · ' + house.year + ', этажей: ' + house.numberOfFloors : 'Без дома'}</span>
                            <div className="button-row"><button type="button" onClick={() => onSelect('houses', item => { setHouse(item); setErrors({}); })}>Выбрать</button>
                                {house && <button type="button" onClick={() => setHouse(null)}>Убрать</button>}</div></div>
                        {errors.houseId && <span className="field-error">{errors.houseId}</span>}
                    </fieldset>
                </div>
                <footer className="dialog-footer"><button type="button" onClick={onClose} disabled={busy}>Отмена</button>
                    <button className="primary" disabled={busy}>{busy ? 'Сохранение…' : id == null ? 'Создать квартиру' : 'Сохранить изменения'}</button></footer>
            </form>}
    </Modal>;
}
