package utilz;

import main.Game;

public class Constants {

    public static final float GRAVITY = 0.0078f * Game.SCALE;
    public static final int ANIMATION_SPEED = 50;

    public static class PlayerBubble {
    public static final int BUBBLE_ANIMATION_SPEED = 25;

        // Bubble Dimensions
        public static final int DEFAULT_W = 18;
        public static final int DEFAULT_H = 18;
        public static final int IMMAGE_W = (int) (DEFAULT_W * Game.SCALE);
        public static final int IMMAGE_H = (int) (DEFAULT_H * Game.SCALE);

        // Hitbox Dimensions
        public static final int HITBOX_W = (int) (16 * Game.SCALE);
        public static final int HITBOX_H = (int) (16 * Game.SCALE);
        public static final int DRAWOFFSET_X = (int) (1 * Game.SCALE);
        public static final int DRAWOFFSET_Y = (int) (1 * Game.SCALE);

        // States
        public static final int PROJECTILE = 0;
        public static final int NORMAL = 1;
        public static final int RED = 2;
        public static final int BLINKING = 3;
        public static final int POP = 4;

        // State Timers (seconds)
        public static final int NORMAL_TIMER = 12000;
        public static final int RED_TIMER = 3000;
        public static final int BLINKING_TIMER = 2000;

        // Movement Constants
        public static final float PROJECTILE_SPEED = 0.7f * Game.SCALE;
        public static final float BUBBLE_SPEED = 0.1f * Game.SCALE;


        public static int getSpriteAmount(int bubbleState) {
            return switch (bubbleState) {
                case PROJECTILE -> 4;
                case NORMAL -> 1;
                case RED -> 1;
                case BLINKING -> 2;
                case POP -> 3;
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

    public static class Directions {
        public static final int NONE = 0;
        public static final int LEFT = 1;
        public static final int UP = 3;
        public static final int RIGHT = 2;
        public static final int DOWN = 4;
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
        public static final int DRAWOFFSET_X = (int) (3 * Game.SCALE);
        public static final int DRAWOFFSET_Y = (int) (3 * Game.SCALE);

        // Movement values and variables
        public static final float WALK_SPEED = 0.33f * Game.SCALE;
        public static final float FALL_SPEED = 0.35f * Game.SCALE;
        public static final float JUMP_SPEED = -0.79f * Game.SCALE;

        // Animation Constants
        public static final int IDLE_ANIMATION = 0;
        public static final int RUNNING_ANIMATION = 1;
        public static final int JUMPING_ANIMATION = 2;
        public static final int FALLING_ANIMATION = 3;
        public static final int ATTACK_AMATION = 4;
        public static final int DEAD_ANIMATION = 5;

        public static final int IMMUNE_TIME_AFTER_RESPAWN = 2000; // 2 seconds
        public static final int ATTACK_TIMER = 500; // 0.2 seconds

        public static int getSpriteAmount(int playerAnimation) {
            return switch (playerAnimation) {
                case IDLE_ANIMATION -> 2;
                case RUNNING_ANIMATION -> 2;
                case JUMPING_ANIMATION -> 2;
                case FALLING_ANIMATION -> 2;
                case ATTACK_AMATION -> 1;
                case DEAD_ANIMATION -> 7;
                default -> 0;
            };
        }
    }

    public static class EnemyConstants {

        // Zen Chan Constants
        public static final int ZEN_CHAN = 1;
        public static final int ZEN_CHAN_HITBOX_W = (int) (14 * Game.SCALE);
        public static final int ZEN_CHAN_HITBOX_H = (int) (15 * Game.SCALE);
        public static final int ZEN_CHAN_DRAWOFFSET_X = (int) (2 * Game.SCALE);
        public static final int ZEN_CHAN_DRAWOFFSET_Y = (int) (2 * Game.SCALE);

        // General Enemy Sprite Sizes
        public static final int ENEMY_DEFAULT_W = 18;
        public static final int ENEMY_DEFAULT_H = 18;
        public static final int ENEMY_W = (int) (ENEMY_DEFAULT_W * Game.SCALE);
        public static final int ENEMY_H = (int) (ENEMY_DEFAULT_H * Game.SCALE);

        // General Animation Constants
        public static final int WALKING_ANIMATION_NORMAL = 0;
        public static final int WALKING_ANIMATION_HUNGRY = 1;
        public static final int BOBBLE_GREEN_ANIMATION = 3;
        public static final int BOOBLE_RED_ANIMATION = 4;
        public static final int DEAD_ANIMATION = 6;

        // Enemy States
        public static final int NORMAL_STATE = 0;
        public static final int HUNGRY_STATE = 1;
        public static final int BOBBLE_STATE = 2;
        public static final int DEAD_STATE = 3;

        // Enemy Action Constants
        public static final float NORMAL_FALL_SPEED = 0.3f * Game.SCALE;
        public static final float NORMAL_FLY_SPEED = 0.25f * Game.SCALE;
        public static final float NORMAL_JUMP_SPEED = - 0.42f * Game.SCALE;
        public static final float NORMAL_WALK_SPEED = 0.3f * Game.SCALE;
        public static final float HUNGRY_FALL_SPEED = 0.3f * Game.SCALE;
        public static final float HUNGRY_FLY_SPEED = 0.25f * Game.SCALE;
        public static final float HUNGRY_JUMP_SPEED = - 0.42f * Game.SCALE;
        public static final float HUNGRY_WALK_SPEED = 0.3f * Game.SCALE;
        public static final float ATTACK_RANGE = Game.TILES_SIZE;
        public static final float VIEWING_RANGE = Game.TILES_SIZE * 5;

        public static final int PLAYER_INFO_MAX_UPDATE_INTERVAL = 8000; // 8 seconds

        public static int getSpriteAmount(int enemyType, int enemyState) {
            if (enemyType == EnemyConstants.ZEN_CHAN) {
                return switch (enemyState) {
                    case EnemyConstants.WALKING_ANIMATION_NORMAL -> 2;
                    case EnemyConstants.WALKING_ANIMATION_HUNGRY -> 2;
                    case EnemyConstants.BOBBLE_GREEN_ANIMATION -> 2;
                    case EnemyConstants.BOOBLE_RED_ANIMATION -> 2;
                    case EnemyConstants.DEAD_ANIMATION -> 4;
                    default -> 0;
                };
            }
            return 0;
        }
    }
}
