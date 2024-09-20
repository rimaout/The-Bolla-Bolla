package profiles;

import java.io.File;
import java.util.HashMap;

public class UsersManager {
    private static UsersManager instance;

    private final HashMap<String, User> users;
    private User currentUser;

    public UsersManager() {
        users = new HashMap<>();
        loadUsers();
    }

    public static UsersManager getInstance() {
        if (instance == null) {
            instance = new UsersManager();
        }
        return instance;
    }

    public void createProfile(String name, int profilePictureIndex) {
        if (!users.containsKey(name)) {
            users.put(name, new User(name, profilePictureIndex));
            saveUsers();
        }
    }

    public void selectProfile(String name) {
        if (users.containsKey(name)) {
            currentUser = users.get(name);
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
            createProfile("default", 0);
        }
    }

    public HashMap<String, User> getUsers() {
        return users;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
