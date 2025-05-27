package com.wwwyssa.lab6.common.util;

/**
 * Интерфейс для объектов, которые могут быть проверены на валидность.
 */
public interface Validatable {
    /**
     * Проверяет, является ли объект допустимым.
     * @return true, если объект допустим, иначе false
     */
    boolean isValid();
}