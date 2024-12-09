package model.bubbles.specialBubbles;

import model.bubbles.playerBubbles.PlayerBubbleModel;
import model.bubbles.playerBubbles.PlayerBubblesManagerModel;
import model.entities.EnemyModel;
import model.entities.EnemyManagerModel;
import model.entities.EntityModel;
import model.entities.PlayerModel;
import model.itemesAndRewards.BubbleRewardModel;
import model.itemesAndRewards.ItemModel;
import model.itemesAndRewards.ItemManagerModel;
import model.itemesAndRewards.PowerUpManagerModel;
import model.levels.LevelManagerModel;
import model.utilz.Constants;
import model.utilz.Constants.Direction;
import model.utilz.PlayingTimer;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static model.utilz.Constants.WaterFLow.*;
import static model.utilz.Constants.Direction.*;
import static model.entities.HelpMethods.*;
import static model.utilz.Constants.Items.BubbleRewardType.WATER_CRISTAL;


public class WaterFlowModel extends EntityModel {
    // The water flow object is generated by the water bubble when it pops

    private final PlayingTimer timer = PlayingTimer.getInstance();
    private final LevelManagerModel levelManagerModel = LevelManagerModel.getInstance();
    private final SpecialBubbleManagerModel bubbleManager = SpecialBubbleManagerModel.getInstance();

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

    public WaterFlowModel(float x, float y) {
        super(x, y, W, H);
        this.direction = GetRandomHorizontalDirection();
        this.previousDirection = direction;
        initHitbox(HITBOX_W, HITBOX_H);
    }

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

    private void updateWaterDropsPositions() {
        if (addWaterDropTimer <= 0) {
            if (lastPositions.size() >= 3)
                lastPositions.removeFirst();

            lastPositions.add(new Point((int) hitbox.x, (int) hitbox.y));
            addWaterDropTimer = ADD_WATER_DROP_INTERVAL;
        }
    }

    private void firstUpdate() {
        //  get starting position (if the water flow is inside a solid tile, move it up)

        if (IsEntityInsideSolid(hitbox, levelManagerModel.getLevelData()))
            hitbox.y += 1;
        else
            firstUpdate = false;
    }

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

    private void updateMove() {

        if (!IsEntityOnFloor(hitbox, levelManagerModel.getLevelData()))
            direction = DOWN;

        switch (direction) {
            case DOWN -> fall();
            case LEFT -> moveLeft();
            case RIGHT -> moveRight();
        }
    }

    private void fall() {
        float newY = hitbox.y + WATER_FLOW_SPEED;
        float correction = 2 * Constants.SCALE;     // correction value to prevent the water flow from getting stuck in the floor

        if (!IsSolid(hitbox.x, newY + hitbox.height - correction, levelManagerModel.getLevelData()))
            hitbox.y = newY;
        else
            direction = previousDirection;
    }

    private void moveLeft() {
        float newX = hitbox.x - WATER_FLOW_SPEED;

        if (!IsSolid(newX, hitbox.y, levelManagerModel.getLevelData()) && !IsSolid(newX, hitbox.y + hitbox.height/2, levelManagerModel.getLevelData()))
            hitbox.x = newX;
        else {
            // change direction
            direction = RIGHT;
            previousDirection = LEFT;
        }
    }

    private void moveRight() {
        float newX = hitbox.x + WATER_FLOW_SPEED;

        if (!IsSolid(newX + hitbox.width, hitbox.y, levelManagerModel.getLevelData()) && !IsSolid(newX + hitbox.width, hitbox.y + hitbox.height/2, levelManagerModel.getLevelData()))
            hitbox.x = newX;
        else {
            // change direction
            direction = LEFT;
            previousDirection = RIGHT;
        }
    }

    private void updateOutOfScreen() {
        if (getTileY() == Constants.TILES_IN_HEIGHT + 1) {

            if (capturedPlayerModel != null)
                spawnPlayer();

            if (capturedEnemiesCounter > 0)
                spawnEnemiesDrops();

            active = false;
        }
    }

    private void spawnPlayer() {
        // spawn player at the top of the screen
        capturedPlayerModel.setActive(true);
        capturedPlayerModel.reset(false, false);
        capturedPlayerModel.setInAir(true);
        capturedPlayerModel.getHitbox().y = Constants.TILES_IN_HEIGHT + 1;
    }

    private void spawnEnemiesDrops() {
        int dropY = calculateEnemyDropY();
        for (int i = 0; i < capturedEnemiesCounter; i++) {
            int dropX = calculateEnemyDropX((int) hitbox.x, i);
            ItemManagerModel.getInstance().getItemsModels().add(new BubbleRewardModel(dropX, dropY, WATER_CRISTAL));
        }
    }

    private int calculateEnemyDropX(int spawnPoint, int i) {
        // This method calculates the x position of the enemy drop, randomly spreading them in a range around the spawn point
        Random random = new Random();
        int offsetRange = 18 * Constants.SCALE;

        return spawnPoint + (i * offsetRange) + random.nextInt(offsetRange);
    }

    private int calculateEnemyDropY() {
        // This method calculates the y position of the enemy drop, by finding the first solid tile below the top of the map
        int dropY = 4 * Constants.TILES_SIZE; // start from the top of the screen

        for (int i = -1; i < Constants.TILES_IN_HEIGHT * Constants.TILES_SIZE; i++) {
            if (IsSolid(hitbox.x, dropY, levelManagerModel.getLevelData())) {
                dropY -= Constants.Items.H;
                break;
            }
            dropY += 1;
        }
        return dropY;
    }

    public void updateCollisions(PlayerModel playerModel) {
        checkCollisionWithPlayer(playerModel);
        EnemyManagerModel.getInstance().getEnemiesModels().forEach(this::checkCollisionWithEnemy);

        if (capturedPlayerModel != null) {
            PlayerBubblesManagerModel.getInstance().getBubblesModels().forEach(this::checkCollisionWithBubble);
            ItemManagerModel.getInstance().getItemsModels().forEach(item -> checkCollisionWithItem(item, playerModel));
        }
    }

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

    private void checkCollisionWithEnemy(EnemyModel enemyModel) {
        if (!enemyModel.isActive())
            return;

        if (hitbox.intersects(enemyModel.getHitbox())) {
            enemyModel.setActive(false);
            enemyModel.setAlive(false);
            capturedEnemiesCounter++;
        }
    }

    private void checkCollisionWithBubble(PlayerBubbleModel bubble) {
        if (!bubble.isActive())
            return;

        if (hitbox.intersects(bubble.getHitbox())) {
            bubble.pop();
        }
    }

    private void checkCollisionWithItem(ItemModel itemModel, PlayerModel playerModel) {
        if (!itemModel.isActive())
            return;

        if (hitbox.intersects(itemModel.getHitbox())) {
            itemModel.deactivateItem();
            itemModel.addPoints(playerModel);
            itemModel.applyEffect(playerModel);
            PowerUpManagerModel.getInstance().increaseItemCollectCounter();
        }
    }

    public int flipX() {
        Direction flipDirection = switch (direction) {
            case UP, DOWN -> previousDirection;
            default -> direction;
        };

        return (flipDirection == RIGHT) ? 0 : W;
    }

    public int flipW() {
        Direction flipDirection = switch (direction) {
            case UP, DOWN -> previousDirection;
            default -> direction;
        };

        return (flipDirection == RIGHT) ? 1 : -1;
    }

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