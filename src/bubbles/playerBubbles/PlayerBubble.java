package bubbles.playerBubbles;

import java.awt.*;

import bubbles.Bubble;
import entities.Player;
import utilz.Constants;

import static utilz.Constants.Bubble.*;

public abstract class PlayerBubble extends Bubble {
    protected final PlayerBubblesManager bubbleManager = PlayerBubblesManager.getInstance();

    public PlayerBubble(float x, float y, Constants.Direction direction) {
        super(x, y, direction);
    }

    public abstract void draw(Graphics g);

    protected abstract void updateDeadAnimation();

    public abstract void pop();
    public abstract void playerPop(Player player, int EnemyBubblePopCounter, ChainExplosionManager chainExplosionManager);

    @Override
    public void update() {
        updateTimers();
        updateAnimationTick();
        setState();

        if (state == DEAD)
            updateDeadAnimation();

        else {
            updateDirection();
            updatePosition();
        }

        updateCollisionBoxes();
        pacManEffect();
    }

    @Override
    public void checkCollisionWithPlayer(Player player) {
        if (!player.isActive())
            return;

        if (!active || state == DEAD)
            return;

        // check if bubble pop
        if (bubbleManager.getPopTimer() <= 0 && isPlayerPoppingBubble(player)) {
            bubbleManager.startChainExplosions(this);
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

    private void updateTimers() {

        if (state == NORMAL)
            normalTimer -= (int) timer.getTimeDelta();

        if (state == RED)
            redTimer -= (int) timer.getTimeDelta();

        if (state == BLINKING)
            blinkingTimer -= (int) timer.getTimeDelta();
    }

    private void setState() {
        int startAnimation = state;

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

            if (this instanceof EnemyBubble)
                ((EnemyBubble) this).respawnEnemy();
        }

        if (state == POP_RED || state == POP_NORMAL) {
            if (animationIndex == 2)
                active = false;
        }

        if (startAnimation != state) {
            animationTick = 0;
            animationIndex = 0;
        }
    }
}
