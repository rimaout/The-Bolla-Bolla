package model.bubbles.playerBubbles;

import model.entities.PlayerModel;

import static model.Constants.Bubble.*;
import static model.Constants.Bubble.BubbleType.EMPTY_BUBBLE;
import static model.Constants.Direction;

public class EmptyBubbleModel extends PlayerBubbleModel {

    public EmptyBubbleModel(float x, float y, Direction direction) {
        super(x, y, direction);
        this.bubbleType = EMPTY_BUBBLE;
    }

    protected void updateDeadAction(){
        // Empty implementation, only used by EnemyBubble
    }

    public void pop() {
        if (state != POP_NORMAL && state != POP_RED) {

            if (state == RED)
                state = POP_RED;
            else
                state = POP_NORMAL;
        }
    }

    public void playerPop(PlayerModel playerModel, int EnemyBubblePopCounter, ChainExplosionManager chainExplosionManager) {
       pop();
    }
}
