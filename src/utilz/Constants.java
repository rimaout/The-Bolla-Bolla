package utilz;

import main.Game;

public class Constants {

    public static final float GRAVITY = 0.0078f * Game.SCALE;
    public static final int ANIMATION_SPEED = 50;

    public static class UI {

        public static class MenuButtons {
            public static final int BUTTON_WIDTH_DEFAULT = 140;
            public static final int BUTTON_HEIGHT_DEFAULT = 56;
            public static final int BUTTON_WIDTH = BUTTON_WIDTH_DEFAULT * Game.SCALE/2;
            public static final int BUTTON_HEIGHT = BUTTON_HEIGHT_DEFAULT * Game.SCALE/2;
        }

        public static class PauseButtons {
            public static final int SOUND_BT_WIDTH_DEFAULT = 42;
            public static final int SOUND_BT_HEIGHT_DEFAULT = 42;
            public static final int SOUND_BT_WIDTH = (int) (SOUND_BT_WIDTH_DEFAULT * Game.SCALE/2);
            public static final int SOUND_BT_HEIGHT = (int) (SOUND_BT_HEIGHT_DEFAULT * Game.SCALE/2);
        }

        public static class UrmButtons {
            public static final int URM_BT_WIDTH_DEFAULT = 56;
            public static final int URM_BT_HEIGHT_DEFAULT = 56;
            public static final int URM_BT_WIDTH = (int) (URM_BT_WIDTH_DEFAULT * Game.SCALE/2);
            public static final int URM_BT_HEIGHT = (int) (URM_BT_HEIGHT_DEFAULT * Game.SCALE/2);
        }

        public static class VolumeButton {
            public static final int VOLUME_BT_WIDTH_DEFAULT = 28;
            public static final int VOLUME_BT_HEIGHT_DEFAULT = 44;
            public static final int VOLUME_BT_WIDTH = (int) (VOLUME_BT_WIDTH_DEFAULT * Game.SCALE/3);
            public static final int VOLUME_BT_HEIGHT = (int) (VOLUME_BT_HEIGHT_DEFAULT * Game.SCALE/3);

            public static final int VOLUME_SLIDER_WIDTH_DEFAULT = 215;
            public static final int VOLUME_SLIDER_HEIGHT_DEFAULT = 44;
            public static final int VOLUME_SLIDER_WIDTH = (int) (VOLUME_SLIDER_WIDTH_DEFAULT * Game.SCALE/3);
            public static final int VOLUME_SLIDER_HEIGHT = (int) (VOLUME_SLIDER_HEIGHT_DEFAULT * Game.SCALE/3);
        }
    }

    public static class Directions {
        public static final int LEFT = 0;
        public static final int UP = 1;
        public static final int RIGHT = 2;
        public static final int DOWN = 3;
    }

    public static class PlayerConstants {

        //Spawning Position
        public static final float SPAWN_X = 3 * Game.TILES_SIZE;
        public static final float SPAWN_Y = 24 * Game.TILES_SIZE + 2 * Game.SCALE;

        // Player Image and Hitbox Constants
        public static final int IMMAGE_WIDTH = (int) (18 * Game.SCALE);
        public static final int IMMAGE_HEIGHT = (int) (18 * Game.SCALE);
        public static final int HITBOX_WIDTH = (int) (13 * Game.SCALE);
        public static final int HITBOX_HEIGHT = (int) (13 * Game.SCALE);
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


        public static int getSpriteAmount(int playerAnimation) {
            switch (playerAnimation) {
                case IDLE_ANIMATION:
                    return 2;
                case RUNNING_ANIMATION:
                    return 2;
                case JUMPING_ANIMATION:
                    return 2;
                case FALLING_ANIMATION:
                    return 2;
                case ATTACK_AMATION:
                    return 1;
                case DEAD_ANIMATION:
                    return 6;
                default:
                    return 0;
            }
        }
    }

    public static class EnemyConstants {

        // Zen Chan Constants
        public static final int ZEN_CHAN = 1;
        public static final int ZEN_CHAN_HITBOX_WIDTH = (int) (14 * Game.SCALE);
        public static final int ZEN_CHAN_HITBOX_HEIGHT = (int) (15 * Game.SCALE);
        public static final int ZEN_CHAN_DRAWOFFSET_X = (int) (2 * Game.SCALE);
        public static final int ZEN_CHAN_DRAWOFFSET_Y = (int) (2 * Game.SCALE);

        // General Enemy Sprite Sizes
        public static final int ENEMY_WIDTH_DEFAULT = 18;
        public static final int ENEMY_HEIGHT_DEFAULT = 18;
        public static final int ENEMY_WIDTH = (int) (ENEMY_WIDTH_DEFAULT * Game.SCALE);
        public static final int ENEMY_HEIGHT = (int) (ENEMY_HEIGHT_DEFAULT * Game.SCALE);

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
            switch (enemyType) {
                case EnemyConstants.ZEN_CHAN:
                    switch (enemyState) {
                        case EnemyConstants.WALKING_ANIMATION_NORMAL:
                            return 2;
                        case EnemyConstants.WALKING_ANIMATION_HUNGRY:
                            return 2;
                        case EnemyConstants.BOBBLE_GREEN_ANIMATION:
                            return 2;
                        case EnemyConstants.BOOBLE_RED_ANIMATION:
                            return 2;
                        case EnemyConstants.DEAD_ANIMATION:
                            return 4;
                        default:
                            return 0;
                    }
                default:
                    return 0;
            }
        }
    }
}
