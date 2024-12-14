package model.entities;

import model.utilz.Constants;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public interface HelpMethods {

    static boolean IsTileRoof(int yTile) {
        return yTile < 4;
    }

    static boolean IsWall(int xTile, int yTile, int[][] levelData) {
        // Check 3 tiles (top, center, bottom) if at least 2 adjacent are solid, then it's a wall

        boolean top = IsTileSolid(xTile, yTile - 1, levelData);
        boolean center = IsTileSolid(xTile, yTile, levelData);
        boolean bottom = IsTileSolid(xTile, yTile +1 , levelData);

        return top && center || center && bottom;
    }

    static boolean IsTilePerimeterWall(int xTile) {
        return xTile < 2 || xTile > Constants.TILES_IN_WIDTH - 3;
    }

    static boolean IsPerimeterWallTile(float x) {
        int tileX = (int) (x / Constants.TILES_SIZE);
        return IsTilePerimeterWall(tileX);
    }

    static boolean IsTileInsideMap(int xTile, int yTile) {
        return xTile >= 0 && xTile < Constants.TILES_IN_WIDTH && yTile >= 0 && yTile < Constants.TILES_IN_HEIGHT;
    }

    static boolean IsTileSolid(int xTile, int yTile, int[][] levelData) {

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

    static boolean IsSolid(float x, float y, int[][] lvlData) {

        float xIndex = x / Constants.TILES_SIZE;
        float yIndex = y / Constants.TILES_SIZE;
        return IsTileSolid((int) xIndex, (int) yIndex, lvlData);
    }

    static boolean IsEntityInsideMap(Rectangle2D.Float hitbox) {
        return hitbox.x >= 0 && hitbox.x + hitbox.width < Constants.TILES_IN_WIDTH * Constants.TILES_SIZE &&
                hitbox.y >= 0 && hitbox.y + hitbox.height < Constants.TILES_IN_HEIGHT * Constants.TILES_SIZE;
    }

    static boolean IsEntityInsideSolid(Rectangle2D.Float hitbox, int[][] levelData) {
        // Check if the hitbox is inside a solid tile
        for (int i = 0; i < hitbox.width; i++)
            for (int j = 0; j < hitbox.height; j++)
                if (IsSolid(hitbox.x + i, hitbox.y + j, levelData))
                    return true;

        return false;
    }

    static boolean CanMoveHere(float x, float y, float width, float height, int[][] levelData) {
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

    static float GetEntityYPosAboveFloor(Rectangle2D.Float hitbox, float airSpeed, int[][] levelData) {
        // Falling (check touching floor)
        float distance = YAxisDistanceToFirstSolid((int) (hitbox.y + hitbox.height + airSpeed), (int) (hitbox.y + hitbox.height), (int) hitbox.x, levelData);
        return hitbox.y + distance;
    }

    static float YAxisDistanceToFirstSolid(int yStart, int yEnd, int x, int[][] lvlData) {
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

    static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
        // Check the pixel below bottom-left and bottom-right and bottom-center of the hitbox
        if (!IsSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData))
            if (!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData))
                if(!IsSolid(hitbox.x + hitbox.width / 2, hitbox.y + hitbox.height + 1, lvlData))
                    return false;

        return true;
    }

    static Point GetRandomPosition(int[][] levelData, Rectangle hitbox) {
        //This method find a random position in the level where the hitbox is not colliding with a solid tile
        int xRangeStart = (Constants.TILES_IN_WIDTH + 3)  * Constants.TILES_SIZE;
        int xRangeEnd = (Constants.TILES_IN_WIDTH - 3 * Constants.TILES_SIZE) - hitbox.width;

        int yRangeStart = 4 * Constants.TILES_SIZE;
        int yRangeEnd = (Constants.TILES_IN_HEIGHT - 3) * Constants.TILES_SIZE - hitbox.height;

        hitbox.x = (int) (Math.random() * (xRangeEnd - xRangeStart) + xRangeStart);
        hitbox.y = (int) (Math.random() * (yRangeEnd - yRangeStart) + yRangeStart);

        while (IsEntityInsideSolid(new Rectangle2D.Float(hitbox.x, hitbox.y, hitbox.width, hitbox.height), levelData)) {
            hitbox.x = (int) (Math.random() * (xRangeEnd - xRangeStart) + xRangeStart);
            hitbox.y = (int) (Math.random() * (yRangeEnd - yRangeStart) + yRangeStart);
        }

        return new Point(hitbox.x, hitbox.y);
    }
}