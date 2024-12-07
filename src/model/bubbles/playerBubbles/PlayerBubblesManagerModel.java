package model.bubbles.playerBubbles;

import java.awt.*;
import java.util.LinkedList;

import model.bubbles.BubbleModel;
import model.entities.EnemyModel;
import model.entities.PlayerModel;
import model.Constants;
import model.PlayingTimer;

import static model.Constants.Bubble.*;

public class PlayerBubblesManagerModel {
    // This class is responsible for managing the model.bubbles that the player shoots
    private static PlayerBubblesManagerModel instance;

    private final PlayerModel playerModel;
    private final PlayingTimer timer = PlayingTimer.getInstance();

    private final LinkedList<PlayerBubbleModel> bubblesModels;
    private final int POP_DELAY_AFTER_CHAIN_EXPLOSION = 200;
    private int popTimer = 0;

    private PlayerBubblesManagerModel(PlayerModel playerModel) {
        this.playerModel = playerModel;
        bubblesModels = new LinkedList<>();
    }

    public static PlayerBubblesManagerModel getInstance(PlayerModel playerModel) {
        if (instance == null) {
            instance = new PlayerBubblesManagerModel(playerModel);
        }
        return instance;
    }

    public static PlayerBubblesManagerModel getInstance() {
        return instance;
    }

    public void update() {
        updateTimers();

        for (BubbleModel b : bubblesModels) {
            if (b.isActive()) {
                b.update();
                b.checkCollisionWithPlayer(playerModel);
            }
        }

        collisionBetweenBubbles();
    }

    private void updateTimers() {
        popTimer -= (int) timer.getTimeDelta();
    }

   private void collisionBetweenBubbles() {
    for (BubbleModel b1 : bubblesModels) {
        if (!b1.isActive() || b1.getState() == DEAD)
            continue;

        for (BubbleModel b2 : bubblesModels) {
            if (b2.isActive() && b1 != b2 && b2.getState() != DEAD)
                applyRepulsion(b1, b2);
        }
    }
}

    void applyRepulsion(BubbleModel bubbleModel1, BubbleModel bubbleModel2) {
        Point centerB1 = bubbleModel1.getCenter();
        Point centerB2 = bubbleModel2.getCenter();

        double dx = centerB2.x - centerB1.x;
        double dy = centerB2.y - centerB1.y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        // Defines allowed overlap between model.bubbles
        double overlapAllowed = 5.0 * Constants.SCALE;        // Allow this much overlap
        double totalRadius = bubbleModel1.getHitbox().width;  // Sum of both bubble radii

        if (distance < totalRadius - overlapAllowed) {
            double overlap = totalRadius - distance;
            double repulsionStrength = 0.05; // Adjust this value as needed

            // Normalize the direction vector
            if (distance > 0) {
                dx /= distance;
                dy /= distance;
            }

            // Move model.bubbles apart
            bubbleModel1.getHitbox().x -= (float) (dx * overlap * repulsionStrength);
            bubbleModel1.getHitbox().y -= (float) (dy * overlap * repulsionStrength);
            bubbleModel2.getHitbox().x += (float) (dx * overlap * repulsionStrength);
            bubbleModel2.getHitbox().x += (float) (dy * overlap * repulsionStrength);

            // Introduce randomness
            double randomAdjustment = 1.0; // Adjust this value as needed
            bubbleModel1.getHitbox().x += (float) ((Math.random() - 0.5) * randomAdjustment);
            bubbleModel1.getHitbox().y += (float) ((Math.random() - 0.5) * randomAdjustment);
            bubbleModel2.getHitbox().x += (float) ((Math.random() - 0.5) * randomAdjustment);
            bubbleModel2.getHitbox().y += (float) ((Math.random() - 0.5) * randomAdjustment);
        }
    }

    public void startChainExplosions(PlayerBubbleModel firstPoppedBubble) {
        popTimer = POP_DELAY_AFTER_CHAIN_EXPLOSION;

        LinkedList<PlayerBubbleModel> bubblesShallowCopy = new LinkedList<>(bubblesModels);
        new ChainExplosionManager(playerModel, firstPoppedBubble, bubblesShallowCopy);
    }

    public void newLevelReset() {
        bubblesModels.clear();
        popTimer = 0;
    }

    public void newPlayReset() {
        newLevelReset();
    }

    public void addBubble(PlayerBubbleModel bubble) {
        bubblesModels.add(bubble);
    }

    public void createEmptyBubble(float x, float y, Constants.Direction direction) {
        EmptyBubbleModel emptyBubble = new EmptyBubbleModel(x, y, direction);
        bubblesModels.add(emptyBubble);
    }

    public void addDeadEnemy(EnemyModel e, PlayerModel playerModel) {
        EnemyBubbleModel deadEnemyBubble = new EnemyBubbleModel(e, e.getHitbox().x, e.getHitbox().y, playerModel.getDirection());
        deadEnemyBubble.playerPop(playerModel);
        bubblesModels.add(deadEnemyBubble);
    }

    public LinkedList<PlayerBubbleModel> getBubblesModels() {
        return bubblesModels;
    }

    public int getPopTimer() {
        return popTimer;
    }
}
