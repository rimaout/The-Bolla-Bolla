package utilz;

import entities.ZenChan;
import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.Constants.EnemyConstants.ZEN_CHAN;

public class HelpMethods {

    public static boolean IsTileRoof(int yTile) {
        return yTile < 3;
    }

    public static boolean IsWall(int xTile, int yTile, int[][] levelData) {
        // Check 3 tiles (top, center, bottom) if at least 2 adjacent are solid, then it's a wall

        boolean top = IsTileSolid(xTile, yTile - 1, levelData);
        boolean center = IsTileSolid(xTile, yTile, levelData);
        boolean bottom = IsTileSolid(xTile, yTile +1 , levelData);

        return top && center || center && bottom;
    }

    public static boolean IsTilePerimeterWall(int xTile) {
        return xTile < 2 || xTile > Game.TILES_IN_WIDTH - 3;
    }

    public static boolean IsTileInsideMap(int xTile, int yTile) {
        return xTile >= 0 && xTile < Game.TILES_IN_WIDTH && yTile >= 0 && yTile < Game.TILES_IN_HEIGHT;
    }

    public static boolean IsTileSolid(int xTile, int yTile, int[][] levelData) {

        // Check if roof (roof is not solid if the tile is not a wall)
        if (IsTileRoof(yTile)) {
            if (IsTilePerimeterWall(xTile))
                return true;
            return false;
        }

        if (IsTileInsideMap(xTile, yTile)) {
            // Check tile type
            int value = levelData[yTile][xTile];
            if (value != 0)
                return true;
        }
        return false;
    }

    public static boolean IsSolid(float x, float y, int[][] lvlData) {

        float xIndex = x / Game.TILES_SIZE;
        float yIndex = y / Game.TILES_SIZE;
        return IsTileSolid((int) xIndex, (int) yIndex, lvlData);
    }

    public static boolean IsEntityInsideMap(Rectangle2D.Float hitbox) {
        return hitbox.x >= 0 && hitbox.x + hitbox.width < Game.TILES_IN_WIDTH * Game.TILES_SIZE &&
                hitbox.y >= 0 && hitbox.y + hitbox.height < Game.TILES_IN_HEIGHT * Game.TILES_SIZE;
    }

    public static boolean IsEntityInsideSolid(Rectangle2D.Float hitbox, int[][] levelData) {
        // Check if the hitbox is inside a solid tile
        for (int i = 0; i < hitbox.width; i++)
            for (int j = 0; j < hitbox.height; j++)
                if (IsSolid(hitbox.x + i, hitbox.y + j, levelData))
                    return true;

        return false;
    }

    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] levelData) {
        // Check top-left, top-right, bottom-left, bottom-right corners of the hitbox and center-left, center-right center-top and center-bottom

        boolean cornersPoints = !IsSolid(x, y, levelData) &&
                !IsSolid(x + width, y, levelData) &&
                !IsSolid(x, y + height, levelData) &&
                !IsSolid(x + width, y + height, levelData);

        boolean centerPoints = !IsSolid(x + width / 2, y, levelData) &&
                !IsSolid(x + width / 2, y + height, levelData) &&
                !IsSolid(x, y + height / 2, levelData) &&
                !IsSolid(x + width, y + height / 2, levelData);

        return cornersPoints && centerPoints;
    }

    public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed, int[][] levelData) {
        {
            if (xSpeed > 0) {
                // Right
                float distance = XAxisDistanceToFirstSolid((int) (hitbox.x + hitbox.width + xSpeed), (int) (hitbox.x + hitbox.width), (int) hitbox.y, levelData);
                return hitbox.x + distance;

            } else {
                // Left
                float distance = XAxisDistanceToFirstSolid((int) hitbox.x, (int) (hitbox.x + xSpeed), (int) hitbox.y, levelData);
                return hitbox.x - distance;
            }
        }
    }

    public static float XAxisDistanceToFirstSolid(int xStart, int xEnd, int y, int[][] lvlData) {
        //Output: distance to the first solid tile from xStart to xEnd

        float distance = 0;

        for (int i = 0; i <= xEnd - xStart; i++) {
            if (IsTileSolid(xStart + i, y, lvlData)) {
                distance = i;
                return distance;
            }
        }
        return distance;
    }

    public static float GetEntityYPosAboveFloor(Rectangle2D.Float hitbox, float airSpeed, int[][] levelData) {
        // Falling (check touching floor)
        float distance = YAxisDistanceToFirstSolid((int) (hitbox.y + hitbox.height + airSpeed), (int) (hitbox.y + hitbox.height), (int) hitbox.x, levelData);
        return hitbox.y + distance;
    }

    public static float YAxisDistanceToFirstSolid(int yStart, int yEnd, int x, int[][] lvlData) {
        //Output: distance to the first solid tile from yStart to yEnd

        float distance = 0;

        for (int i = 0; i <= yEnd - yStart; i++) {
            if (IsTileSolid(x , yStart + i, lvlData)) {
                distance = i;
                return distance;
            }
        }
        return distance;
    }

    public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
        // Check the pixel below bottom-left and bottom-right and bottom-center of the hitbox
        if (!IsSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData))
            if (!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData))
                if(!IsSolid(hitbox.x + hitbox.width / 2, hitbox.y + hitbox.height + 1, lvlData))
                    return false;

        return true;
    }

    public static boolean WillEntityBeOnFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData) {
        // Calculate if the entity will be on the floor after the move (check only x-axis)
        // Check the pixel below bottom-left and bottom-right and bottom-center of the hitbox
        return IsSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvlData) ||
                IsSolid(hitbox.x + hitbox.width + xSpeed, hitbox.y + hitbox.height + 1, lvlData) ||
                IsSolid(hitbox.x + hitbox.width / 2 + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
    }

    public static boolean WillEntityCollideWall(Rectangle2D.Float hitbox, float xSpeed) {

        // Calculate the horizontal index of the tile at the left and right side of the hitbox
        float newX = hitbox.x + xSpeed ;
        float horizontalTileLeftIndex = newX / Game.TILES_SIZE;
        float horizontalTileRightIndex = (newX + hitbox.width) / Game.TILES_SIZE;

        // Check if the hitbox is touching the leftmost or rightmost tiles in the game grid
        return horizontalTileLeftIndex < 2 || horizontalTileRightIndex > Game.TILES_IN_WIDTH - 2;
    }

    public static boolean IsAllTilesWalkable(int xStart, int xEnd, int y, int[][] lvlData) {
        for (int i = 0; i < xEnd - xStart; i++) {
            if (IsTileSolid(xStart + i, y, lvlData))
                return false;
            if (!IsTileSolid(xStart + i, y + 1, lvlData))
                return false;
        }
        return true;
    }

    public static boolean IsSightClear(int[][] levelData, Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox, int yTile) {
        int tileX1 = (int) (firstHitbox.x / Game.TILES_SIZE);
        int tileX2 = (int) (secondHitbox.x / Game.TILES_SIZE);

        if (tileX1 > tileX2)
            return IsAllTilesWalkable(tileX2, tileX1, yTile, levelData);
        else
            return IsAllTilesWalkable(tileX1, tileX2, yTile, levelData);

    }
    public static int[][] GetLevelData(BufferedImage img) {

        // level are stored in an image, where each pixel represents a tile, the color of the pixel determines the tile (is the red component of the pixel that determines the tile)

        int levelData[][] = new int[Game.TILES_IN_HEIGHT][Game.TILES_IN_WIDTH];

        for(int x = 0; x < img.getHeight(); x++)
            for (int y = 0; y < img.getWidth(); y++) {

                Color color = new Color(img.getRGB(y, x));
                int red = color.getRed();
                levelData[x][y] = red;
            }
        return levelData;
    }

    public static ArrayList<ZenChan> getZenChans(BufferedImage img) {

        // levels and enemies type and position are stored in an image, where each pixel represents a tile, the color of the pixel determines the tile (is the red component of the pixel that determines the tile)

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
}