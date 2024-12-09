package model.bubbles.specialBubbles;

import model.bubbles.BubbleModel;
import model.entities.PlayerModel;
import model.utilz.Constants;

import static model.utilz.Constants.Bubble.*;

public abstract class SpecialBubbleModel extends BubbleModel {
    protected final SpecialBubbleManagerModel bubbleManager = SpecialBubbleManagerModel.getInstance();

    public SpecialBubbleModel(float x, float y, Constants.Direction direction) {
        super(x, y, direction);
        this.state = BUBBLE;
    }

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