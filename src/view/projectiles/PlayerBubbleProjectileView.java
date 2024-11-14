package view.projectiles;

import bubbles.playerBubbles.EmptyBubble;
import bubbles.playerBubbles.PlayerBubblesManager;
import projectiles.ProjectileManagerModel;
import projectiles.ProjectileModel;

import java.awt.*;

import static model.utilz.Constants.Projectiles.*;
import static model.utilz.Constants.Projectiles.ProjectileType.PLAYER_BUBBLE;

public class PlayerBubbleProjectileView extends ProjectileView {

    public PlayerBubbleProjectileView(ProjectileModel projectileModel) {
        super(projectileModel);
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(projectileManagerView.getSprites(PLAYER_BUBBLE)[0][animationIndex], (int) projectileModel.getHitbox().x, (int) projectileModel.getHitbox().y, W, H, null);
    }

    @Override
    protected void updateAnimationTick() {
        float projectileSpeedMultiplier = ProjectileManagerModel.getInstance().getPlayerProjectileSpeedMultiplier();
        float projectileDistanceMultiplier = ProjectileManagerModel.getInstance().getPlayerProjectileDistanceMultiplier();

        animationTick += projectileSpeedMultiplier / projectileDistanceMultiplier;

        animationTick++;
        if (animationTick > PROJECTILE_ANIMATION_SPEED) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= 4) {

                // TODO: use controller as a mediator between view and model
                projectileModel.setActive(false);
                PlayerBubblesManager.getInstance().addBubble(new EmptyBubble(projectileModel.getHitbox().x, projectileModel.getHitbox().y, projectileModel.getDirection()));
            }
        }
    }
}
