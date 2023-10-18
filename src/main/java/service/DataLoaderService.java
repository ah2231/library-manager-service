package service;

import jakarta.annotation.PostConstruct;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import model.Item;
import util.ItemDataLoader;
import util.UserDataLoader;

import java.util.List;


@Service
public class DataLoaderService {

    @Value("${inventory.file.path}")
    private String itemsFilePath;

    @Value("${users.file.path}")
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
