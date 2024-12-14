package model.bubbles.specialBubbles;

import model.entities.PlayerModel;
import model.levels.LevelManagerModel;

import java.util.LinkedList;

public class SpecialBubbleManagerModel {

    // This class is responsible for managing the special model.bubbles (not generated by the player), such as waterBubble and fireBubble

    private static SpecialBubbleManagerModel instance;
    private final PlayerModel playerModel;

    private final LinkedList<SpecialBubbleModel> bubblesModels;
    private final LinkedList<WaterFlowModel> waterFlowsModel;

    private BubbleGenerator bubbleGenerator;

    private SpecialBubbleManagerModel() {
        this.playerModel = PlayerModel.getInstance();
        bubblesModels = new LinkedList<>();
        waterFlowsModel = new LinkedList<>();
    }

    public static SpecialBubbleManagerModel getInstance() {
        if (instance == null) {
            instance = new SpecialBubbleManagerModel();
        }
        return instance;
    }

    public void update() {
        bubbleGenerator.update();

        for (SpecialBubbleModel b : bubblesModels) {
            if (b.isActive()) {
                b.update();
                b.checkCollisionWithPlayer(playerModel);
            }
        }

        for (WaterFlowModel w : waterFlowsModel) {
            if (w.isActive()) {
                w.update();
                w.updateCollisions(playerModel);
            }
        }
    }

    public void loadBubbleGenerator() {
        bubbleGenerator = LevelManagerModel.getInstance().getCurrentLevel().getBubbleGenerator();
    }

    public void newLevelReset() {
        bubblesModels.clear();
        waterFlowsModel.clear();
    }

    public void newPlayReset() {
        newLevelReset();
    }

    public void addBubble(SpecialBubbleModel bubble) {
        bubblesModels.add(bubble);
    }

    public void addWaterFlow(WaterFlowModel waterFlowModels) {
        waterFlowsModel.add(waterFlowModels);
    }

    public int getActiveBubblesCount() {
        int count = 0;
        for (SpecialBubbleModel b : bubblesModels) {
            if (b.isActive())
                count++;
        }
        return count;
    }

    public LinkedList<SpecialBubbleModel> getBubblesModels() {
        return bubblesModels;
    }

    public LinkedList<WaterFlowModel> getWaterFlowsModels() {
        return waterFlowsModel;
    }
}