package view.projectiles;

import model.utilz.Constants;
import model.projectiles.ProjectileModel;
import model.utilz.Constants.Projectiles.ProjectileState;

import java.awt.*;

import static model.utilz.Constants.ANIMATION_SPEED;
import static model.utilz.Constants.Projectiles.ProjectileState.IMPACT;

public abstract class ProjectileView {
    protected ProjectileModel projectileModel;
    protected final ProjectileManagerView projectileManagerView = ProjectileManagerView.getInstance();

    protected int animationIndex;
    protected float animationTick;

    protected ProjectileView(ProjectileModel projectileModel) {
        this.projectileModel = projectileModel;

        playSoundEffect();
    }

    protected abstract void draw(Graphics g);
    protected void update() {
        updateAnimationTick();
    }
    protected abstract void playSoundEffect();

    protected void updateAnimationTick() {
        animationTick++;
        if (animationTick >= ANIMATION_SPEED) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= getSpriteAmount(projectileModel.getState(), projectileModel.getType())) {
                animationIndex = 0;
            }
        }

        // Deactivate projectile after impact animation
        if (projectileModel.getState() == IMPACT && animationIndex == getSpriteAmount(projectileModel.getState(), projectileModel.getType()) - 1) {
            projectileModel.setActive(false);   // TODO: use controller as a mediator between view and model
                                                // use a static class with a static method that takes a projectileModel and sets it to inactive
        }
    }

    protected boolean isActive() {
        return projectileModel.isActive();
    }

    protected static int getAnimation(ProjectileState projectileState) {
        return switch (projectileState) {
            case MOVING -> 0;
            case IMPACT -> 1;
        };
    }

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
