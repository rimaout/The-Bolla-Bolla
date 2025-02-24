package com.rima.model.projectiles;

import com.rima.model.utilz.PlayingTimer;
import com.rima.model.entities.EnemyModel;
import com.rima.model.entities.EntityModel;
import com.rima.model.entities.PlayerModel;
import com.rima.model.utilz.Constants.Direction;
import com.rima.model.utilz.Constants.Projectiles.ProjectileState;

import static com.rima.model.utilz.Constants.Projectiles.*;
import static com.rima.model.utilz.Constants.Projectiles.ProjectileState.IMPACT;
import static com.rima.model.utilz.Constants.Projectiles.ProjectileState.MOVING;

/**
 * Abstract class representing the model of projectile objects in the game.
 */
public abstract class ProjectileModel extends EntityModel {
    protected final PlayingTimer timer = PlayingTimer.getInstance();
    private int impactTimer = 100; //time that the projectile is active after an impact

    protected ProjectileState state = MOVING;
    protected ProjectileType type;
    protected Direction direction;


    /**
     * Constructs a ProjectileModel with the specified position, direction, and type.
     *
     * @param x the x-coordinate of the projectile
     * @param y the y-coordinate of the projectile
     * @param direction the direction of the projectile
     * @param type the type of the projectile
     */
    public ProjectileModel(float x, float y, Direction direction, ProjectileType type) {
        super(x, y, H, W);
        this.direction = direction;
        this.type = type;

        initHitbox(HITBOX_W, HITBOX_H);
    }

    /**
     * Updates the position and internal timers of the projectile.
     */
    public void update() {
        updatePos();
        updateTimer();
    }

    /**
     * Updates the position of the projectile.
     */
    protected abstract void updatePos();

    /**
     * Checks if the projectile hits an enemy.
     *
     * @param enemyModel the enemy model to check against
     * @param playerModel the player model to check against
     */
    protected abstract void checkEnemyHit(EnemyModel enemyModel, PlayerModel playerModel);

    /**
     * Checks if the projectile hits the player.
     *
     * @param playerModel the player model to check against
     */
    protected abstract void checkPlayerHit(PlayerModel playerModel);

    /**
     * Updates the impact timer and deactivates the projectile if the timer reaches zero.
     */
    protected void updateTimer(){
        if (state == IMPACT)
            impactTimer -= timer.getTimeDelta();

        if (impactTimer <= 0)
            active = false;
    }

    /**
     * Returns the current state of the projectile.
     *
     * @return the current state of the projectile
     */
    public ProjectileState getState() {
        return state;
    }

    /**
     * Returns the type of the projectile.
     *
     * @return the type of the projectile
     */
    public ProjectileType getType() {
        return type;
    }

    /**
     * Returns the direction of the projectile.
     *
     * @return the direction of the projectile
     */
    public Direction getDirection() {
        return direction;
    }
}