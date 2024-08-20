package bubbles;

import entities.Player;
import gameStates.Playing;
import levels.LevelManager;
import main.Game;
import utilz.LoadSave;

import static java.lang.Math.abs;
import static utilz.HelpMethods.*;
import static utilz.Constants.Direction;
import static utilz.Constants.PlayerBubble.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class BubbleManager {

    private static BubbleManager instance;
    private Playing playing;

    private int[][] levelData;
    private Direction[][] windDirectionData;

    private BufferedImage[][] bubbleSprites;
    private LinkedList<PlayerBubble> bubbles;

    private BubbleManager(Playing playing) {
        this.playing = playing;
        bubbles = new LinkedList<>();

        loadBubbleSprites();
        loadLevelData();
        loadWindData();
    }

    public static BubbleManager getInstance(Playing playing) {
        if (instance == null) {
            instance = new BubbleManager(playing);
        }
        return instance;
    }

    public static BubbleManager getInstance() {
        return instance;
    }

    public void update() {
        for (PlayerBubble b : bubbles) {
            if (b.isActive())
                b.update();
            // TODO
//            else
//                bubbles.remove(b);
        }

        collisionWithPlayer();
        collisionBetweenBubbles();
    }

    public void draw(Graphics g) {

        for (PlayerBubble b : bubbles) {
            if (!b.isActive())
                continue;

            g.drawImage(bubbleSprites[b.getState()][b.getAnimationIndex()], (int) (b.getHitbox().x - HITBOX_OFFSET_X), (int) (b.getHitbox().y - HITBOX_OFFSET_Y), IMMAGE_W, IMMAGE_H, null);
            //b.drawCollisionBox(g);
            //b.drawHitbox(g);
        }
    }

   private void collisionBetweenBubbles() {
    for (PlayerBubble b1 : bubbles) {
        if (!b1.isActive())
            continue;

        for (PlayerBubble b2 : bubbles) {
            if (!b2.isActive() || b1 == b2)
                continue;
            applyRepulsion(b1, b2);
        }
    }
}

    void applyRepulsion(PlayerBubble bubble1, PlayerBubble bubble2) {
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
            bubble1.getHitbox().x -= dx * overlap * repulsionStrength;
            bubble1.getHitbox().y -= dy * overlap * repulsionStrength;
            bubble2.getHitbox().x += dx * overlap * repulsionStrength;
            bubble2.getHitbox().x += dy * overlap * repulsionStrength;

            // Introduce randomness
            double randomAdjustment = 1.0; // Adjust this value as needed
            bubble1.getHitbox().x += (Math.random() - 0.5) * randomAdjustment;
            bubble1.getHitbox().y += (Math.random() - 0.5) * randomAdjustment;
            bubble2.getHitbox().x += (Math.random() - 0.5) * randomAdjustment;
            bubble2.getHitbox().y += (Math.random() - 0.5) * randomAdjustment;
        }
    }

    private void collisionWithPlayer() {
        Player player = playing.getPlayer();
        float collisionThreshold = 5.0f;

        for (PlayerBubble b : bubbles) {

            // skip inactive bubbles
            if (!b.isActive())
                continue;

            // check if bubble pop
            if (b.getInternalCollisionBox().intersects(player.getHitbox())) {
                b.pop();
                return;
            }

            // check player jump on bubble
            if (b.getExternalCollisionBox().intersects(player.getHitbox())) {

                if (b.getHitbox().y > player.getHitbox().y) {
                    if (player.isJumpActive()) {

                        b.getHitbox().y += 3;
                        //b.setPush(DOWN, -5f);
                        player.jumpOnBubble();
                        return;
                    }
                }

                // left push
                if (b.getHitbox().x + b.getHitbox().width - 10 < player.getHitbox().x) {
                    b.getHitbox().x -= abs(player.getXSpeed());
            }

                // right push
                else if (b.getHitbox().x - 10 > player.getHitbox().x) {
                    b.getHitbox().x += abs(player.getXSpeed());
                }

            }
        }
    }

    public void triggerChainExplosion(PlayerBubble poppedBubble) {
        Timer timer = new Timer();
        int delay = 100; // delay in milliseconds
        int delayIncrement = 100; // increment delay for each bubble

        for (PlayerBubble b : bubbles) {
            if (!b.isActive() || b == poppedBubble)
                continue;

            double dx = b.getCenter().x - poppedBubble.getCenter().x;
            double dy = b.getCenter().y - poppedBubble.getCenter().y;
            double distance = Math.sqrt(dx * dx + dy * dy);

            // Define the chain reaction radius
            double chainReactionRadius = 15.0 * Game.SCALE; // Adjust as needed

            if (distance < chainReactionRadius) {
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        b.pop();
                    }
                }, delay);
                delay += delayIncrement;
            }
        }
    }

    private void collisionWithEnemies() {
        // TODO
        // enemy captured
    }

    public void addBubble(float x, float y, Direction direction) {
        int tileX = (int) x / Game.TILES_SIZE;

        int mapLeftMostTileX = 3 * Game.TILES_SIZE;
        int mapRightMostTileX = 28 * Game.TILES_SIZE;

        // check if is not perimeter wall
        if (direction == Direction.LEFT)
            if (!IsTilePerimeterWall(tileX))
                bubbles.add(new PlayerBubble(x, y, direction, levelData, windDirectionData));
            else
                bubbles.add(new PlayerBubble(mapLeftMostTileX, y, direction, levelData, windDirectionData));

        if (direction == Direction.RIGHT)
            if (!IsTilePerimeterWall(tileX))
                bubbles.add(new PlayerBubble(x, y, direction, levelData, windDirectionData));
            else
                bubbles.add(new PlayerBubble( mapRightMostTileX - IMMAGE_W, y, direction, levelData, windDirectionData));
    }

    public void loadBubbleSprites() {
        // Load bubble sprites
        BufferedImage img = LoadSave.GetSprite(LoadSave.BUBBLE_BUD_SPRITE);

        bubbleSprites = new BufferedImage[5][4];
        for (int j = 0; j < bubbleSprites.length; j++)
            for (int i = 0; i < bubbleSprites[j].length; i++)
                bubbleSprites[j][i] = img.getSubimage(i * DEFAULT_W, j*DEFAULT_H, DEFAULT_W, DEFAULT_H);
    }

    public void loadLevelData() {
        levelData = LevelManager.getInstance().getCurrentLevel().getLevelData();
    }

    public void loadWindData() {
        windDirectionData = LevelManager.getInstance().getCurrentLevel().getWindDirectionData();
    }

    public void resetAll() {
        // Reset the bubble manager
        bubbles = new LinkedList<>();
    }
}
