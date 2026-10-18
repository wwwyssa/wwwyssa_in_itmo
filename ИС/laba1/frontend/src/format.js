const numbers = new Intl.NumberFormat('ru-RU', { maximumFractionDigits: 8 });
const dates = new Intl.DateTimeFormat('ru-RU', { dateStyle: 'short', timeStyle: 'short' });

export function display(value, key = '') {
    if (value == null) return '—';
    if (typeof value === 'boolean') return value ? 'Да' : 'Нет';
    if (key === 'creationDate') {
        const date = new Date(value);
        return Number.isNaN(date.valueOf()) ? String(value) : dates.format(date);
    }
    if (['id', 'year', 'houseYear', 'coordinatesId', 'houseId'].includes(key)) return String(value);
    return typeof value === 'number' ? numbers.format(value) : String(value);
}

export const flatColumns = [
    ['id', 'ID'], ['name', 'Название'], ['area', 'Площадь'], ['price', 'Цена'],
    ['numberOfRooms', 'Комнаты'], ['timeToMetroOnFoot', 'До метро'], ['balcony', 'Балкон'],
    ['furniture', 'Мебель'], ['view', 'Вид'], ['transport', 'Транспорт'],
    ['coordinatesId', 'ID координат'], ['x', 'X'], ['y', 'Y'], ['houseId', 'ID дома'],
    ['houseName', 'Дом'], ['houseYear', 'Год дома'], ['houseNumberOfFloors', 'Этажи дома'],
    ['creationDate', 'Создана'],
];
export const referenceColumns = {
    houses: [['id', 'ID'], ['name', 'Название'], ['year', 'Год'], ['numberOfFloors', 'Этажи']],
    coordinates: [['id', 'ID'], ['x', 'X'], ['y', 'Y']],
};
export const sections = {
    flats: { title: 'Квартиры', subtitle: 'Все квартиры и их характеристики', create: 'Добавить квартиру', columns: flatColumns },
    houses: { title: 'Дома', subtitle: 'Дома, которые можно связать с квартирой', create: 'Добавить дом', columns: referenceColumns.houses },
    coordinates: { title: 'Координаты', subtitle: 'Координаты, которые можно использовать повторно', create: 'Добавить координаты', columns: referenceColumns.coordinates },
};

const messages = {
    'name must not be blank': 'Введите название квартиры.',
    'area must be positive': 'Площадь должна быть больше нуля.',
    'price must be a finite positive number': 'Цена должна быть конечным числом больше нуля.',
    'timeToMetroOnFoot must be a finite positive number': 'Время до метро должно быть конечным числом больше нуля.',
    'numberOfRooms must be between 1 and 11': 'Число комнат должно быть от 1 до 11.',
    'view is required': 'Выберите вид из окна.',
    'coordinatesId does not exist': 'Выбранные координаты больше не существуют. Выберите другие.',
    'houseId does not exist': 'Выбранный дом больше не существует. Выберите другой.',
    'flat not found': 'Квартира не найдена. Возможно, её уже удалили.',
    'house not found': 'Дом не найден.',
    'coordinates not found': 'Координаты не найдены.',
    'house year must be positive': 'Год должен быть больше нуля.',
    'numberOfFloors must be positive': 'Число этажей должно быть больше нуля.',
    'x and y must be finite; y must be at most 249': 'Введите числовые координаты. Y не должен превышать 249.',
    'id1, id2 and id3 must be positive': 'Укажите три положительных ID квартир.',
    'Three distinct flat IDs are required': 'Укажите три разных ID квартир.',
    'Invalid JSON or parameter value. Check numeric types and enum values': 'Проверьте числовые значения и выбранные варианты.',
};
export function errorText(error) {
    return error.status >= 500 ? 'Ошибка БД' : messages[error.message] || error.message;
}
