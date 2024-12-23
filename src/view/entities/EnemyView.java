package view.entities;

import model.entities.EnemyModel;

import static view.utilz.Constants.ANIMATION_SPEED;
import static view.utilz.Constants.EnemyConstants.*;
import static model.utilz.Constants.EnemyConstants.*;

/**
 * The EnemyView class represents the view for an {@link EnemyModel}.
 * It handles updating the enemy's animation and state variables.
 */
public class EnemyView {
    protected final EnemyModel enemyModel;

    protected int animationIndex, animationTick;
    protected int animationAction = WALKING_ANIMATION_NORMAL;
    protected float animationSpeedMultiplier = NORMAL_ANIMATION_SPEED_MULTIPLIER;

    /**
     * Constructs an EnemyView with the specified EnemyModel.
     *
     * @param enemyModel the model for the enemy entity
     */
    public EnemyView(EnemyModel enemyModel) {
        this.enemyModel = enemyModel;
    }

    /**
     * Updates the enemy's animation and state variables.
     */
    public void update() {
        updateAnimationTick();
        updateStateVariables();
    }

    /**
     * Updates the animation tick and index based on the animation speed multiplier.
     */
    protected void updateAnimationTick() {
        animationTick++;
        if (animationTick >= ANIMATION_SPEED * animationSpeedMultiplier) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= getSpriteAmount(enemyModel.getEnemyType(), enemyModel.getEnemyState())) {
                animationIndex = 0;
            }
        }
    }

    /**
     * Updates the state variables for the enemy based on its current state.
     */
    protected void updateStateVariables() {
        switch (enemyModel.getEnemyState()) {
            case NORMAL_STATE:
                animationAction = WALKING_ANIMATION_NORMAL;
                animationSpeedMultiplier = NORMAL_ANIMATION_SPEED_MULTIPLIER;
                break;

            case HUNGRY_STATE:
                animationAction = WALKING_ANIMATION_HUNGRY;
                animationSpeedMultiplier = HUNGRY_ANIMATION_SPEED_MULTIPLIER;
                break;

            case BOBBLE_STATE:
                animationAction = BOBBLE_GREEN_ANIMATION;
                animationSpeedMultiplier = NORMAL_ANIMATION_SPEED_MULTIPLIER;
                break;

            case DEAD_STATE:
                animationAction = DEAD_ANIMATION;
                animationSpeedMultiplier = NORMAL_ANIMATION_SPEED_MULTIPLIER;
                break;
        }
    }

    // ---------------- GETTERS ----------------

    /**
     * Checks if the enemy is active.
     *
     * @return true if the enemy is active, false otherwise
     */
    public boolean isActive() {
        return enemyModel.isActive();
    }

    /**
     * Returns the current animation index.
     *
     * @return the current animation index
     */
    public int getAnimationIndex() {
        return animationIndex;
    }
}