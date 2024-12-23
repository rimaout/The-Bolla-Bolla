package view.projectiles;

import model.projectiles.LightingProjectileModel;
import view.audio.AudioPlayer;
import view.utilz.Constants.AudioConstants;
import model.projectiles.ProjectileModel;

import java.awt.*;

import static model.utilz.Constants.Projectiles.*;
import static model.utilz.Constants.Projectiles.H;
import static model.utilz.Constants.Projectiles.ProjectileType.LIGHTNING;

/**
 * The LightingProjectileView class represents the view of the {@link LightingProjectileModel} class.
 * It handles the drawing and animation of the lighting projectile.
 */
public class LightingProjectileView extends ProjectileView {

    /**
     * Constructs a LightingProjectileView with the specified ProjectileModel.
     *
     * @param projectileModel the model of the projectile
     */
    public LightingProjectileView(ProjectileModel projectileModel) {
        super(projectileModel);
    }

    /**
     * {@inheritDoc}
     *
     * @param g the Graphics object to draw the projectile
     */
    @Override
    protected void draw(Graphics g) {
        g.drawImage(projectileManagerView.getSprites(LIGHTNING)[1][0],(int) projectileModel.getHitbox().x + OFFSET_X, (int) projectileModel.getHitbox().y + OFFSET_Y, W, H, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void playSoundEffect() {
        AudioPlayer.getInstance().playSoundEffect(AudioConstants.LIGHTNING);
    }
}