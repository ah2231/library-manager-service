package util;

import model.User;

import java.util.List;

public interface UserDataLoader {
    List<User> loadUsers(String path);
}
