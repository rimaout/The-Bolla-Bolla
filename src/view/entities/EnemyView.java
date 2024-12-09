package view.entities;

import model.entities.EnemyModel;

import static view.utilz.Constants.ANIMATION_SPEED;
import static model.utilz.Constants.EnemyConstants.*;
import static view.utilz.Constants.EnemyConstants.*;

public class EnemyView {
    protected final EnemyModel enemyModel;
    protected final EnemyManagerView enemyManagerView = EnemyManagerView.getInstance();

    protected int animationIndex, animationTick;
    protected int animationAction = WALKING_ANIMATION_NORMAL;
    protected float animationSpeedMultiplier = NORMAL_ANIMATION_SPEED_MULTIPLIER;

    public EnemyView(EnemyModel enemyModel) {
        this.enemyModel = enemyModel;
    }

    public void update() {
        updateAnimationTick();
        updateStateVariables();
    }

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

    public boolean isActive() {
        return enemyModel.isActive();
    }

    public boolean isAlive() {
        return enemyModel.isAlive();
    }

    public int getAnimationIndex() {
        return animationIndex;
    }
}

