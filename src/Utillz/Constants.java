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
            public static final int SOUND_BT_WIDTH = SOUND_BT_WIDTH_DEFAULT * Game.SCALE/2;
            public static final int SOUND_BT_HEIGHT = SOUND_BT_HEIGHT_DEFAULT * Game.SCALE/2;
        }

        public static class UrmButtons {
            public static final int URM_BT_WIDTH_DEFAULT = 56;
            public static final int URM_BT_HEIGHT_DEFAULT = 56;
            public static final int URM_BT_WIDTH = URM_BT_WIDTH_DEFAULT * Game.SCALE/2;
            public static final int URM_BT_HEIGHT = URM_BT_HEIGHT_DEFAULT * Game.SCALE/2;
        }

        public static class VolumeButton {
            public static final int VOLUME_BT_WIDTH_DEFAULT = 28;
            public static final int VOLUME_BT_HEIGHT_DEFAULT = 44;
            public static final int VOLUME_BT_WIDTH = VOLUME_BT_WIDTH_DEFAULT * Game.SCALE/3;
            public static final int VOLUME_BT_HEIGHT = VOLUME_BT_HEIGHT_DEFAULT * Game.SCALE/3;

            public static final int VOLUME_SLIDER_WIDTH_DEFAULT = 215;
            public static final int VOLUME_SLIDER_HEIGHT_DEFAULT = 44;
            public static final int VOLUME_SLIDER_WIDTH = VOLUME_SLIDER_WIDTH_DEFAULT * Game.SCALE/3;
            public static final int VOLUME_SLIDER_HEIGHT = VOLUME_SLIDER_HEIGHT_DEFAULT * Game.SCALE/3;
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
}
