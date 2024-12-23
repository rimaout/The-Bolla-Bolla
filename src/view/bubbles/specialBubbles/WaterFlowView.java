package view.bubbles.specialBubbles;

import model.bubbles.specialBubbles.WaterFlowModel;
import view.utilz.Constants.AudioConstants;
import view.audio.AudioPlayer;
import java.awt.*;

import static view.utilz.Constants.PlayerConstants.IDLE_ANIMATION;
import static model.utilz.Constants.WaterFLow.*;

/**
 * The WaterFlowView class represents the view for a {@link WaterFlowModel}.
 * It handles updating the water flow's animation and drawing the water flow on the screen.
 */
public class WaterFlowView {
    private final WaterFlowModel waterFlowModel;
    private final SpecialBubbleManagerView bubbleManager = SpecialBubbleManagerView.getInstance();
    private boolean soundAlreadyPlayed = false;

    /**
     * Constructs a WaterFlowView with the specified WaterFlowModel.
     *
     * @param waterFlowModel the model for the water flow entity
     */
    public WaterFlowView(WaterFlowModel waterFlowModel) {
        this.waterFlowModel = waterFlowModel;
    }

    /**
     * Draws the water flow on the screen, and plays sound if needed.
     *
     * @param g the Graphics object to draw with
     */
    public void draw(Graphics g) {
        playSound();

        Graphics2D g2d = (Graphics2D) g;
        Composite originalComposite = g2d.getComposite();

        // Set the transparency level based on the transparency state
        float alpha = waterFlowModel.getIsTransparent() ? 0.0f : 0.83f; // 0.0f for complete transparency when transparent

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        // draw the water flow "tail"
        for (Point pos : waterFlowModel.getLastPositions()) {
            g.drawImage(bubbleManager.getWaterBubbleSprites()[1][0], (int) (pos.x - HITBOX_OFFSET_X), (int) (pos.y - HITBOX_OFFSET_Y), W, H, null);
        }

        // draw player if captured
        if (waterFlowModel.isPlayerCaptured())
            g.drawImage(bubbleManager.getPlayerSprites()[IDLE_ANIMATION][0], (int) (waterFlowModel.getHitbox().x - HITBOX_OFFSET_X) + waterFlowModel.flipX(), (int) (waterFlowModel.getHitbox().y - HITBOX_OFFSET_Y), W * waterFlowModel.flipW(), H, null);
        else
            g.drawImage(bubbleManager.getWaterBubbleSprites()[1][0], (int) (waterFlowModel.getHitbox().x - HITBOX_OFFSET_X), (int) (waterFlowModel.getHitbox().y - HITBOX_OFFSET_Y), W, H, null);

        g2d.setComposite(originalComposite);
    }

    /**
     * Plays the water flow sound effect if it is active and has not been played yet.
     * Sets the soundAlreadyPlayed flag to true after playing the sound.
     */
    private void playSound() {
        if (waterFlowModel.isPlaySoundActive() && !soundAlreadyPlayed) {
            AudioPlayer.getInstance().playSoundEffect(AudioConstants.WATER_FLOW);
            soundAlreadyPlayed = true;
        }
    }

    /**
     * Checks if the water flow is active.
     *
     * @return true if the water flow is active, false otherwise
     */
    protected boolean isActive() {
        return waterFlowModel.isActive();
    }

    /**
     * Returns the water flow model.
     *
     * @return the water flow model
     */
    public Object getWaterFlowModel() {
        return waterFlowModel;
    }
}