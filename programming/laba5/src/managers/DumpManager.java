package managers;

import models.Product;
import utils.Console;
import utils.DictToXmlConverter;

import java.io.*;
import java.util.*;
import utils.XMLParser;

public class DumpManager {
    private final String fileName;
    private final Console console;

    /**
     * Конструктор с параметрами.
     * @param fileName имя файла для сохранения/загрузки
     * @param console объект консоли для вывода сообщений
     */
    public DumpManager(String fileName, Console console) {
        this.fileName = fileName;
        this.console = console;
    }

    public void writeMap(Map<Integer, Product> map) {
        DictToXmlConverter converter = new DictToXmlConverter();
        String xml = converter.dictToXml((LinkedHashMap<Integer, Product>) map, "Products");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(xml);
            console.print("Коллекция успешно сохранена в файл!");
        } catch (IOException e) {
            console.printError("Произошла ошибка при сохранении коллекции в файл!");
        }
    }


    public LinkedHashMap<Integer, Product> readMap() {
        if (fileName == null) {
            console.printError("Переменная окружения с загрузочным файлом не найдена!");
            return null;
        }
        try {
            return XMLParser.parseXML(fileName);
        } catch (IOException e) {
            console.printError("Произошла ошибка при загрузке коллекции из файла!");
        }
        return null;
    }



}
