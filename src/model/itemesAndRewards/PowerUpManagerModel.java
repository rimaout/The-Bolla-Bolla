package model.itemesAndRewards;

import static java.lang.Math.abs;

import model.entities.PlayerModel;
import model.entities.EnemyManagerModel;
import model.utilz.Constants;
import model.utilz.Constants.Items.PowerUpType;
import model.projectiles.ProjectileManagerModel;

import java.util.ArrayList;

public class PowerUpManagerModel {
    private static PowerUpManagerModel instance;
    private final PlayerModel playerModel;

    // PowerUp Flags
    private boolean greenCandy = false;
    private boolean blueCandy = false;
    private boolean redCandy = true;
    private boolean shoe = false;
    private boolean orangeParasol = false;
    private boolean blueParasol = false;
    private boolean chacknHeart = false;
    private boolean emeraldRing = false;
    private boolean crystalRing = false;
    private boolean rubyRing = false;

    // PowerUp Counters
    private int bubbleShootCounter = 0;
    private int bubblePopCounter = 0;
    private int jumpCounter = 0;
    private float walkedDistance = 0;
    private int waterBubblePopCounter = 0;
    private int itemCollectCounter = 0;
    private int greenCandyCounter = 0;
    private int blueCandyCounter = 0;
    private int redCandyCounter = 0;


    private PowerUpManagerModel(){
        this.playerModel = PlayerModel.getInstance();
    }

    public static PowerUpManagerModel getInstance(){
        if (instance == null)
            instance = new PowerUpManagerModel();
        return instance;
    }

    public void update() {
        applyPowerUp();
    }

    public void applyPowerUp() {

        if (greenCandy)
            playerModel.setBubbleCadenceMultiplier(0.5f);
        else
            playerModel.setBubbleCadenceMultiplier(1);

        if (blueCandy)
            ProjectileManagerModel.getInstance().setPlayerProjectileSpeedMultiplier(1.65f);
        else
            ProjectileManagerModel.getInstance().setPlayerProjectileSpeedMultiplier(1f);

        if (redCandy) {
            ProjectileManagerModel.getInstance().setPlayerProjectileDistanceMultiplier(1.50f);
            ProjectileManagerModel.getInstance().setPlayerProjectileActiveMultiplier(2f);
        }
        else {
            ProjectileManagerModel.getInstance().setPlayerProjectileDistanceMultiplier(1f);
            ProjectileManagerModel.getInstance().setPlayerProjectileActiveMultiplier(1f);
        }

        if (shoe)
            playerModel.setSpeedMultiplier(1.7f);
        else
            playerModel.setSpeedMultiplier(1);

        if (chacknHeart) {
            playerModel.setChacknHeartImmunity(15000);
            EnemyManagerModel.getInstance().setChacknHeartfreeze(15000);
            chacknHeart = false;
        }

        if (orangeParasol)
            System.out.print("");
        else
            System.out.print("");

        if (blueParasol)
            System.out.print("");
        else
            System.out.print("");

        if (emeraldRing)
            playerModel.setJumpPoints(500);
        else
            playerModel.setJumpPoints(0);

        if (crystalRing)
            playerModel.setWalkPoints(10);
        else
            playerModel.setWalkPoints(0);

        if (rubyRing)
            playerModel.setBubbleShotPoints(100);
        else
            playerModel.setBubbleShotPoints(0);
    }

    public PowerUpType getPowerUpToSpawn() {

        ArrayList<PowerUpType> powerUps = new ArrayList<>();

        if (bubbleShootCounter >= 20 ) powerUps.add(PowerUpType.GREEN_CANDY);       // Working // Original game value = 35
        if (bubblePopCounter >= 20) powerUps.add(PowerUpType.BLUE_CANDY);           // Working // Original game value = 35
        if (jumpCounter >= 20) powerUps.add(PowerUpType.RED_CANDY);                 // Working // Original game value = 35
        if (walkedDistance >= Constants.GAME_WIDTH * 6) powerUps.add(PowerUpType.SHOE);  // Working // Original game value * 15
        if (waterBubblePopCounter >= 15) powerUps.add(PowerUpType.ORANGE_PARASOL);
        if (waterBubblePopCounter >= 20) powerUps.add(PowerUpType.BLUE_PARASOL);
        if (itemCollectCounter >= 4) powerUps.add(PowerUpType.CHACKN_HEART);        // Working // Original game value = 55
        if (blueCandyCounter >= 1) powerUps.add(PowerUpType.CRYSTAL_RING);          // Working // Original game value = 3
        if (greenCandyCounter >= 1) powerUps.add(PowerUpType.EMERALD_RING);         // Working // Original game value = 3
        if (redCandyCounter >= 1) powerUps.add(PowerUpType.RUBY_RING);              // Working // Original game value = 3

        if (powerUps.isEmpty()) return null;

        return powerUps.get((int) (Math.random() * powerUps.size()));
    }

    public void newPlayReset() {
        reset();
    }

    public void reset() {
        greenCandy = false;
        blueCandy = false;
        redCandy = false;
        shoe = false;
        orangeParasol = false;
        blueParasol = false;
        chacknHeart = false;
        crystalRing = false;
        emeraldRing = false;
        rubyRing = false;

        bubbleShootCounter = 0;
        bubblePopCounter = 0;
        jumpCounter = 0;
        walkedDistance = 0;
        waterBubblePopCounter = 0;
        itemCollectCounter = 0;
        greenCandyCounter = 0;
        blueCandyCounter = 0;
        redCandyCounter = 0;
    }

    public void increaseBubbleShootCounter() {
        bubbleShootCounter++;
    }

    public void increaseBubblePopCounter() {
        bubblePopCounter++;
    }

    public void increaseJumpCounter() {
        jumpCounter++;
    }

    public void addDistance(float distance) {
        walkedDistance += abs(distance);
    }

    public void increaseWaterBubblePopCounter() {
        waterBubblePopCounter++;
    }

    public void increaseItemCollectCounter() {
        itemCollectCounter++;
    }

    public void collectedGreenCandy() {
        greenCandyCounter++;
        greenCandy = true;

        // reset quest
        bubbleShootCounter = 0;
    }

    public void collectedBlueCandy() {
        blueCandyCounter++;
        blueCandy = true;

        // reset quest
        bubblePopCounter = 0;
    }

    public void collectedRedCandyCounter() {
        redCandyCounter++;
        redCandy = true;

        // reset quest
        jumpCounter = 0;
    }

    public void collectedShoe() {
        shoe = true;

        // reset quest
        walkedDistance = 0;
    }

    public void collectedOrangeParasol() {
        orangeParasol = true;

        // reset quest
        waterBubblePopCounter = 0;
    }

    public void collectedBlueParasol() {
        blueParasol = true;

        // reset quest
        waterBubblePopCounter = 0;
    }

    public void collectedChacknHeart() {
        chacknHeart = true;

        // reset quest
        itemCollectCounter = 0;
    }

    public void collectedCrystalRing() {
        crystalRing = true;

        // reset quest
        blueCandyCounter = 0;
    }

    public void collectedEmeraldRing() {
        emeraldRing = true;

        // reset quest
        greenCandyCounter = 0;
    }

    public void collectedRubyRing() {
        rubyRing = true;

        // reset quest
        redCandyCounter = 0;
    }
}