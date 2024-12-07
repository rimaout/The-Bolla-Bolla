package view.itemsAndRewards;

import model.itemesAndRewards.ItemModel;
import model.itemesAndRewards.PowerUpModel;
import view.Constants.AudioConstants;
import view.audio.AudioPlayer;

import java.awt.*;

import static model.Constants.Items.H;
import static view.Constants.Items.GetPowerUpImageIndex;
import static model.Constants.Items.W;

public class PowerUpView extends ItemView {

    public PowerUpView(ItemModel itemModel) {
        super(itemModel);
    }

   @Override
    public void draw(Graphics g) {
       PowerUpModel model = (PowerUpModel) itemModel;

        if (!model.isDeSpawning())
            g.drawImage(itemManagerView.getPowerUpImages()[GetPowerUpImageIndex(model.getPowerUpType())], (int) model.getHitbox().x, (int) model.getHitbox().y, W, H, null);
        else
            g.drawImage(itemManagerView.getDeSpawnImages()[animationIndex], (int) model.getHitbox().x, (int) model.getHitbox().y, W, H, null);
    }

    @Override
    public void audioEffects() {
        if (itemModel.isCollected() && !soundPlayed) {
            AudioPlayer.getInstance().playSoundEffect(AudioConstants.POWER_UP_COLLECTED);
            soundPlayed = true;
        }
    }
}