package model.itemesAndRewards;

import model.Constants;
import model.PlayingTimer;
import model.Constants.PointsManager.PointsType;

public class PointsModel {
    private final PlayingTimer timer = PlayingTimer.getInstance();

    private final int value;
    private float x;
    private float y;
    private final PointsType type;

    private int activeTimer = 1200;
    private boolean active = true;

    public PointsModel(int value, float x, float y, PointsType type) {
        this.value = value;
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public void update() {
       updateTimers();
       move();
    }

    private void updateTimers() {
        activeTimer -= (int) timer.getTimeDelta();

        if (activeTimer <= 0) {
            active = false;
        }
    }

    private void move() {
        y -= (float) (0.1 * Constants.SCALE);
    }

    public boolean isActive() {
        return active;
    }

    public PointsType getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}


