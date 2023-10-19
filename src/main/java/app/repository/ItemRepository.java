package app.repository;

import app.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    public Item save(Item item);
    public Optional<Item> findById(Integer id);
    public List<Item> findAll();
    public Optional<Item> updateById(Integer id, Item item);
    public void deleteById(Integer id);
}
