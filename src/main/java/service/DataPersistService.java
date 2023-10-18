package service;

import model.Item;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.ItemRepository;
import repository.UserRepository;

import java.util.List;

@Service
public class DataPersistService {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;

    public void persistItems(List<Item> items) {
        items.forEach(item -> itemRepository.save(item));
    }

    public void persistUsers(List<User> users) {
        users.forEach(user -> userRepository.save(user));
    }
}
