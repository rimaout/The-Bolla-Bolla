package com.rima.model.bubbles.playerBubbles;

import java.util.Timer;
import java.util.TimerTask;
import java.util.LinkedList;

import com.rima.model.utilz.Constants;
import com.rima.model.entities.PlayerModel;
import com.rima.model.itemesAndRewards.PowerUpManagerModel;

import static com.rima.model.utilz.Constants.Bubble.DEAD;

/**
 * Manages the chain reaction of bubble explosions.
 *
 * <p>This class handles the chain reaction of bubbles popping when one bubble is popped by the player.
 * It uses a timer to create delays between consecutive bubble pops and manages the count of enemy bubbles popped.
 */
public class ChainReactionManager {
    private final PlayerModel playerModel;
    private final Timer timer = new Timer();
    private final LinkedList<PlayerBubbleModel> bubbles;

    private int enemyBubblePopCounter = 0;

    /**
     * Constructs a new ChainReactionManager.
     *
     * @param playerModel the player model
     * @param firstPoppedBubble the first bubble popped
     * @param bubbles the list of bubbles
     */
    public ChainReactionManager(PlayerModel playerModel, PlayerBubbleModel firstPoppedBubble, LinkedList<PlayerBubbleModel> bubbles) {
        this.playerModel = playerModel;
        this.bubbles = bubbles;

        chainExplosion(firstPoppedBubble);
    }

    /**
     * Chains the explosion of bubbles.
     *
     * <p>This method recursively pops all bubbles within the chain reaction radius of the initially popped bubble, creating a chain reaction.
     * <p>For each bubble within the radius, there is a small delay before the pop.
     *
     * @param poppedBubble the bubble that was initially popped
     */
    public void chainExplosion(PlayerBubbleModel poppedBubble) {

        for (PlayerBubbleModel b : bubbles) {

            if (!b.isActive() || b == poppedBubble || b.getState() == DEAD)
                continue;

            // Calculate the distance between the first popped bubble and the current bubble
            double dx = b.getCenter().x - poppedBubble.getCenter().x;
            double dy = b.getCenter().y - poppedBubble.getCenter().y;
            double distance = Math.sqrt(dx * dx + dy * dy);

            // check if the current bubble is within the chain reaction radius
            // Constants and variables
            int delay = 150;
            int CHAIN_REACTION_RADIUS = 15 * Constants.SCALE;
            if (distance < CHAIN_REACTION_RADIUS)
                timer.schedule(new TimerTask() {
                    @Override public void run() {chainExplosion(b);}}, delay);
        }

        poppedBubble.playerPop(playerModel, enemyBubblePopCounter, this);
    }

    /**
     * Increases the enemy bubble pop counter.
     */
    public void increaseEnemyBubblePopCounter() {
        enemyBubblePopCounter++;
        PowerUpManagerModel.getInstance().increaseBubblePopCounter();
    }

    /**
     * Gets the enemy bubble pop counter.
     *
     * @return the enemy bubble pop counter
     */
    public int getEnemyBubblePopCounter() {
        return enemyBubblePopCounter;
    }
}
