package itemesAndRewards;

import entities.Player;

import static utilz.Constants.Items.*;
import static utilz.Constants.Items.PowerUpType.*;

import java.awt.*;
import java.awt.image.BufferedImage;


public class PowerUp extends Item {
    private PowerUpManager powerUpManager;

    PowerUpType type;
    BufferedImage[] powerUpImages;
    BufferedImage[] deSpawnImages;

    public PowerUp(int x, int y, PowerUpType type) {
        super(x, y);
        this.type = type;

        powerUpManager = PowerUpManager.getInstance();
        powerUpImages = ItemManager.getInstance().getPowerUpImages();
        deSpawnImages = ItemManager.getInstance().getDeSpawnImages();
    }


    @Override
    public void draw(Graphics g) {
        if (!deSpawning)
            g.drawImage(powerUpImages[GetPowerUpImageIndex(type)], x, y, W, H, null);
        else
            g.drawImage(deSpawnImages[animationIndex], x, y, W, H, null);
    }

    @Override
    public void addPoints(Player player) {
        RewardPointsManager.getInstance(player).addSmallPoints(GetPoints(type));
    }

    @Override
    public void applyEffect(Player player) {
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