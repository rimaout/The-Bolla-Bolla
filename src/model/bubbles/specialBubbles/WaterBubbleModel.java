package model.bubbles.specialBubbles;

import model.entities.PlayerModel;
import model.utilz.Constants;
import model.utilz.Constants.Direction;
import model.itemesAndRewards.PowerUpManagerModel;

import static model.utilz.Constants.Bubble.BubbleType.WATER_BUBBLE;

public class WaterBubbleModel extends SpecialBubbleModel {

    public WaterBubbleModel(float x, float y, Direction direction) {
        super(x, y, direction);
        bubbleType = WATER_BUBBLE;
    }

    @Override
    public void update() {
        updateTimers();
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
            SpecialBubbleManagerModel.getInstance().addWaterFlow(new WaterFlowModel(hitbox.x + Constants.TILES_SIZE, hitbox.y));
            return;
        }


        // if waterBubble conpenetrates with right perimeter wall, spawn waterFlow not in the wall
        if ( getTileX() + Constants.TILES_SIZE > Constants.TILES_IN_WIDTH - 3) {
            SpecialBubbleManagerModel.getInstance().addWaterFlow(new WaterFlowModel(hitbox.x - Constants.TILES_SIZE, hitbox.y));
            return;
        }

        // if the bubble does not concentrate with the perimeter walls, spawn waterFlow in the same position of the bubble
        SpecialBubbleManagerModel.getInstance().addWaterFlow(new WaterFlowModel(hitbox.x, hitbox.y));
    }
}