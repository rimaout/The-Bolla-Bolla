package view;

import java.awt.*;

import static model.Constants.*;
import static model.Constants.Bubble.NORMAL;
import static model.Constants.Bubble.RED;
import static model.Constants.Bubble.BLINKING;
import static model.Constants.Bubble.POP_NORMAL;
import static model.Constants.Bubble.POP_RED;

public class Constants {

    public static final int ANIMATION_SPEED = 55;

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

    public static class Bubble {
        public static final int BUBBLE_ANIMATION_SPEED = 25;

        public static int getPlayerBubbleSpriteAmount(int bubbleState) {
            return switch (bubbleState) {
                case NORMAL -> 2;
                case RED -> 2;
                case BLINKING -> 2;
                case POP_NORMAL, POP_RED -> 3;
                default -> 4;
            };
        }
    }

    public static class PlayerConstants {

        // Animation Constants
        public static final int IDLE_ANIMATION = 0;
        public static final int RUNNING_ANIMATION = 1;
        public static final int JUMPING_ANIMATION = 2;
        public static final int FALLING_ANIMATION = 3;
        public static final int ATTACK_ANIMATION = 4;
        public static final int DEAD_ANIMATION = 5;

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

        public static int getSpriteAmount(model.Constants.EnemyConstants.EnemyType enemyType, int enemyState) {
            if (enemyType == model.Constants.EnemyConstants.EnemyType.ZEN_CHAN || enemyType == model.Constants.EnemyConstants.EnemyType.MAITA) {
                return switch (enemyState) {
                    case WALKING_ANIMATION_NORMAL -> 2;
                    case WALKING_ANIMATION_HUNGRY -> 2;
                    case BOBBLE_GREEN_ANIMATION -> 2;
                    case BOBBLE_RED_ANIMATION -> 2;
                    case DEAD_ANIMATION -> 4;
                    default -> 0;
                };
            }

            if (enemyType == model.Constants.EnemyConstants.EnemyType.SKEL_MONSTA)
                return 2; // SkelMonsta only has animations of 2 frames

            return 0;
        }
    }

    public static class Items {

        public static int GetRewardImageIndex(model.Constants.Items.BubbleRewardType bubbleRewardType) {
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

        public static int GetPowerUpImageIndex(model.Constants.Items.PowerUpType powerUpType) {
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
    }

    public static class Projectiles {
        public static final int PROJECTILE_ANIMATION_SPEED = 25;
    }

    public static class HurryUpManager {
        public static final int HURRY_IMG_W = (int) (40 * SCALE * 1.3);
        public static final int HURRY_IMG_H = (int) (11 * SCALE * 1.3);
        public static final int STARTING_HURRY_IMG_X = (int) (GAME_WIDTH / 2 - 40 * SCALE * 1.3 / 2);
        public static final int STARTING_HURRY_IMG_Y = GAME_HEIGHT - 5 * SCALE;
        public static final float HURRY_IMG_SPEED = 0.3f * SCALE;
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