package utilz;

import main.Game;
import entities.ZenChan;
import static utilz.Constants.EnemyConstants.ZEN_CHAN;

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
import java.util.stream.Collectors;

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
}