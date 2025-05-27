package com.wwwyssa.lab6.server.util;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Scanner;
import com.wwwyssa.lab6.common.models.*;
import java.time.LocalDateTime;


/**
 * Класс для считывания данных из XML-файла и создания объектов Product.
 */
public class ProductXMLScaner {
    LinkedHashMap<Integer, Product> Products;
    File file;
    Scanner scan;
    String curentTag;
    boolean flag;

    /**
     * Конструктор класса ProductXMLScaner.
     *
     * @param file файл, из которого будут считываться данные
     * @throws FileNotFoundException если файл не найден
     */
    public ProductXMLScaner(File file) throws FileNotFoundException {
        this.file = file;
        this.Products = new LinkedHashMap<Integer, Product>();
        try {
            this.scan = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("file not found, try to reboot the program with another arguments");
            throw e;
        }
    }


    /**
     * Метод для считывания данных из XML-файла и создания объектов Product.
     *
     * @return LinkedHashMap с объектами Product
     */
    public LinkedHashMap<Integer, Product> readData() {
        scipUselessLine(1);
        while (scan.hasNextLine()) {
            if (scan.hasNextLine()){
                String line = scan.nextLine();
                if (!line.equals("</Products>")){
                    try{
                        String tmp = scan.nextLine();
                        long curId = Long.parseLong(getStrBetweenTags(tmp));
                        String curProductName = getStrBetweenTags(scan.nextLine());
                        Coordinates curCoords = scanCoordinates();
                        LocalDateTime curTime = LocalDateTime.parse(getStrBetweenTags(scan.nextLine()));
                        int price = Integer.parseInt(getStrBetweenTags(scan.nextLine()));
                        String partNumber = getStrBetweenTags(scan.nextLine());
                        int manufactureCost = Integer.parseInt(getStrBetweenTags(scan.nextLine()));
                        UnitOfMeasure unitOfMeasure = UnitOfMeasure.valueOf(getStrBetweenTags(scan.nextLine()));
                        scipUselessLine(1);
                        Organization manufacturer = scanOrganization();
                        Products.put((int)curId, new Product(curId, curProductName, curCoords, curTime, price, partNumber, manufactureCost, unitOfMeasure, manufacturer));
                        scipUselessLine(1);
                    } catch (Exception e){
                        while (scan.hasNextLine()){
                            if (scan.nextLine().equals("</Product>")){
                                break;
                            }
                        }
                    }
                }else{
                    scan.close();
                    return Products;
                }
            }
        }scan.close();
        return Products;
    }

    /**
     * Метод для считывания лишних строк из файла.
     *
     * @param n количество строк, которые нужно пропустить
     */
    public void scipUselessLine(int n){
        for (int i = 0; i < n; i++){
            scan.nextLine();
        }
    }

    /**
     * Метод для считывания данных об организации.
     *
     * @return объект Organization
     */

    public Organization scanOrganization(){
        scipUselessLine(1);
        String orgName = getStrBetweenTags(scan.nextLine());
        Integer employeesCount = Integer.parseInt(getStrBetweenTags(scan.nextLine()));
        OrganizationType orgType = OrganizationType.valueOf(getStrBetweenTags(scan.nextLine()));
        Address orgAddress = scanAddress();
        scipUselessLine(1);
        return new Organization(1, orgName, employeesCount, orgType, orgAddress);
    }

    /**
     * Метод для считывания данных об адресе.
     *
     * @return объект Address
     */
    public Address scanAddress(){
        scipUselessLine(1);
        String street = getStrBetweenTags(scan.nextLine());
        Location town = scanLocation();
        scipUselessLine(1);
        return new Address(street, town);
    }

    /**
     * Метод для считывания данных о местоположении.
     *
     * @return объект Location
     */

    public Location scanLocation(){
        scipUselessLine(1);
        float x = Float.parseFloat(getStrBetweenTags(scan.nextLine()));
        int y = Integer.parseInt(getStrBetweenTags(scan.nextLine()));
        Integer z = Integer.parseInt(getStrBetweenTags(scan.nextLine()));
        Location location = new Location(x, y, z);
        scipUselessLine(1);
        return location;
    }

    /**
     * Метод для считывания данных о координатах.
     *
     * @return объект Coordinates
     */
    public Coordinates scanCoordinates(){
        scipUselessLine(1);
        Coordinates curCoor = new Coordinates();
        Integer x = Integer.parseInt(getStrBetweenTags(scan.nextLine()));
        long y = Long.parseLong(getStrBetweenTags(scan.nextLine()));
        scipUselessLine(1);
        return new Coordinates(x, y);
    }

    /**
     * Метод для извлечения строки между тегами.
     *
     * @param line строка, из которой нужно извлечь содержимое
     * @return содержимое между тегами
     */

    public String getStrBetweenTags(String line) {
        int start = line.indexOf('>');  // Находим первую '>'
        int end = line.lastIndexOf('<'); // Находим последнее '<'

        if (start == -1 || end == -1 || start + 1 >= end) {
            return ""; // Если теги не найдены или их порядок некорректен
        }

        return line.substring(start + 1, end).trim(); // Извлекаем содержимое между тегами
    }



}
