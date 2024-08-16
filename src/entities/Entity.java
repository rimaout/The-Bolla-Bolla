package entities;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import main.Game;
import static utilz.HelpMethods.*;

public abstract class Entity {
    protected float x, y; // Used protected access modifier so that the subclasses can access the x and y variables, with the private access modifier, the subclasses won't be able to access the x and y variables.
    protected int width, height;
    protected Rectangle2D.Float hitbox;
    protected int animationTick, animationIndex;

    public Entity(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    protected void initHitbox(float width, float height) {
        hitbox = new Rectangle2D.Float(x, y, width, height);
    }

    protected void drawHitbox(Graphics g) {
        // For debugging purposes

        g.setColor(Color.RED);
        g.drawRect((int) hitbox.x, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }

    protected void updateXPos(float xMovement, int[][] levelData) {
        if (CanMoveHere(hitbox.x + xMovement, hitbox.y, hitbox.width, hitbox.height, levelData))
            hitbox.x += xMovement;
    }

    protected void conpenetrationSafeUpdateXPos(float xMovement, int[][] levelData) {

        // Moving right
        if (xMovement > 0) {
            int xTile = (int) ((hitbox.x + hitbox.width + xMovement) / Game.TILES_SIZE);
            int yTile = (int) (hitbox.y / Game.TILES_SIZE);

            if (!IsWall(xTile, yTile, levelData))
                hitbox.x += xMovement;
        }
        // Moving left
        else {
            int xTile = (int) ((hitbox.x + xMovement) / Game.TILES_SIZE);
            int yTile = (int) (hitbox.y / Game.TILES_SIZE);

            if (!IsWall(xTile, yTile, levelData))
                hitbox.x += xMovement;
        }
    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

   public int getAnimationIndex() {
        return animationIndex;
   }
}
