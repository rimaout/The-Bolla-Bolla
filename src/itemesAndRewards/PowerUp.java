package itemesAndRewards;

import java.awt.*;

import model.entities.PlayerModel;
import view.audio.AudioPlayer;
import model.utilz.Constants.AudioConstants;

import static model.utilz.Constants.Items.*;
import static model.utilz.Constants.Items.PowerUpType.*;

public class PowerUp extends Item {
    private final PowerUpManagerModel powerUpManagerModel = PowerUpManagerModel.getInstance();
    private final PowerUpType type;

    public PowerUp(int x, int y, PowerUpType type) {
        super(x, y);
        this.type = type;
    }

    @Override
    public void draw(Graphics g) {
        if (!deSpawning)
            g.drawImage(itemManager.getPowerUpImages()[GetPowerUpImageIndex(type)], x, y, W, H, null);
        else
            g.drawImage(itemManager.getDeSpawnImages()[animationIndex], x, y, W, H, null);
    }

    @Override
    public void audioEffects() {
        if (playSound) {
            AudioPlayer.getInstance().playSoundEffect(AudioConstants.POWER_UP_COLLECTED);
            playSound = false;
        }
    }

    @Override
    public void addPoints(PlayerModel playerModel) {
        RewardPointsManagerModel.getInstance(playerModel).addSmallPoints(GetPoints(type));
    }

    @Override
    public void applyEffect(PlayerModel playerModel) {
        switch (type) {
            case GREEN_CANDY -> powerUpManagerModel.collectedGreenCandy();
            case BLUE_CANDY -> powerUpManagerModel.collectedBlueCandy();
            case RED_CANDY -> powerUpManagerModel.collectedRedCandyCounter();
            case SHOE -> powerUpManagerModel.collectedShoe();
            case ORANGE_PARASOL -> powerUpManagerModel.collectedOrangeParasol();
            case BLUE_PARASOL -> powerUpManagerModel.collectedBlueParasol();
            case CHACKN_HEART -> powerUpManagerModel.collectedChacknHeart();
            case CRYSTAL_RING -> powerUpManagerModel.collectedCrystalRing();
            case EMERALD_RING -> powerUpManagerModel.collectedEmeraldRing();
            case RUBY_RING -> powerUpManagerModel.collectedRubyRing();
        }
    }
}