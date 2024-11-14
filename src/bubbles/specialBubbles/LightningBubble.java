package bubbles.specialBubbles;

import java.awt.*;

import model.entities.PlayerModel;
import model.utilz.Constants;
import model.projectiles.ProjectileManagerModel;
import model.projectiles.LightingProjectileModel;
import model.utilz.Constants.Projectiles.ProjectileType;

import static model.utilz.Constants.Bubble.*;
import static model.utilz.Constants.Bubble.IMAGE_H;

public class LightningBubble extends SpecialBubble {
    public LightningBubble(float x, float y, Constants.Direction direction) {
        super(x, y, direction);
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(bubbleManager.getLightningBubbleSprites()[0][0], (int) (hitbox.x - HITBOX_OFFSET_X), (int) (hitbox.y - HITBOX_OFFSET_Y), IMAGE_W, IMAGE_H, null);
    }

    @Override
    public void update() {
        updateAnimationTick();
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
