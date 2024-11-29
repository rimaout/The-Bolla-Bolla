package model.bubbles.specialBubbles;


import model.entities.PlayerModel;
import model.utilz.Constants;
import model.projectiles.ProjectileManagerModel;
import model.projectiles.LightingProjectileModel;
import model.utilz.Constants.Projectiles.ProjectileType;

import static model.utilz.Constants.Bubble.BubbleType.LIGHTNING_BUBBLE;

public class LightningBubbleModel extends SpecialBubbleModel {
    public LightningBubbleModel(float x, float y, Constants.Direction direction) {
        super(x, y, direction);
        bubbleType = LIGHTNING_BUBBLE;
    }

    @Override
    public void update() {
        updateTimers();
        updateDirection();
        updatePosition();
        updateCollisionBoxes();
        pacManEffect();
    }

    @Override
    public void playerPop(PlayerModel playerModel) {
        active = false;
        spawnWaterLighting();
    }

    private void spawnWaterLighting() {
        ProjectileManagerModel.getInstance().addProjectile(new LightingProjectileModel(hitbox.x, hitbox.y, ProjectileType.LIGHTNING));
    }
}
