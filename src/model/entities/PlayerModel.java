package model.entities;

import model.utilz.Constants;
import model.utilz.PlayingTimer;
import model.levels.LevelManagerModel;
import model.projectiles.ProjectileManagerModel;
import model.itemesAndRewards.PowerUpManagerModel;
import model.projectiles.PlayerBubbleProjectileModel;

import static model.utilz.Constants.*;
import static model.utilz.HelpMethods.*;
import static model.utilz.Constants.Direction.LEFT;
import static model.utilz.Constants.Direction.RIGHT;
import static model.utilz.Constants.PlayerConstants.*;

/**
 * Represents the player character in the game.
 *
 * <p>This class manages the player's state, movement, actions, and interactions with the game world.
 * It follows the singleton pattern to ensure only one instance of the player exists.
 */
public class PlayerModel extends EntityModel {
    private static PlayerModel instance;

    private final LevelManagerModel levelManagerModel = LevelManagerModel.getInstance();
    private final PlayingTimer timer = PlayingTimer.getInstance();

    // Movement values and variables
    private boolean left, right, jump, isJumping, inAir;
    private boolean moving, attacking, respawning, canRespawn;
    private float xSpeed = 0;
    private float airSpeed = 0.0f;

    // General Variables
    private int lives = 3;
    private int points = 0;
    private int flipX = 0;
    private int flipW = 1;

    // Timers
    private int immuneTimer;
    private int attackTimer = 150;
    private int respawnTimer = RESPAWN_TIME;

    // PowerUp Values
    private float speedMultiplier = 1;         // shoes
    private float bubbleCadenceMultiplier = 1; // greenCandy
    private int jumpPoints = 0;                // emeraldRing
    private int walkPoints = 0;                // crystalRing
    private int bubbleShotPoints = 0;          // rubyRing

    /**
     * Returns the singleton instance of the PlayerModel.
     *
     * <p>This method ensures that only one instance of the PlayerModel is created (singleton pattern).
     * If the instance is null, it creates a new PlayerModel object and returns it.
     *
     * @return the singleton instance of the PlayerModel
     */
    public static PlayerModel getInstance() {
        if (instance == null)
            instance = new PlayerModel();
        return instance;
    }

    /**
     * Private constructor to prevent instantiation, without using {@link #getInstance()} method.
     */
    private PlayerModel() {
        super(-3 * Constants.TILES_SIZE, -3 * Constants.TILES_SIZE, PLAYER_W, PLAYER_H); // Set the player outside the map (so it doesn't get drawn)
        initHitbox(HITBOX_W, HITBOX_H);
        initLevelManager();
    }

    /**
     * Updates the player's state, including timers and position.
     */
    public void update() {
        updateTimers();
        updatePosition();

        if (attacking && canAttack())
            attack();
    }

    /**
     * Checks if the player can attack.
     *
     * <p>This method determines if the player is able to perform an attack action. The player can attack if they are not respawning,
     * are inside the map boundaries, and are not under a tile roof.
     *
     * @return true if the player can attack, false otherwise
     */
    public boolean canAttack() {
        return !respawning && IsEntityInsideMap(hitbox) && !IsTileRoof((int) hitbox.y / Constants.TILES_SIZE);
    }

    /**
     * Performs the player's attack action, creating a new bubble projectile.
     *
     * <p>This method determines the direction of the attack based on the player's current facing direction.
     * It then creates a new bubble projectile at the appropriate position and resets the attack timer.
     * Additionally, it increases the bubble shoot counter and adds points based if the player has collected the rubyRing power-up.
     */
    private void attack() {
        Direction direction;

        if (flipW == -1)
            direction = LEFT;
        else
            direction = RIGHT;

        int xOffset = 10 * Constants.SCALE;
        int yOffset = 3 * Constants.SCALE;

        if (direction == LEFT)
            ProjectileManagerModel.getInstance().addProjectile(new PlayerBubbleProjectileModel(hitbox.x - xOffset, hitbox.y - yOffset, direction));
        else
            ProjectileManagerModel.getInstance().addProjectile(new PlayerBubbleProjectileModel(hitbox.x + hitbox.width - xOffset, hitbox.y - yOffset, direction));

        attackTimer = (int) (ATTACK_TIMER * bubbleCadenceMultiplier);

        PowerUpManagerModel.getInstance().increaseBubbleShootCounter();
        addPoints(bubbleShotPoints);     // rubyRing powerUp
    }

    /**
    * Updates the player's timers, including immune, attack, and respawn timers.
    */
    private void updateTimers() {

        if (immune) {
            immuneTimer -= (int) timer.getTimeDelta();
            if (immuneTimer <= 0)
                immune = false;
        }

        attackTimer -= (int) timer.getTimeDelta();
        if (attackTimer > 0)
            attacking = false;

        if (respawning) {
            respawnTimer -= (int) timer.getTimeDelta();

            if (respawnTimer <= 0) {
                canRespawn = true;
                respawnTimer = RESPAWN_TIME;
            } else
                canRespawn = false;
        }
    }

    /**
     * Updates the player's position based on the current movement state.
     */
    private void updatePosition() {

        if (respawning) {
            respawn();
            return;
        }

        updateMovementValues();

        if (!left && !right && !inAir)
            return;

        if (!IsEntityInsideMap(hitbox))
            pacManEffect();

        // MOVE
        if (IsEntityInsideSolid(hitbox, levelManagerModel.getLevelTileData()))
            handleMovementInsideSolid();

        else if (inAir)
            handleInAirMovement();
        else
            handleOnFloorMovement();
    }

    /**
     * Updates the player's movement values based on the current input state.
     */
    private void updateMovementValues() {
        moving = false;

        if (jump && !inAir) {
            inAir = true;
            isJumping = true;

            if (!IsEntityInsideSolid(hitbox, levelManagerModel.getLevelTileData())) {  // can't jump if is inside solid
                airSpeed = JUMP_SPEED;
                PowerUpManagerModel.getInstance().increaseJumpCounter();
                addPoints(jumpPoints);  //emeraldRing powerUp
            }
        }

        if (!left && !right && !inAir)
            return;

        xSpeed = 0;

        if (left) {
            xSpeed -= WALK_SPEED * speedMultiplier;
            flipX = width;
            flipW = -1;
        }
        if (right) {
            xSpeed += WALK_SPEED * speedMultiplier;
            flipX = 0;
            flipW = 1;
        }

        if (!inAir)
            if (!IsEntityOnFloor(hitbox, levelManagerModel.getLevelTileData()))
                inAir = true;
    }

    /**
     * Handles the player's movement while in the air.
     */
    private void handleInAirMovement() {
        if (isJumping)
            jumping();
        else
            falling();
    }

    /**
     * Handles the player's movement while on the floor.
     */
    private void handleOnFloorMovement() {
        updateXPos(xSpeed);
        addPoints(walkPoints);  // crystalRing powerUp
        moving = true;          // Activate running animation
    }

    /**
     * Handles the player's movement while inside a solid tile.
     */
    private void handleMovementInsideSolid() {
        // JUMPING
        if (airSpeed < 0) {
            hitbox.y += airSpeed;
            airSpeed += GRAVITY;
            conpenetrationSafeUpdateXPos(xSpeed);
        }
        // FALLING
        else {
            hitbox.y += airSpeed;
            airSpeed = FALL_SPEED;
            isJumping = false;
            conpenetrationSafeUpdateXPos(xSpeed);
        }
    }

    /**
     * Makes the player jump on a bubble.
     */
    public void jumpOnBubble() {
        airSpeed = JUMP_SPEED;
        inAir = true;
        isJumping = true;
        PowerUpManagerModel.getInstance().increaseJumpCounter();
    }

    /**
     * Handles the player's jumping movement.
     *
     * <p>This method updates the player's vertical position and speed while jumping.
     * It checks if the player is going up or down and updates the position accordingly.
     * If the player is going down and cannot move further, it resets the in-air state.
     */
    private void jumping() {

        // Going up
        if (airSpeed < 0) {
            hitbox.y += airSpeed;
            airSpeed += GRAVITY;
            conpenetrationSafeUpdateXPos(xSpeed);
        }

        // Going down
        else if (airSpeed <= -JUMP_SPEED) {
            if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, levelManagerModel.getLevelTileData())) {
                hitbox.y += airSpeed;
                airSpeed += GRAVITY;
                updateXPos(xSpeed);
            } else {
                hitbox.y = GetEntityYPosAboveFloor(hitbox, airSpeed, levelManagerModel.getLevelTileData());
                resetInAir();
                updateXPos(xSpeed);
            }
        } else {
            isJumping = false;
            updateXPos(xSpeed);
        }
    }

    /**
     * Handles the player's falling movement.
     *
     * <p>This method updates the player's vertical position and speed while falling.
     * It checks if the player can move further down and updates the position accordingly.
     * If the player cannot move further down, it resets the in-air state.
     */
    private void falling() {
        if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, levelManagerModel.getLevelTileData())) {
            hitbox.y += airSpeed;
            airSpeed = FALL_SPEED;
            updateXPos(xSpeed / 3);
        } else {
            hitbox.y = GetEntityYPosAboveFloor(hitbox, airSpeed, levelManagerModel.getLevelTileData());
            updateXPos(xSpeed / 3);
            resetInAir();
        }
    }

    /**
     * Applies the Pac-Man effect to the player's position.
     *
     * <p>This method checks if the player's vertical position is beyond the bottom boundary of the map.
     * If so, it wraps the player's position to the top of the map, creating a Pac-Man effect.
     */
    private void pacManEffect() {
        if (hitbox.y > Constants.TILES_SIZE * Constants.TILES_IN_HEIGHT)
            hitbox.y = -2 * Constants.TILES_SIZE;
    }

    /**
     * Handles the player's death state.
     */
    public void death() {

        if (!immune) {
            immune = true;
            immuneTimer = IMMUNE_TIME_AFTER_RESPAWN;
            respawning = true;
            resetMovements();
            resetInAir();
        }
    }

    /**
     * Respawns the player after death.
     */
    private void respawn() {

        if (canRespawn) { // Last frame of the dying animation
            respawning = false;
            immuneTimer = IMMUNE_TIME_AFTER_RESPAWN;
            hitbox.x = SPAWN_X;
            hitbox.y = SPAWN_Y;
            lives--;

            PowerUpManagerModel.getInstance().reset();    // Reset all powerUps when player dies
        }
    }

    /**
     * Resets the player's in-air state and movement values.
     */
    private void resetInAir() {
        inAir = false;
        isJumping = false;
        airSpeed = 0;
    }

    /**
     * Resets the player's movement values.
     */
    public void resetMovements() {
        left = false;
        right = false;
        jump = false;
        attacking = false;
    }

    /**
     * Resets the player's power-up values.
     */
    private void resetPowerUps() {
        speedMultiplier = 1;
        bubbleCadenceMultiplier = 1;
        jumpPoints = 0;
        walkPoints = 0;
        bubbleShotPoints = 0;
    }

    /**
     * Resets the player's state to the default, including lives and points.
     *
     * @param resetLives  true to reset the player's lives, false otherwise
     * @param resetPoints true to reset the player's points, false otherwise
     */
    public void reset(Boolean resetLives, Boolean resetPoints) {
        resetMovements();
        resetInAir();
        resetPowerUps();

        active = true;
        immune = false;
        immuneTimer = 0;
        attackTimer = 100;
        xSpeed = 0;
        airSpeed = 0.0f;
        flipX = 0;
        flipW = 1;

        if (resetLives)
            lives = 3;

        if (resetPoints)
            points = 0;

        if (!IsEntityOnFloor(hitbox, levelManagerModel.getLevelTileData()))
            inAir = true;
    }

    // ------ Getters and Setters -------

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public void setInAir(boolean inAir) {
        this.inAir = inAir;
    }

    public int getLives() {
        return lives;
    }

    public void addPoints(int pointsToAdd) {
        this.points += pointsToAdd;
    }

    public int getPoints() {
        return points;
    }

    public float getXSpeed() {
        return xSpeed;
    }

    public boolean isJumpActive() {
        return jump;
    }

    public boolean isRespawning() {
        return respawning;
    }

    public Direction getDirection() {
        if (flipW == -1)
            return LEFT;
        else
            return RIGHT;
    }

    public void setSpeedMultiplier(float speedMultiplier) {
        this.speedMultiplier = speedMultiplier;
    }

    public void setBubbleCadenceMultiplier(float bubbleCadenceMultiplier) {
        this.bubbleCadenceMultiplier = bubbleCadenceMultiplier;
    }

    public void setChacknHeartImmunity(int immunityTime) {
        immune = true;
        immuneTimer = immunityTime;
    }

    public void setJumpPoints(int jumpPoints) {
        this.jumpPoints = jumpPoints;
    }

    public void setWalkPoints(int walkPoints) {
        this.walkPoints = walkPoints;
    }

    public void setBubbleShotPoints(int bubbleShotPoints) {
        this.bubbleShotPoints = bubbleShotPoints;
    }

    public boolean getIsJumping() {
        return isJumping;
    }

    public int getImmuneTimer() {
        return immuneTimer;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getFlipX() {
        return flipX;
    }

    public int getFlipW() {
        return flipW;
    }

    public boolean isMoving() {
        return moving;
    }

    public boolean isInAir() {
        return inAir;
    }

    public float getAirSpeed() {
        return airSpeed;
    }

    public boolean isAttacking() {
        return attacking;
    }
}