package com.wwwyssa.lab6.server.managers;

import com.wwwyssa.lab6.common.models.Product;
import com.wwwyssa.lab6.common.util.executions.AnswerString;
import com.wwwyssa.lab6.common.util.executions.ExecutionResponse;
import com.wwwyssa.lab6.server.util.DictToXmlConverter;
import  com.wwwyssa.lab6.server.util.ProductXMLScaner;

import java.io.*;
import java.util.*;




public class DumpManager {
    private final String fileName;
    private static DumpManager instance;
    /**
     * Конструктор с параметрами.
     * @param fileName имя файла для сохранения/загрузки
     */
    public DumpManager(String fileName) {
        this.fileName = fileName;
    }


    public static DumpManager getInstance() {
        if (instance == null) {
            instance = new DumpManager("1.xml");
        }
        return instance;
    }

    public ExecutionResponse writeMap(Map<Integer, Product> map) {
        DictToXmlConverter converter = new DictToXmlConverter();
        String xml = converter.dictToXml((LinkedHashMap<Integer, Product>) map, "Products");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(xml);
            return new ExecutionResponse(true, new AnswerString("Коллекция успешно сохранена в файл!"));
        } catch (IOException e) {
            return new ExecutionResponse(true, new AnswerString("Произошла ошибка при сохранении коллекции в файл!"));
        }
    }


    public LinkedHashMap<Integer, Product> readMap() {
        if (fileName == null) {
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
            return null;
        }
        return tmp;
    }



}
