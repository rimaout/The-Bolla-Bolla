package model.itemesAndRewards;

import model.utilz.Constants;
import model.utilz.PlayingTimer;
import model.utilz.Constants.PointsManager.PointsType;

/**
 * Represents a points item in the game.
 *
 * <p>The `PointsModel` class handles the behavior of points items.
 */
public class PointsModel {
    private final PlayingTimer timer = PlayingTimer.getInstance();

    private final int value;
    private float x;
    private float y;
    private final PointsType type;

    private int activeTimer = 1200;
    private boolean active = true;

    /**
     * Constructs a new `PointsModel` with the specified value, position, and type.
     *
     * @param value the value of the points item
     * @param x the x-coordinate of the points item
     * @param y the y-coordinate of the points item
     * @param type the type of the points item
     */
    public PointsModel(int value, float x, float y, PointsType type) {
        this.value = value;
        this.x = x;
        this.y = y;
        this.type = type;
    }

    /**
     * Updates the state of the points item, including its timers and position.
     */
    public void update() {
       updateTimers();
       move();
    }

    /**
     * Updates the timers for the points item, handling its active state.
     */
    private void updateTimers() {
        activeTimer -= (int) timer.getTimeDelta();

        if (activeTimer <= 0) {
            active = false;
        }
    }

    /**
     * Moves the points item upwards over time.
     */
    private void move() {
        y -= (float) (0.1 * Constants.SCALE);
    }

    // -------- Getters Methods --------

    /**
     * Checks if the points item is active.
     *
     * @return true if the points item is active, false otherwise
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Returns the type of the points item.
     *
     * @return the type of the points item
     */
    public PointsType getType() {
        return type;
    }

    /**
     * Returns the value of the points item.
     *
     * @return the value of the points item
     */
    public int getValue() {
        return value;
    }

    /**
     * Returns the x-coordinate of the points item.
     *
     * @return the x-coordinate of the points item
     */
    public float getX() {
        return x;
    }

    /**
     * Returns the y-coordinate of the points item.
     *
     * @return the y-coordinate of the points item
     */
    public float getY() {
        return y;
    }
}