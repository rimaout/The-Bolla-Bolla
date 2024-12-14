package model.bubbles.playerBubbles;

import model.utilz.Constants;
import model.bubbles.BubbleModel;
import model.entities.PlayerModel;

import static model.utilz.Constants.Bubble.*;

public abstract class PlayerBubbleModel extends BubbleModel {
    protected final PlayerBubblesManagerModel bubbleManager = PlayerBubblesManagerModel.getInstance();

    public PlayerBubbleModel(float x, float y, Constants.Direction direction) {
        super(x, y, direction);
    }

    protected abstract void updateDeadAction();

    public abstract void pop();
    public abstract void playerPop(PlayerModel playerModel, int EnemyBubblePopCounter, ChainExplosionManager chainExplosionManager);

    @Override
    public void update() {
        initLevelManager();
        updateTimers();
        setState();

        if (state == DEAD)
            updateDeadAction();

        else {
            updateDirection();
            updatePosition();
        }

        updateCollisionBoxes();
        pacManEffect();
    }

    @Override
    public void checkCollisionWithPlayer(PlayerModel playerModel) {
        if (!playerModel.isActive())
            return;

        if (!active || state == DEAD)
            return;

        // check if bubble pop
        if (bubbleManager.getPopTimer() <= 0 && isPlayerPoppingBubble(playerModel)) {
            bubbleManager.startChainExplosions(this);
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

    @Override
    protected void updateTimers() {
        super.updateTimers();

        if (state == NORMAL)
            normalTimer -= (int) timer.getTimeDelta();

        if (state == RED)
            redTimer -= (int) timer.getTimeDelta();

        if (state == BLINKING)
            blinkingTimer -= (int) timer.getTimeDelta();

        if (state == POP_NORMAL || state == POP_RED)
            popTimer -= (int) timer.getTimeDelta();
    }

    private void setState() {
        if (state == NORMAL && normalTimer <= 0) {
            previousState = state;
            state = RED;
        }

        if (state == RED && redTimer <= 0) {
            previousState = state;
            state = BLINKING;
        }

        if (state == BLINKING && blinkingTimer <= 0) {
            previousState = state;
            state = POP_RED;

            if (this instanceof EnemyBubbleModel)
                ((EnemyBubbleModel) this).respawnEnemy();
        }

        if (state == POP_RED || state == POP_NORMAL && popTimer <= 0)
                active = false;
    }
}
