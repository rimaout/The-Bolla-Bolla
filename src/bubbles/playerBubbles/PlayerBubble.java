package bubbles.playerBubbles;

import bubbles.Bubble;
import entities.Player;
import utilz.Constants;

import java.awt.*;

import static utilz.Constants.Bubble.*;

public abstract class PlayerBubble extends Bubble {

    public PlayerBubble(float x, float y, Constants.Direction direction) {
        super(x, y, direction);
    }

    public abstract void draw(Graphics g);

    protected abstract void updateDeadAnimation();

    public abstract void playerPop(Player player, int EnemyBubblePopCounter, ChainExplosionManager chainExplosionManager);

    @Override
    public void update() {
        if (isFirstUpdate)
            firstUpdate();

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
    }

    @Override
    public void checkCollisionWithPlayer(Player player) {
        PlayerBubblesManager manager = PlayerBubblesManager.getInstance();

        if (!active || state == DEAD)
            return;

        // check if bubble pop
        if (manager.getPopTimer() <= 0 && isPlayerPoppingBubble(player)) {
            manager.startChainExplosions(this);
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

    protected void firstUpdate() {
        isFirstUpdate = false;

        normalTimer = NORMAL_TIMER;
        redTimer = RED_TIMER;
        blinkingTimer = BLINKING_TIMER;
        lastTimerUpdate = System.currentTimeMillis();
    }

    private void updateTimers() {
        long timeDelta = System.currentTimeMillis() - lastTimerUpdate;
        lastTimerUpdate = System.currentTimeMillis();

        if (state == NORMAL)
            normalTimer -= timeDelta;

        if (state == RED)
            redTimer -= timeDelta;

        if (state == BLINKING)
            blinkingTimer -= timeDelta;
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

        if (state == POP_RED || state == POP_NORMAL)
            if (animationIndex == 2)
                active = false;

        if (startAnimation != state) {
            animationTick = 0;
            animationIndex = 0;
        }
    }
}
