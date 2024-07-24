package Utillz;

import main.Game;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class LoadSave {
    // Sprites
    public static final String PLAYER_SPRITE = "/sprites/Bud.png";
    public static final String LEVEL_SPRITE = "/sprites/LevelTiles.png";
    public static final String LEVEL16_SPRITE = "/sprites/tile16.png";

    // Levels
    public static final String LEVEL1_DATA = "/levels/level1.png";
    public static final String LEVEL1_16_DATA = "/levels/level1_16.png";;

    // this class will only use static methods, so we don't need a constructor

    public static BufferedImage getSprite(String spriteFile) {
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

    public static int[][] getLevelData() {

        // level are stored in an image, where each pixel represents a tile, the color of the pixel determines the tile (is the red component of the pixel that determines the tile)

        int levelData[][] = new int[Game.TILES_IN_HEIGHT][Game.TILES_IN_WIDTH];
        BufferedImage img = getSprite(LEVEL1_16_DATA);

        for(int x=0; x < img.getHeight(); x++)
            for (int y = 0; y < img.getWidth(); y++) {

                Color color = new Color(img.getRGB(y, x));
                int red = color.getRed();
                if (red > 125) {
                    red = 0;
                }
                levelData[x][y] = red;
            }
        return levelData;
    }
}