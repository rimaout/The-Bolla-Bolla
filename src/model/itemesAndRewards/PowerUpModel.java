package model.itemesAndRewards;

import static model.utilz.Constants.Items.*;
import static model.utilz.Constants.Items.PowerUpType.*;

public class PowerUpModel extends ItemModel {
    private final PowerUpManagerModel powerUpManagerModel = PowerUpManagerModel.getInstance();
    private final PowerUpType powerUpType;

    public PowerUpModel(int x, int y, PowerUpType powerUpType) {
        super(x, y, ItemType.POWER_UP);
        this.powerUpType = powerUpType;
    }

    @Override
    public void addPoints() {
        RewardPointsManagerModel.getInstance().addSmallPoints(GetPoints(powerUpType));
    }

    @Override
    public void applyEffect() {
        switch (powerUpType) {
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

    public PowerUpType getPowerUpType() {
        return powerUpType;
    }
}