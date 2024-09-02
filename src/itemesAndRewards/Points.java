package itemesAndRewards;

import main.Game;
import utilz.Constants.PointsManager.PointsType;

public class Points {
    public int value;
    public float x;
    public float y;
    public PointsType type;

    public int drawTime = 1200;
    private long lastTimerUpdate;

    public float alpha = 1.0f; // Initial alpha value (for transparency)

    private boolean firstUpdate = true;
    private boolean active = true;

    public Points(int value, float x, float y, PointsType type) {
        this.value = value;
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public void update() {
        if (firstUpdate)
            firstUpdate();

       updateTimers();
       move();
       fade();
    }

    private void firstUpdate() {
        lastTimerUpdate = System.currentTimeMillis();
        firstUpdate = false;
    }

    private void updateTimers() {
        long timeDelta = System.currentTimeMillis() - lastTimerUpdate;
        lastTimerUpdate = System.currentTimeMillis();
        drawTime -= (int) timeDelta;

        if (drawTime <= 0)
            active = false;
    }

    private void move() {
        y -= (float) (0.1 * Game.SCALE);
    }

    private void fade() {
        alpha -= 0.003f;
    }

    public boolean isActive() {
        return active;
    }
}


