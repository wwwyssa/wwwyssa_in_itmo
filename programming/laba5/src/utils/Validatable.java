package utils;

/**
 * Интерфейс для проверки допустимости объектов.
 */
public interface Validatable {
    /**
     * Проверяет, является ли объект допустимым.
     * @return true, если объект допустим, иначе false
     */
    boolean isValid();
}