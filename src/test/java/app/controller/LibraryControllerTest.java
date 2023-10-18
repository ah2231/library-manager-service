package app.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import app.service.LibraryService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
}
