package itemesAndRewards;


import entities.Player;
import main.Game;
import utilz.Constants.Items.PowerUpType;
import utilz.Constants.Items.PowerUpType.*;

import java.util.ArrayList;

import static java.lang.Math.abs;

public class PowerUpManager{
    private static PowerUpManager instance;
    private Player player;

    // PowerUp Flags
    private boolean greenCandy = false;
    private boolean blueCandy = false;
    private boolean redCandy = false;
    private boolean shoe = false;
    private boolean orangeParasol = false;
    private boolean blueParasol = false;
    private boolean chacknHeart = false;
    private boolean crystalRing = false;
    private boolean emeraldRing = false;
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
        applyPowerUpEffects();
        printPowerUpStatus();
    }

    public void printPowerUpStatus() {
        // print all counters
        System.out.println("bubbleShootCounter: " + bubbleShootCounter);
        System.out.println("bubblePopCounter: " + bubblePopCounter);
        System.out.println("jumpCounter: " + jumpCounter);
        System.out.println("walkedDistance: " + walkedDistance);
        System.out.println("waterBubblePopCounter: " + waterBubblePopCounter);
        System.out.println("itemCollectCounter: " + itemCollectCounter);
        System.out.println("greenCandyCounter: " + greenCandyCounter);
        System.out.println("blueCandyCounter: " + blueCandyCounter);
        System.out.println("redCandyCounter: " + redCandyCounter);
    }

    public void applyPowerUpEffects() {
    }

    public PowerUpType getPowerUpToSpawn() {

        ArrayList<PowerUpType> powerUps = new ArrayList<>();

        if (bubbleShootCounter >= 35) powerUps.add(PowerUpType.GREEN_CANDY);
        if (bubblePopCounter >= 35) powerUps.add(PowerUpType.BLUE_CANDY);
        if (jumpCounter >= 35) powerUps.add(PowerUpType.RED_CANDY);
        if (walkedDistance >= Game.GAME_WIDTH*15) powerUps.add(PowerUpType.SHOE);
        if (waterBubblePopCounter >= 15) powerUps.add(PowerUpType.ORANGE_PARASOL);
        if (itemCollectCounter >= 20) powerUps.add(PowerUpType.BLUE_PARASOL);
        if (itemCollectCounter >= 55) powerUps.add(PowerUpType.CHACKN_HEART);
        if (blueCandyCounter >= 3) powerUps.add(PowerUpType.CRYSTAL_RING);
        if (greenCandyCounter >= 3) powerUps.add(PowerUpType.EMERALD_RING);
        if (redCandyCounter >= 3) powerUps.add(PowerUpType.RUBY_RING);

        if (powerUps.isEmpty()) return null;

        return powerUps.get((int) (Math.random() * powerUps.size()));
    }

    public void resetAll() {
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

    public void increaseGreenCandyCounter() {
        greenCandyCounter++;
    }

    public void increaseBlueCandyCounter() {
        blueCandyCounter++;
    }

    public void increaseRedCandyCounter() {
        redCandyCounter++;
    }

    public void setShoe(boolean shoe) {
        this.shoe = shoe;
    }

    public void setOrangeParasol(boolean orangeParasol) {
        this.orangeParasol = orangeParasol;
    }

    public void setBlueParasol(boolean blueParasol) {
        this.blueParasol = blueParasol;
    }

    public void setChacknHeart(boolean chacknHeart) {
        this.chacknHeart = chacknHeart;
    }

    public void setCrystalRing(boolean crystalRing) {
        this.crystalRing = crystalRing;
    }

    public void setEmeraldRing(boolean emeraldRing) {
        this.emeraldRing = emeraldRing;
    }

    public void setRubyRing(boolean rubyRing) {
        this.rubyRing = rubyRing;
    }
}
