package com.wwwyssa.lab6.server.util;

import com.wwwyssa.lab6.common.models.Product;

import java.util.LinkedHashMap;
import java.util.Map;

public class DictToXmlConverter {

    /**
     * Метод для преобразования LinkedHashMap в XML.
     * @param data данные для преобразования
     * @param rootElement корневой элемент XML
     * @return строка XML
     */
    public static String dictToXml(LinkedHashMap<Integer, Product> data, String rootElement) {
        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append("<").append(rootElement).append(">\n");
        for (Map.Entry<Integer, Product> entry : data.entrySet()) {
            xmlBuilder.append("<Product id=\"").append(entry.getKey()).append("\">\n");
            xmlBuilder.append(objectToXml(entry.getValue()));
            xmlBuilder.append("</Product>\n");
        }
        xmlBuilder.append("</").append(rootElement).append(">");
        return xmlBuilder.toString();
    }

    /**
     * Метод для преобразования объекта Product в XML.
     * @param product объект Product для преобразования
     * @return строка XML
     */
    private static String objectToXml(Product product) {
        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append("<id>").append(product.getId()).append("</id>\n");
        xmlBuilder.append("<name>").append(product.getName()).append("</name>\n");
        xmlBuilder.append("<coordinates>\n")
                .append("<x>").append(product.getCoordinates().getX()).append("</x>\n")
                .append("<y>").append(product.getCoordinates().getY()).append("</y>\n")
                .append("</coordinates>\n");
        xmlBuilder.append("<creationDate>").append(product.getCreationDate()).append("</creationDate>\n");
        xmlBuilder.append("<price>").append(product.getPrice()).append("</price>\n");
        xmlBuilder.append("<partNumber>").append(product.getPartNumber()).append("</partNumber>\n");
        xmlBuilder.append("<manufactureCost>").append(product.getManufactureCost()).append("</manufactureCost>\n");
        xmlBuilder.append("<unitOfMeasure>").append(product.getUnitOfMeasure()).append("</unitOfMeasure>\n");
        xmlBuilder.append("<manufacturer>\n")
                .append("<id>").append(product.getManufacturer().getId()).append("</id>\n")
                .append("<name>").append(product.getManufacturer().getName()).append("</name>\n")
                .append("<employeesCount>").append(product.getManufacturer().getEmployeesCount()).append("</employeesCount>\n")
                .append("<type>").append(product.getManufacturer().getType()).append("</type>\n")
                .append("<officialAddress>\n")
                .append("<street>").append(product.getManufacturer().getOfficialAddress().getStreet()).append("</street>\n")
                .append("<town>\n")
                .append("<x>").append(product.getManufacturer().getOfficialAddress().getTown().getX()).append("</x>\n")
                .append("<y>").append(product.getManufacturer().getOfficialAddress().getTown().getY()).append("</y>\n")
                .append("<z>").append(product.getManufacturer().getOfficialAddress().getTown().getZ()).append("</z>\n")
                .append("</town>\n")
                .append("</officialAddress>\n")
                .append("</manufacturer>\n");
        return xmlBuilder.toString();
    }




}