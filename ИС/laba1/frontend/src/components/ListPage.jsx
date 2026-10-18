import { useEffect } from 'react';
import { buildQueryParams } from '../api.js';
import { sections } from '../format.js';
import { useResource } from '../hooks.js';
import { ErrorMessage, Field, Pagination, Table } from './Common.jsx';

export default function ListPage({ kind, state, setState, revision, refresh, onCreate, onLookup, onAction }) {
    const config = sections[kind];
    const { page, size, sort, direction, filters, draft } = state;
    const resource = useResource(kind + '?' + buildQueryParams({ page, size, sort, direction, ...filters }), revision);
    useEffect(() => {
        if (!resource.loading && resource.data && page > 0 && page >= resource.data.totalPages) {
            setState(previous => ({ ...previous, page: Math.max(0, resource.data.totalPages - 1) }));
        }
    }, [resource.loading, resource.data, page, setState]);
    const updateDraft = event => setState(previous => ({ ...previous, draft: { ...previous.draft, [event.target.name]: event.target.value } }));
    function submit(event) {
        event.preventDefault();
        setState(previous => ({ ...previous, page: 0, filters: Object.fromEntries(Object.entries(previous.draft).filter(([, value]) => value !== '')) }));
        refresh();
    }
    return <section>
        <header className="page-heading">
            <div><h1>{config.title}</h1><p className="subheading">{config.subtitle}</p></div>
            <div className="button-row">
                <button type="button" onClick={refresh} disabled={resource.loading}>Обновить</button>
                <button type="button" onClick={onLookup}>Найти по ID</button>
                <button type="button" className="primary" onClick={onCreate}>{config.create}</button>
            </div>
        </header>
        <div className="panel">
            {kind !== 'coordinates' && <form className={'filter-bar' + (kind === 'houses' ? ' compact' : '')} onSubmit={submit}>
                <Field label={kind === 'houses' ? 'Название дома' : 'Название'} name="name" value={draft.name || ''}
                    onChange={updateDraft} placeholder="Часть названия" autoComplete="off" />
                {kind === 'flats' && <>
                    <Field label="Дом" name="houseName" value={draft.houseName || ''} onChange={updateDraft} placeholder="Часть названия дома" />
                    <Field label="Число комнат" name="numberOfRooms" type="number" min={1} max={11} step={1}
                        value={draft.numberOfRooms || ''} onChange={updateDraft} />
                    <Field label="Пешком до метро" name="timeToMetroOnFoot" type="number" min={0} step="any"
                        value={draft.timeToMetroOnFoot || ''} onChange={updateDraft} />
                </>}
                <div className="button-row"><button>Найти</button><button type="button"
                    onClick={() => { setState(previous => ({ ...previous, page: 0, filters: {}, draft: {} })); refresh(); }}>Сбросить</button></div>
            </form>}
            {resource.error && <div className="table-error"><ErrorMessage error={resource.error} /></div>}
            <div className="table-scroll" aria-label={config.title} aria-busy={resource.loading}>
                <Table items={resource.data?.items || []} columns={config.columns} sort={sort} direction={direction}
                    onSort={field => setState(previous => ({ ...previous, sort: field, page: 0,
                        direction: previous.sort === field && previous.direction === 'asc' ? 'desc' : 'asc' }))}
                    onAction={onAction} actions={kind === 'flats' ? 'flat' : 'view'}
                    empty={resource.loading ? 'Загрузка…' : resource.error ? 'Не удалось загрузить записи.' :
                        Object.keys(filters).length ? 'Ничего не найдено. Измените фильтры или сбросьте их.' : 'Пока нет записей. ' + config.create + '.'} />
            </div>
            {resource.data && <Pagination page={resource.data} busy={resource.loading}
                onPage={value => setState(previous => ({ ...previous, page: value }))}
                onSize={value => setState(previous => ({ ...previous, size: value, page: 0 }))} />}
        </div>
    </section>;
}
