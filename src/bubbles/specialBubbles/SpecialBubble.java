package bubbles.specialBubbles;

import java.awt.*;

import bubbles.Bubble;
import model.entities.PlayerModel;
import model.utilz.Constants;

import static model.utilz.Constants.Bubble.BUBBLE;

public abstract class SpecialBubble extends Bubble {
    protected final SpecialBubbleManager bubbleManager = SpecialBubbleManager.getInstance();

    public SpecialBubble(float x, float y, Constants.Direction direction) {
        super(x, y, direction);
        this.state = BUBBLE;
    }

    public abstract void draw(Graphics g);
    public abstract void update();
    public abstract void playerPop(PlayerModel playerModel);

    @Override
    public void checkCollisionWithPlayer(PlayerModel playerModel) {
        if (!playerModel.isActive())
            return;

        if (isPlayerPoppingBubble(playerModel)) {
            playerPop(playerModel);
            return;
        }

        if (isPlayerTouchingBubble(playerModel)) {

            if (isPlayerJumpingOnBubble(playerModel)) {
                bounceDown();
                playerModel.jumpOnBubble();
                return;
            }

            handlePlayerPush(playerModel);
        }
    }
}
