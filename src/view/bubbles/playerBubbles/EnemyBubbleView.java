package view.bubbles.playerBubbles;

import model.bubbles.BubbleModel;
import model.bubbles.playerBubbles.EnemyBubbleModel;
import view.Constants.AudioConstants;
import view.audio.AudioPlayer;
import view.entities.EnemyManagerView;

import java.awt.*;

import static model.Constants.Bubble.*;
import static model.Constants.EnemyConstants.*;
import static view.Constants.EnemyConstants.*;

public class EnemyBubbleView extends EmptyBubbleView {
    private final EnemyManagerView enemyManagerView = EnemyManagerView.getInstance();

    public EnemyBubbleView(BubbleModel bubbleModel) {
        super(bubbleModel);
    }

    @Override
    public void update() {
        updateAnimationTick();
        checkAnimationResetAfterPop();
    }

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