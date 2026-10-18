export function numberValue(raw, name, errors, { integer = false, positive = false, max = Infinity } = {}) {
    const value = Number(raw);
    if (String(raw).trim() === '' || !Number.isFinite(value)) {
        errors[name] = 'Введите число.';
    } else if (integer && !Number.isSafeInteger(value)) {
        errors[name] = 'Введите целое число допустимого размера.';
    } else if (positive && value <= 0) {
        errors[name] = 'Значение должно быть больше нуля.';
    } else if (value > max) {
        errors[name] = 'Значение не должно превышать ' + max + '.';
    }
    return value;
}

export function focusError(form, errors) {
    const input = form.elements.namedItem(Object.keys(errors)[0]);
    input?.focus();
}
