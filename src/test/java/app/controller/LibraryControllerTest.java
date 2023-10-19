package app.controller;

import app.model.Item;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import app.service.LibraryService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static app.model.ItemType.DVD;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class LibraryControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LibraryService libraryService;

    @Test
    public void testBorrowItemEndpoint() throws Exception {
        when(libraryService.borrowItem(1,1)).thenReturn(true);

        this.mockMvc.perform(post("/api/v1/borrow/1/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Item borrow was successful."));

        verify(libraryService, times(1)).borrowItem(1, 1);
    }

    @Test
    public void testReturnItemEndpoint() throws Exception {
        when(libraryService.returnItem(1,1)).thenReturn(true);

        this.mockMvc.perform(post("/api/v1/return/1/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Item return was successful."));

        verify(libraryService, times(1)).returnItem(1, 1);
    }

    @Test
    public void testBorrowItemEndpointUnsuccessful() throws Exception {
        when(libraryService.borrowItem(1,1)).thenReturn(false);

        this.mockMvc.perform(post("/api/v1/borrow/1/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Item borrow was unsuccessful. It may be unavailable or already borrowed."));

        verify(libraryService, times(1)).borrowItem(1, 1);
    }

    @Test
    public void testGetInventoryEndpoint() throws Exception {
        LocalDate mockNow = LocalDate.of(2023, 10, 19);
        LocalDate mockDueDate = mockNow.plusWeeks(1);

        List<Item> mockInventory = List.of(
                new Item(1, 5, DVD, "Pi", true, mockNow, mockDueDate),
                new Item(1, 6, DVD, "Top Gun", true, mockNow, mockDueDate)
        );

        when(libraryService.getInventory()).thenReturn(mockInventory);

        mockMvc.perform(get("/api/v1/getInventory"))
                .andExpect(status().isOk())
                .andExpect(content().string("[{\"uniqueId\":1,\"itemId\":5,\"itemType\":\"DVD\"," +
                        "\"title\":\"Pi\",\"borrowDate\":\"2023-10-19\",\"dueDate\":\"2023-10-26\",\"borrowed\":true}" +
                        ",{\"uniqueId\":1,\"itemId\":6,\"itemType\":\"DVD\",\"title\":\"Top Gun\"" +
                        ",\"borrowDate\":\"2023-10-19\",\"dueDate\":\"2023-10-26\",\"borrowed\":true}]"));

    }

    @Test
    public void testGetAllOverdueItemsEndpoint() throws Exception {
        LocalDate mockNow = LocalDate.of(2023, 10, 19);
        LocalDate mockDueDate = mockNow.minusWeeks(1);

        List<Item> mockInventory = List.of(
                new Item(1, 5, DVD, "Pi", true, mockNow, null),
                new Item(1, 6, DVD, "Top Gun", true, mockNow, mockDueDate)
        );

        when(libraryService.getAllOverdueItems()).thenReturn(List.of(mockInventory.get(1)));

        mockMvc.perform(get("/api/v1/getAllOverdueItems"))
                .andExpect(status().isOk())
                .andExpect(content().string("[{\"uniqueId\":1,\"itemId\":6,\"itemType\":\"DVD\",\"title\":\"Top Gun\"" +
                        ",\"borrowDate\":\"2023-10-19\",\"dueDate\":\"2023-10-12\",\"borrowed\":true}]"));
    }

    @Test
    public void testGetBorrowedItemsForUserEndpoint() throws Exception {
        LocalDate mockNow = LocalDate.of(2023, 10, 19);
        LocalDate mockDueDate = mockNow.minusWeeks(1);

        List<Item> mockInventory = List.of(
                new Item(1, 6, DVD, "Top Gun", true, mockNow, mockDueDate)
        );

        when(libraryService.getBorrowedItemsForUser(1)).thenReturn(mockInventory);

        mockMvc.perform(get("/api/v1/getBorrowedItemsForUser/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("[{\"uniqueId\":1,\"itemId\":6,\"itemType\":\"DVD\",\"title\":\"Top Gun\"" +
                        ",\"borrowDate\":\"2023-10-19\",\"dueDate\":\"2023-10-12\",\"borrowed\":true}]"));
    }

    @Test
    public void testIsItemAvailableEndpoint() throws Exception {
        when(libraryService.isItemAvailable(6)).thenReturn(true);

        mockMvc.perform(get("/api/v1/isItemAvailable/6"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}
