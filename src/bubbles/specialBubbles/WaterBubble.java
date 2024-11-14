package bubbles.specialBubbles;

import itemesAndRewards.PowerUpManagerModel;
import model.entities.PlayerModel;
import model.utilz.Constants;
import model.utilz.Constants.Direction;

import java.awt.*;

import static model.utilz.Constants.Bubble.*;

public class WaterBubble extends SpecialBubble {

    public WaterBubble(float x, float y, Direction direction) {
        super(x, y, direction);
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(bubbleManager.getWaterBubbleSprites()[0][0], (int) (hitbox.x - HITBOX_OFFSET_X), (int) (hitbox.y - HITBOX_OFFSET_Y), IMAGE_W, IMAGE_H, null);
    }

    @Override
    public void update() {
        updateAnimationTick();
        updateDirection();
        updatePosition();
        updateCollisionBoxes();
        pacManEffect();
    }

    @Override
    public void playerPop(PlayerModel playerModel) {
        active = false;
        spawnWaterFlow();

        PowerUpManagerModel.getInstance().increaseWaterBubblePopCounter();
    }

    private void spawnWaterFlow() {

        // if waterBubble conpenetrates with left perimeter wall, spawn waterFlow not in the wall
        if (getTileX() < 2) {
            SpecialBubbleManager.getInstance().addWaterFlow(new WaterFlow(hitbox.x + Constants.TILES_SIZE, hitbox.y));
            return;
        }


        // if waterBubble conpenetrates with right perimeter wall, spawn waterFlow not in the wall
        if ( getTileX() + Constants.TILES_SIZE > Constants.TILES_IN_WIDTH - 3) {
            SpecialBubbleManager.getInstance().addWaterFlow(new WaterFlow(hitbox.x - Constants.TILES_SIZE, hitbox.y));
            return;
        }

        // if the bubble does not concentrate with the perimeter walls, spawn waterFlow in the same position of the bubble
        SpecialBubbleManager.getInstance().addWaterFlow(new WaterFlow(hitbox.x, hitbox.y));
    }
}
