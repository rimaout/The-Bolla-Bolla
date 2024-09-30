package bubbles.specialBubbles;

import java.awt.*;

import bubbles.Bubble;
import entities.Player;
import utilz.Constants;

import static utilz.Constants.Bubble.BUBBLE;

public abstract class SpecialBubble extends Bubble {
    protected final SpecialBubbleManager bubbleManager = SpecialBubbleManager.getInstance();

    public SpecialBubble(float x, float y, Constants.Direction direction) {
        super(x, y, direction);
        this.state = BUBBLE;
    }

    public abstract void draw(Graphics g);
    public abstract void update();
    public abstract void playerPop(Player player);

    @Override
    public void checkCollisionWithPlayer(Player player) {
        if (!player.isActive())
            return;

        if (isPlayerPoppingBubble(player)) {
            playerPop(player);
            return;
        }

        if (isPlayerTouchingBubble(player)) {

            if (isPlayerJumpingOnBubble(player)) {
                bounceDown();
                player.jumpOnBubble();
                return;
            }

            handlePlayerPush(player);
        }
    }
}
