package entities;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import itemesAndRewards.PowerUpManager;
import levels.LevelManager;
import main.Game;
import utilz.PlayingTimer;

import static utilz.HelpMethods.*;

public abstract class Entity {
    protected final PlayingTimer timer = PlayingTimer.getInstance();
    protected LevelManager levelManager = LevelManager.getInstance();

    protected float x, y;
    protected int width, height;
    protected Rectangle2D.Float hitbox;
    protected int animationIndex;
    protected float animationTick;

    protected boolean immune = false;
    protected boolean active = true;

    public Entity(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    protected void initHitbox(float width, float height) {
        hitbox = new Rectangle2D.Float(x, y, width, height);
    }

    public void drawHitbox(Graphics g) {
        // For debugging purposes

        g.setColor(Color.RED);
        g.drawRect((int) hitbox.x, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }

    protected void updateXPos(float xMovement) {
        if (CanMoveHere(hitbox.x + xMovement, hitbox.y, hitbox.width, hitbox.height, levelManager.getLevelData())) {
            hitbox.x += xMovement;
            PowerUpManager.getInstance().addDistance(xMovement);
        }
    }

    protected void conpenetrationSafeUpdateXPos(float xMovement) {

        // Moving right
        if (xMovement > 0) {
            int xTile = (int) ((hitbox.x + hitbox.width + xMovement) / Game.TILES_SIZE);
            int yTile = (int) (hitbox.y / Game.TILES_SIZE);

            if (!IsWall(xTile, yTile, levelManager.getLevelData())) {
                hitbox.x += xMovement;
                PowerUpManager.getInstance().addDistance(xMovement);
            }
        }

        // Moving left
        else {
            int xTile = (int) ((hitbox.x + xMovement) / Game.TILES_SIZE);
            int yTile = (int) (hitbox.y / Game.TILES_SIZE);

            if (!IsWall(xTile, yTile, levelManager.getLevelData())) {
                hitbox.x += xMovement;
                PowerUpManager.getInstance().addDistance(xMovement);
            }
        }
    }

    public int getTileX() {
        return (int) (hitbox.x / Game.TILES_SIZE);
    }

    public int getTileY() {
        return (int) (hitbox.y / Game.TILES_SIZE);
    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    public int getAnimationIndex() {
        return animationIndex;
    }

    public boolean isImmune() {
       return immune;
   }

    public boolean isActive() {
         return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
