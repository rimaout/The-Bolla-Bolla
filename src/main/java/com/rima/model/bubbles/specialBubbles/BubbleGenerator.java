package com.rima.model.bubbles.specialBubbles;

import com.rima.model.utilz.Constants;
import com.rima.model.utilz.PlayingTimer;
import com.rima.model.utilz.Constants.Direction;

import java.util.Random;

import static com.rima.model.utilz.Constants.BubbleGenerator.*;

/**
 * Generates special bubbles at specified intervals.
 *
 * <p>This class is responsible for generating special bubbles (e.g., water bubbles, lightning bubbles) at regular intervals.
 * It uses a timer to control the spawn rate and determines the position and direction of the bubbles based on the generator type and position.
 */
public class BubbleGenerator {
    private final Random random = new Random();
    private final PlayingTimer timer = PlayingTimer.getInstance();

    private final GeneratorType generatorType;
    private final GeneratorPosition generatorPosition;

    private int spawnTimer = INITIAL_BUBBLE_GENERATOR_INTERVAL;

    /**
     * Constructs a new BubbleGenerator.
     *
     * @param generatorType the type of bubbles to generate
     * @param generatorPosition the position of the generator (top or bottom)
     */
    public BubbleGenerator(GeneratorType generatorType, GeneratorPosition generatorPosition) {
        this.generatorType = generatorType;
        this.generatorPosition = generatorPosition;
    }

    /**
     * Updates the state of the bubble generator.
     *
     * <p>This method updates the timers and checks if a new bubble should be spawned.
     */
    public void update() {
        updateTimers();
        checkBubbleSpawn();
    }

    /**
     * Updates the spawn timer.
     *
     * <p>This method decreases the spawn timer based on the elapsed time.
     */
    public void updateTimers() {
        spawnTimer -= (int) timer.getTimeDelta();
    }

    /**
     * Checks if a new bubble should be spawned.
     *
     * <p>If the spawn timer has elapsed and the number of active special bubbles is below the maximum limit, a new bubble is spawned.
     */
    public void checkBubbleSpawn() {
        if (spawnTimer <= 0) {
            spawnTimer = BUBBLE_GENERATION_INTERVAL;

            if (SpecialBubbleManagerModel.getInstance().getActiveBubblesCount() < MAX_SPECIAL_BUBBLES)
                spawnBubble();
        }
    }

    /**
     * Spawns a new bubble.
     *
     * <p>This method calculates the position and direction of the new bubble and adds it to the special bubble manager.
     */
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