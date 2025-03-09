package commands;

import utils.Executable;
import utils.ExecutionResponse;

import java.security.spec.ECField;
import java.util.Objects;

/**
 * Абстрактный класс, представляющий команду.
 */
public abstract class Command implements Executable {
    private final String name;
    private final String description;
    private final int expectedNumberOfArguments;
    /**
     * Конструктор для создания объекта Command.
     * @param name имя команды
     * @param description описание команды
     */
    public Command(String name, String description, int expectedNumberOfArguments) {
        this.name = name;
        this.description = description;
        this.expectedNumberOfArguments = expectedNumberOfArguments;
    }

    /**
     * Возвращает имя команды.
     * @return имя команды
     */
    public String getName() {
        return name;
    }

    /**
     * Возвращает описание команды.
     * @return описание команды
     */
    public String getDescription() {
        return description;
    }

    /**
     * Проверяет, равен ли данный объект другому объекту.
     * @param o объект для сравнения
     * @return true, если объекты равны, иначе false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Command command = (Command) o;
        return Objects.equals(name, command.name) && Objects.equals(description, command.description);
    }

    /**
     * Возвращает хэш-код объекта Command.
     * @return хэш-код объекта
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, description);
    }

    /**
     * Возвращает строковое представление объекта Command.
     * @return строковое представление объекта
     */
    @Override
    public String toString() {
        return "Command{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }


    public ExecutionResponse validate(String[] args) {
       if (expectedNumberOfArguments == 0 && !args[1].isEmpty()) return new ExecutionResponse(false, "Incorrect number of arguments!\nCorrect: '" + getName() + "'");
       if (expectedNumberOfArguments == 1 && args[1].isEmpty()) return new ExecutionResponse(false, "Incorrect number of arguments!\nCorrect: '" + getName() + "'");
       return new ExecutionResponse(true, "");
    }

}