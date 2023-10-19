package app.repository;

import app.model.Item;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryItemRepositoryImpl implements ItemRepository {

    private final ConcurrentHashMap<Integer, Item> items = new ConcurrentHashMap<>();
    @Override
    public Item save(Item item) {
        items.put(item.getUniqueId(), item);
        return item;
    }

    @Override
    public Optional<Item> findById(Integer id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public List<Item> findAll() {
        return new ArrayList<>(items.values());
    }

    @Override
    public Optional<Item> updateById(Integer id, Item item) {
        if (items.containsKey(id)) {
            items.put(id, item);
            return Optional.of(item);
        }

        return Optional.empty();
    }

    @Override
    public void deleteById(Integer id) {
        items.remove(id);
    }
}
