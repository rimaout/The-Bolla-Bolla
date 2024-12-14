package model.users;

import model.entities.PlayerModel;

import java.io.File;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class UsersManagerModel {
    private static UsersManagerModel instance;

    private final HashMap<String, User> users;
    private User currentUser;

    private UsersManagerModel() {
        this.users = new HashMap<>();
        loadUsers();
    }

    public static UsersManagerModel getInstance() {
        if (instance == null) {
            instance = new UsersManagerModel();
        }
        return instance;
    }

    public void createUser(String name, int profilePictureIndex) {
        if (!users.containsKey(name)) {
            User newUser = new User(name, profilePictureIndex);
            users.put(name, newUser);
            saveUsers();
            selectProfile(name);
        }
    }

    public void selectProfile(String name) {
        if (users.containsKey(name)) {
            currentUser = users.get(name);
            currentUser.updateLastSelectedTime();
            saveUsers();
        }
    }

    public void saveUsers() {
        for (User user : users.values()) {
            user.save( user.getName() + ".dat");
        }
    }

    public void loadUsers() {
        File directory = new File("res/users-data");
        File[] files = directory.listFiles((dir, name) -> name.endsWith(".dat"));

        if (files != null) {
            for (File file : files) {
                User user = User.reed(file.getPath());
                if (user != null) {
                    users.put(user.getName(), user);
                }
            }
        }

        if (users.isEmpty()) {
            createUser("default", 0);
        }
    }

    public void updateCurrentUserInfo(boolean victory) {
        currentUser.setScore(PlayerModel.getInstance().getPoints());

        if (victory)
            currentUser.increaseWins();
        else
            currentUser.increaseGames();

        saveUsers();
    }

    public ArrayList<User> getUsers() {

        // sort users by last selected time (used stream because of exam requirement)
        return (ArrayList<User>) users.values().stream()
                .sorted(Comparator.comparing(User::getLastSelectedTime).reversed())
                .collect(Collectors.toList());
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public boolean doesUserAlreadyExist(String name) {
        return users.containsKey(name);
    }
}