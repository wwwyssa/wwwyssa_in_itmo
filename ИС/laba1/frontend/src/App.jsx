import { useCallback, useEffect, useRef, useState } from 'react';
import { getAuth, setAuth } from './api.js';
import { sections } from './format.js';
import { useUpdates } from './hooks.js';
import AuthForm from './components/AuthForm.jsx';
import FlatEditor from './components/FlatEditor.jsx';
import ListPage from './components/ListPage.jsx';
import Operations from './components/Operations.jsx';
import { DeleteDialog, LookupDialog, ObjectView, ReferenceEditor, ReferencePicker } from './components/ReferenceDialogs.jsx';

function readAuth() {
    const auth = getAuth();
    return auth && typeof auth.token === 'string' && typeof auth.username === 'string' &&
        Number.isFinite(auth.expiresAt) && auth.expiresAt * 1000 > Date.now() ? auth : null;
}
function readSection() {
    const hash = location.hash.slice(1);
    return Object.hasOwn(sections, hash) || hash === 'operations' || hash === 'login' ? hash : 'flats';
}

function Workspace({ auth, current }) {
    const [revision, setRevision] = useState(0);
    const refresh = useCallback(() => setRevision(previous => previous + 1), []);
    const [notice, setNotice] = useState('');
    const [lists, setLists] = useState(() => Object.fromEntries(Object.keys(sections).map(kind =>
        [kind, { page: 0, size: 10, sort: 'id', direction: 'asc', filters: {}, draft: {} }])));
    const [modals, setModals] = useState([]);
    const nextModalId = useRef(0);
    useUpdates(auth.token, refresh);
    function open(modal) {
        const next = { ...modal, modalId: ++nextModalId.current };
        setModals(previous => [...previous, next]);
    }
    function close(modalId) {
        setModals(previous => previous.filter(modal => modal.modalId !== modalId));
    }
    function saved(message) { setNotice(message); refresh(); }
    function select(kind, onSelect) { open({ type: 'pick', kind, onSelect }); }
    function createReference(kind, onCreated) { open({ type: 'reference', kind, onCreated }); }
    function action(kind, type, item) { open({ type: type === 'edit' ? 'edit' : 'view', kind, id: item.id }); }
    function renderModal(modal) {
        const onClose = () => close(modal.modalId);
        const props = { onClose };
        if (modal.type === 'edit') return <FlatEditor key={modal.modalId} {...props} id={modal.id} onSaved={saved} onSelect={select} />;
        if (modal.type === 'reference') return <ReferenceEditor key={modal.modalId} {...props} kind={modal.kind} onSaved={saved} onCreated={modal.onCreated} />;
        if (modal.type === 'pick') return <ReferencePicker key={modal.modalId} {...props} kind={modal.kind} revision={revision} onSelect={modal.onSelect} onCreate={createReference} />;
        if (modal.type === 'lookup') return <LookupDialog key={modal.modalId} {...props} kind={modal.kind} onFound={id => open({ type: 'view', kind: modal.kind, id })} />;
        if (modal.type === 'delete') return <DeleteDialog key={modal.modalId} {...props} flat={modal.flat} onSaved={saved} />;
        return <ObjectView key={modal.modalId} {...props} kind={modal.kind} id={modal.id} revision={revision}
            onEdit={() => { onClose(); open({ type: 'edit', id: modal.id }); }}
            onDelete={flat => { onClose(); open({ type: 'delete', flat }); }} />;
    }
    return <>
        <main>
            {notice && <div className="notice" role="status"><span>{notice}</span>
                <button type="button" aria-label="Скрыть сообщение" onClick={() => setNotice('')}>×</button></div>}
            {current === 'operations' ? <Operations revision={revision} onSelect={select} onAction={(type, item) => action('flats', type, item)} /> :
                <ListPage key={current} kind={current} state={lists[current]} revision={revision} refresh={refresh}
                    setState={update => setLists(previous => ({ ...previous, [current]: update(previous[current]) }))}
                    onCreate={() => current === 'flats' ? open({ type: 'edit' }) : createReference(current)}
                    onLookup={() => open({ type: 'lookup', kind: current })}
                    onAction={(type, item) => action(current, type, item)} />}
        </main>
        {modals.map(renderModal)}
    </>;
}

export default function App() {
    const [auth, updateAuth] = useState(readAuth);
    const [current, setCurrent] = useState(readSection);
    const section = current === 'login' ? 'flats' : current;
    useEffect(() => {
        const changed = () => updateAuth(readAuth());
        const routed = () => setCurrent(readSection());
        document.addEventListener('authchange', changed);
        window.addEventListener('hashchange', routed);
        return () => {
            document.removeEventListener('authchange', changed);
            window.removeEventListener('hashchange', routed);
        };
    }, []);
    useEffect(() => {
        if (!auth && location.hash !== '#login') {
            history.replaceState(null, '', '#login');
            setCurrent('login');
        } else if (auth && current === 'login') {
            history.replaceState(null, '', '#flats');
            setCurrent('flats');
        }
    }, [auth, current]);
    useEffect(() => {
        document.title = (auth ? sections[section]?.title || 'Операции' : 'Вход') + ' · Квартиры';
        if (!auth) return;
        const timer = setTimeout(() => setAuth(null), Math.min(auth.expiresAt * 1000 - Date.now(), 2147483647));
        return () => clearTimeout(timer);
    }, [auth, section]);
    return <>
        <header className="site-header">
            <a className="brand" href={auth ? '#flats' : '#login'}><span className="brand-mark" aria-hidden="true">К</span> Квартиры</a>
            {auth && <><nav aria-label="Основное меню">
                {[...Object.entries(sections).map(([key, value]) => [key, value.title]), ['operations', 'Операции']].map(([key, label]) =>
                    <a key={key} href={'#' + key} aria-current={section === key ? 'page' : undefined}>{label}</a>)}
            </nav><div className="user-menu"><span>{auth.username}</span><button type="button" onClick={() => setAuth(null)}>Выйти</button></div></>}
        </header>
        {auth ? <Workspace key={auth.token} auth={auth} current={section} /> : <AuthForm />}
    </>;
}
