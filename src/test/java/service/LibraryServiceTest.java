package service;

import model.Item;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import repository.ItemRepository;
import repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static model.ItemType.*;
import static org.mockito.Mockito.when;

public class LibraryServiceTest {

    @InjectMocks
    private LibraryService libraryService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.initMocks(this);
    }

    /** The tests below are checking for good path
     * behaviour.
     */

    @Test
    public void testBorrowItemWhenAvailable() {
        int userId = 1;
        int itemId = 5;

        User user = User.builder().userId(userId).name("Alice Smith").build();
        Item item = new Item(1, itemId, DVD, "Pi", false, null, null);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(itemRepository.findAll()).thenReturn(List.of(item));

        Assertions.assertFalse(user.getBorrowedItems().contains(item));

        boolean isBorrowed = libraryService.borrowItem(userId, itemId);

        Assertions.assertTrue(isBorrowed);
        Assertions.assertTrue(user.getBorrowedItems().contains(item));
    }
    @Test
    public void testGetInventory() {
        Item item = new Item(1, 1, DVD, "Pi", false, null, null);
        Item item2 = new Item(2, 2, BOOK, "Java Concurrency In Practice", false, null, null);
        Item item3 = new Item(3, 3, VHS, "WarGames", false, null, null);
        Item item4 = new Item(4, 4, VHS, "Hackers", false, null, null);

        when(itemRepository.findAll()).thenReturn(List.of(item, item2, item3, item4));

        List<Item> items = libraryService.getInventory();

        Assertions.assertEquals(4, items.size());
    }

    @Test
    public void testGetAllOverdueItems() {
        Item item = new Item(1, 1, DVD, "Pi", false, LocalDate.now(), LocalDate.now().plusWeeks(1));
        Item item2 = new Item(2, 2, BOOK, "Java Concurrency In Practice", true, LocalDate.now(), LocalDate.now().plusWeeks(1));
        Item item3 = new Item(3, 3, VHS, "WarGames", true, LocalDate.now(), LocalDate.now().minusWeeks(1));
        Item item4 = new Item(4, 4, VHS, "Hackers", true, LocalDate.now(), LocalDate.now().minusWeeks(1));

        when(itemRepository.findAll()).thenReturn(List.of(item, item2, item3, item4));

        List<Item> items = libraryService.getAllOverdueItems();

        Assertions.assertEquals(2, items.size());
    }

    @Test
    public void testGetBorrowedItemsForUser() {
        int userId = 1;
        int itemId = 5;

        User user = User.builder().userId(userId).name("Alice Smith").build();
        Item item = new Item(1, itemId, DVD, "Pi", false, null, null);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(itemRepository.findAll()).thenReturn(List.of(item));

        Assertions.assertFalse(user.getBorrowedItems().contains(item));

        libraryService.borrowItem(userId, itemId);
        List<Item> items = libraryService.getBorrowedItemsForUser(userId);

        Assertions.assertTrue(items.contains(item));
    }

    @Test
    public void testIsItemAvailable() {
        int itemId = 5;
        Item item = new Item(1, itemId, DVD, "Pi", false, null, null);

        when(itemRepository.findAll()).thenReturn(List.of(item));

        boolean isItemAvailable = libraryService.isItemAvailable(itemId);

        Assertions.assertTrue(isItemAvailable);
    }

    @Test
    public void testIsItemAvailableWithDuplicates() {
        int itemId = 5;
        Item item = new Item(1, itemId, DVD, "Pi", true, LocalDate.now(), LocalDate.now().plusWeeks(1));
        Item item2 = new Item(2, itemId, DVD, "Pi", false, null, null);

        when(itemRepository.findAll()).thenReturn(List.of(item, item2));

        boolean isItemAvailable = libraryService.isItemAvailable(itemId);

        Assertions.assertTrue(isItemAvailable);
    }

    @Test
    public void testConcurrentBorrowing() {

    }

    /** The tests below are checking bad-path
     * behaviour.
     */

    @Test
    public void testBorrowItemWhenAlreadyBorrowed() {
        int userId = 1;
        int itemId = 5;

        User user = User.builder().userId(userId).name("Alice Smith").build();
        Item item = new Item(1, itemId, DVD, "Pi", true, LocalDate.now(), LocalDate.now().plusWeeks(1));

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(itemRepository.findAll()).thenReturn(List.of(item));

        Assertions.assertFalse(user.getBorrowedItems().contains(item));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> libraryService.borrowItem(userId, itemId));
    }

    @Test
    public void testIsItemAvailableWithDuplicatesWhenUnavailable() {
        int itemId = 5;
        Item item = new Item(1, itemId, DVD, "Pi", true, LocalDate.now(), LocalDate.now().plusWeeks(1));
        Item item2 = new Item(2, itemId, DVD, "Pi", true, LocalDate.now(), LocalDate.now().plusWeeks(1));

        when(itemRepository.findAll()).thenReturn(List.of(item, item2));

        boolean isItemAvailable = libraryService.isItemAvailable(itemId);

        Assertions.assertFalse(isItemAvailable);
    }
}
