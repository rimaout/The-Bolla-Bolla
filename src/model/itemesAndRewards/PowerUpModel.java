package model.itemesAndRewards;

import static model.utilz.Constants.Items.*;
import static model.utilz.Constants.Items.PowerUpType.*;

/**
 * Contains the logic a power-up item in the game.
 *
 * <p>The PowerUpModel class extends the {@link ItemModel} class and handles the behavior of power-up items,
 * including their effects on the player and the points they add when collected.
 */
public class PowerUpModel extends ItemModel {
    private final PowerUpManagerModel powerUpManagerModel = PowerUpManagerModel.getInstance();
    private final PowerUpType powerUpType;

    /**
     * Constructs a new PowerUpModel with the specified position and power-up type.
     *
     * @param x the x-coordinate of the power-up item
     * @param y the y-coordinate of the power-up item
     * @param powerUpType the type of the power-up item
     */
    public PowerUpModel(int x, int y, PowerUpType powerUpType) {
        super(x, y, ItemType.POWER_UP);
        this.powerUpType = powerUpType;
    }

    /**
     * Adds points to the player's score based on the power-up type.
     */
    @Override
    public void addPoints() {
        RewardPointsManagerModel.getInstance().addSmallPoints(GetPoints(powerUpType));
    }

    /**
     * Applies the effect of the power-up to the player.
     */
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

    /**
     * Returns the type of the power-up item.
     *
     * @return the type of the power-up item
     */
    public PowerUpType getPowerUpType() {
        return powerUpType;
    }
}