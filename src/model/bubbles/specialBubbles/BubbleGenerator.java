package model.bubbles.specialBubbles;

import model.utilz.Constants;
import model.utilz.PlayingTimer;
import model.utilz.Constants.Direction;

import java.util.Random;

import static model.utilz.Constants.BubbleGenerator.*;

public class BubbleGenerator {
    private final Random random = new Random();
    private final PlayingTimer timer = PlayingTimer.getInstance();

    private final GeneratorType generatorType;
    private final GeneratorPosition generatorPosition;

    private int spawnTimer = INITIAL_BUBBLE_GENERATOR_INTERVAL;

    public BubbleGenerator(GeneratorType generatorType, GeneratorPosition generatorPosition) {
        this.generatorType = generatorType;
        this.generatorPosition = generatorPosition;
    }

    public void update() {
        updateTimers();
        checkBubbleSpawn();
    }

    public void updateTimers() {
        spawnTimer -= (int) timer.getTimeDelta();
    }

    public void checkBubbleSpawn() {
        if (spawnTimer <= 0) {
            spawnTimer = BUBBLE_GENERATION_INTERVAL;

            if (SpecialBubbleManagerModel.getInstance().getActiveBubblesCount() < MAX_SPECIAL_BUBBLES)
                spawnBubble();
        }
    }

    public void spawnBubble() {
        SpecialBubbleManagerModel bubbleManager = SpecialBubbleManagerModel.getInstance();

        // Calculate x pos (chose randomly between Left Generator Position and Right Generator Position)
        int bubbleX;
        if (random.nextBoolean())
            bubbleX = Constants.BubbleGenerator.LEFT_GENERATOR_X;
        else
            bubbleX = Constants.BubbleGenerator.RIGHT_GENERATOR_X;

        // Calculate y pos
        int bubbleY;
        if (generatorPosition == GeneratorPosition.TOP)
            bubbleY = Constants.BubbleGenerator.TOP_GENERATOR_Y;
        else
            bubbleY = Constants.BubbleGenerator.BOTTOM_GENERATOR_Y;

        // Calculate the direction
        Direction direction;
        if (generatorPosition == GeneratorPosition.TOP)
            direction = Direction.DOWN;
        else
            direction = Direction.UP;


        switch (generatorType) {
            case WATER_BUBBLE:
                bubbleManager.addBubble(new WaterBubbleModel(bubbleX, bubbleY, direction));
                break;
            case LIGHTNING_BUBBLE:
                bubbleManager.addBubble(new LightningBubbleModel(bubbleX, bubbleY, direction));
                break;
        }
    }
}