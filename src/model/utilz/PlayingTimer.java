package model.utilz;

/**
 * Singleton class that manages the playing timer, used to heep track of the time delta between updates.
 */
public class PlayingTimer {
    private static PlayingTimer instance;

    private boolean firstUpdate = true;

    private long lastTimerUpdate;
    private long timeDelta;

    /**
     * Private constructor for singleton pattern.
     */
    private PlayingTimer() {
    }

    /**
     * Returns the singleton instance of the PlayingTimer.
     *
     * @return the singleton instance
     */
    public static PlayingTimer getInstance() {
        if (instance == null)
            instance = new PlayingTimer();
        return instance;
    }

    /**
     * Updates the timer. Calculates the time delta since the last update.
     * If this is the first update, initializes the last timer update time.
     */
    public void update() {
        if (firstUpdate) {
            lastTimerUpdate = System.currentTimeMillis();
            firstUpdate = false;
        }

        timeDelta = System.currentTimeMillis() - lastTimerUpdate;
        lastTimerUpdate = System.currentTimeMillis();
    }

    /**
     * Resets the timer by setting the last timer update time to the current system time.
     */
    public void reset() {
        lastTimerUpdate = System.currentTimeMillis();
    }

    /**
     * Resets the timer for a new play session.
     * Sets the first update flag to true and resets the time delta.
     */
    public void newPlayReset() {
        firstUpdate = true;
        timeDelta = 0;
    }

    /**
     * Returns the time delta between the last two updates.
     *
     * @return the time delta in milliseconds
     */
    public long getTimeDelta() {
        return timeDelta;
    }
}