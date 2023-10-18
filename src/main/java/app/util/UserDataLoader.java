package app.util;

import app.model.User;

import java.util.List;

public interface UserDataLoader {
    List<User> loadUsers(String path);
}
