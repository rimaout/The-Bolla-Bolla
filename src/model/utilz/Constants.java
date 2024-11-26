package model.utilz;

import java.awt.*;

public class Constants {

    public final static int FPS_SET = 60;
    public final static int UPS_SET = 180;

    public final static int TILES_DEFAULT_SIZE = 8;
    public final static int SCALE = 3;
    public final static int TILES_IN_WIDTH = 32;
    public final static int TILES_IN_HEIGHT = 28;
    public final static int TILES_SIZE = TILES_DEFAULT_SIZE * SCALE;
    public final static int GAME_WIDTH = TILES_IN_WIDTH * TILES_SIZE;
    public final static int GAME_HEIGHT = TILES_IN_HEIGHT * TILES_SIZE;
    public static final float GRAVITY = 0.0078f * SCALE;
    public static final int ANIMATION_SPEED = 55;

    public enum Direction {
        LEFT, RIGHT, UP, DOWN, NONE;

        public static Direction GetRandomDirection() {
            return switch ((int) (Math.random() * 4)) {
                case 0 -> LEFT;
                case 1 -> RIGHT;
                case 2 -> UP;
                case 3 -> DOWN;
                default -> NONE;
            };
        }

        public static Direction GetRandomHorizontalDirection() {
            return switch ((int) (Math.random() * 2)) {
                case 0 -> LEFT;
                case 1 -> RIGHT;
                default -> NONE;
            };
        }

        public static Direction GetOppositeDirection(Direction direction) {
            return switch (direction) {
                case LEFT -> RIGHT;
                case RIGHT -> LEFT;
                case UP -> DOWN;
                case DOWN -> UP;
                default -> NONE;
            };
        }
    }

    public static class Home {

        // Logo Constants
        public static final int LOGO_END_Y = 15 * SCALE;
        public static final float LOGO_SPEED = 0.85f * 3;

        // Twinkle Bubble Constants
        public static final int BUBBLE_DEFAULT_W = 8;
        public static final int BUBBLE_DEFAULT_H = 8;
        public static final int BUBBLE_W = BUBBLE_DEFAULT_W * SCALE;
        public static final int BUBBLE_H = BUBBLE_DEFAULT_H * SCALE;

        public static final float BUBBLE_SPEED = 0.0664f * 3 * SCALE;
    }

    public static class LevelTransition {

        public enum TransitionState {
            LEVEL_TRANSITION, LOADING_NEW_LEVEL, START_NEW_LEVEL
        }

        public static final float LEVEL_TRANSITION_SPEED = 0.58f * SCALE;
    }

    public static class Bubble {
        public static final int BUBBLE_ANIMATION_SPEED = 25;

        // Bubble Dimensions
        public static final int DEFAULT_W = 18;
        public static final int DEFAULT_H = 18;
        public static final int IMAGE_W = DEFAULT_W * SCALE;
        public static final int IMAGE_H = DEFAULT_H * SCALE;

        // Hitbox Constants
        public static final int HITBOX_W = 16 * SCALE;
        public static final int HITBOX_H = 16 * SCALE;
        public static final int HITBOX_OFFSET_X = SCALE;
        public static final int HITBOX_OFFSET_Y = SCALE;

        // Collision Box Constants
        public static final int INTERNAL_BOX_W = 8 * SCALE;
        public static final int INTERNAL_BOX_H = 8 * SCALE;
        public static final int INTERNAL_BOX_OFFSET_X = (int) (3.5 * SCALE);
        public static final int INTERNAL_BOX_OFFSET_Y = 4 * SCALE;
        public static final int EXTERNAL_BOX_W = (int) (13.5 * SCALE);
        public static final int EXTERNAL_BOX_H = 16 * SCALE;
        public static final int EXTERNAL_BOX_OFFSET_X = (int) (1.5 * SCALE);

        // PlayerBubble states
        public static final int NORMAL = 1;
        public static final int RED = 2;
        public static final int BLINKING = 3;
        public static final int POP_NORMAL = 4;
        public static final int POP_RED = 5;
        public static final int DEAD = 6;

        // SPECIAL BUBBLE STATES
        public static final int BUBBLE = 0;
        public static final int POPPED = 1;

        // State Timers (seconds)
        public static final int NORMAL_TIMER = 12000;
        public static final int RED_TIMER = 3000;
        public static final int BLINKING_TIMER = 2000;

        // Movement Constants
        public static final float BUBBLE_SPEED = 0.1f * SCALE;
        public static final float SHAKING_SPEED = 0.03f * SCALE;
        public static final float DEAD_X_SPEED = 0.6f * SCALE;
        public static final float DEAD_Y_SPEED = 1.2f * SCALE;



        public static int getSpriteAmount(int bubbleState) {
            return switch (bubbleState) {
                case NORMAL -> 2;
                case RED -> 2;
                case BLINKING -> 2;
                case POP_NORMAL, POP_RED -> 3;
                default -> 4;
            };
        }
    }

    public static class WaterFLow {
        public static final int DEFAULT_W = 18;
        public static final int DEFAULT_H = 18;
        public static final int W = DEFAULT_W * SCALE;
        public static final int H = DEFAULT_H * SCALE;

        public static final int HITBOX_W = 6 * SCALE;
        public static final int HITBOX_H = 12 * SCALE;
        public static final int HITBOX_OFFSET_X = 5 * SCALE;
        public static final int HITBOX_OFFSET_Y = 5 * SCALE;

        public static final float WATER_FLOW_SPEED = 1.5f * SCALE;
        public static final int ADD_WATER_DROP_INTERVAL = 60;
    }

    public static class INTRO {

        public static final float TRANSITION_SPEED = 0.53f * SCALE;
        public static final float TEXT_START_Y = 20 * SCALE;
        public static final float PLAYER_START_X = 9 * TILES_SIZE;
        public static final float PLAYER_START_Y = 10 * TILES_SIZE;
        public static final int RADIUS = 75;
        public static final int TOTAL_LAPS = 3;
        public static final float ANGLE_INCREMENT = 0.02f;
        public enum IntroState {
            INTRO, LEVEL_TRANSITION, START_NEW_LEVEL
        }
    }

    public static class PlayerConstants {

        //Spawning Position
        public static final float SPAWN_X = 3 * TILES_SIZE;
        public static final float SPAWN_Y = 24 * TILES_SIZE + 2 * SCALE;

        // Player Image and Hitbox Constants
        public static final int DEFAULT_W = 18;
        public static final int DEFAULT_H = 18;
        public static final int IMAGE_W = 18 * SCALE;
        public static final int IMAGE_H = 18 * SCALE;
        public static final int HITBOX_W = 13 * SCALE;
        public static final int HITBOX_H = 13 * SCALE;
        public static final int OFFSET_X = 3 * SCALE;
        public static final int OFFSET_Y = 3 * SCALE;

        // Movement values and variables
        public static final float WALK_SPEED = 0.33f * SCALE;
        public static final float FALL_SPEED = 0.35f * SCALE;
        public static final float JUMP_SPEED = -0.79f * SCALE;

        // Animation Constants
        public static final int IDLE_ANIMATION = 0;
        public static final int RUNNING_ANIMATION = 1;
        public static final int JUMPING_ANIMATION = 2;
        public static final int FALLING_ANIMATION = 3;
        public static final int ATTACK_ANIMATION = 4;
        public static final int DEAD_ANIMATION = 5;

        public static final int IMMUNE_TIME_AFTER_RESPAWN = 2000; // 2 seconds
        public static final int ATTACK_TIMER = 500;

        public static int getSpriteAmount(int playerAnimation) {
            return switch (playerAnimation) {
                case IDLE_ANIMATION -> 2;
                case RUNNING_ANIMATION -> 2;
                case JUMPING_ANIMATION -> 2;
                case FALLING_ANIMATION -> 2;
                case ATTACK_ANIMATION -> 1;
                case DEAD_ANIMATION -> 7;
                default -> 0;
            };
        }
    }

    public static class EnemyConstants {

        // Enemy Types
        public enum EnemyType {
            ZEN_CHAN, MAITA, SKEL_MONSTA
        }

        // Maita Constants
        public static final int FIREBALL_TIMER = 5000;
        public static final int FIREBALL_INITIAL_TIMER = 2000;

        // Spawning Constants
        public static final int ZEN_CHAN_LEFT = 1;
        public static final int ZEN_CHAN_RIGHT = 2;
        public static final int MAITA_LEFT = 3;
        public static final int MAITA_RIGHT = 4;
        public static final float INITIAL_SPAWN_POINT_Y = -3 * TILES_SIZE;
        public static final float SPAWN_TRANSITION_SPEED = 0.31f * SCALE;

        //SkellMonsta Constants
        public static final int SKEL_MONSTA_SPAWN_X = 3 * TILES_SIZE;
        public static final int SKEL_MONSTA_SPAWN_Y = 24 * TILES_SIZE + 2 * SCALE;
        public static final int SKEL_MONSTA_MOVEMENT_MAX_DISTANCE = 6 * TILES_SIZE;
        public static final int SKEL_MONSTA_MOVEMENT_TIMER = 300;
        public static final int SKEL_MONSTA_SPAWNING_TIMER = 600;
        public static final int SKEL_MONSTA_DESPAWNING_TIMER = 600;

        // Hitbox Constants
        public static final int ENEMY_HITBOX_W = 14 * SCALE;
        public static final int ENEMY_HITBOX_H = 15 * SCALE;
        public static final int ENEMY_HITBOX_OFFSET_X = 2 * SCALE;
        public static final int ENEMY_HITBOX_OFFSET_Y = SCALE;

        // General Enemy Sprite Sizes
        public static final int ENEMY_DEFAULT_W = 18;
        public static final int ENEMY_DEFAULT_H = 18;
        public static final int ENEMY_W = ENEMY_DEFAULT_W * SCALE;
        public static final int ENEMY_H = ENEMY_DEFAULT_H * SCALE;

        // General Animation Constants
        public static final int WALKING_ANIMATION_NORMAL = 0;
        public static final int WALKING_ANIMATION_HUNGRY = 1;
        public static final int BOBBLE_GREEN_ANIMATION = 3;
        public static final int BOBBLE_RED_ANIMATION = 5;
        public static final int DEAD_ANIMATION = 6;
        public static final int BOBBLE_GREEN_POP_ANIMATION = 7;
        public static final int BOBBLE_RED_POP_ANIMATION = 8;
        public static final float NORMAL_ANIMATION_SPEED_MULTIPLIER = 1.0f;
        public static final float HUNGRY_ANIMATION_SPEED_MULTIPLIER = 0.53f;

        // Enemy States
        public static final int NORMAL_STATE = 0;
        public static final int HUNGRY_STATE = 1;
        public static final int BOBBLE_STATE = 2;
        public static final int DEAD_STATE = 3;

        // Enemy Action Constants
        public static final float NORMAL_FALL_SPEED = 0.3f * SCALE;
        public static final float NORMAL_FLY_SPEED = 0.25f * SCALE;
        public static final float NORMAL_WALK_SPEED = 0.3f * SCALE;
        public static final float HUNGRY_FALL_SPEED = 0.3f * SCALE;
        public static final float HUNGRY_FLY_SPEED = 0.5f * SCALE;
        public static final float HUNGRY_WALK_SPEED = 0.7f * SCALE;

        public static final int DIRECTION_CHANGE_MAX_COUNTER = 6;
        public static final int NORMAL_PLAYER_INFO_MAX_UPDATE_INTERVAL = 8000; // 8 seconds
        public static final int HUNGRY_PLAYER_INFO_MAX_UPDATE_INTERVAL = 5000; // 5 seconds
        
        public static final float JUMP_Y_SPEED = -0.44f * SCALE;
        public static final float JUMP_X_SPEED = 0.4f * SCALE;
        public static final float VIEWING_RANGE = TILES_SIZE * 15;

        public static int getSpriteAmount(EnemyType enemyType, int enemyState) {
            if (enemyType == EnemyType.ZEN_CHAN || enemyType == EnemyType.MAITA) {
                return switch (enemyState) {
                    case WALKING_ANIMATION_NORMAL -> 2;
                    case WALKING_ANIMATION_HUNGRY -> 2;
                    case BOBBLE_GREEN_ANIMATION -> 2;
                    case BOBBLE_RED_ANIMATION -> 2;
                    case DEAD_ANIMATION -> 4;
                    default -> 0;
                };
            }

            if (enemyType == EnemyType.SKEL_MONSTA)
                return 2; // SkelMonsta only has animations of 2 frames

            return 0;
        }
    }

    public static class Items {

        public static final int DEFAULT_W = 18;
        public static final int DEFAULT_H = 18;
        public static final int W = DEFAULT_W * SCALE;
        public static final int H = DEFAULT_H * SCALE;

        public static final int HITBOX_W = 6 * SCALE;
        public static final int HITBOX_H = 6 * SCALE;
        public static final int OFFSET_X = 6 * SCALE;
        public static final int OFFSET_Y = 7 * SCALE;

        public static final int DE_SPAWN_TIMER = 8000;
        public static final int SPAWN_POWER_UP_TIMER = 12000;

        public enum ItemType {
            BUBBLE_REWARD, POWER_UP
        }

        public enum BubbleRewardType {
            APPLE, PEPPER, GRAPE, PERSIMMON, CHERRY, MUSHROOM, BANANA, WATER_CRISTAL;

            public static int GetRewardImageIndex(BubbleRewardType bubbleRewardType) {
                return switch (bubbleRewardType) {
                    case APPLE -> 0;
                    case PEPPER -> 1;
                    case GRAPE -> 2;
                    case PERSIMMON -> 3;
                    case CHERRY -> 4;
                    case MUSHROOM -> 5;
                    case BANANA -> 6;
                    case WATER_CRISTAL -> 7;
                };
            }

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

        public enum PowerUpType {
            GREEN_CANDY, BLUE_CANDY, RED_CANDY, SHOE, ORANGE_PARASOL, BLUE_PARASOL, CHACKN_HEART, CRYSTAL_RING, EMERALD_RING, RUBY_RING;

            //TODO: not here put in view
            public static int GetPowerUpImageIndex(PowerUpType powerUpType) {
                return switch (powerUpType) {
                    case GREEN_CANDY -> 0;
                    case BLUE_CANDY -> 1;
                    case RED_CANDY -> 2;
                    case SHOE -> 3;
                    case ORANGE_PARASOL -> 4;
                    case BLUE_PARASOL -> 5;
                    case CHACKN_HEART -> 6;
                    case CRYSTAL_RING -> 7;
                    case EMERALD_RING -> 8;
                    case RUBY_RING -> 9;
                };
            }

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

    public static class PointsManager {

        public static final int SMALL_DEFAULT_W = 18;
        public static final int SMALL_DEFAULT_H = 10;
        public static final int SMALL_W = (int) (SMALL_DEFAULT_W / 1.1 * SCALE);
        public static final int SMALL_H = (int) (SMALL_DEFAULT_H / 1.1 * SCALE);

        public static final int BIG_DEFAULT_W = 28;
        public static final int BIG_DEFAULT_H = 13;
        public static final int BIG_W = (int) (SMALL_DEFAULT_W * 1.35 * SCALE);
        public static final int BIG_H = (int) (SMALL_DEFAULT_H * 1.35 * SCALE);

        public static final int CONSECUTIVE_POP_DELAY = 200;

        public enum PointsType {
            SMALL, BIG
        }
    }

    public static class Projectiles {

        public static final int DEFAULT_W = 18;
        public static final int DEFAULT_H = 18;
        public static final int W = DEFAULT_W * SCALE;
        public static final int H = DEFAULT_H * SCALE;

        public static final int HITBOX_W = 9 * SCALE;
        public static final int HITBOX_H = 9 * SCALE;
        public static final int OFFSET_X = -5 * SCALE;
        public static final int OFFSET_Y = -4 * SCALE;

        public static final int PROJECTILE_ANIMATION_SPEED = 25;
        public static final float PLAYER_BUBBLE_SPEED = 1.2f * SCALE;
        public static final float MAITA_FIREBALL_SPEED = 0.53f * SCALE;
        public static final float LIGHTNING_SPEED = 1f * SCALE;

        public enum ProjectileState {
            MOVING, IMPACT
        }

        public enum ProjectileType {
            PLAYER_BUBBLE, MAITA_FIREBALL, LIGHTNING
        }
    }

    public static class HurryUpManager {
        public static final int HURRY_IMG_W = (int) (40 * SCALE * 1.3);
        public static final int HURRY_IMG_H = (int) (11 * SCALE * 1.3);
        public static final int STARTING_HURRY_IMG_X = (int) (GAME_WIDTH / 2 - 40 * SCALE * 1.3 / 2);
        public static final int STARTING_HURRY_IMG_Y = GAME_HEIGHT - 5 * SCALE;
        public static final float HURRY_IMG_SPEED = 0.3f * SCALE;

        public static final int START_ANIMATION_TIMER = 17000;
        public static final int START_HURRY_UP_TIMER = 20000;
    }

    public static class BubbleGenerator {

        public static final int BUBBLE_GENERATION_INTERVAL = 4000;
        public static final int INITIAL_BUBBLE_GENERATOR_INTERVAL = 2000;
        public static final int MAX_SPECIAL_BUBBLES = 7;

        public static final int LEFT_GENERATOR_X = 10 * TILES_SIZE;
        public static final int RIGHT_GENERATOR_X = 20 * TILES_SIZE;
        public static final int TOP_GENERATOR_Y = -2 * TILES_SIZE;
        public static final int BOTTOM_GENERATOR_Y = TILES_IN_HEIGHT + 2 * TILES_SIZE;

        public enum GeneratorPosition {
            NONE, TOP, BOTTOM
        }

        public enum GeneratorType {
            NONE, WATER_BUBBLE, LIGHTNING_BUBBLE
        }
    }

    public static class AudioConstants {
        public static final float DEFAULT_VOLUME = 0.6f;

        // Songs
        public static final int INTO_AND_PLAYING_SONG = 0;
        public static final int PLAYING_SONG = 1;

        // Sound effects
        public static final int HOME = 0;
        public static final int JUMP = 1;
        public static final int PLAYER_DEATH = 2;
        public static final int BUBBLE_SHOOT = 3;
        public static final int ENEMY_BUBBLE_POP = 4;
        public static final int WATER_FLOW = 5;
        public static final int LIGHTNING = 6;
        public static final int REWARD_COLLECTED = 7;
        public static final int POWER_UP_COLLECTED = 8;
        public static final int GAME_OVER = 9;
        public static final int GAME_COMPLETED = 10;
        public static final int HURRY_UP = 11;
    }

    public static class Overlays {
        public static final Color BUD_GREEN_COLOR = new Color(0x5ce634);
        public static final Color BUD_RED_COLOR = new Color(0xfc8274);
    }

    public static class MenuConstants {

        public static final float SUGGESTION_SPEED = 0.7f; // Adjust the speed as needed
        public static final String[] SUGGESTIONS = {
            "Use Arrows or WASD Keys to move in the menu.",                          // Index 0
            "Press ENTER to select an option in the menu.",                          // Index 1
            "Press ESC to pause the game while playing.",                            // Index 2
            "Use WASD to move the player while playing.",                            // Index 3
            "Press SPACE to jump.",                                                  // Index 4
            "Press ENTER to shoot a magic bubble.",                                  // Index 5
            "If you like VIM try the HJKL keys to move, Z to jump and X to shoot.",  // Index 6
        };
        public static final int[] SUGGESTIONS_WIDTHS = { 677, 662, 633, 625, 313, 555, 998 };

        public static final String SPACE = "            ";
        public static final int SPACE_WIDTH = 108;
    }
}