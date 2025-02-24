package com.rima.view.projectiles;

import com.rima.model.projectiles.MaitaFireProjectileModel;
import com.rima.model.projectiles.ProjectileModel;

import java.awt.*;

import static com.rima.model.utilz.Constants.Projectiles.*;
import static com.rima.model.utilz.Constants.Projectiles.ProjectileType.MAITA_FIREBALL;

/**
 * The MaitaFireProjectileView class represents the view of the {@link MaitaFireProjectileModel} class.
 * It handles the drawing and animation of the fire projectile.
 */
public class MaitaFireProjectileView extends ProjectileView {

    /**
     * Constructs a MaitaFireProjectileView with the specified ProjectileModel.
     *
     * @param projectileModel the model of the projectile
     */
    public MaitaFireProjectileView(ProjectileModel projectileModel) {
        super(projectileModel);
    }

    /**
     * {@inheritDoc}
     *
     * @param g the Graphics object to draw the projectile
     */
    @Override
    protected void draw(Graphics g) {
        g.drawImage(projectileManagerView.getSprites(MAITA_FIREBALL)[getAnimation(projectileModel.getState())][animationIndex], (int) projectileModel.getHitbox().x + OFFSET_X, (int) projectileModel.getHitbox().y + OFFSET_Y, W, H, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void playSoundEffect() {
        // not used, MaitaFireProjectiles don't have a sound effect
    }
}