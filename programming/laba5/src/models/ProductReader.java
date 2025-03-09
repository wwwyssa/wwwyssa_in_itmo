package models;

import utils.Console;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

/**
 * Класс для чтения объектов.
 */
public class ProductReader {

    /**
     * Исключение, используемое для прерывания чтения объекта.
     */
    public static class ObjectReaderBreak extends Exception {}

    /**
     * Читает объект Product из консоли.
     * @param console объект Console для ввода/вывода
     * @param id идентификатор продукта
     * @return объект Product
     * @throws ObjectReaderBreak если ввод прерван
     */
    public static Product readProduct(Console console, long id) throws ObjectReaderBreak {
        try {
            console.println("Ведите Product name:");
            String name;
            while (true) {
                name = console.input();
                if (name.equals("exit")) throw new ObjectReaderBreak();
                if (name.isEmpty()) {
                    console.println("Name can't be empty. Enter location name:");
                } else {
                    break;
                }
            }

            Coordinates coordinates = readCoordinates(console);
            int price = readPrice(console);
            String partNumber = readPartNumber(console);
            int manufactureCost = readManufactureCost(console);
            UnitOfMeasure unitOfMeasure = readUnitOfMeasure(console);
            Organization manufacturer = readOrganization(console);
            LocalDateTime date = LocalDateTime.now();
            return new Product(id, name, coordinates, date, price, partNumber, manufactureCost, unitOfMeasure, manufacturer);
        } catch (IllegalStateException | NoSuchElementException e) {
            console.println("Ошибка ввода. Попробуйте еще раз");
            return null;
        }
    }

    /**
     * Читает объект Coordinates из консоли.
     * @param console объект Console для ввода/вывода
     * @return объект Coordinates
     * @throws ObjectReaderBreak если ввод прерван
     */
    public static Coordinates readCoordinates(Console console) throws ObjectReaderBreak {
        try {
            console.println("Введите координаты");
            console.println("Введите x:");
            Integer x;
            while (true) {
                String inp = console.input().trim();
                if (inp.equals("exit")) throw new ObjectReaderBreak();
                if (inp.isEmpty()) {
                    console.println("X не может быть пустым. Введите x:");
                } else {
                    try {
                        x = Integer.parseInt(inp);
                        if (x > 765) {
                            console.println("X не может быть больше 765. Введите x:");
                        } else {
                            break;
                        }
                    } catch (NumberFormatException e) {
                        console.println("Координаты должны быть целочисленные. Введите x:");
                    }
                }
            }
            console.println("Enter y:");
            Long y;
            while (true) {
                String inp = console.input().trim();
                if (inp.equals("exit")) throw new ObjectReaderBreak();
                if (inp.isEmpty()) {
                    console.println("Y не может быть пустым. Введите y:");
                } else {
                    try {
                        y = Long.parseLong(inp);
                        if (y < -365) {
                            console.println("Y  не может быть меньше -365. Введите y:");
                        } else {
                            break;
                        }
                    } catch (NumberFormatException e) {
                        console.println("Координаты должны быть целочисленные. Введите y:");
                    }
                }
            }
            return new Coordinates(x, y);
        } catch (IllegalStateException | NoSuchElementException e) {
            console.println("Ошибка ввода. Попробуйте еще раз");
            return null;
        }
    }

    /**
     * Читает цену из консоли.
     * @param console объект Console для ввода/вывода
     * @return цена
     * @throws ObjectReaderBreak если ввод прерван
     */
    public static int readPrice(Console console) throws ObjectReaderBreak {
        try {
            console.println("Введите цену:");
            int price;
            while (true) {
                String inp = console.input().trim();
                if (inp.equals("exit")) throw new ObjectReaderBreak();
                if (inp.isEmpty()) {
                    console.println("Цена не может быть пустой. Введите цену:");
                } else {
                    try {
                        price = Integer.parseInt(inp);
                        if (price <= 0) {
                            console.println("Цена должна быть больше 0. Введите цену:");
                        } else {
                            break;
                        }
                    } catch (NumberFormatException e) {
                        console.println("Цена болжна быть целочисленной. Введите цену:");
                    }
                }
            }
            return price;
        } catch (IllegalStateException | NoSuchElementException e) {
            console.println("Ошибка ввода. Попробуйте еще раз");
            return Integer.MIN_VALUE;
        }
    }

    /**
     * Читает номер детали из консоли.
     * @param console объект Console для ввода/вывода
     * @return номер детали
     * @throws ObjectReaderBreak если ввод прерван
     */
    public static String readPartNumber(Console console) throws ObjectReaderBreak {
        try {
            console.println("Введите номер партии:");
            String partNumber;
            while (true) {
                partNumber = console.input().trim();
                if (partNumber.equals("exit")) throw new ObjectReaderBreak();
                if (partNumber.isEmpty()) {
                    console.println("номер партии не может быть пустым. Введите номер партии:");
                } else {
                    break;
                }
            }
            return partNumber;
        } catch (IllegalStateException | NoSuchElementException e) {
            console.println("Ошибка ввода. Попробуйте еще раз");
            return null;
        }
    }

    /**
     * Читает стоимость производства из консоли.
     * @param console объект Console для ввода/вывода
     * @return стоимость производства
     * @throws ObjectReaderBreak если ввод прерван
     */
    public static int readManufactureCost(Console console) throws ObjectReaderBreak {
        try {
            console.println("Ведите стоимость производства:");
            int manufactureCost;
            while (true) {
                String inp = console.input().trim();
                if (inp.equals("exit")) throw new ObjectReaderBreak();
                if (inp.isEmpty()) {
                    console.println("стоимость производства не может быть пустой. Введите стоимость производства:");
                } else {
                    try {
                        manufactureCost = Integer.parseInt(inp);
                        break;
                    } catch (NumberFormatException e) {
                        console.println("Manufacture cost must be int type. Введите стоимость производства:");
                    }
                }
            }
            return manufactureCost;
        } catch (IllegalStateException | NoSuchElementException e) {
            console.println("Ошибка ввода. Попробуйте еще раз");
            return Integer.MIN_VALUE;
        }
    }

    /**
     * Читает единицу измерения из консоли.
     * @param console объект Console для ввода/вывода
     * @return единица измерения
     * @throws ObjectReaderBreak если ввод прерван
     */
    public static UnitOfMeasure readUnitOfMeasure(Console console) throws ObjectReaderBreak {
        try {
            console.println("Введите единицу измерения:");
            console.println("Выберете одну из предложенных:");
            for (UnitOfMeasure unitOfMeasure : UnitOfMeasure.values()) {
                console.println(unitOfMeasure.toString());
            }
            UnitOfMeasure unitOfMeasure;
            while (true) {
                String inp = console.input().trim();
                if (inp.equals("exit")) throw new ObjectReaderBreak();
                if (inp.isEmpty()) {
                    console.println("Единица измерения  не иожет быть пустой. Введите единицу измерения:");
                } else {
                    try {
                        unitOfMeasure = UnitOfMeasure.valueOf(inp.toUpperCase());
                        break;
                    } catch (IllegalArgumentException e) {
                        console.println("Единица измерения должна быть выбрана из предложенных:");
                        for (UnitOfMeasure unit : UnitOfMeasure.values()) {
                            console.println(unit.toString());
                        }
                    }
                }
            }
            return unitOfMeasure;
        } catch (IllegalStateException | NoSuchElementException e) {
            console.println("Ошибка ввода. Попробуйте еще раз");
            return null;
        }
    }

    /**
     * Читает объект Organization из консоли.
     * @param console объект Console для ввода/вывода
     * @return объект Organization
     * @throws ObjectReaderBreak если ввод прерван
     */
    public static Organization readOrganization(Console console) throws ObjectReaderBreak {
        try {
            console.println("Ввдеите организацию");
            console.println("Введите organization name:");
            String name;
            while (true) {
                name = console.input();
                if (name.equals("exit")) throw new ObjectReaderBreak();
                if (name.isEmpty()) {
                    console.println("Имя организации не может быть пустым. Введите имя организации:");
                } else {
                    break;
                }
            }


            console.println("Введите количество сотрудников:");
            Integer employeesCount;
            while (true) {
                String inp = console.input().trim();
                if (inp.equals("exit")) throw new ObjectReaderBreak();
                if (inp.isEmpty()) {
                    console.println("Количество сотрудников не может быть пустым. Введите количество сотрудников:");
                } else {
                    try {
                        employeesCount = Integer.parseInt(inp);
                        if (employeesCount <= 0) {
                            console.println("Количество сотрудников должно быть больше 0. Введите количество сотрудников:");
                        } else {
                            break;
                        }
                    } catch (NumberFormatException e) {
                        console.println("Количество сотрудников должно быть типа int. Запишите количество сотрудников:");
                    }
                }
            }
            console.println("Введите тип организации:");
            console.println("Выберите один из следующих вариантов:");
            for (OrganizationType organizationType : OrganizationType.values()) {
                console.println(organizationType.toString());
            }

            OrganizationType organizationType;
            while (true) {
                String inp = console.input().trim();
                if (inp.equals("exit")) throw new ObjectReaderBreak();
                if (inp.isEmpty()) {
                    console.println("Поле \"Тип организации\" не может быть пустым. Введите тип организации:");
                } else {
                    try {
                        organizationType = OrganizationType.valueOf(inp.toUpperCase());
                        break;
                    } catch (IllegalArgumentException e) {
                        console.println("Тип организации должен быть одним из следующих:");
                        for (OrganizationType type : OrganizationType.values()) {
                            console.println(type.toString());
                        }
                    }
                }
            }

            Address address = readAddress(console);
            return new Organization(name, employeesCount, organizationType, address);
        } catch (IllegalStateException | NoSuchElementException e) {
            console.println("Ошибка ввода. Попробуйте еще раз");
            return null;
        }
    }

    /**
     * Читает объект Address из консоли.
     * @param console объект Console для ввода/вывода
     * @return объект Address
     * @throws ObjectReaderBreak если ввод прерван
     */
    public static Address readAddress(Console console) throws ObjectReaderBreak {
        try {
            console.println("Введите адрес:");
            console.println("Введите улицу:");
            String street;
            while (true) {
                street = console.input();
                if (street.equals("exit")) throw new ObjectReaderBreak();
                if (street.isEmpty()) {
                    console.println("Улица не может быть пустой. Введите на улицу:");
                } else {
                    break;
                }
            }
            Location town = readLocation(console);
            return new Address(street, town);
        } catch (IllegalStateException | NoSuchElementException e) {
            console.println("Ошибка ввода. Попробуйте еще раз");
            return null;
        }
    }

    /**
     * Читает объект Location из консоли.
     * @param console объект Console для ввода/вывода
     * @return объект Location
     * @throws ObjectReaderBreak если ввод прерван
     */
    public static Location readLocation(Console console) throws ObjectReaderBreak {
        try {
            console.println("Введите x:");
            Float x;
            while (true) {
                String inp = console.input().trim();
                if (inp.equals("exit")) throw new ObjectReaderBreak();
                if (inp.isEmpty()) {
                    console.println("X не может быть пустым. Введите x:");
                } else {
                    try {
                        x = Float.parseFloat(inp);
                        break;
                    } catch (NumberFormatException e) {
                        console.println("Координаты должны быть float type. Введите x:");
                    }
                }
            }

            console.println("Введите y:");
            int y;
            while (true) {
                String inp = console.input().trim();
                if (inp.equals("exit")) throw new ObjectReaderBreak();
                if (inp.isEmpty()) {
                    console.println("Y не может быть пустым. Введите y:");
                } else {
                    try {
                        y = Integer.parseInt(inp);
                        break;
                    } catch (NumberFormatException e) {
                        console.println("Координаты должны быть int type. Введите y:");
                    }
                }
            }

            console.println("Введите z:");
            Integer z;
            while (true) {
                String inp = console.input().trim();
                if (inp.equals("exit")) throw new ObjectReaderBreak();
                if (inp.isEmpty()) {
                    console.println("Z не может быть пустым. Введите z:");
                } else {
                    try {
                        z = Integer.parseInt(inp);
                        break;
                    } catch (NumberFormatException e) {
                        console.println("Координаты должны быть int type. Введите z:");
                    }
                }
            }
            return new Location(x, y, z);
        } catch (IllegalStateException | NoSuchElementException e) {
            console.println("Ошибка ввода. Попробуйте еще раз");
            return null;
        }
    }
}