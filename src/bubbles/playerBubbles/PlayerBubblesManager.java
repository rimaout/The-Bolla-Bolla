package bubbles.playerBubbles;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import bubbles.Bubble;
import entities.Enemy;
import model.entities.PlayerModel;
import model.utilz.Constants;
import model.utilz.LoadSave;
import model.utilz.PlayingTimer;

import static model.utilz.Constants.Bubble.*;

public class PlayerBubblesManager {
    // This class is responsible for managing the bubbles that the player shoots
    private static PlayerBubblesManager instance;

    private final PlayerModel playerModel;
    private final PlayingTimer timer = PlayingTimer.getInstance();

    private final LinkedList<PlayerBubble> bubbles;
    private final int POP_DELAY_AFTER_CHAIN_EXPLOSION = 200;
    private int popTimer = 0;

    private BufferedImage[][] playerBubbleSprites;

    private PlayerBubblesManager(PlayerModel playerModel) {
        this.playerModel = playerModel;
        bubbles = new LinkedList<>();
        loadBubbleSprites();
    }

    public static PlayerBubblesManager getInstance(PlayerModel playerModel) {
        if (instance == null) {
            instance = new PlayerBubblesManager(playerModel);
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
                b.checkCollisionWithPlayer(playerModel);
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
        popTimer -= (int) timer.getTimeDelta();
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
        double overlapAllowed = 5.0 * Constants.SCALE;        // Allow this much overlap
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
        new ChainExplosionManager(playerModel, firstPoppedBubble, bubblesShallowCopy);
    }

    public void loadBubbleSprites() {
        BufferedImage img = LoadSave.GetSprite(LoadSave.BUBBLE_BUD_SPRITE);

        playerBubbleSprites = new BufferedImage[6][4];
        for (int j = 0; j < playerBubbleSprites.length; j++)
            for (int i = 0; i < playerBubbleSprites[j].length; i++)
                playerBubbleSprites[j][i] = img.getSubimage(i * DEFAULT_W, j*DEFAULT_H, DEFAULT_W, DEFAULT_H);
    }

    public void newLevelReset() {
        bubbles.clear();
        popTimer = 0;
    }

    public void newPlayReset() {
        newLevelReset();
    }

    public void addBubble(PlayerBubble bubble) {
        bubbles.add(bubble);
    }

    public void createEmptyBubble(float x, float y, Constants.Direction direction) {
        EmptyBubble emptyBubble = new EmptyBubble(x, y, direction);
        bubbles.add(emptyBubble);
    }

    public void addDeadEnemy(Enemy e, PlayerModel playerModel) {
        EnemyBubble deadEnemyBubble = new EnemyBubble(e, e.getHitbox().x, e.getHitbox().y, playerModel.getDirection());
        deadEnemyBubble.playerPop(playerModel);
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
