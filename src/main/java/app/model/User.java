package app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class User {

    private final int userId;
    private final String name;
    @Builder.Default
    private final List<Item> borrowedItems = Collections.synchronizedList(new ArrayList<>());
}
