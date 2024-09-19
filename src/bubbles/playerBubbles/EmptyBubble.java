package bubbles.playerBubbles;

import entities.Player;

import java.awt.*;

import static utilz.Constants.Bubble.*;
import static utilz.Constants.Direction;

public class EmptyBubble extends PlayerBubble {

    public EmptyBubble(float x, float y, Direction direction) {
        super(x, y, direction);
    }

    public void draw(Graphics g) {
        g.drawImage(bubbleManager.getPlayerBubbleSprites()[state][animationIndex], (int) (hitbox.x - HITBOX_OFFSET_X), (int) (hitbox.y - HITBOX_OFFSET_Y), IMMAGE_W, IMMAGE_H, null);
    }

    protected void updateDeadAnimation(){
        // Empty implementation, only used by EnemyBubble
    }

    public void pop() {
        if (state != POP_NORMAL && state != POP_RED) {

            if (state == RED)
                state = POP_RED;
            else
                state = POP_NORMAL;

            animationIndex = 0;
            animationTick = 0;
        }
    }

    public void playerPop(Player player, int EnemyBubblePopCounter, ChainExplosionManager chainExplosionManager) {
       pop();
    }
}
