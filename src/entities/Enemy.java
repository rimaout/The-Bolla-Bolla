package entities;

import main.Game;

import static utilz.HelpMethods.*;
import static utilz.Constants.EnemyConstants.*;
import static utilz.Constants.Directions.*;

public abstract class Enemy extends Entity {
    protected boolean active = true;
    protected int animationAction = WALKING_ANIMATION_NORMAL;
    protected int enemyState, enemyType;
    protected int aniIndex, aniTick, aniSpeed = 50;
    protected boolean firstUpdate = true;
    protected int tileX, tileY;

    protected float xSpeed = 0;
    protected float ySpeed = 0;
    protected int walkingDir = LEFT;

    protected float fallSpeed = 0.3f * Game.SCALE;
    protected float flySpeed = 0.25f * Game.SCALE;
    protected float jumpSpeed = - 0.42f * Game.SCALE;
    protected float walkSpeed = 0.3f * Game.SCALE;

    protected float NORMAL_FALL_SPEED = 0.3f * Game.SCALE;
    protected float NORMAL_FLY_SPEED = 0.25f * Game.SCALE;
    protected float NORMAL_JUMP_SPEED = - 0.42f * Game.SCALE;
    protected float NORMAL_WALK_SPEED = 0.3f * Game.SCALE;

    protected float HUNGRY_FALL_SPEED = 0.3f * Game.SCALE;
    protected float HUNGRY_FLY_SPEED = 0.25f * Game.SCALE;
    protected float HUNGRY_JUMP_SPEED = - 0.42f * Game.SCALE;
    protected float HUNGRY_WALK_SPEED = 0.3f * Game.SCALE;

    protected float BOBBLE_X_SPEED = 0.15f * Game.SCALE;
    protected float BOBBLE_Y_SPEED = 0.15f * Game.SCALE;

    protected final float ATTACK_RANGE = Game.TILES_SIZE;
    protected final float VIEWING_RANGE = Game.TILES_SIZE * 5;
    protected final float GRAVITY = 0.0078f * Game.SCALE;

    // Player info
    protected int playerTileX, playerTileY;

    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        initHitbox(x, y, width, height);
    }

    protected void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= getSpriteAmount(enemyType, enemyState)) {
                aniIndex = 0;

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

    protected void getPlayersPos(Player player) {
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
        aniIndex = 0;
        aniTick = 0;
        firstUpdate = true;
        active = true;
        enemyState = NORMAL_STATE;
        walkingDir = LEFT;
    }

    public int getAniIndex() {
        return aniIndex;
    }

    public int getEnemyState() {
        return enemyState;
    }

    public boolean isActive() {
        return active;
    }
}