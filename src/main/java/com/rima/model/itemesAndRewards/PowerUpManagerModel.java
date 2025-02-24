package com.rima.model.itemesAndRewards;

import static java.lang.Math.abs;

import com.rima.model.entities.PlayerModel;
import com.rima.model.entities.EnemyManagerModel;
import com.rima.model.utilz.Constants;
import com.rima.model.utilz.Constants.Items.PowerUpType;
import com.rima.model.projectiles.ProjectileManagerModel;

import java.util.ArrayList;

/**
 * Manages the power-ups in the game.
 *
 * <p>The PowerUpManagerModel class is responsible for handling the activation and effects of power-ups
 * collected by the player. It tracks various power-up states and counters, applies power-up effects to the player,
 * and determines which power-up to spawn based on game events.
 */
public class PowerUpManagerModel {
    private static PowerUpManagerModel instance;
    private final PlayerModel playerModel;

    // PowerUp Flags
    private boolean greenCandy = false;
    private boolean blueCandy = false;
    private boolean redCandy = false;
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

    /**
     * Constructs a new PowerUpManagerModel and initializes the player model.
     */
    private PowerUpManagerModel(){
        this.playerModel = PlayerModel.getInstance();
    }

    public static PowerUpManagerModel getInstance(){
        if (instance == null)
            instance = new PowerUpManagerModel();
        return instance;
    }

    /**
     * Updates the state of the power-ups and applies their effects to the player.
     */
    public void update() {
        applyPowerUp();
    }

    /**
     * Applies the effects of the active power-ups to the player.
     */
    public void applyPowerUp() {

        if (greenCandy)
            playerModel.setBubbleCadenceMultiplier(0.5f);
        else
            playerModel.setBubbleCadenceMultiplier(1);


        if (blueCandy && redCandy) {
            ProjectileManagerModel.getInstance().setPlayerProjectileSpeedMultiplier(1.6f);
            ProjectileManagerModel.getInstance().setPlayerProjectileDistanceMultiplier(1.6f);
            ProjectileManagerModel.getInstance().setPlayerProjectileActiveMultiplier(1.6f);
        }
        else {
            if (blueCandy)
                ProjectileManagerModel.getInstance().setPlayerProjectileSpeedMultiplier(1.6f);
            else
                ProjectileManagerModel.getInstance().setPlayerProjectileSpeedMultiplier(1f);

            if (redCandy) {
                ProjectileManagerModel.getInstance().setPlayerProjectileDistanceMultiplier(1.5f);
                ProjectileManagerModel.getInstance().setPlayerProjectileActiveMultiplier(1.5f);
            } else {
                ProjectileManagerModel.getInstance().setPlayerProjectileDistanceMultiplier(1f);
                ProjectileManagerModel.getInstance().setPlayerProjectileActiveMultiplier(1f);
            }
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

    /**
     * Determines which power-up to spawn based on game events and counters.
     *
     * @return the type of power-up to spawn, or null if no power-up should be spawned
     */
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

    /**
     * Resets the state of the power-up manager for a new play session.
     */
    public void newPlayReset() {
        reset();
    }

    /**
     * Resets all power-up states and counters.
     */
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

    /**
     * Increases the bubble shoot counter.
     */
    public void increaseBubbleShootCounter() {
        bubbleShootCounter++;
    }

    /**
     * Increases the bubble pop counter.
     */
    public void increaseBubblePopCounter() {
        bubblePopCounter++;
    }

    /**
     * Increases the jump counter.
     */
    public void increaseJumpCounter() {
        jumpCounter++;
    }

    /**
     * Adds the specified distance to the walked distance counter.
     *
     * @param distance the distance to add
     */
    public void addDistance(float distance) {
        walkedDistance += abs(distance);
    }

    /**
     * Increases the water bubble pop counter.
     */
    public void increaseWaterBubblePopCounter() {
        waterBubblePopCounter++;
    }

    /**
     * Increases the item collect counter.
     */
    public void increaseItemCollectCounter() {
        itemCollectCounter++;
    }

    /**
     * Marks the green candy as collected and resets the bubble shoot counter.
     */
    public void collectedGreenCandy() {
        greenCandyCounter++;
        greenCandy = true;

        // reset quest
        bubbleShootCounter = 0;
    }

    /**
     * Marks the blue candy as collected and resets the bubble pop counter.
     */
    public void collectedBlueCandy() {
        blueCandyCounter++;
        blueCandy = true;

        // reset quest
        bubblePopCounter = 0;
    }

    /**
     * Marks the red candy as collected and resets the jump counter.
     */
    public void collectedRedCandyCounter() {
        redCandyCounter++;
        redCandy = true;

        // reset quest
        jumpCounter = 0;
    }

    /**
     * Marks the shoe as collected and resets the walked distance counter.
     */
    public void collectedShoe() {
        shoe = true;

        // reset quest
        walkedDistance = 0;
    }

    /**
     * Marks the orange parasol as collected and resets the water bubble pop counter.
     */
    public void collectedOrangeParasol() {
        orangeParasol = true;

        // reset quest
        waterBubblePopCounter = 0;
    }

    /**
     * Marks the blue parasol as collected and resets the water bubble pop counter.
     */
    public void collectedBlueParasol() {
        blueParasol = true;

        // reset quest
        waterBubblePopCounter = 0;
    }

    /**
     * Marks the Chack'n Heart as collected and resets the item collect counter.
     */
    public void collectedChacknHeart() {
        chacknHeart = true;

        // reset quest
        itemCollectCounter = 0;
    }

    /**
     * Marks the crystal ring as collected and resets the blue candy counter.
     */
    public void collectedCrystalRing() {
        crystalRing = true;

        // reset quest
        blueCandyCounter = 0;
    }

    /**
     * Marks the emerald ring as collected and resets the green candy counter.
     */
    public void collectedEmeraldRing() {
        emeraldRing = true;

        // reset quest
        greenCandyCounter = 0;
    }

    /**
     * Marks the ruby ring as collected and resets the red candy counter.
     */
    public void collectedRubyRing() {
        rubyRing = true;

        // reset quest
        redCandyCounter = 0;
    }
}