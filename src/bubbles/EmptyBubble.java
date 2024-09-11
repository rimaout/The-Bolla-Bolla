package bubbles;

import entities.Player;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utilz.Constants.Direction;
import static utilz.Constants.Bubble.*;

public class EmptyBubble extends Bubble {

    public EmptyBubble(float x, float y, Direction direction) {
        super(x, y, direction);
    }

    public void draw(Graphics g) {
        BufferedImage[][] playerBubbleSprites = BubbleManager.getInstance().getPlayerBubbleSprites();
        g.drawImage(playerBubbleSprites[state][animationIndex], (int) (hitbox.x - HITBOX_OFFSET_X), (int) (hitbox.y - HITBOX_OFFSET_Y), IMMAGE_W, IMMAGE_H, null);
    }

    protected void updateDeadAnimation(){
        // Empty implementation, only used by EnemyBubble
    }

    public void playerPop(Player player, int EnemyBubblePopCounter, ChainExplosionManager chainExplosionManager) {
        if (state != PROJECTILE && state != POP_NORMAL && state != POP_RED) {

            if (state == RED)
                state = POP_RED;
            else
                state = POP_NORMAL;

            animationIndex = 0;
            animationTick = 0;
        }
    }
}
