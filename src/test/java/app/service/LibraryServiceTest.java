package app.service;

import app.model.Item;
import app.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import app.repository.ItemRepository;
import app.repository.UserRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import static app.model.ItemType.*;
import static org.mockito.Mockito.*;

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
    public void testReturnItem() {
        int userId = 1;
        int itemId = 5;

        User user = User.builder().userId(userId).name("Alice Smith").build();
        Item item = new Item(1, itemId, DVD, "Pi", false, null, null);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(itemRepository.findAll()).thenReturn(List.of(item));
        libraryService.borrowItem(userId, itemId);
        boolean isReturned = libraryService.returnItem(userId, item.getUniqueId());

        Assertions.assertTrue(isReturned);
        Assertions.assertFalse(user.getBorrowedItems().contains(item));
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
        LocalDate mockNow = LocalDate.of(2023, 10, 19);
        Item item = new Item(1, 1, DVD, "Pi", false, mockNow, mockNow.plusWeeks(1));
        Item item2 = new Item(2, 2, BOOK, "Java Concurrency In Practice", true, mockNow, mockNow.plusWeeks(1));
        Item item3 = new Item(3, 3, VHS, "WarGames", true, mockNow, mockNow.minusWeeks(1));
        Item item4 = new Item(4, 4, VHS, "Hackers", true, mockNow, mockNow.minusWeeks(1));

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
        LocalDate mockNow = LocalDate.of(2023, 10, 19);
        int itemId = 5;
        Item item = new Item(1, itemId, DVD, "Pi", true, mockNow, mockNow.plusWeeks(1));
        Item item2 = new Item(2, itemId, DVD, "Pi", false, null, null);

        when(itemRepository.findAll()).thenReturn(List.of(item, item2));

        boolean isItemAvailable = libraryService.isItemAvailable(itemId);

        Assertions.assertTrue(isItemAvailable);
    }

    /** This test makes sure that borrowing an item is thread-safe. We
     * try and borrow an item using multiple threads. If the method is
     * thread safe, then there should only be one successful borrow.
     */
    @Test
    public void testBorrowItemThreadSafety() throws InterruptedException {
        final int THREAD_COUNT = 100;
        final AtomicInteger successfulBorrows = new AtomicInteger(0);
        final Integer userId = 1;
        final Integer itemId = 5;

        User user = User.builder().userId(userId).name("Alice Smith").build();
        Item item = new Item(1, itemId, DVD, "Pi", false, null, null);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(itemRepository.findAll()).thenReturn(List.of(item));

        ExecutorService service = Executors.newFixedThreadPool(THREAD_COUNT);
        List<Future<?>> futures = new ArrayList<>();

        for (int i = 0; i < THREAD_COUNT; i++) {
            futures.add(service.submit(() -> {
                if (libraryService.borrowItem(userId, itemId)) {
                    successfulBorrows.incrementAndGet();
                }
            }));
        }

        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        service.shutdown();
        Assertions.assertEquals(1, successfulBorrows.get());
    }


    /** The tests below are checking bad-path
     * behaviour.
     */

    @Test
    public void testBorrowItemWhenAlreadyBorrowed() {
        int userId = 1;
        int itemId = 5;
        LocalDate mockNow = LocalDate.of(2023, 10, 19);

        User user = User.builder().userId(userId).name("Alice Smith").build();
        Item item = new Item(1, itemId, DVD, "Pi", true, mockNow, mockNow.plusWeeks(1));

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(itemRepository.findAll()).thenReturn(List.of(item));

        Assertions.assertFalse(user.getBorrowedItems().contains(item));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> libraryService.borrowItem(userId, itemId));
    }

    @Test
    public void testIsItemAvailableWithDuplicatesWhenUnavailable() {
        int itemId = 5;
        LocalDate mockNow = LocalDate.of(2023, 10, 19);
        Item item = new Item(1, itemId, DVD, "Pi", true, mockNow, mockNow.plusWeeks(1));
        Item item2 = new Item(2, itemId, DVD, "Pi", true, mockNow, mockNow.plusWeeks(1));

        when(itemRepository.findAll()).thenReturn(List.of(item, item2));

        boolean isItemAvailable = libraryService.isItemAvailable(itemId);

        Assertions.assertFalse(isItemAvailable);
    }

    @Test
    public void testBorrowNonExistentUser() {
        int userId = 1;
        int itemId = 999;

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> libraryService.borrowItem(userId, itemId));
    }

    @Test
    public void testBorrowNonExistentItem() {
        int userId = 1;
        int itemId = 999;

        User user = User.builder().userId(userId).name("Alice Smith").build();
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> libraryService.borrowItem(userId, itemId));
    }
}
