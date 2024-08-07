package Utillz;

import main.Game;

public class Constants {

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

    public static class PlayerConstants {

        // Animation Constants
        public static final int ANIMATION_SPEED = 50;
        public static final int IDLE_ANIMATION = 0;
        public static final int RUNNING_ANIMATION = 1;
        public static final int JUMPING_ANIMATION = 2;
        public static final int FALLING_ANIMATION = 3;
        public static final int ATTACK_AMATION = 4;
        public static final int DEAD_ANIMATION = 5;

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
                    return 4;
                default:
                    return 0;
            }
        }
    }

    public static class EnemyConstants {

        // Enemy Types
        public static final int ZEN_CHAN = 1;

        // General Animation Constants
        public static final int WALKING = 0;
        public static final int WALKING_RED = 1;
        public static final int WALKING_BLUE = 2;
        public static final int BOBBLE = 3;
        public static final int BOOBLE_RED = 4;
        public static final int BOOBLE_BLUE = 5;
        public static final int DEAD = 6;
        public static final int ATTACK = 7;

        // General Enemy Sprite Sizes
        public static final int ENEMY_WIDTH_DEFAULT = 18;
        public static final int ENEMY_HEIGHT_DEFAULT = 18;
        public static final int ENEMY_WIDTH = (int) (ENEMY_WIDTH_DEFAULT * Game.SCALE);
        public static final int ENEMY_HEIGHT = (int) (ENEMY_HEIGHT_DEFAULT * Game.SCALE);

        public static int getSpriteAmount(int enemyType, int enemyState) {
            switch (enemyType) {
                case EnemyConstants.ZEN_CHAN:
                    switch (enemyState) {
                        case EnemyConstants.WALKING:
                            return 2;
                        case EnemyConstants.WALKING_RED:
                            return 2;
                        case EnemyConstants.WALKING_BLUE:
                            return 2;
                        case EnemyConstants.BOBBLE:
                            return 2;
                        case EnemyConstants.BOOBLE_RED:
                            return 2;
                        case EnemyConstants.BOOBLE_BLUE:
                            return 2;
                        case EnemyConstants.DEAD:
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
