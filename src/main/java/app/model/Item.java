package app.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Item {

    private final Integer uniqueId;
    private final Integer itemId;
    private final ItemType itemType;
    private final String title;
    private boolean isBorrowed;
    private LocalDate borrowDate;
    private LocalDate dueDate;


}
