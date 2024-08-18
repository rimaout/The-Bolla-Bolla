package utilz;

import entities.ZenChan;
import main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import static utilz.Constants.EnemyConstants.ZEN_CHAN;

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
    public static final String BUBBLE_BUD_SPRITE = "/sprites/bud_bubble.png";

    // Levels
    public static final String LEVEL1_DATA = "/levelMaps/level1.png";
    public static final String LEVEL2_DATA = "/levelMaps/level2.png";
    public static final String LEVEL3_DATA = "/levelMaps/level3.png";
    public static final String LEVEL4_DATA = "/levelMaps/level4.png";
    public static final String LEVEL5_DATA = "/levelMaps/level5.png";
    public static final String LEVEL6_DATA = "/levelMaps/level6.png";

    // IMAGES
    public static final String GAME_LOGO = "/images/bubbleBobble_Logo.png";
    public static final String PAUSE_BACKGROUND = "/images/pause_menu.png";
    public static final String LEVEL_COMPLETED_BACKGROUND = "/images/level_completed_menu.png";

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

    public static BufferedImage[] GetAllLevels() {
        URL url = LoadSave.class.getResource("/levelMaps");
        File file = null;

        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        File[] files = file.listFiles();
        File[] filesSorted = new File[files.length];

        Arrays.stream(files).sorted(Comparator.comparing(File::getName)).toList().toArray(filesSorted);

        BufferedImage[] levelImages = new BufferedImage[filesSorted.length];

        for (int i = 0; i < levelImages.length; i++) {
            try {
                levelImages[i] = ImageIO.read(filesSorted[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return levelImages;
    }

    public static int[][] GetLevelData(BufferedImage img) {

        // level are stored in an image, where each pixel represents a tile, the color of the pixel determines the tile (is the red component of the pixel that determines the tile)

        int levelData[][] = new int[Game.TILES_IN_HEIGHT][Game.TILES_IN_WIDTH];

        for(int x = 0; x < img.getHeight(); x++)
            for (int y = 0; y < img.getWidth(); y++) {

                Color color = new Color(img.getRGB(y, x));
                int red = color.getRed();
                if (red >= 150)
                    red = 0;

                levelData[x][y] = red;
            }
        return levelData;
    }

    public static ArrayList<ZenChan> GetZenChans(BufferedImage img) {

        //enemies type and position and direction are stored in an image, where each pixel represents a tile, the color of the pixel determines the tile info
        // the green component of the pixel determines the type of enemy and facing direction
        // if the green component is:
        //      0 -> no enemy
        //      1 -> Zen-Chan facing left
        //      2 -> Zen-Chan facing right

        ArrayList<ZenChan> list = new ArrayList<>();

        for(int x = 0; x < img.getHeight(); x++)
            for (int y = 0; y < img.getWidth(); y++) {

                Color color = new Color(img.getRGB(y, x));
                int green = color.getGreen();
                if(green >= 10)
                    green = 0;

                if (green == ZEN_CHAN) {
                    list.add(new ZenChan(y * Game.TILES_SIZE, x * Game.TILES_SIZE));
                }
            }

        return list;
    }

    public static int[][] GetWindsCurrentDirections(BufferedImage img) {

        // wind currents are stored in an image, where each pixel represents a tile, the color of the pixel determines the tile (is the blue component of the pixel that determines the tile)
        // if the blu component is:
        //      0 or 100 -> no wind (don't move)
        //      1 or 101 -> wind to the left
        //      2 or 102 -> wind to the right
        //      3 or 103 -> wind up
        //      4 or 104 -> wind down

        int[][] windCurrentData = new int[Game.TILES_IN_HEIGHT][Game.TILES_IN_WIDTH];

        for(int x = 0; x < img.getHeight(); x++)
            for (int y = 0; y < img.getWidth(); y++) {

                Color color = new Color(img.getRGB(y, x));
                int blue = color.getBlue();

                if (blue >= 100)
                    blue -= 100;

                windCurrentData[x][y] = blue;
            }

        return windCurrentData;
    }
}