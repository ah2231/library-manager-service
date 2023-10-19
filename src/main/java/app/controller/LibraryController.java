package app.controller;

import app.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import app.service.LibraryService;

import java.util.List;

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

    @PostMapping("/borrowUnique/{userId}/{uniqueId}")
    public ResponseEntity<String> borrowItemUnique(@PathVariable Integer userId, @PathVariable Integer uniqueId) {
        boolean result = libraryService.borrowItemUnique(userId, uniqueId);

        if (result) {
            return ResponseEntity.ok("Item borrow was successful.");
        } else {
            return ResponseEntity.badRequest().body("Item borrow was unsuccessful. It may be unavailable or already borrowed.");
        }
    }

    @PostMapping("/return/{userId}/{uniqueId}")
    public ResponseEntity<String> returnItem(@PathVariable Integer userId, @PathVariable Integer uniqueId) {
        boolean result = libraryService.returnItem(userId, uniqueId);

        if (result) {
            return ResponseEntity.ok("Item return was successful.");
        } else {
            return ResponseEntity.badRequest().body("Item return was unsuccessful.");
        }
    }

    @GetMapping("/getInventory")
    public ResponseEntity<List<Item>> getInventory() {
        List<Item> items = libraryService.getInventory();
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping("/getAllOverdueItems")
    public ResponseEntity<List<Item>> getAllOverdueItems() {
        List<Item> items = libraryService.getAllOverdueItems();
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping("/getBorrowedItemsForUser/{userId}")
    public ResponseEntity<List<Item>> getBorrowedItemsForUser(@PathVariable Integer userId) {
        List<Item> items = libraryService.getBorrowedItemsForUser(userId);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping("/isItemAvailable/{itemId}")
    public ResponseEntity<Boolean> isItemAvailable(@PathVariable Integer itemId) {
        Boolean isItemAvailable = libraryService.isItemAvailable(itemId);
        return new ResponseEntity<>(isItemAvailable, HttpStatus.OK);
    }
}
