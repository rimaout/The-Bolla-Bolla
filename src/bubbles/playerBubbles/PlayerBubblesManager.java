package bubbles.playerBubbles;

import bubbles.Bubble;
import entities.Enemy;
import entities.Player;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import static utilz.Constants.Bubble.*;

public class PlayerBubblesManager {

    // This class is responsible for managing the bubbles that the player shoots

    private static PlayerBubblesManager instance;
    private final Player player;

    private BufferedImage[][] playerBubbleSprites;

    private LinkedList<PlayerBubble> bubbles;

    private long lastTimerUpdate;
    private int popTimer = 0;
    private final int POP_DELAY_AFTER_CHAIN_EXPLOSION = 200;

    private PlayerBubblesManager(Player player) {
        this.player = player;
        bubbles = new LinkedList<>();
        loadBubbleSprites();
    }

    public static PlayerBubblesManager getInstance(Player player) {
        if (instance == null) {
            instance = new PlayerBubblesManager(player);
        }
        return instance;
    }

    public static PlayerBubblesManager getInstance() {
        return instance;
    }

    public void update() {
        updateTimers();

        for (Bubble b : bubbles) {
            if (b.isActive()) {
                b.update();
                b.checkCollisionWithPlayer(player);
            }
        }

        collisionBetweenBubbles();
    }

    public void draw(Graphics g) {
        for (Bubble b : bubbles) {
            if (b.isActive())
                b.draw(g);
        }
    }

    private void updateTimers() {

        if (lastTimerUpdate == 0)
            lastTimerUpdate = System.currentTimeMillis();

        long timeDelta = System.currentTimeMillis() - lastTimerUpdate;
        lastTimerUpdate = System.currentTimeMillis();
        popTimer -= (int) timeDelta;
    }

   private void collisionBetweenBubbles() {
    for (Bubble b1 : bubbles) {
        if (!b1.isActive() || b1.getState() == DEAD)
            continue;

        for (Bubble b2 : bubbles) {
            if (b2.isActive() && b1 != b2 && b2.getState() != DEAD)
                applyRepulsion(b1, b2);
        }
    }
}

    void applyRepulsion(Bubble bubble1, Bubble bubble2) {
        Point centerB1 = bubble1.getCenter();
        Point centerB2 = bubble2.getCenter();

        double dx = centerB2.x - centerB1.x;
        double dy = centerB2.y - centerB1.y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        // Defines allowed overlap between bubbles
        double overlapAllowed = 5.0 * Game.SCALE;        // Allow this much overlap
        double totalRadius = bubble1.getHitbox().width;  // Sum of both bubble radii

        if (distance < totalRadius - overlapAllowed) {
            double overlap = totalRadius - distance;
            double repulsionStrength = 0.05; // Adjust this value as needed

            // Normalize the direction vector
            if (distance > 0) {
                dx /= distance;
                dy /= distance;
            }

            // Move bubbles apart
            bubble1.getHitbox().x -= (float) (dx * overlap * repulsionStrength);
            bubble1.getHitbox().y -= (float) (dy * overlap * repulsionStrength);
            bubble2.getHitbox().x += (float) (dx * overlap * repulsionStrength);
            bubble2.getHitbox().x += (float) (dy * overlap * repulsionStrength);

            // Introduce randomness
            double randomAdjustment = 1.0; // Adjust this value as needed
            bubble1.getHitbox().x += (float) ((Math.random() - 0.5) * randomAdjustment);
            bubble1.getHitbox().y += (float) ((Math.random() - 0.5) * randomAdjustment);
            bubble2.getHitbox().x += (float) ((Math.random() - 0.5) * randomAdjustment);
            bubble2.getHitbox().y += (float) ((Math.random() - 0.5) * randomAdjustment);
        }
    }

    public void startChainExplosions(PlayerBubble firstPoppedBubble) {
        popTimer = POP_DELAY_AFTER_CHAIN_EXPLOSION;

        LinkedList<PlayerBubble> bubblesShallowCopy = new LinkedList<>(bubbles);
        new ChainExplosionManager(player, firstPoppedBubble, bubblesShallowCopy);
    }

    public void loadBubbleSprites() {
        BufferedImage img = LoadSave.GetSprite(LoadSave.BUBBLE_BUD_SPRITE);

        playerBubbleSprites = new BufferedImage[6][4];
        for (int j = 0; j < playerBubbleSprites.length; j++)
            for (int i = 0; i < playerBubbleSprites[j].length; i++)
                playerBubbleSprites[j][i] = img.getSubimage(i * DEFAULT_W, j*DEFAULT_H, DEFAULT_W, DEFAULT_H);
    }

    public void newLevelReset() {
        bubbles = new LinkedList<>();
        popTimer = 0;
        lastTimerUpdate = 0;
    }

    public void newPlayReset() {
        newLevelReset();
    }

    public void addBubble(PlayerBubble bubble) {
        bubbles.add(bubble);
    }

    public void addDeadEnemy(Enemy e, Player player) {
        EnemyBubble deadEnemyBubble = new EnemyBubble(e, e.getHitbox().x, e.getHitbox().y, player.getDirection());
        deadEnemyBubble.playerPop(player);
        bubbles.add(deadEnemyBubble);
    }

    public LinkedList<PlayerBubble> getBubbles() {
        return bubbles;
    }

    public BufferedImage[][] getPlayerBubbleSprites() {
        return playerBubbleSprites;
    }

    public int getPopTimer() {
        return popTimer;
    }
}
