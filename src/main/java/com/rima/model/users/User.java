package com.rima.model.users;

import java.io.*;

/**
 * Represents a user in the game. This class contains all the information about the user and provides methods to access and save this information to disk using the Serializable interface.
 */
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L; //It serves as a version control identifier for the Serializable class. If you do not explicitly declare it, Java will generate one at runtime based on various aspects of the class. However, this generated value can change if the class structure changes, which can lead to InvalidClassException during deserialization if the class has been modified. If you anticipate making changes to the class that are not backward-compatible, you can change the serialVersionUID to a different value to indicate a new version of the class. However, for most cases, a simple value like 1L is sufficient.

    private String name = "";
    private int profilePictureIndex = 0;

    private int bestScore = 0;
    private int playedGames = 0;
    private int wonGames = 0;

    private long lastSelectedTime = System.currentTimeMillis();

    /**
     * Constructs a User with the specified name and profile picture index.
     *
     * @param name the name of the user
     * @param profilePictureIndex the index of the user's profile picture
     */
    public User(String name, int profilePictureIndex) {
        this.name = name;
        this.profilePictureIndex = profilePictureIndex;
    }

    /**
     * Saves the user data to a file.
     *
     * @param fileName the name of the file to save the user data to
     */
    public void save(String fileName, String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try {
            FileOutputStream fos = new FileOutputStream(directory + File.separator + fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(name);
            oos.writeObject(profilePictureIndex);
            oos.writeObject(bestScore);
            oos.writeObject(playedGames);
            oos.writeObject(wonGames);

            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads the user data from a file and returns a User object.
     *
     * @param fileName the name of the file to read the user data from
     * @return the User object read from the file, or null if an error occurs
     */
    public static User reed(String fileName) {
        try {
            // Open the file
            FileInputStream fis = new FileInputStream(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);

            // Read and Casting
            String name = (String) ois.readObject();
            int profilePictureIndex = (int) ois.readObject();
            int bestScore = (int) ois.readObject();
            int playedGames = (int) ois.readObject();
            int wonGames = (int) ois.readObject();

            // Create the user
            User user = new User(name, profilePictureIndex);
            user.bestScore = bestScore;
            user.playedGames = playedGames;
            user.wonGames = wonGames;

            ois.close();

            return user;
        }
        catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns the hash code of the user, based on the user's name (username is unique to each user).
     *
     * @return the hash code of the user
     */
    public int hashCode() {
        return name.hashCode();
    }


    // --------- Setters Methods --------- //

    /**
     * Updates the last selected time of the user to the current system time.
     */
    public void updateLastSelectedTime() {
        lastSelectedTime = System.currentTimeMillis();
    }

    /**
     * Sets the score of the user. Updates the score if the new score is higher than the best score.
     *
     * @param score the new score of the user
     */
    public void setScore(int score) {
        if (score > bestScore)
            bestScore = score;
    }

    /**
     * Increases the number of games played by the user by one.
     */
    public void increaseGames() {
        playedGames++;
    }

    /**
     * Increases the number of games won by the user by one.
     */
    public void increaseWins() {
        wonGames++;
    }

    // --------- Getters Methods --------- //

    /**
     * Returns the name of the user.
     *
     * @return the name of the user
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the profile picture index of the user.
     *
     * @return the profile picture index of the user
     */
    public int getProfilePictureIndex() {
        return profilePictureIndex;
    }

    /**
     * Returns the best score of the user.
     *
     * @return the best score of the user
     */
    public int getBestScore() {
        return bestScore;
    }

    /**
     * Returns the number of games played by the user.
     *
     * @return the number of games played by the user
     */
    public int getPlayedGames() {
        return playedGames;
    }

    /**
     * Returns the number of games won by the user.
     *
     * @return the number of games won by the user
     */
    public int getWonGames() {
        return wonGames;
    }

    /**
     * Returns the last selected time of the user.
     *
     * @return the last selected time of the user
     */
    public long getLastSelectedTime() {
        return lastSelectedTime;
    }
}