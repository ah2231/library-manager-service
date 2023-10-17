package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Item {

    private final int uniqueId;
    private final int itemId;
    private final ItemType itemType;
    private final String title;
    private boolean isBorrowed;
    private LocalDate borrowDate;
    private LocalDate dueDate;


}
