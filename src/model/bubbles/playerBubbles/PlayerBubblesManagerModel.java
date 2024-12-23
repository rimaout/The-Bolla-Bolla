package model.bubbles.playerBubbles;

import java.awt.*;
import java.util.LinkedList;

import model.utilz.Constants;
import model.utilz.PlayingTimer;
import model.bubbles.BubbleModel;
import model.entities.EnemyModel;
import model.entities.PlayerModel;

import static model.utilz.Constants.Bubble.*;

/**
 * Manages the bubbles created by the player.
 *
 * <p>This singleton class is responsible for updating the state of player bubbles, handling collisions,
 * and managing chain explosions. It maintains a list of active bubbles and provides methods to add new bubbles,
 * reset the state for new levels or plays, and start chain explosions.
 */
public class PlayerBubblesManagerModel {
    // This class is responsible for managing the model.bubbles that the player shoots
    private static PlayerBubblesManagerModel instance;

    private final PlayerModel playerModel;
    private final PlayingTimer timer = PlayingTimer.getInstance();

    private final LinkedList<PlayerBubbleModel> bubblesModels;
    private final int POP_DELAY_AFTER_CHAIN_EXPLOSION = 200;
    private int popTimer = 0;

    /**
     * Constructs a new PlayerBubblesManagerModel. (private because sigleton design pattern implementation)
     */
    private PlayerBubblesManagerModel() {
        this.playerModel = PlayerModel.getInstance();
        bubblesModels = new LinkedList<>();
    }

    /**
     * Returns the singleton instance of the PlayerBubblesManagerModel.
     *
     * @return the singleton instance
     */
    public static PlayerBubblesManagerModel getInstance() {
        if (instance == null) {
            instance = new PlayerBubblesManagerModel();
        }
        return instance;
    }

    /**
     * Updates all active bubbles and checks for collisions with the player.
     */
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

    /**
     * Updates the timers for bubble state transitions.
     */
    private void updateTimers() {
        popTimer -= (int) timer.getTimeDelta();
    }

    /**
     * Checks for collisions between bubbles and applies repulsion if they overlap.
     */
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

    /**
     * Applies repulsion between two overlapping bubbles.
     *
     * @param bubbleModel1 the first bubble
     * @param bubbleModel2 the second bubble
     */
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

    /**
     * Starts a chain reaction from the first popped bubble, chain reaction handled by the {@link ChainReactionManager}.
     *
     * @param firstPoppedBubble the first bubble that was popped
     */
    public void startChainReaction(PlayerBubbleModel firstPoppedBubble) {
        popTimer = POP_DELAY_AFTER_CHAIN_EXPLOSION;

        LinkedList<PlayerBubbleModel> bubblesShallowCopy = new LinkedList<>(bubblesModels);
        new ChainReactionManager(playerModel, firstPoppedBubble, bubblesShallowCopy);
    }

    /**
     * Resets the state for a new level.
     */
    public void newLevelReset() {
        bubblesModels.clear();
        popTimer = 0;
    }

    /**
     * Resets the state for a new play.
     */
    public void newPlayReset() {
        newLevelReset();
    }

    /**
     * Adds a new bubble to the list of bubbles.
     *
     * @param bubble the bubble to add
     */
    public void addBubble(PlayerBubbleModel bubble) {
        bubblesModels.add(bubble);
    }

    /**
     * Creates and adds a new empty bubble at the specified position and direction.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param direction the direction
     */
    public void createEmptyBubble(float x, float y, Constants.Direction direction) {
        EmptyBubbleModel emptyBubble = new EmptyBubbleModel(x, y, direction);
        bubblesModels.add(emptyBubble);
    }

    /**
     * Creates and adds a new enemy bubble to the list of bubbles.
     *
     * @param e the enemy model
     * @param playerModel the player model
     */
    public void createEnemyBubble(EnemyModel e, PlayerModel playerModel) {
        EnemyBubbleModel deadEnemyBubble = new EnemyBubbleModel(e, e.getHitbox().x, e.getHitbox().y, playerModel.getDirection());
        deadEnemyBubble.playerPop(playerModel);
        bubblesModels.add(deadEnemyBubble);
    }

    // ------- Getters and Setters -------

    public LinkedList<PlayerBubbleModel> getBubblesModels() {
        return bubblesModels;
    }

    public int getPopTimer() {
        return popTimer;
    }
}
