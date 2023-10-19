package app.util;

import app.model.Item;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.util.List;

public class CsvItemLoaderImplTest {

    @Test
    public void testParseItemsFromFile() throws IOException {
        CsvItemLoaderImpl csvItemLoader = new CsvItemLoaderImpl();
        List<Item> items = csvItemLoader.loadItems("inventory.csv");
        Assertions.assertEquals(12, items.size());
    }

    @Test
    public void testCSVNotFound() {
        CsvItemLoaderImpl csvItemLoader = new CsvItemLoaderImpl();

        Assertions.assertThrows(RuntimeException.class, () -> {
            csvItemLoader.loadItems("file.csv");
        });
    }
}
