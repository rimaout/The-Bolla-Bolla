package bubbles.specialBubbles;

import entities.Player;
import main.Game;
import utilz.Constants.Direction;

import java.awt.*;

import static utilz.Constants.Bubble.*;

public class WaterBubble extends SpecialBubble {

    public WaterBubble(float x, float y, Direction direction) {
        super(x, y, direction);
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(specialBubbleManager.getWaterBubbleSprites()[0][0], (int) (hitbox.x - HITBOX_OFFSET_X), (int) (hitbox.y - HITBOX_OFFSET_Y), IMMAGE_W, IMMAGE_H, null);
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
    public void playerPop(Player player) {
        active = false;
        spawnWaterFlow();
    }

    private void spawnWaterFlow() {

        // if waterBubble conpenetrates with left perimeter wall, spawn waterFlow not in the wall
        if (getTileX() < 2) {
            SpecialBubbleManager.getInstance().addWaterFlow(new WaterFlow(hitbox.x + Game.TILES_SIZE, hitbox.y));
            return;
        }


        // if waterBubble conpenetrates with right perimeter wall, spawn waterFlow not in the wall
        if ( getTileX() + Game.TILES_SIZE > Game.TILES_IN_WIDTH - 3) {
            SpecialBubbleManager.getInstance().addWaterFlow(new WaterFlow(hitbox.x - Game.TILES_SIZE, hitbox.y));
            return;
        }

        // if the bubble does not conpenetrate with the perimeter walls, spawn waterFlow in the same position of the bubble
        SpecialBubbleManager.getInstance().addWaterFlow(new WaterFlow(hitbox.x, hitbox.y));
    }
}
