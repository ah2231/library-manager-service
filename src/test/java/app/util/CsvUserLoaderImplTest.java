package app.util;

import app.model.Item;
import app.model.User;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.List;

public class CsvUserLoaderImplTest {

    @Test
    public void testParseUsersFromFile() throws IOException {
        CsvUserLoaderImpl csvUserLoader = new CsvUserLoaderImpl();
        List<User> users = csvUserLoader.loadUsers("src/main/resources/users.csv");
        Assertions.assertEquals(5, users.size());
    }

    @Test
    public void testCSVNotFound() {
        CsvUserLoaderImpl csvUserLoader = new CsvUserLoaderImpl();

        Assertions.assertThrows(RuntimeException.class, () -> {
            csvUserLoader.loadUsers("file.csv");
        });
    }
}
