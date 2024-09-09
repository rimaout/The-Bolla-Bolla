package entities;

import utilz.Constants.EnemyConstants.EnemyType;
import utilz.Constants.Direction;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utilz.Constants.ANIMATION_SPEED;
import static utilz.Constants.Direction.*;
import static utilz.Constants.EnemyConstants.*;
import static utilz.Constants.EnemyConstants.EnemyType.SKEL_MONSTA;

public class SkelMonsta extends Enemy{

    private int nextMoveTimer;
    private long lastTimerUpdate;
    private boolean spawning, moving, despawning;

    // Movement Variables
    private float walkedDistance;

    public SkelMonsta() {

        super(SKEL_MONSTA_SPAWN_X, SKEL_MONSTA_SPAWN_Y, ENEMY_DEFAULT_W, ENEMY_DEFAULT_H, SKEL_MONSTA, RIGHT);
        initHitbox(ENEMY_DEFAULT_W, ENEMY_DEFAULT_H);
    }

    public void draw(Graphics g) {
        BufferedImage[][] sprites = EnemyManager.getInstance().getEnemySprite(SKEL_MONSTA);
        g.drawImage(sprites[getAnimation()][animationIndex], (int) hitbox.x, (int) hitbox.y, ENEMY_W, ENEMY_H, null);
    }

    @Override
    public void update(Player player) {
        if (firstUpdate)
            firstUpdate();

        updateState();
        updateAnimationTick();
        updateTimer(player);
        updateMove(player);
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

    private void updateState() {

        if (spawning && animationTick == 2) {
            spawning = false;
            moving = true;
            return;
        }

        if (despawning && animationTick == 2) {
            active = false;
            return;
        }

        int activeEnemiesCounter = EnemyManager.getInstance().getActiveEnemiesCount();

        if (activeEnemiesCounter == 1) {
            despawning = true;
            moving = false;
        }
    }

    private void firstUpdate() {
        lastTimerUpdate = System.currentTimeMillis();

        walkedDistance = 0;
        spawning = true;
        moving = false;
        despawning = false;

        firstUpdate = false;
    }

    private void updateTimer(Player player) {
        long currentTime = System.currentTimeMillis();
        long timeDelta = currentTime - lastTimerUpdate;
        lastTimerUpdate = currentTime;

        nextMoveTimer -= (int) timeDelta;


        if (nextMoveTimer <= 0 && !moving) {
            walkingDir = getDirectionToPlayer(player);
            System.out.println(walkingDir);
        }
        if (nextMoveTimer <= 0)
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
    }

    private void moveOnYAxis(Direction direction, Player player) {
        walkedDistance += NORMAL_WALK_SPEED;

        if (walkedDistance >= SKEL_MONSTA_MOVEMENT_MAX_DISTANCE)
            stopMove();

        if (player.getTileY() == getTileY())
            return;

        if (direction == UP)
            hitbox.y -= NORMAL_WALK_SPEED;

        else if (direction == DOWN)
            hitbox.y += NORMAL_WALK_SPEED;
    }

    private void moveOnXAxis(Direction direction, Player player) {
        walkedDistance += NORMAL_WALK_SPEED;

        if (walkedDistance >= SKEL_MONSTA_MOVEMENT_MAX_DISTANCE)
            stopMove();

        if (player.getTileX() == getTileX())
            return;

        if (direction == LEFT)
            hitbox.x -= NORMAL_WALK_SPEED;

        else if (direction == RIGHT)
            hitbox.x += NORMAL_WALK_SPEED;
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


    @Override
    public EnemyType getEnemyType() {
        return SKEL_MONSTA;
    }
}
