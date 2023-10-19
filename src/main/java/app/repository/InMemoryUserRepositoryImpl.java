package app.repository;

import app.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
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
    public List<User> findAll() {
        return new ArrayList<>(users.values());
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
