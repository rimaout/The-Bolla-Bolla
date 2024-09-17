package bubbles.specialBubbles;

import entities.Player;
import projectiles.LightingProjectile;
import projectiles.ProjectileManager;
import utilz.Constants.Projectiles.ProjectileType;
import utilz.Constants;

import java.awt.*;

import static utilz.Constants.Bubble.*;
import static utilz.Constants.Bubble.IMMAGE_H;


public class LightningBubble extends SpecialBubble {
    public LightningBubble(float x, float y, Constants.Direction direction) {
        super(x, y, direction);
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(specialBubbleManager.getLightningBubbleSprites()[0][0], (int) (hitbox.x - HITBOX_OFFSET_X), (int) (hitbox.y - HITBOX_OFFSET_Y), IMMAGE_W, IMMAGE_H, null);

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
    public void playerPop(Player player) {
        active = false;
        spawnWaterLighting();
    }

    private void spawnWaterLighting() {
        ProjectileManager.getInstance().addProjectile(new LightingProjectile(hitbox.x, hitbox.y, ProjectileType.LIGHTNING));
    }
}
