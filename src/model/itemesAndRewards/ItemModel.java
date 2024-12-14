package model.itemesAndRewards;

import model.utilz.PlayingTimer;

import static model.utilz.Constants.Items.*;

import java.awt.geom.Rectangle2D;

public abstract class ItemModel {
    protected final PlayingTimer timer = PlayingTimer.getInstance();

    protected int x, y;
    protected Rectangle2D.Float hitbox;
    protected ItemType itemType;

    protected int deSpawnTimer = DE_SPAWN_TIMER;
    protected boolean deSpawning = false;
    protected boolean active = true;
    protected boolean collected = false;

    public ItemModel(int x, int y, ItemType itemType) {
        this.x = x;
        this.y = y;
        this.itemType = itemType;

        initHitbox();
    }

    public abstract void addPoints();
    public abstract void applyEffect();

    protected void update(){
        updateTimers();
    }

    private void updateTimers() {
        deSpawnTimer -= (int) timer.getTimeDelta();

        if (deSpawnTimer <= 0 && !deSpawning) {
            deSpawning = true;
            deSpawnTimer = 600;
        }

        if (deSpawnTimer <= 0 && deSpawning) {
            active = false;
        }
    }

    protected void initHitbox() {
        hitbox = new Rectangle2D.Float(x + OFFSET_X, y + OFFSET_Y, HITBOX_W, HITBOX_H);
    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isDeSpawning(){
        return deSpawning;
    }

    public void deactivateItem() {
        active = false;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    public boolean isCollected() {
        return collected;
    }

    public ItemType getItemType() {
        return itemType;
    }
}