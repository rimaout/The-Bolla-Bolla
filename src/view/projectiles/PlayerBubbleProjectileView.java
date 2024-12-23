package view.projectiles;

import model.projectiles.PlayerBubbleProjectileModel;
import view.audio.AudioPlayer;
import view.utilz.Constants.AudioConstants;
import model.projectiles.ProjectileManagerModel;
import model.projectiles.ProjectileModel;

import java.awt.*;

import static model.utilz.Constants.Projectiles.*;
import static model.utilz.Constants.Projectiles.ProjectileType.PLAYER_BUBBLE;
import static view.utilz.Constants.Projectiles.PROJECTILE_ANIMATION_SPEED;

/**
 * The PlayerBubbleProjectileView class represents the view of the {@link PlayerBubbleProjectileModel} class.
 * It handles the drawing and animation of the bubble projectile.
 */
public class PlayerBubbleProjectileView extends ProjectileView {

    /**
     * Constructs a PlayerBubbleProjectileView with the specified ProjectileModel.
     *
     * @param projectileModel the model of the projectile
     */
    public PlayerBubbleProjectileView(ProjectileModel projectileModel) {
        super(projectileModel);
    }

    /**
     * {@inheritDoc}
     *
     * @param g the Graphics object to draw the projectile
     */
    @Override
    public void draw(Graphics g) {
        g.drawImage(projectileManagerView.getSprites(PLAYER_BUBBLE)[0][animationIndex], (int) projectileModel.getHitbox().x, (int) projectileModel.getHitbox().y, W, H, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void playSoundEffect() {
        AudioPlayer.getInstance().playSoundEffect(AudioConstants.BUBBLE_SHOOT);
    }

    /**
     * {@inheritDoc}
     *
     * <p> The animation speed is adjusted based on the projectile speed and distance multipliers.
     */
    @Override
    protected void updateAnimationTick() {
        float projectileSpeedMultiplier = ProjectileManagerModel.getInstance().getPlayerProjectileSpeedMultiplier();
        float projectileDistanceMultiplier = ProjectileManagerModel.getInstance().getPlayerProjectileDistanceMultiplier();

        animationTick += projectileSpeedMultiplier / projectileDistanceMultiplier;
        if (animationTick > PROJECTILE_ANIMATION_SPEED) {
            animationTick = 0;
            animationIndex++;

        }
    }
}