package bubbles.specialBubbles;

import entities.Player;
import utilz.Constants.Direction;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utilz.Constants.Bubble.*;

public class WaterBubble extends SpecialBubble {
    public WaterBubble(float x, float y, Direction direction) {
        super(x, y, direction);
    }

    @Override
    public void draw(Graphics g) {
        BufferedImage[][] waterBubbleSprites = SpecialBubbleManager.getInstance().getWaterBubbleSprites();
        g.drawImage(waterBubbleSprites[0][0], (int) (hitbox.x - HITBOX_OFFSET_X), (int) (hitbox.y - HITBOX_OFFSET_Y), IMMAGE_W, IMMAGE_H, null);
    }

    @Override
    public void update() {
        updateAnimationTick();

        if (state == BUBBLE) {
            updateDirection();
            updatePosition();
            updateCollisionBoxes();
            pacManEffect();
        }
    }

    @Override
    public void playerPop(Player player) {
        active = false;
        SpecialBubbleManager.getInstance().addWaterFlow(new WaterFlow(hitbox.x, hitbox.y, direction));
    }
}
