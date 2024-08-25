package utilz;

import main.Game;

public class Constants {

    public static final float GRAVITY = 0.0078f * Game.SCALE;
    public static final int ANIMATION_SPEED = 55;

    public static class Home {

        // Logo Constants
        public static final int LOGO_END_Y = (int) (15 * Game.SCALE);
        public static final int LOGO_SPEED = 1;

        // Twinkle Bubble Constants
        public static final int BUBBLE_DEFAULT_W = 8;
        public static final int BUBBLE_DEFAULT_H = 8;
        public static final int BUBBLE_W = (int) (BUBBLE_DEFAULT_W * Game.SCALE);
        public static final int BUBBLE_H = (int) (BUBBLE_DEFAULT_H * Game.SCALE);

        public static final float BUBBLE_SPEED = 0.0666f * Game.SCALE;
    }

    public static class LevelTransition {

        public enum TransitionState {
            LEVEL_TRANSITION,
            LOADING_NEW_LEVEL,
            START_NEW_LEVEL
        }
        public static final float LEVEL_TRANSITION_SPEED = 0.58f * Game.SCALE;
    }

    public static class INTRO {

        public enum IntroState {
            INTRO,
            LEVEL_TRANSITION,
            START_NEW_LEVEL
        }

        public static final float TRANSITION_SPEED = 0.58f * Game.SCALE;

        public static final float TEXT_START_Y = 20 * Game.SCALE;

        public static final float PLAYER_START_X = 9 * Game.TILES_SIZE;
        public static final float PLAYER_START_Y = 10 * Game.TILES_SIZE;

        public static final int RADIUS = 70;
        public static final int TOTAL_LAPS = 3;
        public static final float ANGLE_INCREMENT = 0.015f;
    }

    public static class Bubble {
        public static final int BUBBLE_ANIMATION_SPEED = 25;

        // Bubble Dimensions
        public static final int DEFAULT_W = 18;
        public static final int DEFAULT_H = 18;
        public static final int IMMAGE_W = (int) (DEFAULT_W * Game.SCALE);
        public static final int IMMAGE_H = (int) (DEFAULT_H * Game.SCALE);

        // Hitbox Constants
        public static final int HITBOX_W = (int) (16 * Game.SCALE);
        public static final int HITBOX_H = (int) (16 * Game.SCALE);
        public static final int HITBOX_OFFSET_X = (int) (1 * Game.SCALE);
        public static final int HITBOX_OFFSET_Y = (int) (1 * Game.SCALE);

        // Collision Box Constants
        public static final int INTERNAL_BOX_W = (int) (8 * Game.SCALE);
        public static final int INTERNAL_BOX_H = (int) (8 * Game.SCALE);
        public static final int INTERNAL_BOX_OFFSET_X = (int) (3.5 * Game.SCALE);
        public static final int INTERNAL_BOX_OFFSET_Y = (int) (4 * Game.SCALE);
        public static final int EXTERNAL_BOX_W = (int) (13.5 * Game.SCALE);
        public static final int EXTERNAL_BOX_H = (int) (16 * Game.SCALE);
        public static final int EXTERNAL_BOX_OFFSET_X = (int) (1.5 * Game.SCALE);

        // States
        public static final int PROJECTILE = 0;
        public static final int NORMAL = 1;
        public static final int RED = 2;
        public static final int BLINKING = 3;
        public static final int POP_NORMAL = 4;
        public static final int POP_RED = 5;

        // State Timers (seconds)
        public static final int NORMAL_TIMER = 12000;
        public static final int RED_TIMER = 3000;
        public static final int BLINKING_TIMER = 2000;

        // Movement Constants
        public static final float PROJECTILE_SPEED = 1f * Game.SCALE;
        public static final float BUBBLE_SPEED = 0.1f * Game.SCALE;
        public static final float SHAKING_SPEED = 0.03f * Game.SCALE;


        public static int getSpriteAmount(int bubbleState) {
            return switch (bubbleState) {
                case PROJECTILE -> 4;
                case NORMAL -> 2;
                case RED -> 2;
                case BLINKING -> 2;
                case POP_NORMAL, POP_RED -> 3;
                default -> 4;
            };
        }
    }

    public static class UI {

        public static class MenuButtons {
            public static final int BUTTON_DEFAULT_W = 140;
            public static final int BUTTON_DEFAULT_H = 56;
            public static final int BUTTON_W = BUTTON_DEFAULT_W * Game.SCALE/2;
            public static final int BUTTON_H = BUTTON_DEFAULT_H * Game.SCALE/2;
        }

        public static class PauseButtons {
            public static final int SOUND_BT_DEFAULT_W = 42;
            public static final int SOUND_BT_DEFAULT_H = 42;
            public static final int SOUND_BT_W = (int) (SOUND_BT_DEFAULT_W * Game.SCALE/2);
            public static final int SOUND_BT_H = (int) (SOUND_BT_DEFAULT_H * Game.SCALE/2);
        }

        public static class UrmButtons {
            public static final int URM_BT_DEFAULT_W = 56;
            public static final int URM_BT_DEFAULT_H = 56;
            public static final int URM_BT_W = (int) (URM_BT_DEFAULT_W * Game.SCALE/2);
            public static final int URM_BT_H = (int) (URM_BT_DEFAULT_H * Game.SCALE/2);
        }

        public static class VolumeButton {
            public static final int VOLUME_BT_DEFAULT_W = 28;
            public static final int VOLUME_BT_DEFAULT_H = 44;
            public static final int VOLUME_BT_W = (int) (VOLUME_BT_DEFAULT_W * Game.SCALE/3);
            public static final int VOLUME_BT_H = (int) (VOLUME_BT_DEFAULT_H * Game.SCALE/3);

            public static final int VOLUME_SLIDER_DEFAULT_W = 215;
            public static final int VOLUME_SLIDER_DEFAULT_H = 44;
            public static final int VOLUME_SLIDER_W = (int) (VOLUME_SLIDER_DEFAULT_W * Game.SCALE/3);
            public static final int VOLUME_SLIDER_H = (int) (VOLUME_SLIDER_DEFAULT_H * Game.SCALE/3);
        }
    }

    public enum Direction {
        LEFT,
        RIGHT,
        UP,
        DOWN,
        NONE;

        public static Direction GetRandomDirection() {
            return switch ((int) (Math.random() * 4)) {
                case 0 -> Direction.LEFT;
                case 1 -> Direction.RIGHT;
                case 2 -> Direction.UP;
                case 3 -> Direction.DOWN;
                default -> Direction.NONE;
            };
        }

        public static Direction GetOppositeDirection(Direction direction) {
            return switch (direction) {
                case LEFT -> Direction.RIGHT;
                case RIGHT -> Direction.LEFT;
                case UP -> Direction.DOWN;
                case DOWN -> Direction.UP;
                default -> Direction.NONE;
            };
        }
    }

    public static class PlayerConstants {

        //Spawning Position
        public static final float SPAWN_X = 3 * Game.TILES_SIZE;
        public static final float SPAWN_Y = 24 * Game.TILES_SIZE + 2 * Game.SCALE;

        // Player Image and Hitbox Constants
        public static final int DEFAULT_W = 18;
        public static final int DEFAULT_H = 18;
        public static final int IMMAGE_W = (int) (18 * Game.SCALE);
        public static final int IMMAGE_H = (int) (18 * Game.SCALE);
        public static final int HITBOX_W = (int) (13 * Game.SCALE);
        public static final int HITBOX_H = (int) (13 * Game.SCALE);
        public static final int OFFSET_X = (int) (3 * Game.SCALE);
        public static final int OFFSET_Y = (int) (3 * Game.SCALE);

        // Movement values and variables
        public static final float WALK_SPEED = 0.33f * Game.SCALE;
        public static final float FALL_SPEED = 0.35f * Game.SCALE;
        public static final float JUMP_SPEED = -0.79f * Game.SCALE;

        // Animation Constants
        public static final int IDLE_ANIMATION = 0;
        public static final int RUNNING_ANIMATION = 1;
        public static final int JUMPING_ANIMATION = 2;
        public static final int FALLING_ANIMATION = 3;
        public static final int ATTACK_ANIMATION = 4;
        public static final int DEAD_ANIMATION = 5;

        public static final int IMMUNE_TIME_AFTER_RESPAWN = 2000; // 2 seconds
        public static final int ATTACK_TIMER = 300; // 0.3 seconds

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
            ZEN_CHAN;
        }

        public static final float INITIAL_SPAWN_POINT_Y = -3 * Game.TILES_SIZE;
        public static final float SPAWN_TRANSITION_SPEED = 0.31f * Game.SCALE;

        // Zen Chan Constants
        public static final int ZEN_CHAN_LEFT = 1;
        public static final int ZEN_CHAN_RIGHT = 2;
        public static final int ZEN_CHAN_HITBOX_W = (int) (14 * Game.SCALE);
        public static final int ZEN_CHAN_HITBOX_H = (int) (15 * Game.SCALE);
        public static final int ZEN_CHAN_OFFSET_X = (int) (2 * Game.SCALE);
        public static final int ZEN_CHAN_OFFSET_Y = (int) (2 * Game.SCALE);

        // General Enemy Sprite Sizes
        public static final int ENEMY_DEFAULT_W = 18;
        public static final int ENEMY_DEFAULT_H = 18;
        public static final int ENEMY_W = (int) (ENEMY_DEFAULT_W * Game.SCALE);
        public static final int ENEMY_H = (int) (ENEMY_DEFAULT_H * Game.SCALE);

        // General Animation Constants
        public static final int WALKING_ANIMATION_NORMAL = 0;
        public static final int WALKING_ANIMATION_HUNGRY = 1;
        public static final int BOBBLE_GREEN_ANIMATION = 3;
        public static final int BOBBLE_RED_ANIMATION = 5;
        public static final int DEAD_ANIMATION = 6;
        public static final int BOBBLE_GREEN_POP_ANIMATION = 7;
        public static final int BOBBLE_RED_POP_ANIMATION = 8;

        // Enemy States
        public static final int NORMAL_STATE = 0;
        public static final int HUNGRY_STATE = 1;
        public static final int BOBBLE_STATE = 2;
        public static final int DEAD_STATE = 3;

        // Enemy Action Constants
        public static final float NORMAL_FALL_SPEED = 0.3f * Game.SCALE;
        public static final float NORMAL_FLY_SPEED = 0.25f * Game.SCALE;
        public static final float NORMAL_WALK_SPEED = 0.3f * Game.SCALE;
        public static final float HUNGRY_FALL_SPEED = 0.3f * Game.SCALE;
        public static final float HUNGRY_FLY_SPEED = 0.5f * Game.SCALE;
        public static final float HUNGRY_WALK_SPEED = 0.7f * Game.SCALE;

        public static final float JUMP_Y_SPEED = -0.45f * Game.SCALE;
        public static final float JUMP_X_SPEED = 0.4f * Game.SCALE;
        public static final float ATTACK_RANGE = Game.TILES_SIZE;
        public static final float VIEWING_RANGE = Game.TILES_SIZE * 5;

        public static final int PLAYER_INFO_MAX_UPDATE_INTERVAL = 8000; // 8 seconds

        public static int getSpriteAmount(EnemyType enemyType, int enemyState) {
            if (enemyType == EnemyType.ZEN_CHAN ) {
                return switch (enemyState) {
                    case WALKING_ANIMATION_NORMAL -> 2;
                    case WALKING_ANIMATION_HUNGRY -> 2;
                    case BOBBLE_GREEN_ANIMATION -> 2;
                    case BOBBLE_RED_ANIMATION -> 2;
                    case DEAD_ANIMATION -> 4;
                    default -> 0;
                };
            }
            return 0;
        }
    }
}
