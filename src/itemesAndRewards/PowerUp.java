package itemesAndRewards;

import java.awt.*;

import model.entities.PlayerModel;
import model.audio.AudioPlayer;
import model.utilz.Constants.AudioConstants;

import static model.utilz.Constants.Items.*;
import static model.utilz.Constants.Items.PowerUpType.*;

public class PowerUp extends Item {
    private final PowerUpManager powerUpManager = PowerUpManager.getInstance();
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
        RewardPointsManager.getInstance(playerModel).addSmallPoints(GetPoints(type));
    }

    @Override
    public void applyEffect(PlayerModel playerModel) {
        switch (type) {
            case GREEN_CANDY -> powerUpManager.collectedGreenCandy();
            case BLUE_CANDY -> powerUpManager.collectedBlueCandy();
            case RED_CANDY -> powerUpManager.collectedRedCandyCounter();
            case SHOE -> powerUpManager.collectedShoe();
            case ORANGE_PARASOL -> powerUpManager.collectedOrangeParasol();
            case BLUE_PARASOL -> powerUpManager.collectedBlueParasol();
            case CHACKN_HEART -> powerUpManager.collectedChacknHeart();
            case CRYSTAL_RING -> powerUpManager.collectedCrystalRing();
            case EMERALD_RING -> powerUpManager.collectedEmeraldRing();
            case RUBY_RING -> powerUpManager.collectedRubyRing();
        }
    }
}