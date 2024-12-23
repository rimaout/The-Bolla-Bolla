package view.entities;

import model.entities.EnemyModel;
import model.entities.SkelMonstaModel;

import java.awt.*;

import static view.utilz.Constants.ANIMATION_SPEED;
import static model.utilz.Constants.EnemyConstants.ENEMY_H;
import static model.utilz.Constants.EnemyConstants.ENEMY_W;
import static model.utilz.Constants.EnemyConstants.EnemyType.SKEL_MONSTA;

/**
 * The SkelMonstaView class represents the view for the {@link SkelMonstaModel}.
 * It handles drawing and updating the SkelMonsta's animation and state.
 */
public class SkelMonstaView extends EnemyView {
    private boolean despawningAnimationActive = false;

    /**
     * Constructs a SkelMonstaView with the specified EnemyModel.
     *
     * @param enemyModel the model for the SkelMonsta enemy entity
     */
    public SkelMonstaView(EnemyModel enemyModel) {
        super(enemyModel);
    }

    /**
     * Draws the SkelMonsta on the screen.
     *
     * @param g the Graphics object to draw with
     */
    public void draw(Graphics g) {
        SkelMonstaModel model = (SkelMonstaModel) enemyModel;

        g.drawImage(EnemyManagerView.getInstance().getEnemySprite(SKEL_MONSTA)[getAnimation()][animationIndex], (int) model.getHitbox().x + model.flipX(), (int) model.getHitbox().y, ENEMY_W * model.flipW(), ENEMY_H, null);
    }

    /**
     * Updates the state and animation of the SkelMonsta.
     */
    public void update() {

        if (!enemyModel.isActive()) {
            reset();
            return;
        }

        checkIfModelDespawning();
        updateAnimationTick();
    }

    /**
     * Updates the animation tick and index based on the animation speed.
     */
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

    /**
     * Returns the current animation index based on the SkelMonsta's state.
     *
     * @return the current animation index
     */
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

    /**
     * Resets the SkelMonsta's animation and state variables.
     */
    protected void reset(){
        despawningAnimationActive = false;
        animationIndex = 0;
        animationTick = 0;
    }

    /**
     * Checks if the SkelMonsta is despawning and updates the animation state accordingly.
     */
    private void checkIfModelDespawning(){
        SkelMonstaModel model = (SkelMonstaModel) enemyModel;

        if (model.isDespawning() && !despawningAnimationActive){
            despawningAnimationActive = true;
            animationIndex = 0;
            animationTick = 0;
        }
    }
}