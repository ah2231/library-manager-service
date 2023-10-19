package app.util;

import app.model.Item;
import app.model.ItemType;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class CsvItemLoaderImpl implements ItemDataLoader {
    @Override
    public List<Item> loadItems(String path) {
        List<Item> items = new ArrayList<>();

        Resource resource = new ClassPathResource(path);
        try (InputStream inputStream = resource.getInputStream();
             Reader reader = new BufferedReader(new InputStreamReader(inputStream));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            for (CSVRecord csvRecord : csvParser) {
                Integer uniqueId = Integer.parseInt(csvRecord.get("UniqueID"));
                Integer itemId = Integer.parseInt(csvRecord.get("ItemID"));
                ItemType type = ItemType.valueOf(csvRecord.get("Type").toUpperCase());
                String title = csvRecord.get("Title");
                boolean isBorrowed = false;
                LocalDate borrowDate = null;
                LocalDate dueDate = null;

                items.add(new Item(uniqueId, itemId, type, title, isBorrowed, borrowDate, dueDate));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return items;
    }
}
