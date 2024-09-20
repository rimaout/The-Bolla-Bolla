package profiles;

import java.io.*;
import java.time.LocalDateTime;

public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L; //It serves as a version control identifier for the Serializable class. If you do not explicitly declare it, Java will generate one at runtime based on various aspects of the class. However, this generated value can change if the class structure changes, which can lead to InvalidClassException during deserialization if the class has been modified. If you anticipate making changes to the class that are not backward-compatible, you can change the serialVersionUID to a different value to indicate a new version of the class. However, for most cases, a simple value like 1L is sufficient.

    private String name;
    private int profilePictureIndex;
    private LocalDateTime lastSelectedTime;

    private int bestScore;
    private String bestScoreDate;
    private int playedGames;
    private int wonGames;

    public User(String name, int profilePictureIndex) {
        this.name = name;
        this.profilePictureIndex = profilePictureIndex;
    }

    public String getName() {
        return name;
    }

    public int getProfilePictureIndex() {
        return profilePictureIndex;
    }

    public LocalDateTime getLastSelectedTime() {
        return lastSelectedTime;
    }

    public int getBestScore() {
        return bestScore;
    }

    public String getBestScoreDate() {
        return bestScoreDate;
    }

    public int getPlayedGames() {
        return playedGames;
    }

    public int getWonGames() {
        return wonGames;
    }

    public void updateLastSelectedTime() {
        lastSelectedTime = LocalDateTime.now();
    }

    public void setScore(int score, String date) {
        if (score > bestScore) {
            bestScore = score;
            bestScoreDate = date;
        }
    }

    public void increaseGames() {
        playedGames++;
    }

    public void increaseWins() {
        wonGames++;
    }

    public void save(String fileName) {
        File directory = new File("res/users-data");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try {
            FileOutputStream fos = new FileOutputStream(directory + File.separator + fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(name);
            oos.writeObject(profilePictureIndex);
            oos.writeObject(lastSelectedTime);
            oos.writeObject(bestScore);
            oos.writeObject(bestScoreDate);
            oos.writeObject(playedGames);
            oos.writeObject(wonGames);

            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static User reed(String fileName) {
        try {
            // Open the file
            FileInputStream fis = new FileInputStream(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);

            // Read and Casting
            String name = (String) ois.readObject();
            int profilePictureIndex = (int) ois.readObject();
            int bestScore = (int) ois.readObject();
            String bestScoreDate = (String) ois.readObject();
            int playedGames = (int) ois.readObject();
            int wonGames = (int) ois.readObject();

            // Create the user
            User user = new User(name, profilePictureIndex);
            user.bestScore = bestScore;
            user.bestScoreDate = bestScoreDate;
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

    public int hashCode() {
        return name.hashCode();
    }
}
