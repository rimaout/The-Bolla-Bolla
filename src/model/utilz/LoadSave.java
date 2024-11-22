package model.utilz;

import bubbles.specialBubbles.BubbleGenerator;
import model.entities.EnemyModel;
import model.entities.MaitaModel;
import model.entities.ZenChanModel;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
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

import static model.utilz.Constants.Direction;
import static model.utilz.Constants.Direction.*;
import static model.utilz.Constants.EnemyConstants.*;
import static model.utilz.Constants.BubbleGenerator.GeneratorType;
import static model.utilz.Constants.BubbleGenerator.GeneratorType.*;
import static model.utilz.Constants.BubbleGenerator.GeneratorPosition;
import static model.utilz.Constants.BubbleGenerator.GeneratorPosition.*;

public class LoadSave {
    // Sprites
    public static final String PLAYER_SPRITE = "/sprites/Bud.png";
    public static final String PLAYER_TRANSITION_SPRITE = "/sprites/Bud_level_transition.png";
    public static final String ZEN_CHAN_ENEMY_SPRITE = "/sprites/Zen-Chan.png";
    public static final String MAITA_ENEMY_SPRITE = "/sprites/Maita.png";
    public static final String SKEL_MONSTA_ENEMY_SPRITE = "/sprites/Skel-Monsta.png";
    public static final String LEVEL_TILES_SPRITE = "/sprites/level_tiles.png";
    public static final String NUMBERS_TILES_SPRITE = "/sprites/numbers_tiles.png";
    public static final String MENU_BUTTONS_SPRITE = "/sprites/menu_buttons.png";
    public static final String BUBBLE_BUD_SPRITE = "/sprites/Bud_bubble.png";
    public static final String BUBBLE_TWINKLE = "/sprites/bubble-twinkle.png";
    public static final String ITEM_BUBBLE_REWARD_SPRITE = "/sprites/item_bubble_reward.png";
    public static final String ITEM_POWER_UP_SPRITE = "/sprites/power_ups.png";
    public static final String ITEM_DESPAWN_SPRITE = "/sprites/item_despawn.png";
    public static final String BUD_SMALL_POINTS_SPRITE = "/sprites/Bud_small_points.png";
    public static final String BUD_BIG_POINTS_SPRITE = "/sprites/Bud_big_points.png";
    public static final String PROJECTILE_FIREBALL_SPRITE = "/sprites/fireball.png";
    public static final String WATER_BUBBLE_SPRITE = "/sprites/water_bubble.png";
    public static final String LIGHTNING_BUBBLE_SPRITE = "/sprites/lightning_bubble.png";
    public static final String BUD_BUBBLE_PROJECTILE_SPRITE = "/sprites/bud_bubble_projectile.png";
    public static final String USER_PICTURES = "/sprites/user_pictures.png";
    public static final String ARROWS_LEFT_RIGHT = "/sprites/arrows-left-right.png";
    public static final String ARROWS_UP_DOWN = "/sprites/arrows-up-down.png";

    // IMAGES
    public static final String GAME_LOGO = "/images/logo.png";
    public static final String HURRY_IMAGE = "/images/hurry_image.png";
    public static final String QUESTION_MARK_IMAGE = "/images/question-mark.png";
    public static final String GAME_ICON = "/images/game-icon.png";

    // FONTS
    public static final String NES_FONT = "/fonts/nintendo-nes-font.ttf";
    public static final String RETRO_GAMING_FONT = "/fonts/retro-gaming.ttf";

    // AUDIO

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

    private static Font LoadFont(String fontFile) {
        Font font = null;
        try {
            InputStream is = LoadSave.class.getResourceAsStream(fontFile);
            font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(22f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        return font;
    }

    public static Font GetNesFont() {
        return LoadFont(NES_FONT);
    }

    public static Font GetRetroGamingFont() {
        return LoadFont(RETRO_GAMING_FONT);
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

    public static Clip GetAudio(String audioName) {
        URL url = LoadSave.class.getResource("/audioFiles/" + audioName + ".wav");
        AudioInputStream audio;

        try {
            audio = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            return clip;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static int[][] GetLevelData(BufferedImage img) {

        // level are stored in an image, where each pixel represents a tile, the color of the pixel determines the tile (is the red component of the pixel that determines the tile)

        int levelData[][] = new int[Constants.TILES_IN_HEIGHT][Constants.TILES_IN_WIDTH];

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

    public static ArrayList<EnemyModel> GetEnemies(BufferedImage img) {

        //enemies type and position and direction are stored in an image, where each pixel represents a tile, the color of the pixel determines the tile info
        // the green component of the pixel determines the type of enemy and facing direction
        // if the green component is:
        //      0 -> no enemy
        //      1 -> Zen-Chan facing left
        //      2 -> Zen-Chan facing right
        //      3 -> Maita facing left
        //      4 -> Maita facing right

        ArrayList<EnemyModel> list = new ArrayList<>();

        for(int x = 0; x < img.getHeight(); x++)
            for (int y = 0; y < img.getWidth(); y++) {

                Color color = new Color(img.getRGB(y, x));
                int green = color.getGreen();
                if(green >= 10)
                    green = 0;

                switch (green) {
                    case ZEN_CHAN_LEFT -> list.add(new ZenChanModel(y * Constants.TILES_SIZE, x * Constants.TILES_SIZE, LEFT));
                    case ZEN_CHAN_RIGHT -> list.add(new ZenChanModel(y * Constants.TILES_SIZE, x * Constants.TILES_SIZE, RIGHT));
                    case MAITA_LEFT -> list.add(new MaitaModel(y * Constants.TILES_SIZE, x * Constants.TILES_SIZE, LEFT));
                    case MAITA_RIGHT -> list.add(new MaitaModel(y * Constants.TILES_SIZE, x * Constants.TILES_SIZE, RIGHT));
                }
            }
        return list;
    }

    public static Direction[][] GetWindsDirectionsData(BufferedImage img) {

        // wind currents are stored in an image, where each pixel represents a tile, the color of the pixel determines the tile (is the blue component of the pixel that determines the tile)
        // if the blu component is:
        //      0 or 100 -> no wind (don't move)
        //      1 or 101 -> wind to the left
        //      2 or 102 -> wind to the right
        //      3 or 103 -> wind up
        //      4 or 104 -> wind down

        Direction[][] windDirectionData = new Direction[Constants.TILES_IN_HEIGHT][Constants.TILES_IN_WIDTH];

        for(int x = 0; x < img.getHeight(); x++)
            for (int y = 0; y < img.getWidth(); y++) {

                Color color = new Color(img.getRGB(y, x));
                int blue = color.getBlue();

                if (blue >= 100)
                    blue -= 100;

                Direction direction;

                switch (blue) {
                    case 1 -> direction  = Direction.LEFT;
                    case 2 -> direction  = Direction.RIGHT;
                    case 3 -> direction  = Direction.UP;
                    case 4 -> direction  = Direction.DOWN;
                    default -> direction = Direction.NONE;
                }

                windDirectionData[x][y] = direction;
            }

        return windDirectionData;
    }

    public static BubbleGenerator GetBubbleGenerator(BufferedImage img) {

        // bubble generators info are stored in the top left corner pixel of the image
        // if the green component is:
        //      101 -> water bubble generator on top of the level
        //      102 -> water bubble generator on the bottom of the level
        //      103 -> lightning bubble generator on top of the level
        //      104 -> lightning bubble generator on the bottom of the level
        //      any other value -> no bubble generators

        Color color = new Color(img.getRGB(0, 0));
        int green = color.getGreen();
        BubbleGenerator bubbleGenerator;

        switch (green) {
            case 101 -> bubbleGenerator = new BubbleGenerator(WATER_BUBBLE, TOP);
            case 102 -> bubbleGenerator = new BubbleGenerator(WATER_BUBBLE, BOTTOM);
            case 103 -> bubbleGenerator = new BubbleGenerator(LIGHTNING_BUBBLE, TOP);
            case 104 -> bubbleGenerator = new BubbleGenerator(LIGHTNING_BUBBLE, BOTTOM);
            default  -> bubbleGenerator = new BubbleGenerator(GeneratorType.NONE, GeneratorPosition.NONE);
        }

        return bubbleGenerator;
    }
}