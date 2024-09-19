package itemesAndRewards;

import main.Game;
import utilz.Constants.PointsManager.PointsType;
import utilz.PlayingTimer;

import java.awt.*;

import static utilz.Constants.PointsManager.*;
import static utilz.Constants.PointsManager.BIG_H;
import static utilz.Constants.PointsManager.PointsType.SMALL;

public class Points {
    private RewardPointsManager rewardPointsManager = RewardPointsManager.getInstance();
    private PlayingTimer timer = PlayingTimer.getInstance();

    private int value;
    private float x;
    private float y;
    private PointsType type;

    private int drawTime = 1200;
    private float alpha = 1.0f; // Initial alpha value (for transparency)
    private boolean active = true;

    public Points(int value, float x, float y, PointsType type) {
        this.value = value;
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public void draw(Graphics2D g) {
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha)); // Set transparency

        if ( type == SMALL )
            g.drawImage(rewardPointsManager.getSmallPointsImage(value), (int) x, (int) y, SMALL_W, SMALL_H, null);
        else
            g.drawImage(rewardPointsManager.getBigPointsImage(value), (int) x, (int) y, BIG_W, BIG_H, null);

        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f)); // Reset transparency
    }

    public void update() {
       updateTimers();
       move();
       fade();
    }

    private void updateTimers() {
        drawTime -= (int) timer.getTimeDelta();

        if (drawTime <= 0) {
            active = false;
        }
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


