package view.itemsAndRewards;

import model.itemesAndRewards.ItemModel;

import java.awt.*;

import static view.utilz.Constants.ANIMATION_SPEED;

/**
 * The abstract ItemView class represents the view for a {@link ItemModel}.
 * It handles drawing the item, playing audio effects, and updating the item's state.
 */
public abstract class ItemView {
    protected ItemModel itemModel;
    protected ItemManagerView itemManagerView = ItemManagerView.getInstance();

    protected int animationTick, animationIndex; // Only for de-spawning animation
    protected boolean soundPlayed;

    /**
     * Constructs an ItemView with the specified ItemModel.
     *
     * @param itemModel the model of the item
     */
    public ItemView(ItemModel itemModel) {
        this.itemModel = itemModel;
    }

    /**
     * Draws the item on the screen.
     *
     * @param g the Graphics object to draw with
     */
    public abstract void draw(Graphics g);

    /**
     * Plays the audio effects for the item.
     */
    public abstract void audioEffects();

    /**
     * Updates the item's state.
     * If the item is de-spawning, updates the animation tick.
     */
    public void update() {
        if (itemModel.isDeSpawning())
            updateAnimationTick();
    }

    /**
     * Updates the animation tick for the de-spawning animation.
     * Increments the animation tick and index.
     */
    protected void updateAnimationTick() {
        animationTick++;
        if (animationTick >= ANIMATION_SPEED) {
            animationTick = 0;
            animationIndex++;
        }
    }

    /**
     * Checks if the item is active.
     *
     * @return true if the item is active, false otherwise
     */
    protected Boolean isActive() {
        return itemModel.isActive();
    }

    /**
     * Returns the ItemModel associated with this view.
     *
     * @return the ItemModel
     */
    protected ItemModel getItemModel() {
        return itemModel;
    }
}