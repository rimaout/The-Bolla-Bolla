package view.users;

import view.utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UsersManagerView {
    private static UsersManagerView instance;

    private BufferedImage[] userPictures;

    private UsersManagerView() {
        loadUserPictures();
    }

    public static UsersManagerView getInstance() {
        if (instance == null) {
            instance = new UsersManagerView();
        }
        return instance;
    }

    // todo: move to view (maybe)
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
