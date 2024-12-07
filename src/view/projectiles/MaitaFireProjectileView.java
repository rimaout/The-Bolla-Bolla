package view.projectiles;

import model.projectiles.ProjectileModel;

import java.awt.*;

import static model.Constants.Projectiles.*;
import static model.Constants.Projectiles.ProjectileType.MAITA_FIREBALL;

public class MaitaFireProjectileView extends ProjectileView {

    public MaitaFireProjectileView(ProjectileModel projectileModel) {
        super(projectileModel);
    }

    @Override
    protected void draw(Graphics g) {
        g.drawImage(projectileManagerView.getSprites(MAITA_FIREBALL)[getAnimation(projectileModel.getState())][animationIndex], (int) projectileModel.getHitbox().x + OFFSET_X, (int) projectileModel.getHitbox().y + OFFSET_Y, W, H, null);
    }

    @Override
    protected void playSoundEffect() {
        // not used, MaitaFireProjectiles don't have a sound effect
    }
}
