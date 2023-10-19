package app.repository;

import app.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    public User save(User user);
    public Optional<User> findById(Integer id);
    public List<User> findAll();
    public Optional<User> updateById(Integer id, User user);
    public void deleteById(Integer id);
}
