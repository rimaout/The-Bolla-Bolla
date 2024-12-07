package model.itemesAndRewards;

import model.entities.PlayerModel;
import model.PlayingTimer;

import java.util.ArrayList;

import static model.Constants.PointsManager.*;
import static model.Constants.PointsManager.PointsType.*;

public class RewardPointsManagerModel {
    private static RewardPointsManagerModel instance;
    private final PlayerModel playerModel;
    private final PlayingTimer timer = PlayingTimer.getInstance();

    private ArrayList<PointsModel> pointsModelModelArray;
    private int consecutivePops;
    private int consecutivePopsTimer;

    private RewardPointsManagerModel(PlayerModel playerModel) {
        this.playerModel = playerModel;
        pointsModelModelArray = new ArrayList<>();

    }

    public static RewardPointsManagerModel getInstance(PlayerModel playerModel) {
        if (instance == null) {
            instance = new RewardPointsManagerModel(playerModel);
        }
        return instance;
    }

    public static RewardPointsManagerModel getInstance() {
        return instance;
    }

    public void update() {

        updateTimer();
        updateChainReactionReward();

        for (PointsModel p : pointsModelModelArray) {
            if (p.isActive())
                p.update();
        }
    }

    private void updateTimer() {
        consecutivePopsTimer -= (int) timer.getTimeDelta();
    }

    public void addSmallPoints(int value) {
        playerModel.addPoints(value);
        pointsModelModelArray.add(new PointsModel(value, playerModel.getHitbox().x, playerModel.getHitbox().y, SMALL));
    }

    public void addBigPoints(int value) {
        playerModel.addPoints(value);
        pointsModelModelArray.add(new PointsModel(value, playerModel.getHitbox().x, playerModel.getHitbox().y, BIG));
    }

    public void addChainReactionReward(int consecutivePops) {
        this.consecutivePops = consecutivePops;
        restartConsecutivePopsTimer();
    }

    private void updateChainReactionReward() {
        if (consecutivePopsTimer <= 0 && consecutivePops > 0) {
            addBigPoints(1000 * (int) Math.pow(2, consecutivePops-1));
            consecutivePops = 0;
        }
    }

    private void restartConsecutivePopsTimer() {
        consecutivePopsTimer = CONSECUTIVE_POP_DELAY;
    }

    public void newPlayReset() {
        pointsModelModelArray.clear();
        consecutivePops = 0;
        consecutivePopsTimer = 0;
    }

    public void newLevelReset() {
        pointsModelModelArray.clear();
        consecutivePops = 0;
        consecutivePopsTimer = 0;
    }

    public ArrayList<PointsModel> getPointsModelModelArray() {
        return pointsModelModelArray;
    }
}