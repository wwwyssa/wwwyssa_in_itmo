import { useEffect, useState } from 'react';
import { buildQueryParams } from '../api.js';
import { display, flatColumns } from '../format.js';
import { focusError, numberValue } from '../validation.js';
import { useResource } from '../hooks.js';
import { Details, ErrorMessage, Field, Table } from './Common.jsx';

const operations = [
    ['max-area', 'Максимальная площадь', 'Найти квартиру с самой большой площадью.'],
    ['count-by-house', 'Количество квартир в доме', 'Посчитать квартиры, связанные с выбранным домом. Можно выбрать квартиры без дома.'],
    ['name-prefix', 'Поиск по началу названия', 'Регистр учитывается; пустая строка найдёт все квартиры.'],
    ['most-expensive-without-balcony', 'Самая дорогая без балкона', 'Найти самую дорогую квартиру, у которой точно нет балкона.'],
    ['most-expensive-of-three', 'Самая дорогая из трёх', 'Укажите три разных ID квартир, чтобы выбрать из них самую дорогую.'],
];

function OperationResult({ request, revision, onAction }) {
    const resource = useResource(request.path, revision);
    const result = resource.data;
    const empty = resource.error?.status === 404 && request.key !== 'most-expensive-of-three';
    return <div className="operation-result" role="status" aria-busy={resource.loading}>
        {resource.loading && <p>Загрузка…</p>}
        {empty ? <p>Подходящих квартир пока нет.</p> : <ErrorMessage error={resource.error} />}
        {typeof result === 'number' ? <><div className="result-count">{display(result)}</div>
            <p className="hint">Квартир {request.house ? 'в выбранном доме' : 'без дома'}</p></> :
            Array.isArray(result) ? <><h3>Найдено: {result.length}</h3><div className="table-scroll result-table">
                <Table items={result} columns={flatColumns} actions="flat" onAction={onAction} empty="Квартир с таким началом названия нет." /></div></> :
                result && <><h3>Найдена квартира</h3><Details item={result} columns={flatColumns.filter(([key]) => ['id', 'name', 'area', 'price', 'balcony', 'houseName'].includes(key))} />
                    <div className="button-row result-actions"><button type="button" onClick={() => onAction('view', result)}>Открыть квартиру</button>
                        <button type="button" onClick={() => onAction('edit', result)}>Редактировать</button></div></>}
    </div>;
}

export default function Operations({ revision, onSelect, onAction }) {
    const [active, setActive] = useState(operations[0][0]);
    const [values, setValues] = useState({ prefix: '', id1: '', id2: '', id3: '' });
    const [house, setHouse] = useState(null);
    const [errors, setErrors] = useState({});
    const [request, setRequest] = useState(null);
    const [sequence, setSequence] = useState(0);
    const [, title, description] = operations.find(([key]) => key === active);
    useEffect(() => { setRequest(null); setErrors({}); setHouse(null); setValues({ prefix: '', id1: '', id2: '', id3: '' }); }, [active]);
    function submit(event) {
        event.preventDefault();
        const found = {};
        const params = {};
        if (active === 'count-by-house') params.houseId = house?.id ?? null;
        if (active === 'name-prefix') params.prefix = values.prefix;
        if (active === 'most-expensive-of-three') {
            for (const name of ['id1', 'id2', 'id3']) params[name] = numberValue(values[name], name, found, { integer: true, positive: true, max: 2147483647 });
            if (!Object.keys(found).length && new Set(Object.values(params)).size !== 3) found.id3 = 'Укажите три разных ID.';
        }
        setErrors(found);
        if (Object.keys(found).length) { focusError(event.currentTarget, found); return; }
        setSequence(previous => previous + 1);
        setRequest({ key: active, path: 'flats/operations/' + active + '?' + buildQueryParams(params), house });
    }
    const change = event => {
        const { name, value } = event.target;
        setValues(previous => ({ ...previous, [name]: value }));
    };
    return <section>
        <header className="page-heading"><div><h1>Специальные операции</h1><p className="subheading">Поиск и сравнение квартир</p></div></header>
        <div className="operations-layout">
            <div className="operations-menu" aria-label="Выбор операции">{operations.map(([key, label]) =>
                <button key={key} type="button" className="operation-choice" aria-pressed={active === key} onClick={() => setActive(key)}>{label}</button>)}</div>
            <div className="panel operation-panel"><h2>{title}</h2><p className="hint">{description}</p>
                <form className="operation-form" noValidate onSubmit={submit}>
                    {active === 'name-prefix' && <Field label="Начало названия" name="prefix" value={values.prefix} onChange={change} placeholder="Например, Квартира" />}
                    {active === 'most-expensive-of-three' && <div className="form-grid">{[1, 2, 3].map(index =>
                        <Field key={index} label={'ID квартиры ' + index} name={'id' + index} value={values['id' + index]} onChange={change}
                            type="number" min={1} max={2147483647} step={1} error={errors['id' + index]} required />)}</div>}
                    {active === 'count-by-house' && <div className="selection-row full">
                        <span>{house ? (house.name || 'Дом без названия') + ' · №' + house.id : 'Квартиры без дома'}</span>
                        <div className="button-row"><button type="button" onClick={() => onSelect('houses', setHouse)}>Выбрать дом</button>
                            {house && <button type="button" onClick={() => setHouse(null)}>Убрать</button>}</div></div>}
                    <button className="primary">Выполнить</button>
                </form>
                {request && <OperationResult key={sequence} request={request} revision={revision} onAction={onAction} />}
            </div>
        </div>
    </section>;
}
