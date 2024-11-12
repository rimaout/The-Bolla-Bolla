package model.users;

import main.Game;
import model.utilz.LoadSave;

import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.awt.image.BufferedImage;

public class UsersManager {
    private static UsersManager instance;
    private final Game game;

    private final HashMap<String, User> users;
    private User currentUser;

    BufferedImage[] userPictures;

    private UsersManager(Game game) {
        this.users = new HashMap<>();
        this.game = game;
        loadUsers();
        loadUserPictures();
    }

    public static UsersManager getInstance(Game game) {
        if (instance == null) {
            instance = new UsersManager(game);
        }
        return instance;
    }

    public static UsersManager getInstance() {
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
        currentUser.setScore(game.getPlaying().getPlayerOneModel().getPoints());

        if (victory)
            currentUser.increaseWins();
        else
            currentUser.increaseGames();

        saveUsers();
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
