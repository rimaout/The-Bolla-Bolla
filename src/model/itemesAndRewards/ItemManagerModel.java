package model.itemesAndRewards;

import java.awt.*;
import java.util.ArrayList;

import model.entities.PlayerModel;
import model.gameStates.PlayingModel;
import model.PlayingTimer;
import model.levels.LevelManagerModel;

import static model.Constants.Items.*;
import static model.HelpMethods.GetRandomPosition;

public class ItemManagerModel {
    private static ItemManagerModel instance;
    private final PlayingModel playingModel;
    private final PlayingTimer timer = PlayingTimer.getInstance();

    private int spawnPowerUpTimer = SPAWN_POWER_UP_TIMER;
    private boolean powerUpSpawned = false;

    private final ArrayList<ItemModel> itemsModels;

    private ItemManagerModel(PlayingModel playingModel) {
        itemsModels = new ArrayList<>();
        this.playingModel = playingModel;
    }

    public static ItemManagerModel getInstance(PlayingModel playingModel) {
        if (instance == null)
            instance = new ItemManagerModel(playingModel);
        return instance;
    }

    public static ItemManagerModel getInstance() {
        return instance;
    }

    public void update() {
        updateTimers();
        updateItems();
        checkPowerUpSpawn();
    }

    private void updateItems() {
        for (ItemModel i : itemsModels) {
            if (i.isActive()) {
                i.update();
                checkCollisionWithPlayer(i);
            }
        }
    }

    private void updateTimers() {
        spawnPowerUpTimer -= (int) timer.getTimeDelta();
    }

    private void checkPowerUpSpawn() {
        if (spawnPowerUpTimer <= 0 && !powerUpSpawned) {
            powerUpSpawned = true;

            Point spawnPoint = GetRandomPosition(LevelManagerModel.getInstance().getLevelData(), new Rectangle(0, 0, W, H));
            PowerUpType powerUp = PowerUpManagerModel.getInstance().getPowerUpToSpawn();

            if (powerUp != null) {
                itemsModels.add(new PowerUpModel(spawnPoint.x, spawnPoint.y, powerUp));
            }
        }
    }

    private void checkCollisionWithPlayer(ItemModel itemModel) {
        PlayerModel playerModel = playingModel.getPlayerOneModel();

        if (!playerModel.isActive())
            return;

        if(itemModel.getHitbox().intersects(playerModel.getHitbox())){
            itemModel.deactivateItem();
            itemModel.addPoints(playerModel);
            itemModel.applyEffect(playerModel);
            itemModel.setCollected(true);
            PowerUpManagerModel.getInstance().increaseItemCollectCounter();
        }
    }

    public void addBubbleReward(int x, int y, BubbleRewardType type) {

        itemsModels.add(new BubbleRewardModel(x, y, type));
    }

    public void newLevelReset() {
        itemsModels.clear();
        powerUpSpawned = false;
        spawnPowerUpTimer = SPAWN_POWER_UP_TIMER;
    }

    public void newPlayReset() {
        newLevelReset();
    }

    public ArrayList<ItemModel> getItemsModels() {
        return itemsModels;
    }

}