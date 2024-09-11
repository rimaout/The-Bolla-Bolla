package itemesAndRewards;


import entities.EnemyManager;
import entities.Player;
import main.Game;
import projectiles.ProjectileManager;
import utilz.Constants.Items.PowerUpType;

import java.util.ArrayList;

import static java.lang.Math.abs;

public class PowerUpManager{
    private static PowerUpManager instance;
    private final Player player;

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


    private PowerUpManager(Player player){
        this.player = player;
    }

    public static PowerUpManager getInstance(Player player){
        if (instance == null)
            instance = new PowerUpManager(player);
        return instance;
    }

    public static PowerUpManager getInstance(){
        return instance;
    }

    public void update() {
        applyPowerUp();
    }

    public void applyPowerUp() {

        if (greenCandy)
            player.setBubbleCadenceMultiplier(0.5f);
        else
            player.setBubbleCadenceMultiplier(1);

        if (blueCandy)
            ProjectileManager.getInstance().setPlayerProjectileSpeedMultiplier(1.65f);
        else
            ProjectileManager.getInstance().setPlayerProjectileSpeedMultiplier(1f);

        if (redCandy)
            ProjectileManager.getInstance().setPlayerProjectileDistanceMultiplier(1.65f);
        else
            ProjectileManager.getInstance().setPlayerProjectileDistanceMultiplier(1f);

        if (shoe)
            player.setSpeedMultiplier(1.7f);
        else
            player.setSpeedMultiplier(1);

        if (chacknHeart) {
            player.setChacknHeartImmunity(15000);
            EnemyManager.getInstance().setChacknHeartfreeze(15000);
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
            player.setJumpPoints(500);
        else
            player.setJumpPoints(0);

        if (crystalRing)
            player.setWalkPoints(10);
        else
            player.setWalkPoints(0);

        if (rubyRing)
            player.setBubbleShotPoints(100);
        else
            player.setBubbleShotPoints(0);
    }

    public PowerUpType getPowerUpToSpawn() {

        ArrayList<PowerUpType> powerUps = new ArrayList<>();

        if (bubbleShootCounter >= 10 ) powerUps.add(PowerUpType.GREEN_CANDY);       // Working // TODO: change to 35
        if (bubblePopCounter >= 10) powerUps.add(PowerUpType.BLUE_CANDY);           // Working // TODO: change to 35
        if (jumpCounter >= 10) powerUps.add(PowerUpType.RED_CANDY);                 // Working // TODO: change to 35
        if (walkedDistance >= Game.GAME_WIDTH * 5) powerUps.add(PowerUpType.SHOE);  // Working // TODO: change to * 15
        if (waterBubblePopCounter >= 15) powerUps.add(PowerUpType.ORANGE_PARASOL);  // TODO: not implemented water bubble yet
        if (waterBubblePopCounter >= 20) powerUps.add(PowerUpType.BLUE_PARASOL);    // TODO: not implemented water bubble yet
        if (itemCollectCounter >= 4) powerUps.add(PowerUpType.CHACKN_HEART);        // Working // TODO: change to 55
        if (blueCandyCounter >= 1) powerUps.add(PowerUpType.CRYSTAL_RING);          // Working // TODO: change to 3
        if (greenCandyCounter >= 1) powerUps.add(PowerUpType.EMERALD_RING);         // Working // TODO: change to 3
        if (redCandyCounter >= 1) powerUps.add(PowerUpType.RUBY_RING);              // Working // TODO: change to 3

        if (powerUps.isEmpty()) return null;

        return powerUps.get((int) (Math.random() * powerUps.size()));
    }

    public void resetAll() {
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

    // TODO
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
