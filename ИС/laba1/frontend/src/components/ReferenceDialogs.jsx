import { useEffect, useState } from 'react';
import { buildQueryParams, requestApi } from '../api.js';
import { referenceColumns } from '../format.js';
import { useResource } from '../hooks.js';
import { focusError, numberValue } from '../validation.js';
import { Details, ErrorMessage, Field, FlatDetails, Modal, Pagination, Table } from './Common.jsx';

export function ReferenceEditor({ kind, onClose, onSaved, onCreated }) {
    const [values, setValues] = useState({ name: '', year: '', numberOfFloors: '', x: '', y: '' });
    const [errors, setErrors] = useState({});
    const [error, setError] = useState(null);
    const [busy, setBusy] = useState(false);
    const houses = kind === 'houses';
    const fields = houses ? [
        ['year', 'Год', { integer: true, positive: true }, { min: 1, step: 1 }],
        ['numberOfFloors', 'Число этажей', { integer: true, positive: true, max: 2147483647 }, { min: 1, max: 2147483647, step: 1 }],
    ] : [['x', 'X', {}, { step: 'any' }], ['y', 'Y', { max: 249 }, { max: 249, step: 'any' }]];
    const change = event => {
        const { name, value } = event.target;
        setValues(previous => ({ ...previous, [name]: value }));
        setErrors(previous => ({ ...previous, [name]: undefined }));
    };
    async function submit(event) {
        event.preventDefault();
        if (busy) return;
        const found = {};
        const body = houses ? { name: values.name } : {};
        for (const [name, , rules] of fields) body[name] = numberValue(values[name], name, found, rules);
        setErrors(found);
        setError(null);
        if (Object.keys(found).length) { focusError(event.currentTarget, found); return; }
        setBusy(true);
        try {
            const item = await requestApi(kind, { method: 'POST', body });
            onCreated?.(item);
            onSaved(houses ? 'Дом создан.' : 'Координаты созданы.');
            onClose();
        } catch (exception) { setError(exception); }
        finally { setBusy(false); }
    }
    return <Modal title={houses ? 'Новый дом' : 'Новые координаты'} onClose={onClose} busy={busy} small>
        <form noValidate onSubmit={submit}>
            <div className="dialog-body"><ErrorMessage error={error} /><div className="form-grid">
                {houses && <Field label="Название" name="name" value={values.name} onChange={change} hint="Можно оставить пустым" disabled={busy} />}
                {fields.map(([name, label, , attrs]) => <Field key={name} label={label} name={name} value={values[name]} onChange={change}
                    type="number" {...attrs} error={errors[name]} disabled={busy} required />)}
            </div></div>
            <footer className="dialog-footer"><button type="button" onClick={onClose} disabled={busy}>Отмена</button>
                <button className="primary" disabled={busy}>{busy ? 'Сохранение…' : 'Создать'}</button></footer>
        </form>
    </Modal>;
}

export function ReferencePicker({ kind, revision, onClose, onSelect, onCreate }) {
    const [draft, setDraft] = useState('');
    const [search, setSearch] = useState('');
    const [page, setPage] = useState(0);
    const [validation, setValidation] = useState(null);
    const path = kind === 'coordinates' && search ? 'coordinates/' + search :
        kind + '?' + buildQueryParams({ page, size: 5, sort: 'id', direction: 'desc', ...(kind === 'houses' ? { name: search } : {}) });
    const resource = useResource(path, revision);
    const data = kind === 'coordinates' && search && resource.data ?
        { items: [resource.data], page: 0, size: 5, totalElements: 1, totalPages: 1 } : resource.data;
    useEffect(() => {
        if (!resource.loading && data && page > 0 && page >= data.totalPages) setPage(Math.max(0, data.totalPages - 1));
    }, [data, resource.loading, page]);
    function filter(event) {
        event.preventDefault();
        const value = draft.trim();
        if (kind === 'coordinates' && value && (!/^\d+$/.test(value) || !Number.isSafeInteger(Number(value)) || Number(value) <= 0)) {
            setValidation('Введите положительный целый ID.');
            return;
        }
        setValidation(null);
        setSearch(value);
        setPage(0);
    }
    return <Modal title={kind === 'houses' ? 'Выбрать дом' : 'Выбрать координаты'} onClose={onClose}
        footer={<><button type="button" onClick={onClose}>Отмена</button>
            <button type="button" onClick={() => onCreate(kind, item => { onSelect(item); onClose(); })}>
                {kind === 'houses' ? 'Создать дом' : 'Создать координаты'}</button></>}>
        <div className="dialog-body">
            <form className="inline-form" onSubmit={filter}>
                <Field label={kind === 'houses' ? 'Название дома' : 'ID координат'} name="search" value={draft} onChange={event => setDraft(event.target.value)}
                    placeholder={kind === 'houses' ? 'Поиск по части названия' : 'Все записи или конкретный ID'} />
                <button>Найти</button><button type="button" onClick={() => { setDraft(''); setSearch(''); setPage(0); setValidation(null); }}>Сбросить</button>
            </form>
            <ErrorMessage error={validation || resource.error} />
            <div className="table-scroll picker-table" aria-busy={resource.loading}>
                <Table items={data?.items || []} columns={referenceColumns[kind]} actions="pick"
                    onAction={(_, item) => { onSelect(item); onClose(); }}
                    empty={resource.loading ? 'Загрузка…' : search ? 'Ничего не найдено.' : 'Пока нет записей. Можно создать новый объект.'} />
            </div>
            {data && <Pagination page={data} busy={resource.loading} onPage={setPage} />}
        </div>
    </Modal>;
}

export function ObjectView({ kind, id, revision, onClose, onEdit, onDelete }) {
    const resource = useResource(kind + '/' + id, revision);
    const isFlat = kind === 'flats';
    const title = (isFlat ? 'Квартира №' : kind === 'houses' ? 'Дом №' : 'Координаты №') + id;
    return <Modal title={title} onClose={onClose} small={!isFlat}
        footer={<><button type="button" onClick={onClose}>Закрыть</button>
            {isFlat && resource.data && <><button type="button" onClick={() => onDelete(resource.data)}>Удалить</button>
                <button type="button" className="primary" onClick={onEdit}>Редактировать</button></>}</>}>
        <div className="dialog-body" aria-busy={resource.loading}>
            <ErrorMessage error={resource.error} />
            {resource.data ? isFlat ? <FlatDetails flat={resource.data} /> : <Details item={resource.data} columns={referenceColumns[kind]} /> :
                resource.loading && <p>Загрузка…</p>}
        </div>
    </Modal>;
}

export function DeleteDialog({ flat, onClose, onSaved }) {
    const [error, setError] = useState(null);
    const [busy, setBusy] = useState(false);
    async function submit(event) {
        event.preventDefault();
        if (busy) return;
        setBusy(true);
        setError(null);
        try {
            await requestApi('flats/' + flat.id, { method: 'DELETE' });
            onSaved('Квартира удалена.');
            onClose();
        } catch (exception) { setError(exception); }
        finally { setBusy(false); }
    }
    return <Modal title="Удалить квартиру?" onClose={onClose} busy={busy} small>
        <form onSubmit={submit}>
            <div className="dialog-body"><p>Квартира «{flat.name}», ID {flat.id}, будет удалена.</p>
                <p className="hint">Это действие нельзя отменить.</p><ErrorMessage error={error} /></div>
            <footer className="dialog-footer"><button type="button" onClick={onClose} disabled={busy}>Отмена</button>
                <button className="primary" disabled={busy}>{busy ? 'Удаление…' : 'Удалить'}</button></footer>
        </form>
    </Modal>;
}

export function LookupDialog({ kind, onClose, onFound }) {
    const [id, setId] = useState('');
    const [error, setError] = useState(null);
    const [errors, setErrors] = useState({});
    const [busy, setBusy] = useState(false);
    async function submit(event) {
        event.preventDefault();
        if (busy) return;
        const found = {};
        const value = numberValue(id, 'id', found, { integer: true, positive: true, max: kind === 'flats' ? 2147483647 : Number.MAX_SAFE_INTEGER });
        setErrors(found);
        if (Object.keys(found).length) { focusError(event.currentTarget, found); return; }
        setBusy(true);
        setError(null);
        try {
            await requestApi(kind + '/' + value);
            onFound(value);
            onClose();
        } catch (exception) { setError(exception); }
        finally { setBusy(false); }
    }
    return <Modal title="Найти по ID" onClose={onClose} busy={busy} small>
        <form noValidate onSubmit={submit}>
            <div className="dialog-body"><ErrorMessage error={error} />
                <Field label="ID" name="id" type="number" value={id} onChange={event => setId(event.target.value)} min={1} step={1}
                    error={errors.id} disabled={busy} required /></div>
            <footer className="dialog-footer"><button type="button" onClick={onClose} disabled={busy}>Отмена</button>
                <button className="primary" disabled={busy}>Открыть</button></footer>
        </form>
    </Modal>;
}
