package bubbles.specialBubbles;

import utilz.Constants;

import java.util.Random;

import static utilz.Constants.BubbleGenerator.*;
import utilz.Constants.Direction;

public class BubbleGenerator {
    private GeneratorType generatorType;
    private GeneratorPosition generatorPosition;

    private boolean firstUpdate = true;
    private long lastTimerUpdate;
    private int spawnTimer;

    private Random random = new Random();

    public BubbleGenerator(GeneratorType generatorType, GeneratorPosition generatorPosition) {
        this.generatorType = generatorType;
        this.generatorPosition = generatorPosition;
    }

    public void update() {
        updateTimers();
        checkBubbleSpawn();
    }

    public void updateTimers() {
        if (firstUpdate) {
            lastTimerUpdate = System.currentTimeMillis();
            spawnTimer = BUBBLE_GENERATION_INTERVAL/2;
            firstUpdate = false;
        }

        long timeDelta = System.currentTimeMillis() - lastTimerUpdate;
        lastTimerUpdate = System.currentTimeMillis();
        spawnTimer -= timeDelta;
    }

    public void checkBubbleSpawn() {
        if (spawnTimer <= 0) {
            spawnTimer = BUBBLE_GENERATION_INTERVAL;

            if (SpecialBubbleManager.getInstance().getActiveBubblesCount() < MAX_SPECIAL_BUBBLES)
                spawnBubble();
        }
    }

    public void spawnBubble() {
        SpecialBubbleManager bubbleManager = SpecialBubbleManager.getInstance();

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
                bubbleManager.addBubble(new WaterBubble(bubbleX, bubbleY, direction));
                break;
            case LIGHTNING_BUBBLE:
                bubbleManager.addBubble(new LightningBubble(bubbleX, bubbleY, direction));
                break;
        }
    }
}
