package models;

import managers.CollectionManager;
import utils.Console;

import java.util.Date;
import java.util.NoSuchElementException;

public class ObjectReader {
    public static Product readProduct(Console console) {
        try {
            console.println("Enter location name:");
            String name;
            while (true) {
                name = console.input();
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
            Date date = new Date();
            return new Product(CollectionManager.generateId(), name, coordinates, date,  price, partNumber, manufactureCost, unitOfMeasure, manufacturer);
        } catch (IllegalStateException | NoSuchElementException e) {
            console.println("Read ERROR. Try again.");
            return null;
        }
    }

    public static Coordinates readCoordinates(Console console) {
        try {
            console.println("Enter coordinates");
            console.println("Enter x:");
            Integer x;
            while (true) {
                String inp = console.input().trim();
                if (inp.isEmpty()) {
                    console.println("X can't be empty. Enter x:");
                } else {
                    try {
                        x = Integer.parseInt(inp);
                        if (x > 765) {
                            console.println("X can't be more than 765. Enter x:");
                        } else {
                            break;
                        }
                    } catch (NumberFormatException e) {
                        console.println("Coordinates must be int type. Write x:");
                    }
                }
            }
            console.println("Enter y:");
            Long y;
            while (true) {
                String inp = console.input().trim();
                if (inp.isEmpty()) {
                    console.println("Y can't be empty. Enter y:");
                } else {
                    try {
                        y = Long.parseLong(inp);
                        if (y < -365) {
                            console.println("Y can't be less than -365. Enter y:");
                        } else {
                            break;
                        }
                    } catch (NumberFormatException e) {
                        console.println("Coordinates must be long type. Write y:");
                    }
                }
            }
            return new Coordinates(x, y);
        } catch (IllegalStateException | NoSuchElementException e) {
            console.println("Read ERROR. Try again.");
            return null;
        }
    }

    public static int readPrice(Console console) {
        try {
            console.println("Enter price:");
            int price;
            while (true) {
                String inp = console.input().trim();
                if (inp.isEmpty()) {
                    console.println("Price can't be empty. Enter price:");
                } else {
                    try {
                        price = Integer.parseInt(inp);
                        if (price <= 0) {
                            console.println("Price must be more than 0. Enter price:");
                        } else {
                            break;
                        }
                    } catch (NumberFormatException e) {
                        console.println("Price must be int type. Write price:");
                    }
                }
            }
            return price;
        } catch (IllegalStateException | NoSuchElementException e) {
            console.println("Read ERROR. Try again.");
            return Integer.MIN_VALUE;
        }
    }

    public static String readPartNumber(Console console) {
        try {
            console.println("Enter part number:");
            String partNumber;
            while (true) {
                partNumber = console.input().trim();
                if (partNumber.isEmpty()) {
                    console.println("Part number can't be empty. Enter part number:");
                } else {
                    break;
                }
            }
            return partNumber;
        } catch (IllegalStateException | NoSuchElementException e) {
            console.println("Read ERROR. Try again.");
            return null;
        }
    }

    public static int readManufactureCost(Console console) {
        try {
            console.println("Enter manufacture cost:");
            int manufactureCost;
            while (true) {
                String inp = console.input().trim();
                if (inp.isEmpty()) {
                    console.println("Manufacture cost can't be empty. Enter manufacture cost:");
                } else {
                    try {
                        manufactureCost = Integer.parseInt(inp);
                        break;
                    } catch (NumberFormatException e) {
                        console.println("Manufacture cost must be int type. Write manufacture cost:");
                    }
                }
            }
            return manufactureCost;
        } catch (IllegalStateException | NoSuchElementException e) {
            console.println("Read ERROR. Try again.");
            return Integer.MIN_VALUE;
        }
    }

    public static UnitOfMeasure readUnitOfMeasure(Console console) {
        try {
            console.println("Enter unit of measure:");
            console.println("Choose one of the following:");
            for (UnitOfMeasure unitOfMeasure : UnitOfMeasure.values()) {
                console.println(unitOfMeasure.toString());
            }
            UnitOfMeasure unitOfMeasure;
            while (true) {
                String inp = console.input().trim();
                if (inp.isEmpty()) {
                    console.println("Unit of measure can't be empty. Enter unit of measure:");
                } else {
                    try {
                        unitOfMeasure = UnitOfMeasure.valueOf(inp.toUpperCase());
                        break;
                    } catch (IllegalArgumentException e) {
                        console.println("Unit of measure must be one of the following:");
                        for (UnitOfMeasure unit : UnitOfMeasure.values()) {
                            console.println(unit.toString());
                        }
                    }
                }
            }
            return unitOfMeasure;
        } catch (IllegalStateException | NoSuchElementException e) {
            console.println("Read ERROR. Try again.");
            return null;
        }
    }

    public static Organization readOrganization(Console console) {
        try {
            console.println("Enter organization");
            console.println("Enter organization name:");
            String name;
            while (true) {
                name = console.input();
                if (name.isEmpty()) {
                    console.println("Name can't be empty. Enter organization name:");
                } else {
                    break;
                }
            }
            console.println("Enter organization type:");
            console.println("Choose one of the following:");
            for (OrganizationType organizationType : OrganizationType.values()) {
                console.println(organizationType.toString());
            }

            Integer employeesCount;
            while (true) {
                String inp = console.input().trim();
                if (inp.isEmpty()) {
                    console.println("Employees count can't be empty. Enter employees count:");
                } else {
                    try {
                        employeesCount = Integer.parseInt(inp);
                        if (employeesCount <= 0) {
                            console.println("Employees count must be more than 0. Enter employees count:");
                        } else {
                            break;
                        }
                    } catch (NumberFormatException e) {
                        console.println("Employees count must be int type. Write employees count:");
                    }
                }
            }
            OrganizationType organizationType;
            while (true) {
                String inp = console.input().trim();
                if (inp.isEmpty()) {
                    console.println("Organization type can't be empty. Enter organization type:");
                } else {
                    try {
                        organizationType = OrganizationType.valueOf(inp.toUpperCase());
                        break;
                    } catch (IllegalArgumentException e) {
                        console.println("Organization type must be one of the following:");
                        for (OrganizationType type : OrganizationType.values()) {
                            console.println(type.toString());
                        }
                    }
                }
            }

            Address address = readAddress(console);
            return new Organization(name, employeesCount, organizationType, address);
        } catch (IllegalStateException | NoSuchElementException e) {
            console.println("Read ERROR. Try again.");
            return null;
        }

    }

    public static Address readAddress(Console console) {
        try {
            console.println("Enter address");
            console.println("Enter street:");
            String street;
            while (true) {
                street = console.input();
                if (street.isEmpty()) {
                    console.println("Street can't be empty. Enter street:");
                } else {
                    break;
                }
            }
            Location town = readLocation(console);
            return new Address(street, town);
        } catch (IllegalStateException | NoSuchElementException e) {
            console.println("Read ERROR. Try again.");
            return null;
        }

    }

    public static Location readLocation(Console console) {
        try {
            console.println("Enter x:");
            Float x;
            while (true) {
                String inp = console.input().trim();
                if (inp.isEmpty()) {
                    console.println("X can't be empty. Enter x:");
                } else {
                    try {
                        x = Float.parseFloat(inp);
                        break;
                    } catch (NumberFormatException e) {
                        console.println("Coordinates must be float type. Write x:");
                    }
                }
            }

            console.println("Enter y:");
            int y;
            while (true) {
                String inp = console.input().trim();
                if (inp.isEmpty()) {
                    console.println("Y can't be empty. Enter x:");
                } else {
                    try {
                        y = Integer.parseInt(inp);
                        break;
                    } catch (NumberFormatException e) {
                        console.println("Coordinates must be int type. Write x:");
                    }
                }
            }

            console.println("Enter z:");
            Integer z;
            while (true) {
                String inp = console.input().trim();
                if (inp.isEmpty()) {
                    console.println("Z can't be empty. Enter x:");
                } else {
                    try {
                        z = Integer.parseInt(inp);
                        break;
                    } catch (NumberFormatException e) {
                        console.println("Coordinates must be int type. Write x:");
                    }
                }
            }
            return new Location(x, y, z);
        } catch (IllegalStateException | NoSuchElementException e) {
            console.println("Read ERROR. Try again.");
            return null;
        }

    }

}

