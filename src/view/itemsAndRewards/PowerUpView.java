package view.itemsAndRewards;

import model.itemesAndRewards.ItemModel;
import model.itemesAndRewards.PowerUpModel;
import view.audio.AudioPlayer;
import view.utilz.Constants.AudioConstants;

import java.awt.*;

import static model.utilz.Constants.Items.W;
import static model.utilz.Constants.Items.H;
import static view.utilz.Constants.Items.GetPowerUpImageIndex;

/**
 * The PowerUpView class represents the view for the {@link PowerUpModel} class.
 * It handles drawing the power-up item and playing audio effects when the item is collected.
 */
public class PowerUpView extends ItemView {

    /**
     * Constructs a PowerUpView with the specified ItemModel.
     *
     * @param itemModel the model of the power-up item
     */
    public PowerUpView(ItemModel itemModel) {
        super(itemModel);
    }

    /**
     * Draws the power-up item on the screen.
     * If the item is de-spawning, it draws the de-spawning animation.
     *
     * @param g the Graphics object to draw with
     */
   @Override
    public void draw(Graphics g) {
       PowerUpModel model = (PowerUpModel) itemModel;

        if (!model.isDeSpawning())
            g.drawImage(itemManagerView.getPowerUpImages()[GetPowerUpImageIndex(model.getPowerUpType())], (int) model.getHitbox().x, (int) model.getHitbox().y, W, H, null);
        else
            g.drawImage(itemManagerView.getDeSpawnImages()[animationIndex], (int) model.getHitbox().x, (int) model.getHitbox().y, W, H, null);
    }

    /**
     * Plays the audio effects for the power-up item when it is collected.
     * Ensures the sound is played only once.
     */
    @Override
    public void audioEffects() {
        if (itemModel.isCollected() && !soundPlayed) {
            AudioPlayer.getInstance().playSoundEffect(AudioConstants.POWER_UP_COLLECTED);
            soundPlayed = true;
        }
    }
}