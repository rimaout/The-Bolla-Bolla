package com.rima.view.bubbles.playerBubbles;

import com.rima.model.bubbles.BubbleModel;
import com.rima.model.bubbles.playerBubbles.EnemyBubbleModel;
import com.rima.model.utilz.Constants.EnemyConstants.*;
import com.rima.view.utilz.Constants.AudioConstants;
import com.rima.view.audio.AudioPlayer;
import com.rima.view.entities.EnemyManagerView;

import java.awt.*;

import static com.rima.model.utilz.Constants.Bubble.*;
import static com.rima.model.utilz.Constants.EnemyConstants.ENEMY_HITBOX_OFFSET_X;
import static com.rima.view.utilz.Constants.EnemyConstants.*;

/**
 * The EnemyBubbleView class represents the view for an {@link EnemyBubbleModel}.
 * It handles updating the bubble's animation, drawing the bubble on the screen, and playing sound effects.
 */
public class EnemyBubbleView extends EmptyBubbleView {
    private final EnemyManagerView enemyManagerView = EnemyManagerView.getInstance();

    /**
     * Constructs an EnemyBubbleView with the specified BubbleModel.
     *
     * @param bubbleModel the model for the bubble entity
     */
    public EnemyBubbleView(BubbleModel bubbleModel) {
        super(bubbleModel);
    }

    /**
     * Updates the bubble's state and animation.
     */
    @Override
    public void update() {
        updateAnimationTick();
        checkAnimationResetAfterPop();
    }

    /**
     * Draws the enemy bubble on the screen.
     *
     * @param g the Graphics object to draw with
     */
    @Override
    public void draw(Graphics g) {
        EnemyBubbleModel enemyBubbleModel = (EnemyBubbleModel) bubbleModel;
        EnemyType enemyType = enemyBubbleModel.getEnemyModel().getEnemyType();

        if (bubbleModel.getState() == NORMAL)
            g.drawImage(enemyManagerView.getEnemySprite(enemyType)[BOBBLE_GREEN_ANIMATION][animationIndex], (int) (bubbleModel.getHitbox().x - ENEMY_HITBOX_OFFSET_X), (int) (bubbleModel.getHitbox().y - HITBOX_OFFSET_Y), W, H, null);

        else if (bubbleModel.getState() == RED || bubbleModel.getState() == BLINKING)
            g.drawImage(enemyManagerView.getEnemySprite(enemyType)[BOBBLE_RED_ANIMATION][animationIndex], (int) (bubbleModel.getHitbox().x - HITBOX_OFFSET_X), (int) (bubbleModel.getHitbox().y - HITBOX_OFFSET_Y), W, H, null);

        else if (bubbleModel.getState() == POP_NORMAL)
            g.drawImage(enemyManagerView.getEnemySprite(enemyType)[BOBBLE_GREEN_POP_ANIMATION][animationIndex], (int) (bubbleModel.getHitbox().x - HITBOX_OFFSET_X), (int) (bubbleModel.getHitbox().y - HITBOX_OFFSET_Y), W, H, null);

        else if (bubbleModel.getState() == POP_RED)
            g.drawImage(enemyManagerView.getEnemySprite(enemyType)[BOBBLE_RED_POP_ANIMATION][animationIndex], (int) (bubbleModel.getHitbox().x - HITBOX_OFFSET_X), (int) (bubbleModel.getHitbox().y - HITBOX_OFFSET_Y), W, H, null);

        else if (bubbleModel.getState() == DEAD)
            g.drawImage(enemyManagerView.getEnemySprite(enemyType)[DEAD_ANIMATION][animationIndex], (int) (bubbleModel.getHitbox().x - HITBOX_OFFSET_X), (int) (bubbleModel.getHitbox().y - HITBOX_OFFSET_Y), W, H, null);

        // Play sound effect when bubble is popped by player
        if (enemyBubbleModel.isPlayerPopped() && !soundPlayed) {
            soundPlayed = true;
            AudioPlayer.getInstance().playSoundEffect(AudioConstants.ENEMY_BUBBLE_POP);
        }
    }

    /**
     * Checks and resets the animation after the bubble is popped.
     */
    private void checkAnimationResetAfterPop() {
        if (animationReset)
            return;

        EnemyBubbleModel enemyBubbleModel = (EnemyBubbleModel) this.bubbleModel;

        // Reset animation index and tick when bubble is popped
        if (enemyBubbleModel.getState() == POP_NORMAL || enemyBubbleModel.getState() == POP_RED) {
            animationReset = false;
            animationIndex = 0;
            animationTick = 0;
        }

        // Reset animation index and tick when bubble is popped by player
        else if (enemyBubbleModel.isPlayerPopped()) {
            animationReset = true;
            animationIndex = 0;
            animationTick = 0;
        }
    }
}