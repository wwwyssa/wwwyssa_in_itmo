package utils;

import java.io.*;
import java.util.*;
import models.*;

import com.fasterxml.jackson.dataformat.xml.*;
import java.util.LinkedHashMap;

public class XMLParser {
    public static LinkedHashMap<Integer, Product> parseXML(String filename) throws IOException {
        LinkedHashMap<Integer, Product> products = new LinkedHashMap<>();
        var reader = new BufferedReader(new FileReader(filename));
        var xmlString = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            xmlString.append(line);
        }

        String xml = xmlString.toString();
        System.out.println(xml);
        XmlMapper xmlMapper = new XmlMapper();
        try{
            System.out.println("Парсинг XML файла");
            Product product = xmlMapper.readValue(xml, Product.class);
            products.put((int) product.getId(), product);
            return products;
        } catch (Exception e) {
            System.out.println("Ошибка при чтении XML файла" + e.getMessage());
        }
        return null;
    }


}
