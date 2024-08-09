package Utillz;

import main.Game;
import entities.ZenChan;
import static Utillz.Constants.EnemyConstants.ZEN_CHAN;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class LoadSave {
    // Sprites
    public static final String PLAYER_SPRITE = "/sprites/Bud.png";
    public static final String ZEN_CHAN_ENEMY_SPRITE = "/sprites/Zen-Chan.png";
    public static final String LEVEL_TILES_SPRITE = "/sprites/level_tiles.png";
    public static final String NUMBERS_TILES_SPRITE = "/sprites/numbers_tiles.png";
    public static final String MENU_BUTTONS_SPRITE = "/sprites/menu_buttons.png";
    public static final String SOUND_BUTTONS_SPRITE = "/sprites/sound_buttons.png";
    public static final String URM_BUTTONS_SPRITE = "/sprites/urm_buttons.png";
    public static final String VOLUME_BUTTON_SPRITE = "/sprites/volume_button.png";

    // Levels
    public static final String LEVEL1_DATA = "/levels/level1.png";
    public static final String LEVEL2_DATA = "/levels/level2.png";
    public static final String LEVEL3_DATA = "/levels/level3.png";
    public static final String LEVEL4_DATA = "/levels/level4.png";
    public static final String LEVEL5_DATA = "/levels/level5.png";
    public static final String LEVEL6_DATA = "/levels/level6.png";

    // IMAGES
    public static final String GAME_LOGO = "/images/bubbleBobble_Logo.png";
    public static final String PAUSE_BACKGROUND = "/images/pause_menu.png";

    // this class will only use static methods, so we don't need a constructor

    public static BufferedImage GetSprite(String spriteFile) {
        InputStream is = LoadSave.class.getResourceAsStream(spriteFile);
        BufferedImage img = null;

        // it's a good practice ti use try-catch when using ImageIO.read to avoid runtime exceptions particularly when the image is not found
        try {
            img = ImageIO.read(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {

            try {
                is.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return img;
    }

    public static ArrayList<ZenChan> getZenChans() {

        // levels and enemies type and position are stored in an image, where each pixel represents a tile, the color of the pixel determines the tile (is the red component of the pixel that determines the tile)

        BufferedImage img = GetSprite(LEVEL1_DATA);
        ArrayList<ZenChan> list = new ArrayList<>();

        for(int x = 0; x < img.getHeight(); x++)
            for (int y = 0; y < img.getWidth(); y++) {

                Color color = new Color(img.getRGB(y, x));
                int green = color.getGreen();
                if (green > 125) {
                    green = 0;
                }

                if (green == ZEN_CHAN) {
                    list.add(new ZenChan(y * Game.TILES_SIZE, x * Game.TILES_SIZE));
                }
            }

        return list;
    }

    public static int[][] GetLevelData() {

        // level are stored in an image, where each pixel represents a tile, the color of the pixel determines the tile (is the red component of the pixel that determines the tile)

        BufferedImage img = GetSprite(LEVEL1_DATA);
        int levelData[][] = new int[Game.TILES_IN_HEIGHT][Game.TILES_IN_WIDTH];

        for(int x = 0; x < img.getHeight(); x++)
            for (int y = 0; y < img.getWidth(); y++) {

                Color color = new Color(img.getRGB(y, x));
                int red = color.getRed();
                levelData[x][y] = red;
            }
        return levelData;
    }
}