package entities;

import main.Game;

import static Utillz.HelpMethods.*;
import static Utillz.Constants.EnemyConstants.*;
import static Utillz.Constants.Directions.*;

public abstract class Enemy extends Entity {
    protected int enemyState, enemyType;
    protected int aniIndex, aniTick, aniSpeed = 50;
    protected boolean firstUpdate = true;
    protected int tileX, tileY;

    protected float xSpeed = 0;
    protected float ySpeed = 0;
    protected int walkingDir = LEFT;

    protected final float FALL_SPEED = 0.3f * Game.SCALE;
    protected final float FLY_SPEED = 0.25f * Game.SCALE;
    protected final float JUMP_SPEED = - 0.42f * Game.SCALE;
    protected final float WALK_SPEED = 0.3f * Game.SCALE;
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

    public int getAniIndex() {
        return aniIndex;
    }

    public int getEnemyState() {
        return enemyState;
    }
}