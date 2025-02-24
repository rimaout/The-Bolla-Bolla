package com.rima.view.users;

import com.rima.view.utilz.Load;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The UsersManagerView class is responsible for managing user profile pictures and their associated colors.
 * It implements the singleton pattern to ensure a single instance.
 */
public class UsersManagerView {
    private static UsersManagerView instance;

    private BufferedImage[] userPictures;

    /**
     * Private constructor to implement singleton pattern.
     * Loads user profile pictures.
     */
    private UsersManagerView() {
        loadUserPictures();
    }

    /**
     * Returns the singleton instance of the UsersManagerView.
     *
     * @return the singleton instance
     */
    public static UsersManagerView getInstance() {
        if (instance == null) {
            instance = new UsersManagerView();
        }
        return instance;
    }

    /**
     * Loads user profile pictures from a sprite sheet.
     */
    public void loadUserPictures() {
        userPictures = new BufferedImage[10];
        BufferedImage numbersSprite = Load.GetSprite(Load.USER_PICTURES);
        for (int i = 0; i < userPictures.length; i++) {
            userPictures[i] = numbersSprite.getSubimage(i * 18, 0, 18, 18);
        }
    }

    /**
     * Returns the user profile picture at the specified index.
     *
     * @param userPictureIndex the index of the user profile picture
     * @return the user profile picture
     */
    public BufferedImage getUserPicture(int userPictureIndex) {
        return userPictures[userPictureIndex];
    }

    /**
     * Returns the total number of user profile pictures.
     *
     * @return the number of user profile pictures
     */
    public int getUserPicturesCount() {
        return userPictures.length;
    }

    /**
     * Returns the primary color associated with the specified profile picture index.
     *
     * @param profilePictureIndex the index of the profile picture
     * @return the primary color
     */
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

    /**
     * Returns the secondary color associated with the specified profile picture index.
     *
     * @param profilePictureIndex the index of the profile picture
     * @return the secondary color
     */
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