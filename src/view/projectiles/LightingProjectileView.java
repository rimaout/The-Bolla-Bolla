package view.projectiles;

import projectiles.ProjectileModel;

import java.awt.*;

import static model.utilz.Constants.Projectiles.*;
import static model.utilz.Constants.Projectiles.H;
import static model.utilz.Constants.Projectiles.ProjectileType.LIGHTNING;

public class LightingProjectileView extends ProjectileView {

    public LightingProjectileView(ProjectileModel projectileModel) {
        super(projectileModel);
    }

    @Override
    protected void draw(Graphics g) {
        g.drawImage(projectileManagerView.getSprites(LIGHTNING)[1][0],(int) projectileModel.getHitbox().x + OFFSET_X, (int) projectileModel.getHitbox().y + OFFSET_Y, W, H, null);
    }
}
