package model.itemesAndRewards;

import java.awt.*;
import java.util.ArrayList;

import model.utilz.PlayingTimer;
import model.entities.PlayerModel;
import model.levels.LevelManagerModel;

import static model.utilz.Constants.Items.*;
import static model.utilz.HelpMethods.GetRandomPosition;

/**
 * Manages the items and rewards in the game.
 *
 * <p>This class is responsible for handling the spawning, updating, and collision detection
 * of items and rewards in the game.
 */
public class ItemManagerModel {
    private static ItemManagerModel instance;
    private final PlayerModel playerModel;
    private final PlayingTimer timer = PlayingTimer.getInstance();

    private int spawnPowerUpTimer = SPAWN_POWER_UP_TIMER;
    private boolean powerUpSpawned = false;

    private final ArrayList<ItemModel> itemsModels;

    /**
     * private constructor for the ItemManagerModel.
     */
    private ItemManagerModel() {
       itemsModels = new ArrayList<>();
       this.playerModel = PlayerModel.getInstance();
    }

    /**
     * Returns the singleton instance of the ItemManagerModel.
     *
     * @return the singleton instance
     */
    public static ItemManagerModel getInstance() {
        if (instance == null)
            instance = new ItemManagerModel();
        return instance;
    }

    /**
     * Updates the state of the items and checks for power-up spawns.
     */
    public void update() {
        updateTimers();
        updateItems();
        checkPowerUpSpawn();
    }

    /**
     * Updates the state of each item and checks for collisions with the player.
     */
    private void updateItems() {
        for (ItemModel i : itemsModels) {
            if (i.isActive()) {
                i.update();
                checkCollisionWithPlayer(i);
            }
        }
    }

    /**
     * Updates the timers for spawning power-ups.
     */
    private void updateTimers() {
        spawnPowerUpTimer -= (int) timer.getTimeDelta();
    }

    /**
     * Checks if a power-up should be spawned and spawns it if necessary.
     *
     * <p>This method checks if the timer for spawning a power-up has expired and if a power-up has not already been spawned.
     * If these conditions are met, it spawns a new power-up at a random position.
     * <p> The power-up to be spawned is determined by the {@link PowerUpManagerModel#getPowerUpToSpawn()}.
     */
    private void checkPowerUpSpawn() {
        if (spawnPowerUpTimer <= 0 && !powerUpSpawned) {
            powerUpSpawned = true;

            Point spawnPoint = GetRandomPosition(LevelManagerModel.getInstance().getLevelTileData(), new Rectangle(0, 0, W, H));
            PowerUpType powerUp = PowerUpManagerModel.getInstance().getPowerUpToSpawn();

            if (powerUp != null) {
                itemsModels.add(new PowerUpModel(spawnPoint.x, spawnPoint.y, powerUp));
            }
        }
    }

    /**
     * Checks for collisions between an item and the player, and applies the item's effects if a collision is detected.
     *
     * @param itemModel the item to check for collision
     */
    private void checkCollisionWithPlayer(ItemModel itemModel) {

        if (!playerModel.isActive())
            return;

        if(itemModel.getHitbox().intersects(playerModel.getHitbox())){
            itemModel.deactivateItem();
            itemModel.addPoints();
            itemModel.applyEffect();
            itemModel.setCollected(true);
            PowerUpManagerModel.getInstance().increaseItemCollectCounter();
        }
    }

    /**
     * Adds a bubble reward item at the specified position.
     *
     * @param x the x-coordinate of the reward
     * @param y the y-coordinate of the reward
     * @param type the type of bubble reward
     */
    public void addBubbleReward(int x, int y, BubbleRewardType type) {

        itemsModels.add(new BubbleRewardModel(x, y, type));
    }

    /**
     * Resets the state of the item manager for a new level.
     */
    public void newLevelReset() {
        itemsModels.clear();
        powerUpSpawned = false;
        spawnPowerUpTimer = SPAWN_POWER_UP_TIMER;
    }

    /**
     * Resets the state of the item manager for a new play session.
     */
    public void newPlayReset() {
        newLevelReset();
    }

    /**
     * Returns the list of item models managed by this class.
     *
     * @return the list of item models
     */
    public ArrayList<ItemModel> getItemsModels() {
        return itemsModels;
    }
}