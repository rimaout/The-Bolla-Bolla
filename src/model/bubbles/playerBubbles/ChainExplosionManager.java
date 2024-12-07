package model.bubbles.playerBubbles;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import model.entities.PlayerModel;
import model.itemesAndRewards.PowerUpManagerModel;
import model.Constants;

import static model.Constants.Bubble.DEAD;

public class ChainExplosionManager {
    private final PlayerModel playerModel;
    private final Timer timer = new Timer();
    private final LinkedList<PlayerBubbleModel> bubbles;

    private int enemyBubblePopCounter = 0;

    public ChainExplosionManager(PlayerModel playerModel, PlayerBubbleModel firstPoppedBubble, LinkedList<PlayerBubbleModel> bubbles) {
        this.playerModel = playerModel;
        this.bubbles = bubbles;

        chainExplosion(firstPoppedBubble);
    }

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

    public void increaseEnemyBubblePopCounter() {
        enemyBubblePopCounter++;
        PowerUpManagerModel.getInstance().increaseBubblePopCounter();
    }

    public int getEnemyBubblePopCounter() {
        return enemyBubblePopCounter;
    }
}
