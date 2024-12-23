package model.utilz;

/**
 * Interface containing various constants used in the game.
 */
public interface Constants {

    // Base Game Constants
    int FPS_SET = 60;
    int UPS_SET = 180;
    int SCALE = 3;
    float GRAVITY = 0.0078f * SCALE;

    // Game Dimensions
    int TILES_DEFAULT_SIZE = 8;
    int TILES_IN_WIDTH = 32;
    int TILES_IN_HEIGHT = 28;
    int TILES_SIZE = TILES_DEFAULT_SIZE * SCALE;
    int GAME_WIDTH = TILES_IN_WIDTH * TILES_SIZE;
    int GAME_HEIGHT = TILES_IN_HEIGHT * TILES_SIZE;


    /**
     * Enum representing the possible directions.
     */
    enum Direction {
        LEFT, RIGHT, UP, DOWN, NONE;

        /**
         * Returns a random horizontal direction (LEFT or RIGHT).
         *
         * @return a random horizontal direction
         */
        public static Direction GetRandomHorizontalDirection() {
            return switch ((int) (Math.random() * 2)) {
                case 0 -> LEFT;
                case 1 -> RIGHT;
                default -> NONE;
            };
        }
    }

    /**
     * Class containing constants and enums related to level transitions.
     */
    class LevelTransition {

        /**
         * Enum representing the possible states of a level transition.
         */
        public enum TransitionState {
            LEVEL_TRANSITION, LOADING_NEW_LEVEL, START_NEW_LEVEL
        }

        public static final float LEVEL_TRANSITION_SPEED = 0.58f * SCALE;
    }

    /**
     * Interface containing constants related to bubbles.
     */
    interface Bubble {

        // Bubble Dimensions
        int DEFAULT_W = 18;
        int DEFAULT_H = 18;
        int W = DEFAULT_W * SCALE;
        int H = DEFAULT_H * SCALE;

        // Hitbox Constants
        int HITBOX_W = 16 * SCALE;
        int HITBOX_H = 16 * SCALE;
        int HITBOX_OFFSET_X = SCALE;
        int HITBOX_OFFSET_Y = SCALE;

        // Collision Box Constants
        int INTERNAL_BOX_W = 8 * SCALE;
        int INTERNAL_BOX_H = 8 * SCALE;
        int INTERNAL_BOX_OFFSET_X = (int) (3.5 * SCALE);
        int INTERNAL_BOX_OFFSET_Y = 4 * SCALE;
        int EXTERNAL_BOX_W = (int) (13.5 * SCALE);
        int EXTERNAL_BOX_H = 16 * SCALE;
        int EXTERNAL_BOX_OFFSET_X = (int) (1.5 * SCALE);

        // PlayerBubble states
        int NORMAL = 1;
        int RED = 2;
        int BLINKING = 3;
        int POP_NORMAL = 4;
        int POP_RED = 5;
        int DEAD = 6;

        // SPECIAL BUBBLE STATES
        int BUBBLE = 0;
        int POPPED = 1;

        // State Timers (seconds)
        int NORMAL_TIMER = 12000;
        int RED_TIMER = 3000;
        int BLINKING_TIMER = 2000;
        int POP_TIMER = 500;
        int SHAKE_TIMER = 100;

        // Movement Constants
        float BUBBLE_SPEED = 0.1f * SCALE;
        float SHAKING_SPEED = 0.03f * SCALE;
        float DEAD_X_SPEED = 0.6f * SCALE;
        float DEAD_Y_SPEED = 1.2f * SCALE;

        /**
         * Enum representing the different types of bubbles.
         */
        enum BubbleType {
            ENEMY_BUBBLE, EMPTY_BUBBLE, WATER_BUBBLE, LIGHTNING_BUBBLE
        }
    }

    /**
     * Interface containing constants related to water flow.
     */
    interface WaterFLow {
        // Dimensions
        int DEFAULT_W = 18;
        int DEFAULT_H = 18;
        int W = DEFAULT_W * SCALE;
        int H = DEFAULT_H * SCALE;

        // Hitbox
        int HITBOX_W = 6 * SCALE;
        int HITBOX_H = 12 * SCALE;
        int HITBOX_OFFSET_X = 5 * SCALE;
        int HITBOX_OFFSET_Y = 5 * SCALE;

        // Movement Constants
        float WATER_FLOW_SPEED = 1.5f * SCALE;
        int ADD_WATER_DROP_INTERVAL = 60;
    }

    /**
     * Interface containing constants related to the game intro.
     */
    interface INTRO {

        float TRANSITION_SPEED = 0.53f * SCALE;
        float TEXT_START_Y = 20 * SCALE;
        float PLAYER_START_X = 9 * TILES_SIZE;
        float PLAYER_START_Y = 10 * TILES_SIZE;
        int RADIUS = 75;
        int TOTAL_LAPS = 3;
        float ANGLE_INCREMENT = 0.02f;

        /**
         * Enum representing the different states of the intro.
         */
        enum IntroState {
            INTRO, LEVEL_TRANSITION, START_NEW_LEVEL
        }
    }

    /**
     * Interface containing constants related to the player.
     */
    interface PlayerConstants {

        //Spawning Position
        float SPAWN_X = 3 * TILES_SIZE;
        float SPAWN_Y = 24 * TILES_SIZE + 2 * SCALE;

        // Player Image and Hitbox Constants
        int DEFAULT_W = 18;
        int DEFAULT_H = 18;
        int PLAYER_W = 18 * SCALE;
        int PLAYER_H = 18 * SCALE;
        int HITBOX_W = 13 * SCALE;
        int HITBOX_H = 13 * SCALE;
        int OFFSET_X = 3 * SCALE;
        int OFFSET_Y = 3 * SCALE;

        // Movement values and variables
        float WALK_SPEED = 0.33f * SCALE;
        float FALL_SPEED = 0.35f * SCALE;
        float JUMP_SPEED = -0.79f * SCALE;

        // Timers
        int IMMUNE_TIME_AFTER_RESPAWN = 4000; // 2 seconds
        int ATTACK_TIMER = 500;
        int RESPAWN_TIME = 2000;
    }

    /**
     * Interface containing constants related to enemies.
     */
    interface EnemyConstants {

        /**
         * Enum representing the different types of enemies.
         */
        enum EnemyType {
            ZEN_CHAN, MAITA, SKEL_MONSTA
        }

        // Maita Constants
        int FIREBALL_TIMER = 5000;
        int FIREBALL_INITIAL_TIMER = 2000;

        // Spawning Constants
        int ZEN_CHAN_LEFT = 1;
        int ZEN_CHAN_RIGHT = 2;
        int MAITA_LEFT = 3;
        int MAITA_RIGHT = 4;
        float INITIAL_SPAWN_POINT_Y = -3 * TILES_SIZE;
        float SPAWN_TRANSITION_SPEED = 0.31f * SCALE;

        //SkellMonsta Constants
        int SKEL_MONSTA_SPAWN_X = 3 * TILES_SIZE;
        int SKEL_MONSTA_SPAWN_Y = 24 * TILES_SIZE + 2 * SCALE;
        int SKEL_MONSTA_MOVEMENT_MAX_DISTANCE = 6 * TILES_SIZE;
        int SKEL_MONSTA_MOVEMENT_TIMER = 300;
        int SKEL_MONSTA_SPAWNING_TIMER = 600;
        int SKEL_MONSTA_DESPAWNING_TIMER = 600;

        // Hitbox Constants
        int ENEMY_HITBOX_W = 14 * SCALE;
        int ENEMY_HITBOX_H = 15 * SCALE;
        int ENEMY_HITBOX_OFFSET_X = 2 * SCALE;
        int ENEMY_HITBOX_OFFSET_Y = SCALE;

        // General Enemy Sprite Sizes
        int ENEMY_DEFAULT_W = 18;
        int ENEMY_DEFAULT_H = 18;
        int ENEMY_W = ENEMY_DEFAULT_W * SCALE;
        int ENEMY_H = ENEMY_DEFAULT_H * SCALE;

        // Enemy States
        int NORMAL_STATE = 0;
        int HUNGRY_STATE = 1;
        int BOBBLE_STATE = 2;
        int DEAD_STATE = 3;

        // Enemy Action Constants
        float NORMAL_FALL_SPEED = 0.3f * SCALE;
        float NORMAL_FLY_SPEED = 0.25f * SCALE;
        float NORMAL_WALK_SPEED = 0.3f * SCALE;
        float HUNGRY_FALL_SPEED = 0.3f * SCALE;
        float HUNGRY_FLY_SPEED = 0.5f * SCALE;
        float HUNGRY_WALK_SPEED = 0.7f * SCALE;
        int NORMAL_PLAYER_INFO_MAX_UPDATE_INTERVAL = 8000; // 8 seconds
        int HUNGRY_PLAYER_INFO_MAX_UPDATE_INTERVAL = 5000; // 5
        float JUMP_Y_SPEED = -0.44f * SCALE;
        float JUMP_X_SPEED = 0.4f * SCALE;
        float VIEWING_RANGE = TILES_SIZE * 15;
    }

    /**
     * Interface containing constants related to items.
     */
    interface Items {

        // Size
        int DEFAULT_W = 18;
        int DEFAULT_H = 18;
        int W = DEFAULT_W * SCALE;
        int H = DEFAULT_H * SCALE;

        // Hitbox Constants
        int HITBOX_W = 6 * SCALE;
        int HITBOX_H = 6 * SCALE;
        int OFFSET_X = 6 * SCALE;
        int OFFSET_Y = 7 * SCALE;

        // Timers
        int DE_SPAWN_TIMER = 8000;
        int SPAWN_POWER_UP_TIMER = 12000;

        /**
         * Enum representing the different types of items.
         */
        enum ItemType {
            BUBBLE_REWARD, POWER_UP
        }

        /**
         * Enum representing the different types of bubble rewards.
         */
        enum BubbleRewardType {
            APPLE, PEPPER, GRAPE, PERSIMMON, CHERRY, MUSHROOM, BANANA, WATER_CRISTAL;

            /**
             * Returns the bubble reward type based on the consecutive pops counter.
             *
             * @param consecutivePopsCounter the number of consecutive pops
             * @return the corresponding bubble reward type
             */
            public static BubbleRewardType GetBubbleRewardType(int consecutivePopsCounter) {
                return switch (consecutivePopsCounter) {
                    case 1 -> APPLE;
                    case 2 -> PEPPER;
                    case 3 -> GRAPE;
                    case 4 -> PERSIMMON;
                    case 5 -> CHERRY;
                    case 6 -> MUSHROOM;
                    case 7 -> BANANA;
                    default -> BANANA;
                };
            }

            /**
             * Returns the points associated with the given bubble reward type.
             *
             * @param bubbleRewardType the bubble reward type
             * @return the points associated with the bubble reward type
             */
            public static int GetPoints(BubbleRewardType bubbleRewardType) {
                return switch (bubbleRewardType) {
                    case APPLE -> 1000;
                    case PEPPER -> 2000;
                    case GRAPE -> 3000;
                    case PERSIMMON -> 4000;
                    case CHERRY -> 5000;
                    case MUSHROOM -> 6000;
                    case BANANA -> 7000;
                    case WATER_CRISTAL -> 6000;
                };
            }
        }

        /**
         * Enum representing the different types of power-ups.
         */
        enum PowerUpType {
            GREEN_CANDY, BLUE_CANDY, RED_CANDY, SHOE, ORANGE_PARASOL, BLUE_PARASOL, CHACKN_HEART, CRYSTAL_RING, EMERALD_RING, RUBY_RING;

            /**
             * Returns the points associated with the given power-up type.
             *
             * @param powerUpType the power-up type
             * @return the points associated with the power-up type
             */
            public static int GetPoints (PowerUpType powerUpType) {
                return switch (powerUpType) {
                    case GREEN_CANDY -> 100;
                    case BLUE_CANDY -> 100;
                    case RED_CANDY -> 100;
                    case SHOE -> 100;
                    case ORANGE_PARASOL -> 200;
                    case BLUE_PARASOL -> 200;
                    case CHACKN_HEART -> 3000;
                    case CRYSTAL_RING -> 1000;
                    case EMERALD_RING -> 1000;
                    case RUBY_RING -> 1000;
                };
            }
        }
    }

    /**
     * Interface containing constants related to the points manager.
     */
    interface PointsManager {

        // Size Small
        int SMALL_DEFAULT_W = 18;
        int SMALL_DEFAULT_H = 10;
        int SMALL_W = (int) (SMALL_DEFAULT_W / 1.1 * SCALE);
        int SMALL_H = (int) (SMALL_DEFAULT_H / 1.1 * SCALE);

        // Size Big
        int BIG_DEFAULT_W = 28;
        int BIG_DEFAULT_H = 13;
        int BIG_W = (int) (SMALL_DEFAULT_W * 1.35 * SCALE);
        int BIG_H = (int) (SMALL_DEFAULT_H * 1.35 * SCALE);

        // Timers
        int CONSECUTIVE_POP_DELAY = 200;

        /**
         * Enum representing the different types of points.
         */
        enum PointsType {
            SMALL, BIG
        }
    }

    /**
     * Interface containing constants related to projectiles.
     */
    interface Projectiles {

        // Size
        int DEFAULT_W = 18;
        int DEFAULT_H = 18;
        int W = DEFAULT_W * SCALE;
        int H = DEFAULT_H * SCALE;

        // Hitbox Constants
        int HITBOX_W = 9 * SCALE;
        int HITBOX_H = 9 * SCALE;
        int OFFSET_X = -5 * SCALE;
        int OFFSET_Y = -4 * SCALE;

        // Movement Constants
        float PLAYER_BUBBLE_SPEED = 1.2f * SCALE;
        float MAITA_FIREBALL_SPEED = 0.53f * SCALE;
        float LIGHTNING_SPEED = 1f * SCALE;

        /**
         * Enum representing the different states of a projectile.
         */
        enum ProjectileState {
            MOVING, IMPACT
        }

        /**
         * Enum representing the different types of projectiles.
         */
        enum ProjectileType {
            PLAYER_BUBBLE, MAITA_FIREBALL, LIGHTNING
        }
    }

    /**
     * Interface containing constants related to the hurry-up manager.
     */
    interface HurryUpManager {
        // Timers
        int START_HURRY_UP_TIMER = 17000;
        int ACTIVATE_SKEL_MONSTA_TIMER = 20000;
    }

    /**
     * Interface containing constants related to the bubble generator.
     */
    interface BubbleGenerator {

        // Timers
        int BUBBLE_GENERATION_INTERVAL = 4000;
        int INITIAL_BUBBLE_GENERATOR_INTERVAL = 2000;
        int MAX_SPECIAL_BUBBLES = 7;

        // Position Constants
        int LEFT_GENERATOR_X = 10 * TILES_SIZE;
        int RIGHT_GENERATOR_X = 20 * TILES_SIZE;
        int TOP_GENERATOR_Y = -2 * TILES_SIZE;
        int BOTTOM_GENERATOR_Y = TILES_IN_HEIGHT + 2 * TILES_SIZE;

        /**
         * Enum representing the different positions of the bubble generator.
         */
        enum GeneratorPosition {
            NONE, TOP, BOTTOM
        }

        /**
         * Enum representing the different types of bubble generators.
         */
        enum GeneratorType {
            NONE, WATER_BUBBLE, LIGHTNING_BUBBLE
        }
    }
}