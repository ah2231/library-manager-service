package util;

import model.Item;
import model.ItemType;
import model.User;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Reader;
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

        try (Reader reader = Files.newBufferedReader(Paths.get(path));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            for (CSVRecord csvRecord : csvParser) {
                Integer uniqueId = Integer.parseInt(csvRecord.get("UniqueID"));
                Integer itemId = Integer.parseInt(csvRecord.get("ItemID"));
                ItemType type = ItemType.valueOf(csvRecord.get("Type"));
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
