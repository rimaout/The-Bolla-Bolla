package bubbles;

import entities.Enemy;
import entities.Player;
import levels.LevelManager;
import main.Game;
import utilz.LoadSave;

import static java.lang.Math.abs;
import static utilz.HelpMethods.*;
import static utilz.Constants.Direction;
import static utilz.Constants.Bubble.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;

public class BubbleManager {

    private static BubbleManager instance;
    private Player player;

    private BufferedImage[][] playerBubbleSprites;

    private LinkedList<Bubble> bubbles;

    private long lastTimerUpdate;
    private int popTimer = 0;
    private final int POP_DELAY_AFTER_CHAIN_EXPLOSION = 350;

    // PowerUp Multipliers
    private float projectileSpeedMultiplier = 1;
    private float projectileDistanceMultiplier = 1;


    private BubbleManager(Player player) {
        this.player = player;

        bubbles = new LinkedList<>();

        loadBubbleSprites();
    }

    public static BubbleManager getInstance(Player player) {
        if (instance == null) {
            instance = new BubbleManager(player);
        }
        return instance;
    }

    public static BubbleManager getInstance() {
        return instance;
    }

    public void update() {
        updateTimers();

        for (Bubble b : bubbles) {
            if (b.isActive())
                b.update();
        }

        collisionWithPlayer();
        collisionBetweenBubbles();
        collisionWithEnemies();
    }

    public void draw(Graphics g) {

        for (Bubble b : bubbles) {
            if (!b.isActive())
                continue;

            b.draw(g);
            //b.drawCollisionBox(g);
            //b.drawHitbox(g);
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
        if (!b1.isActive() || b1.state == DEAD)
            continue;

        for (Bubble b2 : bubbles) {
            if (!b2.isActive() || b1 == b2 || b2.state == DEAD)
                continue;
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

    private void collisionWithPlayer() {

        for (Bubble b : bubbles) {

            // skip inactive bubbles
            if (!b.isActive() || b.getState() == DEAD)
                continue;

            // check if bubble pop
            if (popTimer <= 0)
                if (b.getInternalCollisionBox().intersects(player.getHitbox())) {
                    startChainExplosions(b);
                    return;
                }

            // check player jump on bubble
            if (b.getExternalCollisionBox().intersects(player.getHitbox())) {

                // Jump on bubble
                if (b.getHitbox().y > player.getHitbox().y) {
                    if (player.isJumpActive()) {
                        b.getHitbox().y += 2 * Game.SCALE;
                        player.jumpOnBubble();
                        return;
                    }
                }

                int correctionOffset = 5 * Game.SCALE;

                // left push
                if (b.getHitbox().x + b.getHitbox().width - correctionOffset <= player.getHitbox().x)
                    b.getHitbox().x -= abs(player.getXSpeed());

                // right push
                else if (b.getHitbox().x + correctionOffset >= player.getHitbox().x + player.getHitbox().width)
                    b.getHitbox().x += abs(player.getXSpeed());
            }
        }
    }

    private void collisionWithEnemies() {
        ArrayList<Enemy> EnemyArray = LevelManager.getInstance().getCurrentLevel().getEnemies();
        ArrayList<Bubble> EnemyBubblesToAdd = new ArrayList<>();

        for (Bubble b : bubbles) {
            if (!b.isActive() || b.state != PROJECTILE || !(b instanceof EmptyBubble))
                continue;

            for (Enemy e : EnemyArray) {
                if (!e.isActive() || e.isImmune())
                    continue;

                if (b.getExternalCollisionBox().intersects(e.getHitbox())) {
                    EnemyBubblesToAdd.add(new EnemyBubble(e, e.getHitbox().x, e.getHitbox().y, b.getDirection()));
                    b.setActive(false);
                    e.bubbleCapture();
                }
            }
        }

        bubbles.addAll(EnemyBubblesToAdd);
    }

    public void addBubble(Bubble bubble) {
        bubbles.add(bubble);
    }

    public void addBubble(float x, float y, Direction direction) {
        int tileX = (int) x / Game.TILES_SIZE;

        int mapLeftMostTileX = 3 * Game.TILES_SIZE;
        int mapRightMostTileX = 28 * Game.TILES_SIZE;
        int xOffset = 4 * Game.SCALE;

        if (direction == Direction.LEFT)
            if (!IsTilePerimeterWall(tileX))
                bubbles.add(new EmptyBubble(x + xOffset, y, direction));
            else
                bubbles.add(new EmptyBubble(mapLeftMostTileX, y, direction));

        if (direction == Direction.RIGHT)
            if (!IsTilePerimeterWall(tileX))
                bubbles.add(new EmptyBubble(x - xOffset, y, direction));
            else
                bubbles.add(new EmptyBubble( mapRightMostTileX - IMMAGE_W, y, direction));
    }

    public void addDeadEnemy(Enemy e, Player player) {
         EnemyBubble deadEnemyBubble = new EnemyBubble(e, e.getHitbox().x, e.getHitbox().y, player.getDirection());
         deadEnemyBubble.playerPop(player);
         bubbles.add(deadEnemyBubble);
    }

    public void loadBubbleSprites() {
        // Load bubble sprites
        BufferedImage img = LoadSave.GetSprite(LoadSave.BUBBLE_BUD_SPRITE);

        playerBubbleSprites = new BufferedImage[6][4];
        for (int j = 0; j < playerBubbleSprites.length; j++)
            for (int i = 0; i < playerBubbleSprites[j].length; i++)
                playerBubbleSprites[j][i] = img.getSubimage(i * DEFAULT_W, j*DEFAULT_H, DEFAULT_W, DEFAULT_H);
    }

    public void resetAll() {
        // Reset the bubble manager
        bubbles = new LinkedList<>();
        popTimer = 0;
        lastTimerUpdate = 0;

        projectileSpeedMultiplier = 1;
    }

    public BufferedImage[][] getPlayerBubbleSprites() {
        return playerBubbleSprites;
    }

    public void startChainExplosions(Bubble firstPoppedBubble) {
        popTimer = POP_DELAY_AFTER_CHAIN_EXPLOSION;

        LinkedList<Bubble> bubblesShallowCopy = new LinkedList<>(bubbles);
        new ChainExplosionManager(player, firstPoppedBubble, bubblesShallowCopy);
    }

    public void setProjectileSpeedMultiplier(float projectileSpeedMultiplier) {
        this.projectileSpeedMultiplier = projectileSpeedMultiplier;
    }

    public float getProjectileSpeedMultiplier() {
        return projectileSpeedMultiplier;
    }

    public void setProjectileDistanceMultiplier(float projectileDistanceMultiplier) {
        this.projectileDistanceMultiplier = projectileDistanceMultiplier;
    }

    public float getProjectileDistanceMultiplier() {
        return projectileDistanceMultiplier;
    }
}
