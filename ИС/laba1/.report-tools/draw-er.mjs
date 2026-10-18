import fs from 'node:fs/promises';
import { createRequire } from 'node:module';
import { fileURLToPath } from 'node:url';

const require = createRequire(import.meta.url);
const sharp = require('C:/Users/grisa/.cache/codex-runtimes/codex-primary-runtime/dependencies/node/node_modules/sharp');
const root = fileURLToPath(new URL('../', import.meta.url));

const escape = value => String(value).replaceAll('&', '&amp;').replaceAll('<', '&lt;').replaceAll('>', '&gt;');
const parts = [];
function text(x, y, value, { size = 21, weight = 400, fill = '#222', mono = false, anchor = 'start' } = {}) {
    parts.push(`<text x="${x}" y="${y}" font-size="${size}" font-weight="${weight}" fill="${fill}" text-anchor="${anchor}" font-family="${mono ? 'Consolas, monospace' : 'Arial, sans-serif'}">${escape(value)}</text>`);
}
function line(x1, y1, x2, y2, width = 2) {
    parts.push(`<line x1="${x1}" y1="${y1}" x2="${x2}" y2="${y2}" stroke="#333" stroke-width="${width}"/>`);
}
function circle(x, y) {
    parts.push(`<circle cx="${x}" cy="${y}" r="6" fill="white" stroke="#333" stroke-width="2"/>`);
}
function table(x, y, width, name, rows) {
    const header = 52;
    const columns = 34;
    const rowHeight = 44;
    const height = header + columns + rows.length * rowHeight;
    parts.push(`<rect x="${x}" y="${y}" width="${width}" height="${height}" fill="white" stroke="#333" stroke-width="2"/>`);
    parts.push(`<rect x="${x + 1}" y="${y + 1}" width="${width - 2}" height="${header - 1}" fill="#e9e9e9"/>`);
    text(x + 18, y + 34, name, { size: 24, weight: 700 });
    text(x + 16, y + header + 24, 'Ключ', { size: 16, fill: '#555' });
    text(x + 78, y + header + 24, 'Поле', { size: 16, fill: '#555' });
    text(x + width - 40, y + header + 24, 'NULL', { size: 16, fill: '#555', anchor: 'middle' });
    line(x, y + header, x + width, y + header, 1);
    for (const [index, [key, field, nullable]] of rows.entries()) {
        const top = y + header + columns + index * rowHeight;
        if (key === 'FK') {
            parts.push(`<rect x="${x + 1}" y="${top}" width="${width - 2}" height="${rowHeight}" fill="#f4f4f4"/>`);
        }
        line(x, top, x + width, top, 1);
        text(x + 16, top + 29, key, { size: 18, weight: key ? 700 : 400 });
        text(x + 78, top + 29, field, { size: 20, mono: true });
        text(x + width - 40, top + 29, nullable ? 'да' : 'нет', { size: 18, anchor: 'middle' });
    }
}

parts.push('<svg xmlns="http://www.w3.org/2000/svg" width="1140" height="1130" viewBox="0 0 1140 1130" role="img" aria-labelledby="title desc">');
parts.push('<title id="title">Логическая модель базы данных и ER-диаграмма</title>');
parts.push('<desc id="desc">Таблицы flat, coordinates, house и app_user. Квартира имеет ровно одни координаты и ноль или один дом. Одни координаты и один дом могут относиться к нулю или нескольким квартирам. Пользователь не связан внешними ключами с квартирами.</desc>');
parts.push('<rect width="1140" height="1130" fill="white"/>');
text(40, 53, 'Логическая модель базы данных', { size: 32, weight: 700 });
text(40, 88, 'ER-диаграмма · нотация «воронья лапка»', { size: 21, fill: '#555' });

table(40, 130, 560, 'flat · Квартира', [
    ['PK', 'id', false],
    ['', 'name', false],
    ['FK', 'coordinates_id', false],
    ['', 'creation_date', false],
    ['', 'area', false],
    ['', 'price', false],
    ['', 'balcony', true],
    ['', 'time_to_metro_on_foot', false],
    ['', 'number_of_rooms', false],
    ['', 'furniture', true],
    ['', 'view_type', false],
    ['', 'transport_type', true],
    ['FK', 'house_id', true],
]);
table(730, 130, 370, 'coordinates · Координаты', [
    ['PK', 'id', false], ['', 'x', false], ['', 'y', false],
]);
table(730, 450, 370, 'house · Дом', [
    ['PK', 'id', false], ['', 'name', false], ['', 'year', false], ['', 'number_of_floors', false],
]);
table(40, 835, 560, 'app_user · Пользователь', [
    ['PK', 'id', false], ['UQ', 'username', false], ['', 'password_hash', false],
]);

function relationship(flatY, parentY, optional) {
    parts.push(`<path d="M600 ${flatY} H670 V${parentY} H730" fill="none" stroke="#333" stroke-width="2"/>`);
    // Zero or many flats at the flat end.
    line(600, flatY - 12, 626, flatY);
    line(600, flatY + 12, 626, flatY);
    circle(645, flatY);
    // Exactly one coordinates record; zero or one house record.
    line(717, parentY - 12, 717, parentY + 12);
    if (optional) circle(695, parentY);
    else line(703, parentY - 12, 703, parentY + 12);
    text(605, flatY - 25, '0..*', { size: 19 });
    text(699, parentY - 25, optional ? '0..1' : '1', { size: 19, anchor: 'middle' });
}
relationship(326, 238, false);
relationship(766, 558, true);

text(730, 850, 'Обозначения', { size: 23, weight: 700 });
text(730, 889, 'PK — первичный ключ', { size: 19 });
text(730, 921, 'FK — внешний ключ', { size: 19 });
text(730, 953, 'UQ — уникальное значение', { size: 19 });
text(730, 996, 'Пользователь не имеет', { size: 19, fill: '#555' });
text(730, 1026, 'внешних ключей на квартиры.', { size: 19, fill: '#555' });
text(40, 1102, 'Обе связи: ON DELETE RESTRICT. View и Transport хранятся как значения полей.', { size: 18, fill: '#555' });
parts.push('</svg>');

const svg = parts.join('\n');
await fs.writeFile(root + 'er.svg', svg, 'utf8');
await sharp(Buffer.from(svg), { density: 288 }).png().toFile(root + 'er.png');
console.log('Created er.svg and er.png');
