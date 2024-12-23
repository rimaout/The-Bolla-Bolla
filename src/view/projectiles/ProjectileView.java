package view.projectiles;

import model.utilz.Constants;
import model.projectiles.ProjectileModel;
import model.utilz.Constants.Projectiles.ProjectileState;

import java.awt.*;

import static view.utilz.Constants.ANIMATION_SPEED;

/**
 * Abstract class representing the view of the {@link ProjectileModel} class.
 * Handles the animation and rendering of projectiles.
 */
public abstract class ProjectileView {
    protected ProjectileModel projectileModel;
    protected final ProjectileManagerView projectileManagerView = ProjectileManagerView.getInstance();

    protected int animationIndex;
    protected float animationTick;

    /**
     * Constructs a ProjectileView with the specified ProjectileModel.
     * Initializes the projectile model and plays the sound effect.
     *
     * @param projectileModel the model of the projectile
     */
    protected ProjectileView(ProjectileModel projectileModel) {
        this.projectileModel = projectileModel;

        playSoundEffect();
    }

    /**
     * Draws the projectile on the screen.
     *
     * @param g the Graphics object to draw the projectile
     */
    protected abstract void draw(Graphics g);

    /**
     * Updates the state of the projectile view, including its animation tick.
     */
    protected void update() { updateAnimationTick(); }

    /**
     * Plays the sound effect associated with the projectile.
     */
    protected abstract void playSoundEffect();

    /**
     * Updates the animation tick and index for the projectile.
     */
    protected void updateAnimationTick() {
        animationTick++;
        if (animationTick >= ANIMATION_SPEED) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= getSpriteAmount(projectileModel.getState(), projectileModel.getType())) {
                animationIndex = 0;
            }
        }
    }

    /**
     * Checks if the projectile is active.
     *
     * <p>Active projectiles views are active only if the projectile model is active.
     *
     * @return true if the projectile is active, false otherwise
     */
    protected boolean isActive() {
        return projectileModel.isActive();
    }

    /**
     * Returns the animation index for the specified projectile state.
     *
     * @param projectileState the state of the projectile
     * @return the animation index
     */
    protected static int getAnimation(ProjectileState projectileState) {
        return switch (projectileState) {
            case MOVING -> 0;
            case IMPACT -> 1;
        };
    }

    /**
     * Returns the number of sprites for the specified projectile state and type.
     *
     * @param projectileState the state of the projectile
     * @param projectileType the type of the projectile
     * @return the number of sprites
     */
    protected static int getSpriteAmount(ProjectileState projectileState, Constants.Projectiles.ProjectileType projectileType) {

        if (projectileType == Constants.Projectiles.ProjectileType.MAITA_FIREBALL)
            return switch (projectileState) {
                case MOVING -> 4;
                case IMPACT -> 2;
            };

        if (projectileType == Constants.Projectiles.ProjectileType.PLAYER_BUBBLE)
            return switch (projectileState) {
                case MOVING -> 4;
                case IMPACT -> 0;
            };

        return 0;
    }
}