package com.wwwyssa.lab6.client.managers;

import com.wwwyssa.lab6.client.util.Console;
import com.wwwyssa.lab6.common.models.*;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

/**
 * Класс для чтения объектов.
 */
public class Asker {


    public static class IllegalInputException extends IllegalArgumentException {
        /**
         * Конструктор исключения с сообщением.
         * @param message Сообщение об ошибке.
         */
        public IllegalInputException(String message) {
            super(message);
        }
    }
    /**
     * Исключение, используемое для прерывания чтения объекта.
     */
    public static class ObjectReaderBreak extends Exception {}

    /**
     * Читает объект Product из консоли.
     * @param defaultConsole объект Console для ввода/вывода
     * @param id идентификатор продукта
     * @return объект Product
     * @throws ObjectReaderBreak если ввод прерван
     */
    public static Product readProduct(Console defaultConsole, long id) throws ObjectReaderBreak {
        try {
            defaultConsole.println("Ведите Product name:");
            String name;
            while (true) {
                name = defaultConsole.input();
                if (name.equals("exit")) throw new ObjectReaderBreak();
                if (name.isEmpty()) {
                    defaultConsole.println("Name can't be empty. Enter location name:");
                } else {
                    break;
                }
            }

            Coordinates coordinates = readCoordinates(defaultConsole);
            int price = readPrice(defaultConsole);
            String partNumber = readPartNumber(defaultConsole);
            int manufactureCost = readManufactureCost(defaultConsole);
            UnitOfMeasure unitOfMeasure = readUnitOfMeasure(defaultConsole);
            Organization manufacturer = readOrganization(defaultConsole);
            LocalDateTime date = LocalDateTime.now();
            return new Product(id, name, coordinates, date, price, partNumber, manufactureCost, unitOfMeasure, manufacturer);
        } catch (IllegalStateException | NoSuchElementException e) {
            defaultConsole.println("Ошибка ввода. Попробуйте еще раз");
            return null;
        }
    }

    /**
     * Читает объект Coordinates из консоли.
     * @param defaultConsole объект Console для ввода/вывода
     * @return объект Coordinates
     * @throws ObjectReaderBreak если ввод прерван
     */
    public static Coordinates readCoordinates(Console defaultConsole) throws ObjectReaderBreak {
        try {
            defaultConsole.println("Введите координаты");
            defaultConsole.println("Введите x:");
            Integer x;
            while (true) {
                String inp = defaultConsole.input().trim();
                if (inp.equals("exit")) throw new ObjectReaderBreak();
                if (inp.isEmpty()) {
                    defaultConsole.println("X не может быть пустым. Введите x:");
                } else {
                    try {
                        x = Integer.parseInt(inp);
                        if (x > 765) {
                            defaultConsole.println("X не может быть больше 765. Введите x:");
                        } else {
                            break;
                        }
                    } catch (NumberFormatException e) {
                        defaultConsole.println("Координаты должны быть целочисленные. Введите x:");
                    }
                }
            }
            defaultConsole.println("Enter y:");
            Long y;
            while (true) {
                String inp = defaultConsole.input().trim();
                if (inp.equals("exit")) throw new ObjectReaderBreak();
                if (inp.isEmpty()) {
                    defaultConsole.println("Y не может быть пустым. Введите y:");
                } else {
                    try {
                        y = Long.parseLong(inp);
                        if (y < -365) {
                            defaultConsole.println("Y  не может быть меньше -365. Введите y:");
                        } else {
                            break;
                        }
                    } catch (NumberFormatException e) {
                        defaultConsole.println("Координаты должны быть целочисленные. Введите y:");
                    }
                }
            }
            return new Coordinates(x, y);
        } catch (IllegalStateException | NoSuchElementException e) {
            defaultConsole.println("Ошибка ввода. Попробуйте еще раз");
            return null;
        }
    }

    /**
     * Читает цену из консоли.
     * @param defaultConsole объект Console для ввода/вывода
     * @return цена
     * @throws ObjectReaderBreak если ввод прерван
     */
    public static int readPrice(Console defaultConsole) throws ObjectReaderBreak {
        try {
            defaultConsole.println("Введите цену:");
            int price;
            while (true) {
                String inp = defaultConsole.input().trim();
                if (inp.equals("exit")) throw new ObjectReaderBreak();
                if (inp.isEmpty()) {
                    defaultConsole.println("Цена не может быть пустой. Введите цену:");
                } else {
                    try {
                        price = Integer.parseInt(inp);
                        if (price <= 0) {
                            defaultConsole.println("Цена должна быть больше 0. Введите цену:");
                        } else {
                            break;
                        }
                    } catch (NumberFormatException e) {
                        defaultConsole.println("Цена болжна быть целочисленной. Введите цену:");
                    }
                }
            }
            return price;
        } catch (IllegalStateException | NoSuchElementException e) {
            defaultConsole.println("Ошибка ввода. Попробуйте еще раз");
            return Integer.MIN_VALUE;
        }
    }

    /**
     * Читает номер детали из консоли.
     * @param defaultConsole объект Console для ввода/вывода
     * @return номер детали
     * @throws ObjectReaderBreak если ввод прерван
     */
    public static String readPartNumber(Console defaultConsole) throws ObjectReaderBreak {
        try {
            defaultConsole.println("Введите номер партии:");
            String partNumber;
            while (true) {
                partNumber = defaultConsole.input().trim();
                if (partNumber.equals("exit")) throw new ObjectReaderBreak();
                if (partNumber.isEmpty()) {
                    defaultConsole.println("номер партии не может быть пустым. Введите номер партии:");
                } else {
                    break;
                }
            }
            return partNumber;
        } catch (IllegalStateException | NoSuchElementException e) {
            defaultConsole.println("Ошибка ввода. Попробуйте еще раз");
            return null;
        }
    }

    /**
     * Читает стоимость производства из консоли.
     * @param defaultConsole объект Console для ввода/вывода
     * @return стоимость производства
     * @throws ObjectReaderBreak если ввод прерван
     */
    public static int readManufactureCost(Console defaultConsole) throws ObjectReaderBreak {
        try {
            defaultConsole.println("Ведите стоимость производства:");
            int manufactureCost;
            while (true) {
                String inp = defaultConsole.input().trim();
                if (inp.equals("exit")) throw new ObjectReaderBreak();
                if (inp.isEmpty()) {
                    defaultConsole.println("стоимость производства не может быть пустой. Введите стоимость производства:");
                } else {
                    try {
                        manufactureCost = Integer.parseInt(inp);
                        break;
                    } catch (NumberFormatException e) {
                        defaultConsole.println("Manufacture cost must be int type. Введите стоимость производства:");
                    }
                }
            }
            return manufactureCost;
        } catch (IllegalStateException | NoSuchElementException e) {
            defaultConsole.println("Ошибка ввода. Попробуйте еще раз");
            return Integer.MIN_VALUE;
        }
    }

    /**
     * Читает единицу измерения из консоли.
     * @param defaultConsole объект Console для ввода/вывода
     * @return единица измерения
     * @throws ObjectReaderBreak если ввод прерван
     */
    public static UnitOfMeasure readUnitOfMeasure(Console defaultConsole) throws ObjectReaderBreak {
        try {
            defaultConsole.println("Введите единицу измерения:");
            defaultConsole.println("Выберете одну из предложенных:");
            for (UnitOfMeasure unitOfMeasure : UnitOfMeasure.values()) {
                defaultConsole.println(unitOfMeasure.toString());
            }
            UnitOfMeasure unitOfMeasure;
            while (true) {
                String inp = defaultConsole.input().trim();
                if (inp.equals("exit")) throw new ObjectReaderBreak();
                if (inp.isEmpty()) {
                    defaultConsole.println("Единица измерения  не иожет быть пустой. Введите единицу измерения:");
                } else {
                    try {
                        unitOfMeasure = UnitOfMeasure.valueOf(inp.toUpperCase());
                        break;
                    } catch (IllegalArgumentException e) {
                        defaultConsole.println("Единица измерения должна быть выбрана из предложенных:");
                        for (UnitOfMeasure unit : UnitOfMeasure.values()) {
                            defaultConsole.println(unit.toString());
                        }
                    }
                }
            }
            return unitOfMeasure;
        } catch (IllegalStateException | NoSuchElementException e) {
            defaultConsole.println("Ошибка ввода. Попробуйте еще раз");
            return null;
        }
    }

    /**
     * Читает объект Organization из консоли.
     * @param defaultConsole объект Console для ввода/вывода
     * @return объект Organization
     * @throws ObjectReaderBreak если ввод прерван
     */
    public static Organization readOrganization(Console defaultConsole) throws ObjectReaderBreak {
        try {
            defaultConsole.println("Ввдеите организацию");
            defaultConsole.println("Введите organization name:");
            String name;
            while (true) {
                name = defaultConsole.input();
                if (name.equals("exit")) throw new ObjectReaderBreak();
                if (name.isEmpty()) {
                    defaultConsole.println("Имя организации не может быть пустым. Введите имя организации:");
                } else {
                    break;
                }
            }


            defaultConsole.println("Введите количество сотрудников:");
            Integer employeesCount;
            while (true) {
                String inp = defaultConsole.input().trim();
                if (inp.equals("exit")) throw new ObjectReaderBreak();
                if (inp.isEmpty()) {
                    defaultConsole.println("Количество сотрудников не может быть пустым. Введите количество сотрудников:");
                } else {
                    try {
                        employeesCount = Integer.parseInt(inp);
                        if (employeesCount <= 0) {
                            defaultConsole.println("Количество сотрудников должно быть больше 0. Введите количество сотрудников:");
                        } else {
                            break;
                        }
                    } catch (NumberFormatException e) {
                        defaultConsole.println("Количество сотрудников должно быть типа int. Запишите количество сотрудников:");
                    }
                }
            }
            defaultConsole.println("Введите тип организации:");
            defaultConsole.println("Выберите один из следующих вариантов:");
            for (OrganizationType organizationType : OrganizationType.values()) {
                defaultConsole.println(organizationType.toString());
            }

            OrganizationType organizationType;
            while (true) {
                String inp = defaultConsole.input().trim();
                if (inp.equals("exit")) throw new ObjectReaderBreak();
                if (inp.isEmpty()) {
                    defaultConsole.println("Поле \"Тип организации\" не может быть пустым. Введите тип организации:");
                } else {
                    try {
                        organizationType = OrganizationType.valueOf(inp.toUpperCase());
                        break;
                    } catch (IllegalArgumentException e) {
                        defaultConsole.println("Тип организации должен быть одним из следующих:");
                        for (OrganizationType type : OrganizationType.values()) {
                            defaultConsole.println(type.toString());
                        }
                    }
                }
            }

            Address address = readAddress(defaultConsole);
            return new Organization(name, employeesCount, organizationType, address);
        } catch (IllegalStateException | NoSuchElementException e) {
            defaultConsole.println("Ошибка ввода. Попробуйте еще раз");
            return null;
        }
    }

    /**
     * Читает объект Address из консоли.
     * @param defaultConsole объект Console для ввода/вывода
     * @return объект Address
     * @throws ObjectReaderBreak если ввод прерван
     */
    public static Address readAddress(Console defaultConsole) throws ObjectReaderBreak {
        try {
            defaultConsole.println("Введите адрес:");
            defaultConsole.println("Введите улицу:");
            String street;
            while (true) {
                street = defaultConsole.input();
                if (street.equals("exit")) throw new ObjectReaderBreak();
                if (street.isEmpty()) {
                    defaultConsole.println("Улица не может быть пустой. Введите на улицу:");
                } else {
                    break;
                }
            }
            Location town = readLocation(defaultConsole);
            return new Address(street, town);
        } catch (IllegalStateException | NoSuchElementException e) {
            defaultConsole.println("Ошибка ввода. Попробуйте еще раз");
            return null;
        }
    }

    /**
     * Читает объект Location из консоли.
     * @param defaultConsole объект Console для ввода/вывода
     * @return объект Location
     * @throws ObjectReaderBreak если ввод прерван
     */
    public static Location readLocation(Console defaultConsole) throws ObjectReaderBreak {
        try {
            defaultConsole.println("Введите x:");
            Float x;
            while (true) {
                String inp = defaultConsole.input().trim();
                if (inp.equals("exit")) throw new ObjectReaderBreak();
                if (inp.isEmpty()) {
                    defaultConsole.println("X не может быть пустым. Введите x:");
                } else {
                    try {
                        x = Float.parseFloat(inp);
                        break;
                    } catch (NumberFormatException e) {
                        defaultConsole.println("Координаты должны быть float type. Введите x:");
                    }
                }
            }

            defaultConsole.println("Введите y:");
            int y;
            while (true) {
                String inp = defaultConsole.input().trim();
                if (inp.equals("exit")) throw new ObjectReaderBreak();
                if (inp.isEmpty()) {
                    defaultConsole.println("Y не может быть пустым. Введите y:");
                } else {
                    try {
                        y = Integer.parseInt(inp);
                        break;
                    } catch (NumberFormatException e) {
                        defaultConsole.println("Координаты должны быть int type. Введите y:");
                    }
                }
            }

            defaultConsole.println("Введите z:");
            Integer z;
            while (true) {
                String inp = defaultConsole.input().trim();
                if (inp.equals("exit")) throw new ObjectReaderBreak();
                if (inp.isEmpty()) {
                    defaultConsole.println("Z не может быть пустым. Введите z:");
                } else {
                    try {
                        z = Integer.parseInt(inp);
                        break;
                    } catch (NumberFormatException e) {
                        defaultConsole.println("Координаты должны быть int type. Введите z:");
                    }
                }
            }
            return new Location(x, y, z);
        } catch (IllegalStateException | NoSuchElementException e) {
            defaultConsole.println("Ошибка ввода. Попробуйте еще раз");
            return null;
        }
    }
}