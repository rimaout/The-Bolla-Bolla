package itemesAndRewards;

import entities.Player;

import static utilz.Constants.ANIMATION_SPEED;
import static utilz.Constants.Items.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class Item {
    protected int x, y;
    protected Rectangle2D.Float hitbox;
    protected int animationTick, animationIndex; // Only for de-spawning animation

    protected long lastTimerUpdate = System.currentTimeMillis();
    protected int deSpawnTimer = DE_SPAWN_TIMER;

    protected boolean deSpawning = false;
    protected boolean active = true;

    public Item(int x, int y){
        this.x = x;
        this.y = y;

        initHitbox();
    }

    protected void update(){
        updateTimers();

        if (deSpawning)
            updateAnimationTick();
    }

    abstract void draw(Graphics g);
    abstract void addPoints(Player player);
    abstract void applyEffect(Player player);

    protected void updateAnimationTick() {
        animationTick++;
        if (animationTick >= ANIMATION_SPEED) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= 2) {
                active = false;
            }
        }
    }

    private void updateTimers() {
        long timeDelta = System.currentTimeMillis() - lastTimerUpdate;
        lastTimerUpdate = System.currentTimeMillis();

        deSpawnTimer -= timeDelta;

        if (deSpawnTimer <= 0)
            deSpawning = true;

    }

    protected void initHitbox() {
        hitbox = new Rectangle2D.Float(x + OFFSET_X, y + OFFSET_Y, HITBOX_W, HITBOX_H);
    }

    public void drawHitbox(Graphics g) {
        // For debugging purposes

        g.setColor(Color.RED);
        g.drawRect((int) hitbox.x, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isDeSpawning() {
        return deSpawning;
    }

}
