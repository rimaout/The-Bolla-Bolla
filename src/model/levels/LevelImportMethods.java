package model.levels;

import model.bubbles.specialBubbles.BubbleGenerator;
import model.entities.EnemyModel;
import model.entities.MaitaModel;
import model.entities.ZenChanModel;
import model.utilz.Constants;

import javax.imageio.ImageIO;         // used for level map data
import java.awt.*;                    // used for level map data
import java.awt.image.BufferedImage;  // used for level map data
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Comparator;

import static model.utilz.Constants.BubbleGenerator.GeneratorPosition.BOTTOM;
import static model.utilz.Constants.BubbleGenerator.GeneratorPosition.TOP;
import static model.utilz.Constants.BubbleGenerator.GeneratorType.LIGHTNING_BUBBLE;
import static model.utilz.Constants.BubbleGenerator.GeneratorType.WATER_BUBBLE;
import static model.utilz.Constants.Direction.LEFT;
import static model.utilz.Constants.Direction.RIGHT;
import static model.utilz.Constants.EnemyConstants.*;
import static model.utilz.Constants.EnemyConstants.MAITA_RIGHT;

/**
 * Provides methods to import level data from images.
 *
 * <p>The LevelImportMethods interface includes static methods to read level data, enemy data, wind direction data,
 * and bubble generator data from images. These methods are used to initialize the game levels.
 */
public interface LevelImportMethods {

    /**
     * Returns an array of BufferedImages representing all levels.
     *
     * <p>This method reads all level map images from the `/levelMaps` directory,
     * sorts them by file name, and loads them into an array of BufferedImages.
     *
     * @return an array of BufferedImages representing all levels
     */
    static BufferedImage[] GetAllLevels() {
        URL url = LevelImportMethods.class.getResource("/levelMaps");
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

    /**
     * Returns the level tiles data from the given image.
     *
     * <p>This method reads the level map from the provided image, where each pixel represents a tile.
     * The red component of each pixel determines the tile type.
     * <ul>
     *   <li>0: Empty tile</li>
     *   <li>1: Level 1 tile type</li>
     *   <li>2: Level 2 tile type</li>
     *   <li>...</li>
     * </ul>
     *
     * @param img the image representing the level
     * @return a 2D array of integers representing the level tiles data
     */
    static int[][] GetLevelTilesData(BufferedImage img) {

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

    /**
     * Returns a list of enemies from the given level image.
     *
     * <p>This method reads the enemy data from the provided image, where each pixel represents a tile.
     * The green component of each pixel determines the type and direction of the enemy.
     * <ul>
     *   <li>0: No enemy</li>
     *   <li>1: Zen-Chan facing left</li>
     *   <li>2: Zen-Chan facing right</li>
     *   <li>3: Maita facing left</li>
     *   <li>4: Maita facing right</li>
     * </ul>
     *
     * @param img the image representing the level
     * @return a list of EnemyModel objects representing the enemies
     */
    static ArrayList<EnemyModel> GetEnemies(BufferedImage img) {

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

    /**
     * Returns the wind directions data from the given image.
     *
     * <p>This method reads the wind currents data from the provided image, where each pixel represents a tile.
     * The blue component of each pixel determines the wind direction.
     * <ul>
     *   <li>0 or 100: No wind (don't move)</li>
     *   <li>1 or 101: Wind to the left</li>
     *   <li>2 or 102: Wind to the right</li>
     *   <li>3 or 103: Wind up</li>
     *   <li>4 or 104: Wind down</li>
     * </ul>
     *
     * @param img the image representing the level
     * @return a 2D array of Direction enums representing the wind directions data
     */
    static Constants.Direction[][] GetWindsDirectionsData(BufferedImage img) {

        Constants.Direction[][] windDirectionData = new Constants.Direction[Constants.TILES_IN_HEIGHT][Constants.TILES_IN_WIDTH];

        for(int x = 0; x < img.getHeight(); x++)
            for (int y = 0; y < img.getWidth(); y++) {

                Color color = new Color(img.getRGB(y, x));
                int blue = color.getBlue();

                if (blue >= 100)
                    blue -= 100;

                Constants.Direction direction;

                switch (blue) {
                    case 1 -> direction  = Constants.Direction.LEFT;
                    case 2 -> direction  = Constants.Direction.RIGHT;
                    case 3 -> direction  = Constants.Direction.UP;
                    case 4 -> direction  = Constants.Direction.DOWN;
                    default -> direction = Constants.Direction.NONE;
                }

                windDirectionData[x][y] = direction;
            }

        return windDirectionData;
    }

    /**
     * Returns the bubble generator data from the given image.
     *
     * <p>This method reads the bubble generator data from the top left corner pixel of the provided image.
     * The green component of the pixel determines the type and position of the bubble generator.
     * <ul>
     *   <li>101: Water bubble generator on top of the level</li>
     *   <li>102: Water bubble generator on the bottom of the level</li>
     *   <li>103: Lightning bubble generator on top of the level</li>
     *   <li>104: Lightning bubble generator on the bottom of the level</li>
     *   <li>Any other value: No bubble generators</li>
     * </ul>
     *
     * @param img the image representing the level
     * @return a BubbleGenerator object representing the bubble generator data
     */
    static BubbleGenerator GetBubbleGenerator(BufferedImage img) {

        Color color = new Color(img.getRGB(0, 0));
        int green = color.getGreen();
        BubbleGenerator bubbleGenerator;

        switch (green) {
            case 101 -> bubbleGenerator = new BubbleGenerator(WATER_BUBBLE, TOP);
            case 102 -> bubbleGenerator = new BubbleGenerator(WATER_BUBBLE, BOTTOM);
            case 103 -> bubbleGenerator = new BubbleGenerator(LIGHTNING_BUBBLE, TOP);
            case 104 -> bubbleGenerator = new BubbleGenerator(LIGHTNING_BUBBLE, BOTTOM);
            default  -> bubbleGenerator = new BubbleGenerator(Constants.BubbleGenerator.GeneratorType.NONE, Constants.BubbleGenerator.GeneratorPosition.NONE);
        }

        return bubbleGenerator;
    }
}