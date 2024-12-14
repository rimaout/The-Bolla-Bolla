package view.bubbles.specialBubbles;

import model.bubbles.specialBubbles.WaterFlowModel;
import view.utilz.Constants.AudioConstants;
import view.audio.AudioPlayer;
import java.awt.*;

import static view.utilz.Constants.PlayerConstants.IDLE_ANIMATION;
import static model.utilz.Constants.WaterFLow.*;

public class WaterFlowView {

    private final WaterFlowModel waterFlowModel;
    private final SpecialBubbleManagerView bubbleManager = SpecialBubbleManagerView.getInstance();

    private boolean soundAlreadyPlayed = false;

    public WaterFlowView(WaterFlowModel waterFlowModel) {
        this.waterFlowModel = waterFlowModel;
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Composite originalComposite = g2d.getComposite();

        // Set the transparency level based on the transparency state
        float alpha = waterFlowModel.getIsTransparent() ? 0.0f : 0.83f; // 0.0f for complete transparency when transparent

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        for (Point pos : waterFlowModel.getLastPositions()) {
            g.drawImage(bubbleManager.getWaterBubbleSprites()[1][0], (int) (pos.x - HITBOX_OFFSET_X), (int) (pos.y - HITBOX_OFFSET_Y), W, H, null);
        }

        if (waterFlowModel.isPlayerCaptured())
            g.drawImage(bubbleManager.getPlayerSprites()[IDLE_ANIMATION][0], (int) (waterFlowModel.getHitbox().x - HITBOX_OFFSET_X) + waterFlowModel.flipX(), (int) (waterFlowModel.getHitbox().y - HITBOX_OFFSET_Y), W * waterFlowModel.flipW(), H, null);
        else
            g.drawImage(bubbleManager.getWaterBubbleSprites()[1][0], (int) (waterFlowModel.getHitbox().x - HITBOX_OFFSET_X), (int) (waterFlowModel.getHitbox().y - HITBOX_OFFSET_Y), W, H, null);

        g2d.setComposite(originalComposite);

        if (waterFlowModel.isPlaySoundActive() && !soundAlreadyPlayed) {
            AudioPlayer.getInstance().playSoundEffect(AudioConstants.WATER_FLOW);
            soundAlreadyPlayed = true;
        }
    }

    protected boolean isActive() {
        return waterFlowModel.isActive();
    }

    public Object getWaterFlowModel() {
        return waterFlowModel;
    }
}