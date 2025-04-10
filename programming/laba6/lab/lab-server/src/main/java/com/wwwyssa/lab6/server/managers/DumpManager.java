package managers;

import models.Product;
import utils.console.DefaultConsole;
import utils.DictToXmlConverter;

import java.io.*;
import java.util.*;

import utils.ProductXMLScaner;


public class DumpManager {
    private final String fileName;
    private final DefaultConsole defaultConsole;

    /**
     * Конструктор с параметрами.
     * @param fileName имя файла для сохранения/загрузки
     * @param defaultConsole объект консоли для вывода сообщений
     */
    public DumpManager(String fileName, DefaultConsole defaultConsole) {
        this.fileName = fileName;
        this.defaultConsole = defaultConsole;
    }

    public void writeMap(Map<Integer, Product> map) {
        DictToXmlConverter converter = new DictToXmlConverter();
        String xml = converter.dictToXml((LinkedHashMap<Integer, Product>) map, "Products");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(xml);
            defaultConsole.print("Коллекция успешно сохранена в файл!");
        } catch (IOException e) {
            defaultConsole.printError("Произошла ошибка при сохранении коллекции в файл!");
        }
    }


    public LinkedHashMap<Integer, Product> readMap() {
        if (fileName == null) {
            defaultConsole.printError("Переменная окружения с загрузочным файлом не найдена!");
            return null;
        }
        LinkedHashMap<Integer, Product> tmp = new LinkedHashMap<>();
        try {
            ProductXMLScaner xmlScaner = new ProductXMLScaner(new File(fileName));
            tmp =  xmlScaner.readData();
            for(Integer key : tmp.keySet()){
                Product product = tmp.get(key);
                if (!product.isValid()){

                    tmp.remove(key);
                }
            }
        } catch (IOException e) {
            defaultConsole.printError("Произошла ошибка при загрузке коллекции из файла!");
        }
        return tmp;
    }



}
