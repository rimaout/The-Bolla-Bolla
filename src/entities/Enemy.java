package entities;

import main.Game;

import static utilz.Constants.ANIMATION_SPEED;
import static utilz.HelpMethods.*;
import static utilz.Constants.EnemyConstants.*;
import static utilz.Constants.Directions.*;

public abstract class Enemy extends Entity {
    protected boolean active = true;
    protected int animationAction = WALKING_ANIMATION_NORMAL;
    protected int enemyState, enemyType;
    protected boolean firstUpdate = true;

    // Enemy Movement Variables
    protected float xSpeed;
    protected float ySpeed;
    protected float fallSpeed;
    protected float flySpeed;
    protected float jumpSpeed;
    protected float walkSpeed;
    protected int tileX, tileY;
    protected int walkingDir = LEFT;

    // Player info
    protected int playerTileX, playerTileY;

    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
    }

    protected void updateAnimationTick() {
        animationTick++;
        if (animationTick >= ANIMATION_SPEED) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= getSpriteAmount(enemyType, enemyState)) {
                animationIndex = 0;

                switch (enemyState) {
                    case NORMAL_STATE:
                        fallSpeed = NORMAL_FALL_SPEED;
                        flySpeed = NORMAL_FLY_SPEED;
                        jumpSpeed = NORMAL_JUMP_SPEED;
                        walkSpeed = NORMAL_WALK_SPEED;
                        animationAction = WALKING_ANIMATION_NORMAL;
                        break;

                    case HUNGRY_STATE:
                        fallSpeed = HUNGRY_FALL_SPEED;
                        flySpeed = HUNGRY_FLY_SPEED;
                        jumpSpeed = HUNGRY_JUMP_SPEED;
                        walkSpeed = HUNGRY_WALK_SPEED;
                        animationAction = WALKING_ANIMATION_HUNGRY;
                        break;

                    case BOBBLE_STATE:
                        animationAction = BOBBLE_GREEN_ANIMATION;
                        break;

                    case DEAD_STATE:
                        active = false;
                        animationAction = DEAD_ANIMATION;
                        break;
                }
            }
        }
    }

    protected boolean canSeePlayer(int[][] levelData, Player player) {
        int playerTileY = (int) (player.getHitbox().y / Game.TILES_SIZE);

        return playerTileY == tileY                                             // Same row
                && isPlayerInViewingRange(player)                               // Player is in range
                && IsSightClear(levelData, hitbox, player.hitbox, tileY);       // No obstacles in the way
    }

    protected boolean isPlayerInViewingRange(Player player) {
        return Math.abs(player.getHitbox().x - hitbox.x) <= VIEWING_RANGE;
    }

    protected boolean isPlayerInAttackRange(Player player) {
        return Math.abs(player.getHitbox().x - hitbox.x) <= ATTACK_RANGE;
    }

    protected void calculatePlayersPos(Player player) {
        playerTileX = (int) (player.getHitbox().x / Game.TILES_SIZE);
        playerTileY = (int)(player.getHitbox().y / Game.TILES_SIZE);
    }

    protected void changeWalkingDir() {
        if (walkingDir == LEFT)
            walkingDir = RIGHT;
        else
            walkingDir = LEFT;
    }

    protected int flipX() {
        if (walkingDir == RIGHT)
            return width;
        else
            return 0;
    }

    protected int flipW() {
        if (walkingDir == RIGHT)
            return -1;
        else
            return 1;
    }

    protected void resetEnemy() {
        hitbox.x = x;
        hitbox.y = y;
        animationIndex = 0;
        animationTick = 0;
        firstUpdate = true;
        active = true;
        enemyState = NORMAL_STATE;
        walkingDir = LEFT;
    }

    public int getEnemyState() {
        return enemyState;
    }

    public boolean isActive() {
        return active;
    }
}