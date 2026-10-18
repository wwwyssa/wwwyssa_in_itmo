import { useEffect, useId, useRef } from 'react';
import { display, errorText, flatColumns } from '../format.js';

export function ErrorMessage({ error }) {
    return error ? <p className="form-error" role="alert">{typeof error === 'string' ? error : errorText(error)}</p> : null;
}

export function Field({ label, name, error, hint, full = false, children, ...props }) {
    const id = useId();
    const inputProps = { id, name, 'aria-invalid': error ? true : undefined, 'aria-describedby': error || hint ? id + '-help' : undefined, ...props };
    return <div className={'field' + (full ? ' full' : '')}>
        <label htmlFor={id}>{label}</label>
        {children ? <select {...inputProps}>{children}</select> : <input {...inputProps} />}
        {(error || hint) && <span id={id + '-help'} className={error ? 'field-error' : 'hint'}>{error || hint}</span>}
    </div>;
}

export function Modal({ title, children, footer, onClose, busy = false, small = false }) {
    const ref = useRef(null);
    const titleId = useId();
    useEffect(() => {
        const dialog = ref.current;
        dialog.showModal();
        return () => dialog.close();
    }, []);
    return <dialog ref={ref} className={small ? 'dialog-small' : 'dialog-wide'} aria-labelledby={titleId} aria-busy={busy}
        onCancel={event => { event.preventDefault(); if (!busy) onClose(); }}>
        <header className="dialog-header">
            <h2 id={titleId}>{title}</h2>
            <button type="button" className="icon-button" aria-label="Закрыть" disabled={busy} onClick={onClose}>×</button>
        </header>
        {children}
        {footer && <footer className="dialog-footer">{footer}</footer>}
    </dialog>;
}

export function Table({ items, columns, sort, direction, onSort, onAction, actions = 'view', empty = 'Пока нет записей.' }) {
    return <table><thead><tr>
        {columns.map(([key, label]) => <th key={key} aria-sort={sort === key ? direction === 'asc' ? 'ascending' : 'descending' : 'none'}>
            {onSort ? <button type="button" className="sort-button" onClick={() => onSort(key)}>
                {label}<span className="sort-symbol" aria-hidden="true">{sort === key ? direction === 'asc' ? '↑' : '↓' : ''}</span>
            </button> : label}
        </th>)}
        <th className="actions-cell">Действия</th>
    </tr></thead><tbody>
        {items.length === 0 ? <tr><td className="empty-state" colSpan={columns.length + 1}>{empty}</td></tr> :
            items.map(item => <tr key={item.id}>
                {columns.map(([key]) => <td key={key} className={key === 'name' || key === 'houseName' ? 'name-cell' : undefined}
                    title={display(item[key], key)}>{display(item[key], key)}</td>)}
                <td className="actions-cell"><div className="button-row">
                    <button type="button" className="table-action" onClick={() => onAction(actions === 'pick' ? 'pick' : 'view', item)}>
                        {actions === 'pick' ? 'Выбрать' : 'Открыть'}
                    </button>
                    {actions === 'flat' && <button type="button" className="table-action" onClick={() => onAction('edit', item)}>Изменить</button>}
                </div></td>
            </tr>)}
    </tbody></table>;
}

export function Pagination({ page, busy, onPage, onSize }) {
    const start = page.totalElements ? page.page * page.size + 1 : 0;
    const end = Math.min((page.page + 1) * page.size, page.totalElements);
    return <div className="pagination">
        <span className="pagination-info">{display(start)}–{display(end)} из {display(page.totalElements)}</span>
        <div className="page-controls">
            {onSize && <label className="page-size">На странице <select aria-label="Записей на странице" value={page.size} disabled={busy}
                onChange={event => onSize(Number(event.target.value))}>{[10, 20, 50].map(size => <option key={size}>{size}</option>)}</select></label>}
            <button type="button" disabled={busy || page.page === 0} onClick={() => onPage(page.page - 1)}>Назад</button>
            <span>{page.totalPages ? page.page + 1 : 0} / {page.totalPages}</span>
            <button type="button" disabled={busy || page.page + 1 >= page.totalPages} onClick={() => onPage(page.page + 1)}>Далее</button>
        </div>
    </div>;
}

export function Details({ item, columns }) {
    return <dl className="details">{columns.map(([key, label]) => <div key={key}><dt>{label}</dt><dd>{display(item[key], key)}</dd></div>)}</dl>;
}

export function FlatDetails({ flat }) {
    const related = ['coordinatesId', 'x', 'y', 'houseId', 'houseName', 'houseYear', 'houseNumberOfFloors'];
    return <>
        <section className="detail-group"><h3>Квартира</h3><Details item={flat} columns={flatColumns.filter(([key]) => !related.includes(key))} /></section>
        <section className="detail-group"><h3>Координаты</h3><Details item={flat} columns={[[ 'coordinatesId', 'ID' ], [ 'x', 'X' ], [ 'y', 'Y' ]]} /></section>
        <section className="detail-group"><h3>Дом</h3>{flat.houseId == null ? <p className="hint">Дом не указан.</p> :
            <Details item={flat} columns={[[ 'houseId', 'ID' ], [ 'houseName', 'Название' ], [ 'houseYear', 'Год' ], [ 'houseNumberOfFloors', 'Этажи' ]]} />}</section>
    </>;
}
