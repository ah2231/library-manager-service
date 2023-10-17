package repository;

import model.Item;
import model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {
    public User save(User user);
    public Optional<User> findById(Integer id);
    public Collection<User> findAll();
    public Optional<User> updateById(Integer id, User user);
    public void deleteById(Integer id);
}
