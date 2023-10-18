package util;

import model.Item;
import model.User;

import java.util.List;

public interface ItemDataLoader {
    List<Item> loadItems(String path);
}
