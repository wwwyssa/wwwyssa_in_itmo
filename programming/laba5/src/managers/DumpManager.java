package managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.JsonParseException;
import models.Product;

import java.io.*;
import java.time.LocalDate;
import java.util.Collection;

import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import utils.Console;


public class DumpManager {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
    private final String fileName;
    private final Console console;

    public DumpManager(String fileName, Console console) {
        this.fileName = fileName;
        this.console = console;
    }

    public void writeCollection(Collection<Product> collection) {
        try (PrintWriter collectionPrintWriter = new PrintWriter(new File(fileName))) {
            collectionPrintWriter.println(gson.toJson(collection));
            console.println("Коллекция сохранена в файл!");
        } catch (IOException exception) {
            console.printError("Загрузочный файл не может быть открыт!");
        }
    }


    public Collection<Product> readCollection() {
        return null;
    }
}
