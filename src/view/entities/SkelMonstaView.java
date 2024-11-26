package view.entities;

import model.entities.SkelMonstaModel;
import model.entities.EnemyModel;

import java.awt.*;

import static model.utilz.Constants.ANIMATION_SPEED;
import static model.utilz.Constants.EnemyConstants.ENEMY_H;
import static model.utilz.Constants.EnemyConstants.ENEMY_W;
import static model.utilz.Constants.EnemyConstants.EnemyType.SKEL_MONSTA;

public class SkelMonstaView extends EnemyView {
    private boolean despawningAnimationActive = false;

    public SkelMonstaView(EnemyModel enemyModel) {
        super(enemyModel);
    }

    public void draw(Graphics g) {
        SkelMonstaModel model = (SkelMonstaModel) enemyModel;

        g.drawImage(EnemyManagerView.getInstance().getEnemySprite(SKEL_MONSTA)[getAnimation()][animationIndex], (int) model.getHitbox().x + model.flipX(), (int) model.getHitbox().y, ENEMY_W * model.flipW(), ENEMY_H, null);
    }

    public void update() {

        if (!enemyModel.isActive()) {
            reset();
            return;
        }

        checkIfModelDespawning();
        updateAnimationTick();
    }

    @Override
    protected void updateAnimationTick() {
        animationTick++;
        if (animationTick >= ANIMATION_SPEED) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= 2) {
                animationIndex = 0;
            }
        }
    }

    private int getAnimation() {
        SkelMonstaModel model = (SkelMonstaModel) enemyModel;

        if (model.isSpawning())
            return 0;
        else if (model.isMoving())
            return 1;
        else if (model.isDespawning())
            return 2;
        else
            return 1;
    }

    protected void reset(){
        despawningAnimationActive = false;
        animationIndex = 0;
        animationTick = 0;
    }

    private void checkIfModelDespawning(){
        SkelMonstaModel model = (SkelMonstaModel) enemyModel;

        if (model.isDespawning() && !despawningAnimationActive){
            despawningAnimationActive = true;
            animationIndex = 0;
            animationTick = 0;
        }
    }
}