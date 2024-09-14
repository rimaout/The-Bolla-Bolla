package itemesAndRewards;

import entities.EnemyManager;
import entities.Player;
import gameStates.Playing;
import levels.LevelManager;
import utilz.LoadSave;
import static utilz.Constants.Items.*;
import static utilz.HelpMethods.GetRandomPosition;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ItemManager {
    private static ItemManager instance;
    private Playing playing;

    private boolean firstUpdate = true;
    private long lastTimerUpdate;
    private int spawnPowerUpTimer;

    private boolean powerUpSpawned = false;
    private boolean allRewardsDeSpawned = false;
    private ArrayList<Item> items;

    // Sprites
    private BufferedImage[] deSpawnImages;
    private BufferedImage[] bubbleRewardImages;
    private BufferedImage[] powerUpImages;

    private ItemManager(Playing playing) {
        items = new ArrayList<>();
        this.playing = playing;

        loadSprites();
    }

    public static ItemManager getInstance(Playing playing) {
        if (instance == null)
            instance = new ItemManager(playing);
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

        // Update Reward Bubble Items
        int deSpawnCounter = 0;

        for (Item i : items) {

            if (i.isActive()) {
                i.update();
                checkCollisionWithPlayer(i);
            }

            else{
                deSpawnCounter++;

                if (deSpawnCounter == EnemyManager.getInstance().getEnemyCount())
                    allRewardsDeSpawned = true;
            }
        }
    }

    private void updateTimers() {

        if (firstUpdate) {
            lastTimerUpdate = System.currentTimeMillis();
            spawnPowerUpTimer = SPAWN_POWER_UP_TIMER;
            powerUpSpawned = false;
            firstUpdate = false;
        }

        long timeDelta = System.currentTimeMillis() - lastTimerUpdate;
        lastTimerUpdate = System.currentTimeMillis();
        spawnPowerUpTimer -= timeDelta;
    }

    private void checkPowerUpSpawn() {
        if (spawnPowerUpTimer <= 0 && !powerUpSpawned) {
            powerUpSpawned = true;

            Point spawnPoint = GetRandomPosition(LevelManager.getInstance().getCurrentLevel().getLevelData(), new Rectangle(0, 0, W, H));
            PowerUpType powerUp = PowerUpManager.getInstance().getPowerUpToSpawn();

            if (powerUp != null) {
                items.add(new PowerUp(spawnPoint.x, spawnPoint.y, powerUp));
            }
        }
    }

    private void checkCollisionWithPlayer(Item item) {
        Player player = playing.getPlayerOne();

        if(item.getHitbox().intersects(player.getHitbox())){
            item.setActive(false);
            item.addPoints(player);
            item.applyEffect(player);
            PowerUpManager.getInstance().increaseItemCollectCounter();
        }
    }

    public void draw(java.awt.Graphics g) {

        // Draw Reward Bubble Items
        for (Item i : items) {
            if (i.isActive())
                i.draw(g);
        }
    }

    public void addBubbleReward(int x, int y, BubbleRewardType type) {
        
        items.add(new BubbleReward(x, y, type));
    }

    private void loadSprites() {
        // Load bubble reward sprites
        BufferedImage bubbleRewardsSprite = LoadSave.GetSprite(LoadSave.ITEM_BUBBLE_REWARD_SPRITE);
        bubbleRewardImages = new BufferedImage[7];
        for (int i = 0; i < 7; i++)
            bubbleRewardImages[i] = bubbleRewardsSprite.getSubimage(i * DEFAULT_W, 0, DEFAULT_W, DEFAULT_H);

        // Load power-up sprites
        BufferedImage powerUpSprite = LoadSave.GetSprite(LoadSave.ITEM_POWER_UP_SPRITE);
        powerUpImages = new BufferedImage[10];
        for (int i = 0; i < 10; i++)
            powerUpImages[i] = powerUpSprite.getSubimage(i * DEFAULT_W, 0, DEFAULT_W, DEFAULT_H);

        // Load de-spawn sprites
        BufferedImage deSpawnSprite = LoadSave.GetSprite(LoadSave.ITEM_DESPAWN_SPRITE);
        deSpawnImages = new BufferedImage[2];
        for (int i = 0; i < 2; i++)
            deSpawnImages[i] = deSpawnSprite.getSubimage(i * DEFAULT_W, 0, DEFAULT_W, DEFAULT_H);
    }

    public void resetForNewLevel() {
        // Reset all Items
        items.clear();
        allRewardsDeSpawned = false;
        firstUpdate = true;
    }

    public void resetAll() {
        // Only use when resetting the game
        firstUpdate = true;

        // Reset all Items
        items.clear();
        allRewardsDeSpawned = false;

        //TODO: reset all variables
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

    public boolean areAllRewardsDeSpawned() {
        return allRewardsDeSpawned;
    }
}
