package entities;

import main.Game;

import static Utillz.HelpMethods.*;
import static Utillz.Constants.EnemyConstants.*;
import static Utillz.Constants.Directions.*;

public abstract class Enemy extends Entity {
    protected int enemyState, enemyType;
    protected int aniIndex, aniTick, aniSpeed = 50;
    protected boolean inAir, firstUpdate = true;
    protected int tileX, tileY;

    protected float fallSpeed = 0.6f * Game.SCALE;
    protected float walkingSpeed = 0.5f * Game.SCALE;
    protected int walkingDir = LEFT;

    protected float attackRange = Game.TILES_SIZE;

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
            }
        }
    }

    protected void firstUpdate(int[][] levelData) {
        if (!IsEntityOnFloor(hitbox, levelData))
            inAir = true;
        firstUpdate = false;
    }

    protected void updateInAir(int[][] levelData) {
        // Falling
        if (CanMoveHere(hitbox.x, hitbox.y + fallSpeed, hitbox.width, hitbox.height, levelData)) {
            hitbox.y += fallSpeed;

        } else {
            inAir = false;
            hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, fallSpeed);
            tileY = (int) (hitbox.y / Game.TILES_SIZE);
        }
    }

    protected boolean canSeePlayer(int[][] levelData, Player player) {
        int playerTileY = (int) (player.getHitbox().y / Game.TILES_SIZE);

        return playerTileY == tileY                                             // Same row
                && isPlayerInViewingRange(player)                               // Player is in range
                && IsSightClear(levelData, hitbox, player.hitbox, tileY);       // No obstacles in the way
    }

    protected boolean isPlayerInViewingRange(Player player) {
        return Math.abs(player.getHitbox().x - hitbox.x) <= attackRange * 5;
    }

    protected boolean isPlayerInAttackRange(Player player) {
        return Math.abs(player.getHitbox().x - hitbox.x) <= attackRange;
    }

    protected void turnTowardsPlayer(Player player) {
        if (player.getHitbox().x < hitbox.x)
            walkingDir = LEFT;
        else
            walkingDir = RIGHT;
    }

    protected void newState(int enemyState) {
        this.enemyState = enemyState;
        aniIndex = 0;
        aniTick = 0;
    }

    protected void changeWalkingDir() {
        if(walkingDir == LEFT)
            walkingDir = RIGHT;
        else
            walkingDir = LEFT;
    }

    public int getAniIndex() {
        return aniIndex;
    }

    public int getEnemyState() {
        return enemyState;
    }
}