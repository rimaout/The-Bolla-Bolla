package view.itemsAndRewards;

import model.itemesAndRewards.ItemModel;

import java.awt.*;

import static view.Constants.ANIMATION_SPEED;

public abstract class ItemView {
    protected ItemModel itemModel;
    protected ItemManagerView itemManagerView = ItemManagerView.getInstance();

    protected int animationTick, animationIndex; // Only for de-spawning animation
    protected boolean soundPlayed;

    public ItemView(ItemModel itemModel) {
        this.itemModel = itemModel;
    }

    public abstract void draw(Graphics g);
    public abstract void audioEffects();

    public void update() {
        if (itemModel.isDeSpawning())
            updateAnimationTick();
    }

    protected void updateAnimationTick() {
        animationTick++;
        if (animationTick >= ANIMATION_SPEED) {
            animationTick = 0;
            animationIndex++;
        }
    }

    protected Boolean isActive() {
        return itemModel.isActive();
    }

    protected ItemModel getItemModel() {
        return itemModel;
    }
}