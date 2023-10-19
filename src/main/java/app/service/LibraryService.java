package app.service;

import app.model.Item;
import app.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import app.repository.ItemRepository;
import app.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
public class LibraryService {
    private ItemRepository itemRepository;
    private UserRepository userRepository;

    @Autowired
    public LibraryService(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    /**
     * Lets a user borrow an item from the library.
     *
     * @param userId The ID of the user wishing to borrow the item.
     * @param itemId The item ID of the item to be borrowed.
     * @return true if the item was successfully borrowed.
     */
    // @Transactional
    public boolean borrowItem(Integer userId, Integer itemId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with userId: " + userId + " was not found."));

        Item item = itemRepository.findAll()
                .stream()
                .filter(i -> i.getItemId().equals(itemId) && !i.isBorrowed())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Item with itemId: " + itemId + " was not found or was not available."));

        item.setBorrowed(true);
        item.setBorrowDate(LocalDate.now());
        item.setDueDate(LocalDate.now().plusWeeks(1));

        user.getBorrowedItems().add(item);

        userRepository.updateById(userId, user);
        itemRepository.updateById(item.getUniqueId(), item);

        return true;
    }

    /**
     * Lets a user return an item from the library.
     *
     * @param userId The ID of the user wishing to return the item.
     * @param uniqueId The unique item ID of the item to be returned.
     * @return true if the item was successfully returned.
     */
//    @Transactional -
    public boolean returnItem(Integer userId, Integer uniqueId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with userId: " + userId + " was not found."));

        Item item = itemRepository.findAll()
                .stream()
                .filter(i -> i.getUniqueId().equals(uniqueId) && i.isBorrowed())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Item with itemId: " + uniqueId + " was not found or was already available."));

        item.setBorrowed(false);
        item.setBorrowDate(null);
        item.setDueDate(null);

        user.getBorrowedItems().remove(item);

        userRepository.updateById(userId, user);
        itemRepository.updateById(item.getUniqueId(), item);

        return true;
    }

    /**
     * Returns the current inventory of the library.
     *
     * @return a list of items in the library.
     */
    public List<Item> getInventory() {
        return (List<Item>) itemRepository.findAll();
    }

    /**
     * Determine the list of items that are currently overdue.
     *
     * @return a list of items that are overdue.
     */
    public List<Item> getAllOverdueItems() {
        return itemRepository.findAll().stream().filter(item -> item.isBorrowed()
                && item.getDueDate().isBefore(LocalDate.now())).toList();
    }

    /**
     * Determines the list of items that a user has borrowed.
     *
     * @param userId The ID of the user.
     * @return a list of items that the user has borrowed. .
     *
     */
    public List<Item> getBorrowedItemsForUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with userId: " + userId + " was not found."));

        return user.getBorrowedItems();
    }

    /**
     * Determines if an item is available to borrow.
     *
     * @return true if the item is available, false otherwise.
     */
    public boolean isItemAvailable(Integer itemId) {
        Optional<Item> item = itemRepository.findAll()
                .stream()
                .filter(i -> i.getItemId() == itemId && !i.isBorrowed())
                .findFirst();

        return item.isPresent();
    }
}
