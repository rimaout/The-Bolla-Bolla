package model.levels;

import model.bubbles.specialBubbles.BubbleGenerator;
import model.entities.EnemyModel;
import model.entities.MaitaModel;
import model.entities.ZenChanModel;
import model.utilz.Constants;
import view.utilz.LoadSave;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import static model.utilz.Constants.BubbleGenerator.GeneratorPosition.BOTTOM;
import static model.utilz.Constants.BubbleGenerator.GeneratorPosition.TOP;
import static model.utilz.Constants.BubbleGenerator.GeneratorType.LIGHTNING_BUBBLE;
import static model.utilz.Constants.BubbleGenerator.GeneratorType.WATER_BUBBLE;
import static model.utilz.Constants.Direction.LEFT;
import static model.utilz.Constants.Direction.RIGHT;
import static model.utilz.Constants.EnemyConstants.*;
import static model.utilz.Constants.EnemyConstants.MAITA_RIGHT;

public interface LevelImportMethods {

    static BufferedImage[] GetAllLevels() {
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

    static int[][] GetLevelData(BufferedImage img) {

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

    static ArrayList<EnemyModel> GetEnemies(BufferedImage img) {

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

    static Constants.Direction[][] GetWindsDirectionsData(BufferedImage img) {

        // wind currents are stored in an image, where each pixel represents a tile, the color of the pixel determines the tile (is the blue component of the pixel that determines the tile)
        // if the blu component is:
        //      0 or 100 -> no wind (don't move)
        //      1 or 101 -> wind to the left
        //      2 or 102 -> wind to the right
        //      3 or 103 -> wind up
        //      4 or 104 -> wind down

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

    static BubbleGenerator GetBubbleGenerator(BufferedImage img) {

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
            default  -> bubbleGenerator = new BubbleGenerator(Constants.BubbleGenerator.GeneratorType.NONE, Constants.BubbleGenerator.GeneratorPosition.NONE);
        }

        return bubbleGenerator;
    }
}
