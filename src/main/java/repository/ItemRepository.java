package repository;

import model.Item;

import java.util.Collection;
import java.util.Optional;

public interface ItemRepository {
    public Item save(Item item);
    public Optional<Item> findById(Integer id);
    public Collection<Item> findAll();
    public Optional<Item> updateById(Integer id, Item item);
    public void deleteById(Integer id);
}
