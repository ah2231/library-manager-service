package repository;

import model.Item;
import model.User;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserRepositoryImpl implements UserRepository {

    private final ConcurrentHashMap<Integer, User> users = new ConcurrentHashMap<>();

    @Override
    public User save(User user) {
        users.put(user.getUserId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(Integer id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public Optional<User> updateById(Integer id, User user) {
        if (users.containsKey(id)) {
            users.put(id, user);
            return Optional.of(user);
        }

        return Optional.empty();
    }

    @Override
    public void deleteById(Integer id) {
        users.remove(id);
    }
}
