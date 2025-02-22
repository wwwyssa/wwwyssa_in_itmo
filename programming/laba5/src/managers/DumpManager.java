package managers;

import models.Product;
import utils.Console;

import javax.xml.bind.*;
import javax.xml.bind.annotation.*;
import java.io.*;
import java.util.*;

@XmlRootElement(name = "products")
class ProductWrapper {
    private Map<Integer, Product> products = new LinkedHashMap<>();

    public ProductWrapper() {}

    public ProductWrapper(Map<Integer, Product> products) {
        this.products = products;
    }

    @XmlElement(name = "product")
    public List<ProductEntry> getProducts() {
        List<ProductEntry> list = new ArrayList<>();
        for (Map.Entry<Integer, Product> entry : products.entrySet()) {
            list.add(new ProductEntry(entry.getKey(), entry.getValue()));
        }
        return list;
    }
}

class ProductEntry {
    private int id;
    private Product product;

    public ProductEntry() {}

    public ProductEntry(int id, Product product) {
        this.id = id;
        this.product = product;
    }

    @XmlAttribute
    public int getId() {
        return id;
    }

    @XmlElement
    public Product getProduct() {
        return product;
    }
}


public class DumpManager {
    private final String fileName;
    private final Console console;

    public DumpManager(String fileName, Console console) {
        this.fileName = fileName;
        this.console = console;
    }

    public void writeMap(Map<Integer, Product> map) {
        try {
            JAXBContext context = JAXBContext.newInstance(ProductWrapper.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(new ProductWrapper(map), new File(fileName));
        } catch (JAXBException e) {
            console.printError("Ошибка записи в XML файл!");
        }
    }

    public LinkedHashMap<Integer, Product> readMap() {
        if (fileName == null) {
            console.printError("Переменная окружения с загрузочным файлом не найдена!");
            return null;
        }

        try {
            JAXBContext context = JAXBContext.newInstance(ProductWrapper.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            ProductWrapper wrapper = (ProductWrapper) unmarshaller.unmarshal(new File(fileName));
            LinkedHashMap<Integer, Product> map = new LinkedHashMap<>();
            for (ProductEntry entry : wrapper.getProducts()) {
                map.put(entry.getId(), entry.getProduct());
            }
            return map;
        } catch (JAXBException e) {
            console.printError("Ошибка парсинга XML!");
        }
        return null;
    }
}
