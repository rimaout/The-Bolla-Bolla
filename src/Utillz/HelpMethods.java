package Utillz;

import main.Game;

import java.awt.geom.Rectangle2D;

public class HelpMethods {
    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] levelData) {

        return !IsSolid(x, y, levelData) &&
                !IsSolid(x + width, y, levelData) &&
                !IsSolid(x, y + height, levelData) &&
                !IsSolid(x + width, y + height, levelData);
    }

    private static boolean IsSolid(float x, float y, int[][] levelData) {
        if (x < 0 || x >= Game.GAME_WIDTH) {
            return true;
        }
        if (y < 0 || y >= Game.GAME_HEIGHT) {
            return true;
        }

        float xIndex = x / Game.TILES_SIZE;
        float yIndex = y / Game.TILES_SIZE;

        int value = levelData[(int) yIndex][(int) xIndex];

        if (value != 0) {
            // tile 0 is the only non-solid tile
            return true;
        }

        return false;
    }

    public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed) {
        int currentTile = (int) (hitbox.x / Game.TILES_SIZE);
        if (xSpeed > 0) {
            // Right
            int tileXPos = currentTile * Game.TILES_SIZE;
            int xOffset = (int) (Game.TILES_SIZE - hitbox.width);
            return tileXPos + xOffset - 1;
        } else
            // Left
            return currentTile * Game.TILES_SIZE;
    }

    public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed) {
        int currentTile = (int) (hitbox.y / Game.TILES_SIZE);
        if (airSpeed > 0) {
            // Falling - touching floor
            int tileYPos = currentTile * Game.TILES_SIZE;
            int yOffset = (int) (Game.TILES_SIZE - hitbox.height);
            return tileYPos + yOffset - 1;
        } else
            // Jumping
            return currentTile * Game.TILES_SIZE;

    }

    public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
        // Check the pixel below bottomleft and bottomright
        if (!IsSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData))
            if (!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData))
                return false;

        return true;

    }

    public static boolean WillEntityCollideRoof(Rectangle2D.Float hitbox, float ySpeed) {
        // Calculate the vertical index of the tile based on the y coordinate
        float newY = hitbox.y + ySpeed;
        float verticalTileIndex = newY / Game.TILES_SIZE;

        // If the vertical index is less than 1, it is considered a roof tile
        if (verticalTileIndex < 1)
            return true;

        return false;
    }

    public static boolean WillEntityCollideWall(Rectangle2D.Float hitbox, float xSpeed) {

        // Calculate the horizontal index of the tile at the left and right side of the hitbox
        float newX = hitbox.x + xSpeed ;
        float horizontalTileLeftIndex = newX / Game.TILES_SIZE;
        float horizontalTileRightIndex = (newX + hitbox.width) / Game.TILES_SIZE;

        // Check if the hitbox is touching the leftmost or rightmost tiles in the game grid
        if (horizontalTileLeftIndex <= 1 || horizontalTileRightIndex >= Game.TILES_IN_WIDTH - 1)
            return true;

        return false;
    }
}


