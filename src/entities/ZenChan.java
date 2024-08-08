package entities;

import Utillz.Constants;
import main.Game;

import static Utillz.Constants.Directions.LEFT;
import static Utillz.Constants.EnemyConstants.*;
import static Utillz.HelpMethods.*;

public class ZenChan extends Enemy {
    public ZenChan(float x, float y) {
        super(x, y, ENEMY_WIDTH, ENEMY_HEIGHT, ZEN_CHAN);
        initHitbox(x, y, ZEN_CHAN_HITBOX_WIDTH, ZEN_CHAN_HITBOX_HEIGHT);
    }

    public void update(int[][] lvlData, Player player) {
        updateMove(lvlData, player);
        updateAnimationTick();
    }

    private void updateMove(int[][] levelData, Player player) {
        if (firstUpdate)
            firstUpdate(levelData);

        if (inAir)
            updateInAir(levelData);

        else {
            nextMove(levelData, player);
        }
    }

    private void nextMove(int[][] levelData, Player player) {
        switch (enemyState) {
            case WALKING:

                // qualcosa non funziona

                if (canSeePlayer(levelData, player)) {
                    turnTowardsPlayer(player);
                    if (isPlayerInAttackRange(player))
                        attack(player);
                }
                move(levelData);
        }
    }

    private void move(int[][] levelData) {
        float xSpeed = 0;

        if (walkingDir == LEFT)
            xSpeed = -walkingSpeed;
        else
            xSpeed = walkingSpeed;

        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, levelData)) {
            if (WillEntityBeOnFloor(hitbox, xSpeed, levelData)) {
                hitbox.x += xSpeed;
                return;
            }
        }
        changeWalkingDir();
    }

    private void attack(Player player) {
        // Zen Chan attacks and player dies
        System.out.println("Zen Chan attacks player");
    }

    private void fly() {
        // Zen Chan flies
    }

    private void jump() {
        // Zen Chan walks
    }
}