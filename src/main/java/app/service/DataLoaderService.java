package app.service;

import jakarta.annotation.PostConstruct;
import app.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import app.model.Item;
import app.util.ItemDataLoader;
import app.util.UserDataLoader;

import java.util.List;


@Service
public class DataLoaderService {

    @Value("${file.path.items}")
    private String itemsFilePath;

    @Value("${file.path.users}")
    private String usersFilePath;

    @Autowired
    private ItemDataLoader itemDataLoader;

    @Autowired
    private UserDataLoader userDataLoader;

    @Autowired
    private DataPersistService dataPersistService;

    @PostConstruct
    public void loadInitialData() {
        List<Item> items = itemDataLoader.loadItems(itemsFilePath);
        dataPersistService.persistItems(items);
        List<User> users = userDataLoader.loadUsers(usersFilePath);
        dataPersistService.persistUsers(users);
    }
}
