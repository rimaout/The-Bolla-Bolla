package bubbles;

import entities.Player;
import main.Game;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import static utilz.Constants.Bubble.DEAD;

public class ChainExplosionManager {
    private Player player;
    private LinkedList<Bubble> bubbles;

    private Timer timer = new Timer();

    // Constants and variables
    private int delay = 150;
    private final int CHAIN_REACTION_RADIUS = 15 * Game.SCALE;

    int enemyBubblePopCounter = 0;

    public ChainExplosionManager(Player player, Bubble firstPoppedBubble, LinkedList<Bubble> bubbles) {
        this.player = player;
        this.bubbles = bubbles;

        chainExplosion(firstPoppedBubble);
    }

    public void chainExplosion(Bubble poppedBubble) {

        for (Bubble b : bubbles) {

            if (!b.isActive() || b == poppedBubble || b.state == DEAD)
                continue;

            // Calculate the distance between the first popped bubble and the current bubble
            double dx = b.getCenter().x - poppedBubble.getCenter().x;
            double dy = b.getCenter().y - poppedBubble.getCenter().y;
            double distance = Math.sqrt(dx * dx + dy * dy);

            // check if the current bubble is within the chain reaction radius
            if (distance < CHAIN_REACTION_RADIUS)
                timer.schedule(new TimerTask() {
                    @Override public void run() {chainExplosion(b);}}, delay);
        }

        poppedBubble.playerPop(player, enemyBubblePopCounter, this);
    }

    public void increaseEnemyBubblePopCounter() {
        enemyBubblePopCounter++;
    }

    public int getEnemyBubblePopCounter() {
        return enemyBubblePopCounter;
    }

}
