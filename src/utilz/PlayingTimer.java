package utilz;

public class PlayingTimer {
    private static PlayingTimer instance;

    private boolean firstUpdate = true;

    private long lastTimerUpdate;
    private long timeDelta;

    private PlayingTimer() {
    }

    public static PlayingTimer getInstance() {
        if (instance == null)
            instance = new PlayingTimer();
        return instance;
    }

    public void update() {
        if (firstUpdate) {
            lastTimerUpdate = System.currentTimeMillis();
            firstUpdate = false;
        }

        timeDelta = System.currentTimeMillis() - lastTimerUpdate;
        lastTimerUpdate = System.currentTimeMillis();
    }

    public void reset() {
        lastTimerUpdate = System.currentTimeMillis();
    }

    public void newPlayReset() {
        firstUpdate = true;
        timeDelta = 0;
    }

    public long getTimeDelta() {
        return timeDelta;
    }
}
