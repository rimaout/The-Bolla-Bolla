package view.projectiles;

import model.Constants;
import model.projectiles.ProjectileModel;
import model.Constants.Projectiles.ProjectileState;

import java.awt.*;

import static view.Constants.ANIMATION_SPEED;

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
