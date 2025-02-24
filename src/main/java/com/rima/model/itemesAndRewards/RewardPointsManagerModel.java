package com.rima.model.itemesAndRewards;

import com.rima.model.bubbles.playerBubbles.ChainReactionManager;
import com.rima.model.utilz.PlayingTimer;
import com.rima.model.entities.PlayerModel;

import java.util.ArrayList;

import static com.rima.model.utilz.Constants.PointsManager.*;
import static com.rima.model.utilz.Constants.PointsManager.PointsType.*;

/**
 * Manages the reward points ({@link PointsModel}) in the game.
 *
 * <p>The RewardPointsManagerModel class is responsible for handling the points ({@link PointsModel}) awarded to the player.
 * It tracks the points items, manages the chain reaction rewards, and updates the state of the points items.
 */
public class RewardPointsManagerModel {
    private static RewardPointsManagerModel instance;
    private final PlayerModel playerModel;
    private final PlayingTimer timer = PlayingTimer.getInstance();

    private ArrayList<PointsModel> pointsModelModelArray;
    private int consecutivePops;
    private int consecutivePopsTimer;

    /**
     * Constructs a new RewardPointsManagerModel and initializes the player model and points list.
     */
    private RewardPointsManagerModel() {
        this.playerModel = PlayerModel.getInstance();
        pointsModelModelArray = new ArrayList<>();
    }

    /**
     * Returns the singleton instance of the RewardPointsManagerModel.
     *
     * @return the singleton instance
     */
    public static RewardPointsManagerModel getInstance() {
        if (instance == null)
            instance = new RewardPointsManagerModel();
        return instance;
    }

    /**
     * Updates the state of all active the reward points, including timers and chain reaction rewards.
     */
    public void update() {

        updateTimer();
        updateChainReactionReward();

        for (PointsModel p : pointsModelModelArray) {
            if (p.isActive())
                p.update();
        }
    }

    /**
     * Updates the timer for consecutive pops.
     */
    private void updateTimer() {
        consecutivePopsTimer -= (int) timer.getTimeDelta();
    }

    /**
     * Adds small points (points generated collecting items) to the player's score and creates a new points item.
     *
     * @param value the value of the points to add
     */
    public void addSmallPoints(int value) {
        playerModel.addPoints(value);
        pointsModelModelArray.add(new PointsModel(value, playerModel.getHitbox().x, playerModel.getHitbox().y, SMALL));
    }

    /**
     * Adds big points (point generated my popping bubbles) to the player's score and creates a new points item.
     *
     * @param value the value of the points to add
     */
    public void addBigPoints(int value) {
        playerModel.addPoints(value);
        pointsModelModelArray.add(new PointsModel(value, playerModel.getHitbox().x, playerModel.getHitbox().y, BIG));
    }

    /**
     * Adds a points to the player based on the number of consecutive pops, calculated by the {@link ChainReactionManager} class.
     *
     * @param consecutivePops the number of consecutive pops
     */
    public void addChainReactionReward(int consecutivePops) {
        this.consecutivePops = consecutivePops;
        restartConsecutivePopsTimer();
    }

    /**
     * Updates the chain reaction reward based on the timer.
     *
     * <p>If the timer for consecutive pops has expired and there are consecutive pops recorded,
     * this method awards big points to the player and resets the consecutive pops count.
     */
    private void updateChainReactionReward() {
        if (consecutivePopsTimer <= 0 && consecutivePops > 0) {
            addBigPoints(1000 * (int) Math.pow(2, consecutivePops-1));
            consecutivePops = 0;
        }
    }

    /**
     * Restarts the timer for consecutive pops.
     */
    private void restartConsecutivePopsTimer() {
        consecutivePopsTimer = CONSECUTIVE_POP_DELAY;
    }

    /**
     * Resets the state of the reward points manager for a new play session.
     */
    public void newPlayReset() {
        pointsModelModelArray.clear();
        consecutivePops = 0;
        consecutivePopsTimer = 0;
    }

    /**
     * Resets the state of the reward points manager for a new level.
     */
    public void newLevelReset() {
        pointsModelModelArray.clear();
        consecutivePops = 0;
        consecutivePopsTimer = 0;
    }

    /**
     * Returns the list of points items.
     *
     * @return the list of points items
     */
    public ArrayList<PointsModel> getPointsModelModelArray() {
        return pointsModelModelArray;
    }
}