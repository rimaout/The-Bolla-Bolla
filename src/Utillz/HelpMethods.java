package Utillz;

import main.Game;

import java.awt.geom.Rectangle2D;

public class HelpMethods {

    private static boolean IsSolid(float x, float y, int[][] lvlData) {
        int maxWidth = lvlData[0].length * Game.TILES_SIZE;
        if (x < 0 || x >= maxWidth)
            return true;
        if (y < 0 || y >= Game.GAME_HEIGHT)
            return true;

        float xIndex = x / Game.TILES_SIZE;
        float yIndex = y / Game.TILES_SIZE;

        return IsTileSolid((int) xIndex, (int) yIndex, lvlData);
    }

    // DOVREBBE FUNZIONARE
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

    public static boolean IsTileSolid(int xTile, int yTile, int[][] lvlData) {

        // Check if roof (roof is not solid)
        if (yTile < 3)
            return false;

        // Check tile type
        int value = lvlData[yTile][xTile];
        if (value >= 10 || value < 0 || value != 0)
            return true;

        return false;
    }

//    public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed) {
//        int currentTile = (int) (hitbox.x / Game.TILES_SIZE);
//        if (xSpeed > 0) {
//            // Right
//            int tileXPos = currentTile * Game.TILES_SIZE;
//            int xOffset = (int) (Game.TILES_SIZE - hitbox.width);
//            return tileXPos + xOffset - 1;
//        } else
//            // Left
//            return currentTile * Game.TILES_SIZE;
//    }

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

//    public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed) {
//        int currentTile = (int) (hitbox.y / Game.TILES_SIZE);
//        if (airSpeed > 0) {
//            // Falling - touching floor
//            int tileYPos = currentTile * Game.TILES_SIZE;
//            int yOffset = (int) (Game.TILES_SIZE - hitbox.height);
//            return tileYPos + yOffset - 1;
//        } else
//            // Jumping
//            return currentTile * Game.TILES_SIZE;
//
//    }

    public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed, int[][] levelData) {
        if (airSpeed > 0) {
            // Falling (check touching floor)
            float distance = YAxisDistanceToFirstSolid((int) (hitbox.y + hitbox.height + airSpeed), (int) (hitbox.y + hitbox.height), (int) hitbox.x, levelData);
            return hitbox.y + distance;

        } else {
            // Jumping (check touching roof)
            float distance = YAxisDistanceToFirstSolid((int) hitbox.y, (int) (hitbox.y + airSpeed), (int) hitbox.x, levelData);
            return hitbox.y - distance;
        }
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

    // DOVREBBE FUNZIONARE
    public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
        // Check the pixel below bottom-left and bottom-right and bottom-center of the hitbox
        if (!IsSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData))
            if (!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData))
                if(!IsSolid(hitbox.x + hitbox.width / 2, hitbox.y + hitbox.height + 1, lvlData))
                    return false;

        return true;
    }

    // DOVREBBE FUNZIONARE
    public static boolean WillEntityBeOnFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData) {
        // Calculate if the entity will be on the floor after the move (check only x-axis)
        // Check the pixel below bottom-left and bottom-right and bottom-center of the hitbox
        return IsSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvlData) ||
                IsSolid(hitbox.x + hitbox.width + xSpeed, hitbox.y + hitbox.height + 1, lvlData) ||
                IsSolid(hitbox.x + hitbox.width / 2 + xSpeed, hitbox.y + hitbox.height + 1, lvlData);

    }

    // DOVREBBE FUNZIONARE
    public static boolean WillEntityCollideWall(Rectangle2D.Float hitbox, float xSpeed) {

        // Calculate the horizontal index of the tile at the left and right side of the hitbox
        float newX = hitbox.x + xSpeed ;
        float horizontalTileLeftIndex = newX / Game.TILES_SIZE;
        float horizontalTileRightIndex = (newX + hitbox.width) / Game.TILES_SIZE;

        // Check if the hitbox is touching the leftmost or rightmost tiles in the game grid
        if (horizontalTileLeftIndex <= 2 || horizontalTileRightIndex >= Game.TILES_IN_WIDTH - 2)
            return true;

        return false;
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

}