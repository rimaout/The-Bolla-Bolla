package com.rima.model.users;

import com.rima.model.entities.PlayerModel;

import java.io.File;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * Manages user profiles in the game, including creating, selecting, saving, and loading users.
 */
public class UsersManagerModel {
    private static UsersManagerModel instance;

    private final HashMap<String, User> users;
    private User currentUser;

    /**
     * The directory where user profiles are saved.
     */
    private static final String PROFILE_DIR = System.getProperty("user.home") + File.separator + "The-Bolla-Bolla-Game-data" + File.separator + "users-data";

    /**
     * Private constructor to used for singleton pattern.
     * Initializes the users map and loads existing users from disk.
     */
    private UsersManagerModel() {
        this.users = new HashMap<>();
        loadUsers();
    }

    /**
     * Returns the singleton instance of the UsersManagerModel.
     *
     * @return the singleton instance
     */
    public static UsersManagerModel getInstance() {
        if (instance == null) {
            instance = new UsersManagerModel();
        }
        return instance;
    }

    /**
     * Creates a new user with the specified name and profile picture index.
     * <p>Saves the new user to disk and selects the profile.
     * <p>If a user with the same name already exists, the new user is not created.
     * <p>When created the user is selected as the current user.
     *
     * @param name the name of the new user
     * @param profilePictureIndex the index of the profile picture for the new user
     */
    public void createUser(String name, int profilePictureIndex) {
        if (!users.containsKey(name)) {
            User newUser = new User(name, profilePictureIndex);
            users.put(name, newUser);
            saveUsers();
            selectProfile(name);
        }
    }

    /**
     * Selects the user profile with the specified name.
     * Updates the last selected time and saves the users to disk.
     *
     * @param name the name of the user to select
     */
    public void selectProfile(String name) {
        if (users.containsKey(name)) {
            currentUser = users.get(name);
            currentUser.updateLastSelectedTime();
            saveUsers();
        }
    }

    /**
     * Saves all user profiles to disk.
     */
    public void saveUsers() {
        for (User user : users.values()) {
            user.save( user.getName() + ".dat", PROFILE_DIR);
        }
    }

    /**
     * Loads user profiles from disk.
     * If no users are found, creates a default user.
     */
    public void loadUsers() {
        File directory = new File(PROFILE_DIR);
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
            createUser("bob", 0); // default user
        }
    }

    /**
     * Updates the current user's information based on the game result.
     * Increases the number of games played and increases the number of wins if the player won.
     *
     * @param victory true if the player won the game, false otherwise
     */
    public void updateCurrentUserInfo(boolean victory) {
        currentUser.setScore(PlayerModel.getInstance().getPoints());

        if (victory)
            currentUser.increaseWins();

        currentUser.increaseGames();

        saveUsers();
    }

    /**
     * Sets the current user.
     *
     * @param currentUser the user to set as the current user
     */
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * Returns a list of all users, sorted by last selected time in descending order.
     *
     * @return the list of users
     */
    public ArrayList<User> getUsers() {

        // sort users by last selected time (used stream because of exam requirement)
        return (ArrayList<User>) users.values().stream()
                .sorted(Comparator.comparing(User::getLastSelectedTime).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Returns the current user.
     *
     * @return the current user
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Checks if a user with the specified name already exists.
     *
     * @param name the name to check
     * @return true if the user already exists, false otherwise
     */
    public boolean doesUserAlreadyExist(String name) {
        return users.containsKey(name);
    }
}