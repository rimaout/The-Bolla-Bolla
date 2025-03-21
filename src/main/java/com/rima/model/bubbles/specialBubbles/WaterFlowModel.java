package com.rima.model.bubbles.specialBubbles;

import com.rima.model.entities.EnemyModel;
import com.rima.model.entities.PlayerModel;
import com.rima.model.entities.EntityModel;
import com.rima.model.entities.EnemyManagerModel;

import com.rima.model.bubbles.playerBubbles.PlayerBubbleModel;
import com.rima.model.bubbles.playerBubbles.PlayerBubblesManagerModel;

import com.rima.model.itemesAndRewards.ItemModel;
import com.rima.model.itemesAndRewards.ItemManagerModel;
import com.rima.model.itemesAndRewards.BubbleRewardModel;
import com.rima.model.itemesAndRewards.PowerUpManagerModel;

import com.rima.model.levels.LevelManagerModel;

import com.rima.model.utilz.Constants;
import com.rima.model.utilz.PlayingTimer;
import com.rima.model.utilz.Constants.Direction;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static com.rima.model.utilz.Constants.WaterFLow.*;
import static com.rima.model.utilz.Constants.Direction.*;
import static com.rima.model.utilz.HelpMethods.*;
import static com.rima.model.utilz.Constants.Items.BubbleRewardType.WATER_CRISTAL;

/**
 * Represents a water flow generated by a water bubble when it pops.
 *
 * <p>This class extends the {@link EntityModel} and provides specific behavior for water flows.
 * Water flows move in a random horizontal direction and interact with players, enemies, bubbles, and items.
 */
public class WaterFlowModel extends EntityModel {
    // The water flow object is generated by the water bubble when it pops

    private final PlayingTimer timer = PlayingTimer.getInstance();
    private final LevelManagerModel levelManagerModel = LevelManagerModel.getInstance();

    private Direction direction;
    private boolean firstUpdate = true;
    private Direction previousDirection;

    private PlayerModel capturedPlayerModel;
    private int capturedEnemiesCounter;

    private int addWaterDropTimer = ADD_WATER_DROP_INTERVAL;

    private long transparencyTimer = 0;
    private boolean isTransparent = false;

    private final List<Point> lastPositions = new LinkedList<>();

    private boolean playSound = true;

    /**
     * Constructs a new WaterFlowModel.
     *
     * @param x the starting x coordinate
     * @param y the starting y coordinate
     */
    public WaterFlowModel(float x, float y) {
        super(x, y, W, H);
        this.direction = GetRandomHorizontalDirection();
        this.previousDirection = direction;
        initHitbox(HITBOX_W, HITBOX_H);
    }

    /**
     * Updates the state and position of the water flow.
     *
     * <p>This method handles the first update, updates timers, moves the water flow, updates water drop positions, and checks if the water flow is out of the screen.
     */
    public void update() {
        if (firstUpdate) {
            firstUpdate();
            return;
        }

        timersUpdate();
        updateMove();
        updateWaterDropsPositions();
        updateOutOfScreen();
    }
    /**
     * Updates the positions of the water drops left by the water flow.
     *
     * <p>This method manages the timing for adding new water drop positions to the list.
     * If the timer reaches zero, it adds the current position of the water flow to the list
     * and resets the timer. The list maintains a maximum of three positions, removing the
     * oldest position when a new one is added.
     */
    private void updateWaterDropsPositions() {
        if (addWaterDropTimer <= 0) {
            if (lastPositions.size() >= 3)
                lastPositions.removeFirst();

            lastPositions.add(new Point((int) hitbox.x, (int) hitbox.y));
            addWaterDropTimer = ADD_WATER_DROP_INTERVAL;
        }
    }

    /**
     * Performs the first update for the water flow.
     *
     * <p>This method checks if the water flow is inside a solid tile at its starting position.
     * If it is, the water flow is moved up by one unit to avoid being stuck inside the tile.
     * If the water flow is not inside a solid tile, the first update is marked as complete.
     */
    private void firstUpdate() {
        //  get starting position (if the water flow is inside a solid tile, move it up)

        if (IsEntityInsideSolid(hitbox, levelManagerModel.getLevelTileData()))
            hitbox.y += 1;
        else
            firstUpdate = false;
    }

    /**
     * Updates the timers for the water flow.
     *
     * <p>This method decreases the water drop timer based on the elapsed time.
     * It also updates the transparency timer to toggle the transparency state
     * of the water flow at specified intervals.
     */
    public void timersUpdate() {

        addWaterDropTimer -= (int) timer.getTimeDelta(); ;

        // Update the transparency timer
        long currentTime = System.currentTimeMillis();
        if (isTransparent && currentTime - transparencyTimer >= 30) {
            isTransparent = false;
            transparencyTimer = currentTime;
        } else if (!isTransparent && currentTime - transparencyTimer >= 200) {
            isTransparent = true;
            transparencyTimer = currentTime;
        }
    }

    /**
     * Updates the movement of the water flow.
     *
     * <p>This method checks if the water flow is on the floor. If it is not, the direction is set to DOWN.
     * Depending on the current direction, it calls the appropriate method to move the water flow:
     * <ul>
     *   <li>{@link #fall()} if the direction is DOWN</li>
     *   <li>{@link #moveLeft()} if the direction is LEFT</li>
     *   <li>{@link #moveRight()} if the direction is RIGHT</li>
     * </ul>
     */
    private void updateMove() {

        if (!IsEntityOnFloor(hitbox, levelManagerModel.getLevelTileData()))
            direction = DOWN;

        switch (direction) {
            case DOWN -> fall();
            case LEFT -> moveLeft();
            case RIGHT -> moveRight();
        }
    }

    /**
     * Makes the water flow fall downwards.
     *
     * <p>This method updates the y-coordinate of the water flow to simulate falling.
     * It checks if the new position is solid. If it is not solid, the water flow moves down.
     * If the new position is solid, the water flow changes its direction to the previous direction.
     */
    private void fall() {
        float newY = hitbox.y + WATER_FLOW_SPEED;
        float correction = 2 * Constants.SCALE;     // correction value to prevent the water flow from getting stuck in the floor

        if (!IsSolid(hitbox.x, newY + hitbox.height - correction, levelManagerModel.getLevelTileData()))
            hitbox.y = newY;
        else
            direction = previousDirection;
    }

    /**
     * Moves the water flow to the left.
     *
     * <p>This method updates the x-coordinate of the water flow to simulate leftward movement.
     * It checks if the new position is solid. If it is not solid, the water flow moves left.
     * If the new position is solid, the water flow changes its direction to the right.
     */
    private void moveLeft() {
        float newX = hitbox.x - WATER_FLOW_SPEED;

        if (!IsSolid(newX, hitbox.y, levelManagerModel.getLevelTileData()) && !IsSolid(newX, hitbox.y + hitbox.height/2, levelManagerModel.getLevelTileData()))
            hitbox.x = newX;
        else {
            // change direction
            direction = RIGHT;
            previousDirection = LEFT;
        }
    }

    /**
     * Moves the water flow to the right.
     *
     * <p>This method updates the x-coordinate of the water flow to simulate rightward movement.
     * It checks if the new position is solid. If it is not solid, the water flow moves right.
     * If the new position is solid, the water flow changes its direction to the left.
     */
    private void moveRight() {
        float newX = hitbox.x + WATER_FLOW_SPEED;

        if (!IsSolid(newX + hitbox.width, hitbox.y, levelManagerModel.getLevelTileData()) && !IsSolid(newX + hitbox.width, hitbox.y + hitbox.height/2, levelManagerModel.getLevelTileData()))
            hitbox.x = newX;
        else {
            // change direction
            direction = LEFT;
            previousDirection = RIGHT;
        }
    }

    /**
     * Updates the state of the water flow when it moves out of the screen.
     *
     * <p>This method checks if the water flow has moved beyond the bottom of the screen.
     * If the water flow has captured a player, it spawns the player at the top of the screen.
     * If the water flow has captured any enemies, it spawns enemy drops.
     * Finally, it deactivates the water flow.
     */
    private void updateOutOfScreen() {
        if (getTileY() == Constants.TILES_IN_HEIGHT + 1) {

            if (capturedPlayerModel != null)
                spawnPlayer();

            if (capturedEnemiesCounter > 0)
                spawnEnemiesDrops();

            active = false;
        }
    }

    /**
     * Spawns the player at the top of the screen, used when a waterflow captures the player.
     *
     * <p>This method reactivates the captured player, resets their state, sets them to be in the air,
     * and positions them at the top of the screen.
     */
    private void spawnPlayer() {
        // spawn player at the top of the screen
        capturedPlayerModel.setActive(true);
        capturedPlayerModel.reset(false, false);
        capturedPlayerModel.setInAir(true);
        capturedPlayerModel.getHitbox().y = Constants.TILES_IN_HEIGHT + 1;
    }

    /**
     * Spawns enemy drops at the water flow's position.
     *
     * <p>This method calculates the y-coordinate for the enemy drops and iterates over the number of captured enemies.
     * For each captured enemy, it calculates the x-coordinate for the drop and adds a new {@link BubbleRewardModel} to the item manager.
     */
    private void spawnEnemiesDrops() {
        int dropY = calculateEnemyDropY();
        for (int i = 0; i < capturedEnemiesCounter; i++) {
            int dropX = calculateEnemyDropX((int) hitbox.x, i);
            ItemManagerModel.getInstance().getItemsModels().add(new BubbleRewardModel(dropX, dropY, WATER_CRISTAL));
        }
    }

    /**
     * Calculates the x-coordinate for the enemy drop.
     *
     * <p>This method calculates the x position of the enemy drop, randomly spreading them in a range around the spawn point.
     *
     * @param spawnPoint the x-coordinate of the spawn point
     * @param i the index of the enemy drop
     * @return the calculated x-coordinate for the enemy drop
     */
    private int calculateEnemyDropX(int spawnPoint, int i) {
        // This method calculates the x position of the enemy drop, randomly spreading them in a range around the spawn point
        Random random = new Random();
        int offsetRange = 18 * Constants.SCALE;

        return spawnPoint + (i * offsetRange) + random.nextInt(offsetRange);
    }

    /**
     * Calculates the y-coordinate for the enemy drop.
     *
     * <p>This method calculates the y position of the enemy drop by finding the first solid tile below the top of the map.
     * It starts from a predefined y-coordinate and iterates downwards until it finds a solid tile.
     * If a solid tile is found, it adjusts the y-coordinate to position the drop just above the solid tile.
     *
     * @return the calculated y-coordinate for the enemy drop
     */
    private int calculateEnemyDropY() {
        // This method calculates the y position of the enemy drop, by finding the first solid tile below the top of the map
        int dropY = 4 * Constants.TILES_SIZE; // start from the top of the screen

        for (int i = -1; i < Constants.TILES_IN_HEIGHT * Constants.TILES_SIZE; i++) {
            if (IsSolid(hitbox.x, dropY, levelManagerModel.getLevelTileData())) {
                dropY -= Constants.Items.H;
                break;
            }
            dropY += 1;
        }
        return dropY;
    }

    /**
     * Updates the collisions for the water flow.
     *
     * <p>This method checks for collisions with the player, enemies.
     * If the water flow captures the player, it also checks for collisions with bubbles and items.
     *
     * @param playerModel the player model to check for collisions with the player
     */
    public void updateCollisions(PlayerModel playerModel) {
        checkCollisionWithPlayer(playerModel);
        EnemyManagerModel.getInstance().getEnemiesModels().forEach(this::checkCollisionWithEnemy);

        if (capturedPlayerModel != null) {
            PlayerBubblesManagerModel.getInstance().getBubblesModels().forEach(this::checkCollisionWithBubble);
            ItemManagerModel.getInstance().getItemsModels().forEach(item -> checkCollisionWithItem(item));
        }
    }

    /**
     * Checks for collisions with the player and captures the player if a collision is detected.
     *
     * <p>This method first checks if the player is active. If the player is not active, the method returns immediately.
     * It then checks if the player is above the water flow. If the player is above, the method returns without capturing the player.
     * If the player is not above the water flow and a collision is detected, the player is captured by setting the player as inactive
     * and storing the player model in the `capturedPlayerModel` field.
     *
     * @param playerModel the player model to check for collisions
     */
    private void checkCollisionWithPlayer(PlayerModel playerModel) {
        if (!playerModel.isActive())
            return;

        // if player is above the water flow, don't capture him
        if (playerModel.getTileY() < getTileY())
            return;

        // check collision with player
        if (hitbox.intersects(playerModel.getHitbox())) {
            capturedPlayerModel = playerModel;
            playerModel.setActive(false);
        }
    }

    /**
     * Checks for collisions with an enemy and captures the enemy if a collision is detected.
     *
     * <p>This method first checks if the enemy is active. If the enemy is not active, the method returns immediately.
     * If a collision is detected between the water flow and the enemy, the enemy is deactivated and marked as not alive.
     * The counter for captured enemies is then incremented.
     *
     * @param enemyModel the enemy model to check for collisions
     */
    private void checkCollisionWithEnemy(EnemyModel enemyModel) {
        if (!enemyModel.isActive())
            return;

        if (hitbox.intersects(enemyModel.getHitbox())) {
            enemyModel.setActive(false);
            enemyModel.setAlive(false);
            capturedEnemiesCounter++;
        }
    }

    /**
     * Checks for collisions with a player's bubble and pops the bubble if a collision is detected.
     *
     * <p>This method first checks if the bubble is active. If the bubble is not active, the method returns immediately.
     * If a collision is detected between the water flow and the bubble, the bubble is popped.
     *
     * @param bubble the player's bubble model to check for collisions
     */
    private void checkCollisionWithBubble(PlayerBubbleModel bubble) {
        if (!bubble.isActive())
            return;

        if (hitbox.intersects(bubble.getHitbox())) {
            bubble.pop();
        }
    }

    /**
     * Checks for collisions with an item and handles the interaction if a collision is detected.
     *
     * <p>This method first checks if the item is active. If the item is not active, the method returns immediately.
     * If a collision is detected between the water flow and the item, the item is deactivated, points are added,
     * the item's effect is applied, and the item collect counter is incremented.
     *
     * @param itemModel the item model to check for collisions
     */
    private void checkCollisionWithItem(ItemModel itemModel) {
        if (!itemModel.isActive())
            return;

        if (hitbox.intersects(itemModel.getHitbox())) {
            itemModel.deactivateItem();
            itemModel.addPoints();
            itemModel.applyEffect();
            PowerUpManagerModel.getInstance().increaseItemCollectCounter();
        }
    }

    /**
     * Calculates the x-coordinate flip value for rendering the water flow.
     *
     * <p>This method determines the direction in which the water flow should be flipped
     * based on its current direction. If the direction is UP or DOWN, it uses the previous direction.
     * If the direction is RIGHT, it returns 0; otherwise, it returns the width of the water flow.
     *
     * @return the x-coordinate flip value for rendering
     */
    public int flipX() {
        Direction flipDirection = switch (direction) {
            case UP, DOWN -> previousDirection;
            default -> direction;
        };

        return (flipDirection == RIGHT) ? 0 : W;
    }

    /**
     * Calculates the width flip value for rendering the water flow.
     *
     * <p>This method determines the direction in which the water flow should be flipped
     * based on its current direction. If the direction is UP or DOWN, it uses the previous direction.
     * If the direction is RIGHT, it returns 1; otherwise, it returns -1.
     *
     * @return the width flip value for rendering
     */
    public int flipW() {
        Direction flipDirection = switch (direction) {
            case UP, DOWN -> previousDirection;
            default -> direction;
        };

        return (flipDirection == RIGHT) ? 1 : -1;
    }

    // ----- Getters Methods -----

    public boolean getIsTransparent() {
        return isTransparent;
    }

    public List<Point> getLastPositions() {
        return lastPositions;
    }

    public boolean isPlayerCaptured() {
        return capturedPlayerModel != null;
    }

    public boolean isPlaySoundActive() {
        return playSound;
    }
}