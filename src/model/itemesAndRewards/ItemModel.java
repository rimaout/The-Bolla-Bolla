package model.itemesAndRewards;

import model.utilz.PlayingTimer;

import static model.utilz.Constants.Items.*;

import java.awt.geom.Rectangle2D;

/**
 * Represents an abstract item in the game.
 *
 * <p>The `ItemModel` class serves as a base class for all items in the game. It provides common properties
 * and methods for handling item behavior.
 */
public abstract class ItemModel {
    protected final PlayingTimer timer = PlayingTimer.getInstance();

    protected int x, y;
    protected Rectangle2D.Float hitbox;
    protected ItemType itemType;

    protected int deSpawnTimer = DE_SPAWN_TIMER;
    protected boolean deSpawning = false;
    protected boolean active = true;
    protected boolean collected = false;

    /**
     * Constructs a new `ItemModel` with the specified position and item type.
     *
     * @param x the x-coordinate of the item
     * @param y the y-coordinate of the item
     * @param itemType the type of the item
     */
    public ItemModel(int x, int y, ItemType itemType) {
        this.x = x;
        this.y = y;
        this.itemType = itemType;

        initHitbox();
    }

    /**
     * Adds points to the player's score based on the item type.
     */
    public abstract void addPoints();

    /**
     * Applies the effect of the item to the player.
     */
    public abstract void applyEffect();

    /**
     * Updates the state of the item.
     */
    protected void update(){
        updateTimers();
    }

    /**
     * Updates the timers for the item, handling despawning logic.
     */
    private void updateTimers() {
        deSpawnTimer -= (int) timer.getTimeDelta();

        if (deSpawnTimer <= 0 && !deSpawning) {
            deSpawning = true;
            deSpawnTimer = 600;
        }

        if (deSpawnTimer <= 0 && deSpawning) {
            active = false;
        }
    }

    /**
     * Initializes the hitbox for the item.
     */
    protected void initHitbox() {
        hitbox = new Rectangle2D.Float(x + OFFSET_X, y + OFFSET_Y, HITBOX_W, HITBOX_H);
    }

    /**
     * Returns the hitbox of the item.
     *
     * @return the hitbox of the item
     */
    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    /**
     * Checks if the item is active.
     *
     * @return true if the item is active, false otherwise
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Checks if the item is despawning.
     *
     * @return true if the item is despawning, false otherwise
     */
    public boolean isDeSpawning(){
        return deSpawning;
    }

    /**
     * Deactivates the item.
     */
    public void deactivateItem() {
        active = false;
    }

    /**
     * Sets the collected state of the item.
     *
     * @param collected true if the item is collected, false otherwise
     */
    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    /**
     * Checks if the item is collected.
     *
     * @return true if the item is collected, false otherwise
     */
    public boolean isCollected() {
        return collected;
    }

    /**
     * Returns the type of the item.
     *
     * @return the type of the item
     */
    public ItemType getItemType() {
        return itemType;
    }
}