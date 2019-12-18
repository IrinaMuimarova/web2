package service;

import model.User;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class UserService {
    private static UserService instance;

    private UserService() {
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    /* хранилище данных */
    private Map<Long, User> dataBase = Collections.synchronizedMap(new HashMap<>());
    /* счетчик id */
    private AtomicLong maxId = new AtomicLong(0);
    /* список авторизованных пользователей */
    private Map<Long, User> authMap = Collections.synchronizedMap(new HashMap<>());

    public AtomicLong getMaxId() {
        return maxId;
    }


    public List<User> getAllUsers() {
        return new ArrayList<>(dataBase.values());
    }

    public User getUserById(Long id) {
        return dataBase.get(id);
    }

    public boolean addUser(User user) {
        user.setId(maxId.getAndIncrement());
        dataBase.put(user.getId(), user);
        return true;
    }

    public void deleteAllUser() {
        dataBase.clear();
        maxId.set(0);
    }

    public boolean isExistsThisUser(User user) {
        for (User u : dataBase.values()) {
            if (u.getEmail().equals(user.getEmail())) {
                return true;
            }
        }
        return false;
    }

    public List<User> getAllAuth() {
        return new ArrayList<>(authMap.values());
    }

    public boolean authUser(User user) {
        User outBase = searchUser(user.getEmail());
        if (outBase.getPassword().equals(user.getPassword())) {
            authMap.put(outBase.getId(), outBase);
            return true;
        }
        return false;
    }

    public void logoutAllUsers() {
        authMap.clear();
    }

    public boolean isUserAuthById(Long id) {
        return authMap.containsKey(id);
    }

    private User searchUser(String email) {
        for (User u : dataBase.values()) {
            if (u.getEmail().equals(email)) {
                return u;
            }
        }
        return null;
    }
}
