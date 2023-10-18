package app.util;

import app.model.Item;

import java.util.List;

public interface ItemDataLoader {
    List<Item> loadItems(String path);
}
