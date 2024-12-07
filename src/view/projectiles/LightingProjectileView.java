package view.projectiles;

import view.audio.AudioPlayer;
import view.Constants.AudioConstants;
import model.projectiles.ProjectileModel;

import java.awt.*;

import static model.Constants.Projectiles.*;
import static model.Constants.Projectiles.H;
import static model.Constants.Projectiles.ProjectileType.LIGHTNING;

public class LightingProjectileView extends ProjectileView {

    public LightingProjectileView(ProjectileModel projectileModel) {
        super(projectileModel);
    }

    @Override
    protected void draw(Graphics g) {
        g.drawImage(projectileManagerView.getSprites(LIGHTNING)[1][0],(int) projectileModel.getHitbox().x + OFFSET_X, (int) projectileModel.getHitbox().y + OFFSET_Y, W, H, null);
    }

    @Override
    protected void playSoundEffect() {
        AudioPlayer.getInstance().playSoundEffect(AudioConstants.LIGHTNING);
    }
}