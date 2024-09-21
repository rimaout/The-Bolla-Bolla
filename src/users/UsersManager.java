package users;

import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class UsersManager {
    private static UsersManager instance;

    private final HashMap<String, User> users;
    private User currentUser;

    BufferedImage[] userPictures;

    public UsersManager() {
        users = new HashMap<>();
        loadUsers();
        loadUserPictures();
    }

    public static UsersManager getInstance() {
        if (instance == null) {
            instance = new UsersManager();
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

    public void loadUserPictures() {
        userPictures = new BufferedImage[10];
        BufferedImage numbersSprite = LoadSave.GetSprite(LoadSave.USER_PICTURES);
        for (int i = 0; i < userPictures.length; i++) {
            userPictures[i] = numbersSprite.getSubimage(i * 18, 0, 18, 18);
        }
    }

    public BufferedImage getUserPicture(int userPictureIndex) {
        return userPictures[userPictureIndex];
    }

    public int getUserPicturesCount() {
        return userPictures.length;
    }

    public ArrayList<User> getUsers() {
        return new ArrayList<>(users.values());
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

    public Color getUserColor1(int profilePictureIndex) {
        return switch (profilePictureIndex) {
            case 0 -> new Color(0x5ce634);
            case 1 -> new Color(0x4ccedc);
            case 2 -> new Color(0x2463ce);
            case 3 -> new Color(0xfc8274);
            case 4 -> new Color(0x4ccedc);
            case 5 -> new Color(0x5ce634);
            case 6 -> new Color(0x5ce634);
            case 7 -> new Color(0x4ccedc);
            case 8 -> new Color(0x2463ce);
            case 9 -> new Color(0x5ce634);
            default -> Color.WHITE;
        };
    }

    public Color getUserColor2(int profilePictureIndex) {
        return switch (profilePictureIndex) {
            case 0 -> new Color(0xfc8274);
            case 1 -> new Color(0xc476fc);
            case 2 -> new Color(0xfc6ecc);
            case 3 -> new Color(0xb41e7c);
            case 4 -> new Color(0xc476fc);
            case 5 -> new Color(0xfc8274);
            case 6 -> new Color(0xfc8274);
            case 7 -> new Color(0xc476fc);
            case 8 -> new Color(0xfc6ecc);
            case 9 -> new Color(0xfc8274);
            default -> new Color(0xfc8274);
        };
    }
}
