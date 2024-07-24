package Utillz;

public class Constants {

    public static class PlayerConstants {

        // Animation Constants
        public static final int ANIMATION_SPEED = 10;
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

    public static class Directions{
        public static final int STOP = -1;
        public static final int LEFT = 0;
        public static final int UP = 1;
        public static final int RIGHT = 2;
        public static final int DOWN = 3;

    }
}
