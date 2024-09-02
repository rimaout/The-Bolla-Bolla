package itemesAndRewards;

import entities.EnemyManager;
import entities.Player;
import gameStates.Playing;
import utilz.LoadSave;
import static utilz.Constants.Items.*;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ItemManager {
    private static ItemManager instance;
    private Playing playing;

    private boolean allRewardsDeSpawned = false;
    private ArrayList<BubbleReward> bubbleRewards;

    // Sprites
    private BufferedImage[] deSpawnImages;
    private BufferedImage[] bubbleRewardImages;

    private ItemManager(Playing playing) {
        bubbleRewards = new ArrayList<>();
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
        updateBubbleRewards();

    }

    private void updateBubbleRewards() {

        // Update Reward Bubble Items
        int deSpawnCounter = 0;

        for (BubbleReward br : bubbleRewards) {

            if (br.isActive()) {
                br.update();
                checkCollisionWithPlayer(br);
            }

            else{
                deSpawnCounter++;

                if (deSpawnCounter == EnemyManager.getInstance().getEnemyCount())
                    allRewardsDeSpawned = true;
            }
        }
    }

    private void checkCollisionWithPlayer(Item item) {
        Player player = playing.getPlayerOne();

        if(item.getHitbox().intersects(player.getHitbox())){
            item.setActive(false);
            item.addPoints(player);
            item.applyEffect(player);
        }
    }

    public void draw(java.awt.Graphics g) {

        // Draw Reward Bubble Items
        for (BubbleReward br : bubbleRewards) {
            if (br.isActive())
                br.draw(g);
        }
    }

    public void addBubbleReward(int x, int y, BubbleRewardType type) {
        bubbleRewards.add(new BubbleReward(x, y, type));
    }

    private void loadSprites() {
        BufferedImage bubbleRewardsSprite = LoadSave.GetSprite(LoadSave.ITEM_BUBBLE_REWARD_SPRITE);
        bubbleRewardImages = new BufferedImage[7];

        for (int i = 0; i < 7; i++)
            bubbleRewardImages[i] = bubbleRewardsSprite.getSubimage(i * ITEM_DEFAULT_W, 0, ITEM_DEFAULT_W, ITEM_DEFAULT_H);

        BufferedImage deSpawnSprite = LoadSave.GetSprite(LoadSave.ITEM_DESPAWN_SPRITE);
        deSpawnImages = new BufferedImage[2];

        for (int i = 0; i < 2; i++)
            deSpawnImages[i] = deSpawnSprite.getSubimage(i * ITEM_DEFAULT_W, 0, ITEM_DEFAULT_W, ITEM_DEFAULT_H);
    }

    public void resetForNewLevel() {
        // Reset all Items
        bubbleRewards.clear();
        allRewardsDeSpawned = false;
    }

    public void resetAll() {
        // Only use when resetting the game

        // Reset all Items
        bubbleRewards.clear();
        allRewardsDeSpawned = false;

        //TODO: reset all variables

    }

    public BufferedImage[] getBubbleRewardImages() {
        return bubbleRewardImages;
    }

    public BufferedImage[] getDeSpawnImages() {
        return deSpawnImages;
    }

    public boolean areAllRewardsDeSpawned() {
        return allRewardsDeSpawned;
    }
}
