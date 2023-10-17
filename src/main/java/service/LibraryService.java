package service;

import model.Item;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.ItemRepository;
import repository.UserRepository;

import java.util.List;

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
     * Lets a user borrow a specific item from the library.
     *
     * @param userId   The ID of the user wishing to borrow the item.
     * @param uniqueId The unique ID of the item to be borrowed.
     * @return true if the item was successfully borrowed, false otherwise.
     */
    public boolean borrowSpecificItem(Integer userId, Integer uniqueId) {

    }

    /**
     * Lets a user borrow an item from the library.
     *
     * @param userId The ID of the user wishing to borrow the item.
     * @param itemId The item ID of the item to be borrowed.
     * @return true if the item was successfully borrowed, false otherwise.
     */
    public boolean borrowItem(Integer userId, Integer itemId) {

    }

    /**
     * Returns the current inventory of the library.
     *
     * @return a list of items in the library.
     */
    public List<Item> getInventory() {

    }

    /**
     * Determine the list of items that are currently overdue.
     *
     * @return a list of items that are overdue.
     */
    public List<Item> getAllOverdueItems() {

    }

    /**
     * Determines the list of items that a user has borrowed.
     *
     * @param userId The ID of the user.
     * @return a list of items that the user has borrowed. .
     *
     */
    public List<Item> getBorrowedItemsForUser(Integer userId) {

    }

    /**
     * Determines if an item is available to borrow.
     *
     * @return true if the item is available, false otherwise.
     */
    public boolean isItemAvailable(Integer itemId) {

    }
}
