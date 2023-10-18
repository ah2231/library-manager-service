package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import app.service.LibraryService;

@RestController
@RequestMapping("/api/v1")
public class LibraryController {

    private final LibraryService libraryService;

    @Autowired
    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @PostMapping("/borrow/{userId}/{itemId}")
    public ResponseEntity<String> borrowItem(@PathVariable Integer userId, @PathVariable Integer itemId) {
        boolean result = libraryService.borrowItem(userId, itemId);

        if (result) {
            return ResponseEntity.ok("Item borrow was successful.");
        } else {
            return ResponseEntity.badRequest().body("Item borrow was unsuccessful. It may be unavailable or already borrowed.");
        }
    }
}
