package itemesAndRewards;

import java.awt.*;
import java.util.ArrayList;
import java.awt.image.BufferedImage;

import model.utilz.LoadSave;
import model.entities.PlayerModel;
import model.gameStates.PlayingModel;
import model.utilz.PlayingTimer;
import model.levels.LevelManagerModel;

import static model.utilz.Constants.Items.*;
import static model.utilz.HelpMethods.GetRandomPosition;

public class ItemManager {
    private static ItemManager instance;
    private final PlayingModel playingModel;
    private final PlayingTimer timer = PlayingTimer.getInstance();

    private int  spawnPowerUpTimer = SPAWN_POWER_UP_TIMER;
    private boolean powerUpSpawned = false;
    private final ArrayList<Item> items;

    // Sprites
    private BufferedImage[] deSpawnImages;
    private BufferedImage[] bubbleRewardImages;
    private BufferedImage[] powerUpImages;

    private ItemManager(PlayingModel playingModel) {
        items = new ArrayList<>();
        this.playingModel = playingModel;

        loadSprites();
    }

    public static ItemManager getInstance(PlayingModel playingModel) {
        if (instance == null)
            instance = new ItemManager(playingModel);
        return instance;
    }

    public static ItemManager getInstance() {
        return instance;
    }

    public void update() {
        updateTimers();
        updateItems();
        checkPowerUpSpawn();
    }

    private void updateItems() {
        for (Item i : items) {
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
            PowerUpType powerUp = PowerUpManager.getInstance().getPowerUpToSpawn();

            if (powerUp != null) {
                items.add(new PowerUp(spawnPoint.x, spawnPoint.y, powerUp));
            }
        }
    }

    private void checkCollisionWithPlayer(Item item) {
        PlayerModel playerModel = playingModel.getPlayerOneModel();

        if (!playerModel.isActive())
            return;

        if(item.getHitbox().intersects(playerModel.getHitbox())){
            item.setActive(false);
            item.addPoints(playerModel);
            item.applyEffect(playerModel);
            item.setPlaySound(true);
            PowerUpManager.getInstance().increaseItemCollectCounter();
        }
    }

    public void draw(java.awt.Graphics g) {

        // Draw Reward Bubble Items
        for (Item i : items) {
            if (i.isActive())
                i.draw(g);
            i.audioEffects();
        }
    }

    public void addBubbleReward(int x, int y, BubbleRewardType type) {
        
        items.add(new BubbleReward(x, y, type));
    }

    private void loadSprites() {
        // Load bubble reward sprites
        BufferedImage bubbleRewardsSprite = LoadSave.GetSprite(LoadSave.ITEM_BUBBLE_REWARD_SPRITE);
        bubbleRewardImages = new BufferedImage[8];
        for (int i = 0; i < bubbleRewardImages.length; i++)
            bubbleRewardImages[i] = bubbleRewardsSprite.getSubimage(i * DEFAULT_W, 0, DEFAULT_W, DEFAULT_H);

        // Load power-up sprites
        BufferedImage powerUpSprite = LoadSave.GetSprite(LoadSave.ITEM_POWER_UP_SPRITE);
        powerUpImages = new BufferedImage[10];
        for (int i = 0; i < powerUpImages.length; i++)
            powerUpImages[i] = powerUpSprite.getSubimage(i * DEFAULT_W, 0, DEFAULT_W, DEFAULT_H);

        // Load de-spawn sprites
        BufferedImage deSpawnSprite = LoadSave.GetSprite(LoadSave.ITEM_DESPAWN_SPRITE);
        deSpawnImages = new BufferedImage[2];
        for (int i = 0; i < deSpawnImages.length; i++)
            deSpawnImages[i] = deSpawnSprite.getSubimage(i * DEFAULT_W, 0, DEFAULT_W, DEFAULT_H);
    }

    public void newLevelReset() {
        items.clear();
        powerUpSpawned = false;
        spawnPowerUpTimer = SPAWN_POWER_UP_TIMER;
    }

    public void newPlayReset() {
        newLevelReset();
    }

    public BufferedImage[] getBubbleRewardImages() {
        return bubbleRewardImages;
    }

    public BufferedImage[] getPowerUpImages() {
        return powerUpImages;
    }

    public BufferedImage[] getDeSpawnImages() {
        return deSpawnImages;
    }

    public ArrayList<Item> getItems() {
        return items;
    }
}
