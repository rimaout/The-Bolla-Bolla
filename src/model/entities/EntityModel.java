package model.entities;

import java.awt.geom.Rectangle2D;

import model.utilz.Constants;
import model.utilz.PlayingTimer;
import model.levels.LevelManagerModel;
import model.itemesAndRewards.PowerUpManagerModel;

import static model.utilz.HelpMethods.*;

/**
 * Represents a generic entity in the game.
 *
 * <p>This abstract class provides common properties and methods for all game entities,
 * such as position, dimensions, hitbox, and movement logic.
 */
public abstract class EntityModel {
    protected final PlayingTimer timer = PlayingTimer.getInstance();
    protected LevelManagerModel levelManagerModel;

    protected float x, y;
    protected int width, height;
    protected Rectangle2D.Float hitbox;

    protected boolean immune = false;
    protected boolean active = true;

    /**
     * Constructs a new EntityModel with the specified position and dimensions.
     *
     * @param x the x-coordinate of the entity
     * @param y the y-coordinate of the entity
     * @param width the width of the entity
     * @param height the height of the entity
     */
    public EntityModel(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Initializes the level manager, if null.
     */
    protected void initLevelManager() {
        if (levelManagerModel == null)
            levelManagerModel = LevelManagerModel.getInstance();
    }

    /**
     * Initializes the hitbox for the entity with the specified dimensions.
     *
     * @param width the width of the hitbox
     * @param height the height of the hitbox
     */
    protected void initHitbox(float width, float height) {
        hitbox = new Rectangle2D.Float(x, y, width, height);
    }

    /**
     * Updates the x-coordinate position of the entity by the specified movement amount.
     *
     * @param xMovement the amount to move the entity along the x-axis
     */
    protected void updateXPos(float xMovement) {
        if (CanMoveHere(hitbox.x + xMovement, hitbox.y, hitbox.width, hitbox.height, levelManagerModel.getLevelTileData())) {
            hitbox.x += xMovement;
            PowerUpManagerModel.getInstance().addDistance(xMovement);
        }
    }

    /**
     * Safely updates the x-coordinate position of the entity by the specified movement amount,
     * ensuring no penetration through walls.
     *
     * @param xMovement the amount to move the entity along the x-axis
     */
    protected void conpenetrationSafeUpdateXPos(float xMovement) {

        // Moving right
        if (xMovement > 0) {
            int xTile = (int) ((hitbox.x + hitbox.width + xMovement) / Constants.TILES_SIZE);
            int yTile = (int) (hitbox.y / Constants.TILES_SIZE);

            if (!IsWall(xTile, yTile, levelManagerModel.getLevelTileData())) {
                hitbox.x += xMovement;
                PowerUpManagerModel.getInstance().addDistance(xMovement);
            }
        }

        // Moving left
        else {
            int xTile = (int) ((hitbox.x + xMovement) / Constants.TILES_SIZE);
            int yTile = (int) (hitbox.y / Constants.TILES_SIZE);

            if (!IsWall(xTile, yTile, levelManagerModel.getLevelTileData())) {
                hitbox.x += xMovement;
                PowerUpManagerModel.getInstance().addDistance(xMovement);
            }
        }
    }

    // ------ Getters -------

    /**
     * Returns the x-coordinate tile position of the entity.
     *
     * @return the x-coordinate tile position
     */
    public int getTileX() {
        return (int) (hitbox.x / Constants.TILES_SIZE);
    }

    /**
     * Returns the y-coordinate tile position of the entity.
     *
     * @return the y-coordinate tile position
     */
    public int getTileY() {
        return (int) (hitbox.y / Constants.TILES_SIZE);
    }

    /**
     * Returns the hitbox of the entity.
     *
     * @return the hitbox of the entity
     */
    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    /**
     * Returns the immune status of the entity.
     *
     * @return true if the entity is immune, false otherwise
     */
    public boolean isImmune() {
       return immune;
   }

    /**
     * Returns the active status of the entity.
     *
     * @return true if the entity is active, false otherwise
     */
    public boolean isActive() {
         return active;
    }

    // ------ Setters -------

    /**
     * Sets the active status of the entity.
     *
     * @param active the new active status to set
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Deactivates the entity.
     */
    public void deactivate() {
        active = false;
    }
}