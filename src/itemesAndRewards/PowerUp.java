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
    void draw(Graphics g) {
        if (!deSpawning)
            g.drawImage(powerUpImages[GetPowerUpImageIndex(type)], x, y, W, H, null);
        else
            g.drawImage(deSpawnImages[animationIndex], x, y, W, H, null);
    }

    @Override
    void addPoints(Player player) {
        RewardPointsManager.getInstance(player).addSmallPoints(GetPoints(type));
    }

    @Override
    void applyEffect(Player player) {
        switch (type) {
            case GREEN_CANDY -> powerUpManager.increaseGreenCandyCounter();
            case BLUE_CANDY -> powerUpManager.increaseBlueCandyCounter();
            case RED_CANDY -> powerUpManager.increaseRedCandyCounter();
            case SHOE -> powerUpManager.setShoe(true);
            case ORANGE_PARASOL -> powerUpManager.setOrangeParasol(true);
            case BLUE_PARASOL -> powerUpManager.setBlueParasol(true);
            case CHACKN_HEART -> powerUpManager.setChacknHeart(true);
            case CRYSTAL_RING -> powerUpManager.setCrystalRing(true);
            case EMERALD_RING -> powerUpManager.setEmeraldRing(true);
            case RUBY_RING -> powerUpManager.setRubyRing(true);
        }
    }
}