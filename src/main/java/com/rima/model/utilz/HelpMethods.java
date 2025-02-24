package com.rima.model.utilz;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Provides helper methods for various game-related calculations and checks.
 */
public interface HelpMethods {

    /**
     * Checks if the specified tile is a roof tile.
     *
     * @param yTile the y-coordinate of the tile
     * @return true if the tile is a roof tile, false otherwise
     */
    static boolean IsTileRoof(int yTile) {
        return yTile < 4;
    }

    /**
     * Checks if the specified tile is a wall.
     *
     * @param xTile the x-coordinate of the tile
     * @param yTile the y-coordinate of the tile
     * @param levelData the level data array
     * @return true if the tile is a wall, false otherwise
     */
    static boolean IsWall(int xTile, int yTile, int[][] levelData) {
        // Check 3 tiles (top, center, bottom) if at least 2 adjacent are solid, then it's a wall

        boolean top = IsTileSolid(xTile, yTile - 1, levelData);
        boolean center = IsTileSolid(xTile, yTile, levelData);
        boolean bottom = IsTileSolid(xTile, yTile +1 , levelData);

        return top && center || center && bottom;
    }

    /**
     * Checks if the specified tile is a perimeter wall tile.
     *
     * @param xTile the x-coordinate of the tile
     * @return true if the tile is a perimeter wall tile, false otherwise
     */
    static boolean IsTilePerimeterWall(int xTile) {
        return xTile < 2 || xTile > Constants.TILES_IN_WIDTH - 3;
    }

    /**
     * Checks if the specified x-coordinate is within a perimeter wall tile.
     *
     * @param x the x-coordinate
     * @return true if the x-coordinate is within a perimeter wall tile, false otherwise
     */
    static boolean IsPerimeterWallTile(float x) {
        int tileX = (int) (x / Constants.TILES_SIZE);
        return IsTilePerimeterWall(tileX);
    }

    /**
     * Checks if the specified tile is inside the map boundaries.
     *
     * @param xTile the x-coordinate of the tile
     * @param yTile the y-coordinate of the tile
     * @return true if the tile is inside the map boundaries, false otherwise
     */
    static boolean IsTileInsideMap(int xTile, int yTile) {
        return xTile >= 0 && xTile < Constants.TILES_IN_WIDTH && yTile >= 0 && yTile < Constants.TILES_IN_HEIGHT;
    }

    /**
     * Checks if the specified tile is solid.
     *
     * @param xTile the x-coordinate of the tile
     * @param yTile the y-coordinate of the tile
     * @param levelData the level data array
     * @return true if the tile is solid, false otherwise
     */
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

    /**
     * Checks if the specified coordinates are within a solid tile.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param lvlData the level data array
     * @return true if the coordinates are within a solid tile, false otherwise
     */
    static boolean IsSolid(float x, float y, int[][] lvlData) {

        float xIndex = x / Constants.TILES_SIZE;
        float yIndex = y / Constants.TILES_SIZE;
        return IsTileSolid((int) xIndex, (int) yIndex, lvlData);
    }

    /**
     * Checks if the specified hitbox is inside the map boundaries.
     *
     * @param hitbox the hitbox of the entity
     * @return true if the hitbox is inside the map boundaries, false otherwise
     */
    static boolean IsEntityInsideMap(Rectangle2D.Float hitbox) {
        return hitbox.x >= 0 && hitbox.x + hitbox.width < Constants.TILES_IN_WIDTH * Constants.TILES_SIZE &&
                hitbox.y >= 0 && hitbox.y + hitbox.height < Constants.TILES_IN_HEIGHT * Constants.TILES_SIZE;
    }

    /**
     * Checks if the specified hitbox is inside a solid tile.
     *
     * @param hitbox the hitbox of the entity
     * @param levelData the level data array
     * @return true if the hitbox is inside a solid tile, false otherwise
     */
    static boolean IsEntityInsideSolid(Rectangle2D.Float hitbox, int[][] levelData) {
        // Check if the hitbox is inside a solid tile
        for (int i = 0; i < hitbox.width; i++)
            for (int j = 0; j < hitbox.height; j++)
                if (IsSolid(hitbox.x + i, hitbox.y + j, levelData))
                    return true;

        return false;
    }

    /**
     * Checks if the entity can move to the specified position.
     *
     * @param x the x-coordinate of the position
     * @param y the y-coordinate of the position
     * @param width the width of the entity
     * @param height the height of the entity
     * @param levelData the level data array
     * @return true if the entity can move to the position, false otherwise
     */
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

    /**
     * Returns the y-coordinate position of the entity above the floor.
     *
     * @param hitbox the hitbox of the entity
     * @param airSpeed the air speed of the entity
     * @param levelData the level data array
     * @return the y-coordinate position above the floor
     */
    static float GetEntityYPosAboveFloor(Rectangle2D.Float hitbox, float airSpeed, int[][] levelData) {
        // Falling (check touching floor)
        float distance = YAxisDistanceToFirstSolid((int) (hitbox.y + hitbox.height + airSpeed), (int) (hitbox.y + hitbox.height), (int) hitbox.x, levelData);
        return hitbox.y + distance;
    }

    /**
     * Returns the distance to the first solid tile along the y-axis.
     *
     * @param yStart the starting y-coordinate
     * @param yEnd the ending y-coordinate
     * @param x the x-coordinate
     * @param lvlData the level data array
     * @return the distance to the first solid tile
     */
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

    /**
     * Checks if the entity is on the floor.
     *
     * @param hitbox the hitbox of the entity
     * @param lvlData the level data array
     * @return true if the entity is on the floor, false otherwise
     */
    static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
        // Check the pixel below bottom-left and bottom-right and bottom-center of the hitbox
        if (!IsSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData))
            if (!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData))
                if(!IsSolid(hitbox.x + hitbox.width / 2, hitbox.y + hitbox.height + 1, lvlData))
                    return false;

        return true;
    }

    /**
     * Returns a random position in the level where the hitbox is not colliding with a solid tile.
     *
     * @param levelData the level data array
     * @param hitbox the hitbox of the entity
     * @return a random position in the level
     */
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