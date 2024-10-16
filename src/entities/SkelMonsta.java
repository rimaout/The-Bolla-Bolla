package entities;

import model.utilz.Constants.Direction;

import java.awt.*;

import static model.utilz.Constants.Direction.*;
import static model.utilz.Constants.ANIMATION_SPEED;
import static model.utilz.Constants.EnemyConstants.*;
import static model.utilz.Constants.EnemyConstants.EnemyType.SKEL_MONSTA;

public class SkelMonsta extends Enemy{

    private int nextMoveTimer = SKEL_MONSTA_MOVEMENT_TIMER;
    private float walkedDistance = 0;

    private boolean spawning = true;
    private boolean moving = false;
    private boolean despawning = false;

    public SkelMonsta() {
        super(SKEL_MONSTA_SPAWN_X, SKEL_MONSTA_SPAWN_Y, ENEMY_W, ENEMY_H, SKEL_MONSTA, RIGHT);
        active = false;
        y = SKEL_MONSTA_SPAWN_Y;    // Set the y position to the spawn point (the super constructor sets it outside the screen, but skelMonsta spawns from the ground)
        walkingDir = RIGHT;
        previousWalkingDir = RIGHT;
        initHitbox(ENEMY_HITBOX_W, ENEMY_HITBOX_H);
    }

    public void draw(Graphics g) {
        g.drawImage(EnemyManager.getInstance().getEnemySprite(SKEL_MONSTA)[getAnimation()][animationIndex], (int) hitbox.x + flipX(), (int) hitbox.y, ENEMY_W * flipW(), ENEMY_H, null);
    }

    @Override
    public void update(Player player) {
        loadLevelManager(); // Load the level manager if it's not loaded (enemies are created before the level manager use this method to avoid null pointer exceptions)

        updateState();
        updateAnimationTick();
        updateTimer();
        calculateNextMove(player);
        updateMove(player);
        checkPlayerHit(player);
    }

    @Override
    protected void updateAnimationTick() {
        animationTick++;
        if (animationTick >= ANIMATION_SPEED) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= 2) {
                animationIndex = 0;
            }
        }
    }

    private void checkPlayerHit(Player player) {

        if (spawning || despawning || !player.isActive())
            return;

        if (hitbox.intersects(player.getHitbox())) {
            player.death();
            HurryUpManager.getInstance().restart();
        }
    }

    private void updateState() {

        if (spawning && animationIndex >= 1) {
            animationTick = 0;
            animationIndex = 0;

            spawning = false;
            moving = true;
            return;
        }

        if (despawning && animationIndex >= 1) {
            active = false;
            HurryUpManager.getInstance().newLevelReset();
        }

        if (EnemyManager.getInstance().getActiveEnemiesCount() == 0 && !despawning)
            despawn();
    }

    private void updateTimer() {
        nextMoveTimer -= (int)  timer.getTimeDelta();
    }

    private void calculateNextMove(Player player) {

        if (nextMoveTimer <= 0 && !moving) {
            if (walkingDir != UP && walkingDir != DOWN)
                previousWalkingDir = walkingDir;

            walkingDir = getDirectionToPlayer(player);
        }

        if (nextMoveTimer <= 0 && !spawning && !despawning)
            moving = true;
        else
            moving = false;
    }

    private void updateMove(Player player) {
        if (!moving)
            return;

        switch (walkingDir) {
            case UP -> moveOnYAxis(UP, player);
            case DOWN -> moveOnYAxis(DOWN, player);
            case LEFT -> moveOnXAxis(LEFT, player);
            case RIGHT -> moveOnXAxis(RIGHT, player);
        }

        updateWalkedDistance();
    }

    private void moveOnYAxis(Direction direction, Player player) {
        if (player.getTileY() == getTileY())
            return;

        switch (direction) {
            case UP -> hitbox.y -= NORMAL_WALK_SPEED;
            case DOWN -> hitbox.y += NORMAL_WALK_SPEED;
        }
    }

    private void moveOnXAxis(Direction direction, Player player) {
        if (player.getTileX() == getTileX())
            return;

        switch (direction) {
            case LEFT -> hitbox.x -= NORMAL_WALK_SPEED;
            case RIGHT -> hitbox.x += NORMAL_WALK_SPEED;
        }
    }

    private void updateWalkedDistance() {
        walkedDistance += NORMAL_WALK_SPEED;

        if (walkedDistance >= SKEL_MONSTA_MOVEMENT_MAX_DISTANCE)
            stopMove();
    }

    private Direction getDirectionToPlayer(Player player) {
        Direction upOrDown = isPlayerUpOrDown(player);
        Direction leftOrRight = isPlayerLeftOrRight(player);

        if (upOrDown == UP)
            return UP;
        else if (upOrDown == DOWN)
            return DOWN;
        else if (leftOrRight == LEFT)
            return LEFT;
        else if (leftOrRight == RIGHT)
            return RIGHT;
        else
            return NONE;
    }

    private int getAnimation() {
        if (spawning)
            return 0;
        else if (moving)
            return 1;
        else if (despawning)
            return 2;
        else
            return 1;
    }

    private void stopMove() {
        nextMoveTimer = SKEL_MONSTA_MOVEMENT_TIMER;
        walkedDistance = 0;
    }

    public void activate() {
        active = true;
    }

    public void reset() {
        active = false;

        hitbox.x = SKEL_MONSTA_SPAWN_X;
        hitbox.y = SKEL_MONSTA_SPAWN_Y;
        walkingDir = RIGHT;
        previousWalkingDir = RIGHT;

        animationIndex = 0;
        animationTick = 0;
        nextMoveTimer = SKEL_MONSTA_MOVEMENT_TIMER;
        spawning = true;
        moving = false;
        despawning = false;
        walkedDistance = 0;
    }

    public void despawn() {
        animationTick = 0;
        animationIndex = 0;

        despawning = true;
        moving = false;
    }

    @Override
    public EnemyType getEnemyType() {
        return SKEL_MONSTA;
    }
}
