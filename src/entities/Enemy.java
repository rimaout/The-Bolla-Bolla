package entities;

import main.Game;

import static Utillz.HelpMethods.*;
import static Utillz.Constants.EnemyConstants.*;
import static Utillz.Constants.Directions.*;

public abstract class Enemy extends Entity {
    private int animationIndex, enemyState, enemyType;
    private int aniTick, aniSpeed = 50;
    private boolean inAir, firstUpdate = true;

    private float fallSpeed = 0.6f * Game.SCALE;
    private float walkingSpeed = 0.5f * Game.SCALE;
    private int walkingDir = LEFT;

    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        initHitbox(x, y, width, height);
    }

    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            animationIndex++;
            if (animationIndex >= getSpriteAmount(enemyType, enemyState)) {
                animationIndex = 0;
            }
        }
    }
    public void update(int[][] lvlData) {
        updateMove(lvlData);
        updateAnimationTick();
    }

    private void updateMove(int[][] levelData){

        if(firstUpdate) {
            if (!IsEntityOnFloor(hitbox, levelData))
                inAir = true;
            firstUpdate = false;
        }

        if(inAir) {
            // Falling
            if (CanMoveHere(hitbox.x, hitbox.y + fallSpeed, hitbox.width, hitbox.height, levelData)) {
                hitbox.y += fallSpeed;

            } else {
                inAir = false;
                hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, fallSpeed);
            }
        }
        else {
            // Patrolling
            switch (enemyState) {
                case WALKING:
                    float xSpeed = 0;

                    if(walkingDir == LEFT)
                        xSpeed = -walkingSpeed;
                    else
                        xSpeed = walkingSpeed;

                    if(CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, levelData)) {
                        // Test: this might not work
                        if(WillEntityBeOnFloor(hitbox, xSpeed, levelData)) {
                           hitbox.x += xSpeed;
                           return;
                       }
                    }

                    changeWalkingDir();
                    break;
            }
        }
    }

    public void changeWalkingDir() {
        if(walkingDir == LEFT)
            walkingDir = RIGHT;
        else
            walkingDir = LEFT;
    }

    public int getAnimationIndex() {
        return animationIndex;
    }

    public int getEnemyState() {
        return enemyState;
    }
}
