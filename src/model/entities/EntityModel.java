package model.entities;

import java.awt.geom.Rectangle2D;

import model.Constants;
import model.PlayingTimer;
import model.levels.LevelManagerModel;
import model.itemesAndRewards.PowerUpManagerModel;

import static model.HelpMethods.*;

public abstract class EntityModel {
    protected final PlayingTimer timer = PlayingTimer.getInstance();
    protected LevelManagerModel levelManagerModel = LevelManagerModel.getInstance();

    protected float x, y;
    protected int width, height;
    protected Rectangle2D.Float hitbox;

    protected boolean immune = false;
    protected boolean active = true;

    public EntityModel(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    protected void initHitbox(float width, float height) {
        hitbox = new Rectangle2D.Float(x, y, width, height);
    }

    protected void updateXPos(float xMovement) {
        if (CanMoveHere(hitbox.x + xMovement, hitbox.y, hitbox.width, hitbox.height, levelManagerModel.getLevelData())) {
            hitbox.x += xMovement;
            PowerUpManagerModel.getInstance().addDistance(xMovement);
        }
    }

    protected void conpenetrationSafeUpdateXPos(float xMovement) {

        // Moving right
        if (xMovement > 0) {
            int xTile = (int) ((hitbox.x + hitbox.width + xMovement) / Constants.TILES_SIZE);
            int yTile = (int) (hitbox.y / Constants.TILES_SIZE);

            if (!IsWall(xTile, yTile, levelManagerModel.getLevelData())) {
                hitbox.x += xMovement;
                PowerUpManagerModel.getInstance().addDistance(xMovement);
            }
        }

        // Moving left
        else {
            int xTile = (int) ((hitbox.x + xMovement) / Constants.TILES_SIZE);
            int yTile = (int) (hitbox.y / Constants.TILES_SIZE);

            if (!IsWall(xTile, yTile, levelManagerModel.getLevelData())) {
                hitbox.x += xMovement;
                PowerUpManagerModel.getInstance().addDistance(xMovement);
            }
        }
    }

    public int getTileX() {
        return (int) (hitbox.x / Constants.TILES_SIZE);
    }

    public int getTileY() {
        return (int) (hitbox.y / Constants.TILES_SIZE);
    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
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

    public void deactivate() {
        active = false;
    }
}
