package app.service;

import app.model.Item;
import app.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import app.repository.ItemRepository;
import app.repository.UserRepository;

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
