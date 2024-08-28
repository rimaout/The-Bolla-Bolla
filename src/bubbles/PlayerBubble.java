package bubbles;

import entities.Player;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utilz.Constants.Direction;
import static utilz.Constants.Bubble.*;

public class PlayerBubble extends Bubble {

    public PlayerBubble(float x, float y, Direction direction, int[][] levelData, Direction[][] windLevelData) {
        super(x, y, direction, levelData, windLevelData);
    }

    public void draw(Graphics g) {
        BufferedImage[][] playerBubbleSprites = BubbleManager.getInstance().getPlayerBubbleSprites();
        g.drawImage(playerBubbleSprites[state][animationIndex], (int) (hitbox.x - HITBOX_OFFSET_X), (int) (hitbox.y - HITBOX_OFFSET_Y), IMMAGE_W, IMMAGE_H, null);
    }

    protected void updateDeadAnimation(){
        // Empty implementation
        // Only used by EnemyBubble
    }

    public void playerPop(Player player) {
        if (state != PROJECTILE && state != POP_NORMAL && state != POP_RED) {

            if (state == RED)
                state = POP_RED;
            else
                state = POP_NORMAL;

            animationIndex = 0;
            animationTick = 0;
            BubbleManager bubbleManager = BubbleManager.getInstance();
            bubbleManager.triggerChainExplosion(this);
        }
    }
}
