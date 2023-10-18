package app.util;

import app.model.User;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class CsvUserLoaderImpl implements UserDataLoader {
    @Override
    public List<User> loadUsers(String path) {
        List<User> users = new ArrayList<>();

        try (Reader reader = Files.newBufferedReader(Paths.get(path));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            for (CSVRecord csvRecord : csvParser) {
                Integer userId = Integer.parseInt(csvRecord.get("UserId"));
                String name = csvRecord.get("Name");

                User user = User.builder().userId(userId).name(name).build();
                users.add(user);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return users;
    }
}
