package view.utilz;

import java.awt.*;

import static model.utilz.Constants.*;
import static model.utilz.Constants.Bubble.NORMAL;
import static model.utilz.Constants.Bubble.RED;
import static model.utilz.Constants.Bubble.BLINKING;
import static model.utilz.Constants.Bubble.POP_NORMAL;
import static model.utilz.Constants.Bubble.POP_RED;

public interface Constants {

    int ANIMATION_SPEED = 55;

    interface Home {

        // Logo Constants
        int LOGO_END_Y = 15 * SCALE;
        float LOGO_SPEED = 0.85f * 3;

        // Twinkle Bubble Constants (background animation)
        int BUBBLE_DEFAULT_W = 8;
        int BUBBLE_DEFAULT_H = 8;
        int BUBBLE_W = BUBBLE_DEFAULT_W * SCALE;
        int BUBBLE_H = BUBBLE_DEFAULT_H * SCALE;
        float BUBBLE_SPEED = 0.0664f * 3 * SCALE;
    }

    interface Bubble {
        int BUBBLE_ANIMATION_SPEED = 25;

        static int getPlayerBubbleSpriteAmount(int bubbleState) {
            return switch (bubbleState) {
                case NORMAL -> 2;
                case RED -> 2;
                case BLINKING -> 2;
                case POP_NORMAL, POP_RED -> 3;
                default -> 4;
            };
        }
    }

    interface PlayerConstants {

        // Animation Constants
        int IDLE_ANIMATION = 0;
        int RUNNING_ANIMATION = 1;
        int JUMPING_ANIMATION = 2;
        int FALLING_ANIMATION = 3;
        int ATTACK_ANIMATION = 4;
        int DEAD_ANIMATION = 5;

        static int getSpriteAmount(int playerAnimation) {
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

    interface EnemyConstants {

        // General Animation Constants
        int WALKING_ANIMATION_NORMAL = 0;
        int WALKING_ANIMATION_HUNGRY = 1;
        int BOBBLE_GREEN_ANIMATION = 3;
        int BOBBLE_RED_ANIMATION = 5;
        int DEAD_ANIMATION = 6;
        int BOBBLE_GREEN_POP_ANIMATION = 7;
        int BOBBLE_RED_POP_ANIMATION = 8;
        float NORMAL_ANIMATION_SPEED_MULTIPLIER = 1.0f;
        float HUNGRY_ANIMATION_SPEED_MULTIPLIER = 0.53f;

        static int getSpriteAmount(model.utilz.Constants.EnemyConstants.EnemyType enemyType, int enemyState) {
            if (enemyType == model.utilz.Constants.EnemyConstants.EnemyType.ZEN_CHAN || enemyType == model.utilz.Constants.EnemyConstants.EnemyType.MAITA) {
                return switch (enemyState) {
                    case WALKING_ANIMATION_NORMAL -> 2;
                    case WALKING_ANIMATION_HUNGRY -> 2;
                    case BOBBLE_GREEN_ANIMATION -> 2;
                    case BOBBLE_RED_ANIMATION -> 2;
                    case DEAD_ANIMATION -> 4;
                    default -> 0;
                };
            }

            if (enemyType == model.utilz.Constants.EnemyConstants.EnemyType.SKEL_MONSTA)
                return 2; // SkelMonsta only has animations of 2 frames

            return 0;
        }
    }

    interface Items {

        static int GetRewardImageIndex(model.utilz.Constants.Items.BubbleRewardType bubbleRewardType) {
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

        static int GetPowerUpImageIndex(model.utilz.Constants.Items.PowerUpType powerUpType) {
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

    interface Projectiles {
        int PROJECTILE_ANIMATION_SPEED = 25;
    }

    interface HurryUpManager {
        int HURRY_IMG_W = (int) (40 * SCALE * 1.3);
        int HURRY_IMG_H = (int) (11 * SCALE * 1.3);
        int STARTING_HURRY_IMG_X = (int) (GAME_WIDTH / 2 - 40 * SCALE * 1.3 / 2);
        int STARTING_HURRY_IMG_Y = GAME_HEIGHT - 5 * SCALE;
        float HURRY_IMG_SPEED = 0.3f * SCALE;
    }

    interface AudioConstants {
        float DEFAULT_VOLUME = 0.6f;

        // Songs
        int INTO_AND_PLAYING_SONG = 0;
        int PLAYING_SONG = 1;

        // Sound effects
        int HOME = 0;
        int JUMP = 1;
        int PLAYER_DEATH = 2;
        int BUBBLE_SHOOT = 3;
        int ENEMY_BUBBLE_POP = 4;
        int WATER_FLOW = 5;
        int LIGHTNING = 6;
        int REWARD_COLLECTED = 7;
        int POWER_UP_COLLECTED = 8;
        int GAME_OVER = 9;
        int GAME_COMPLETED = 10;
        int HURRY_UP = 11;
    }

    interface Overlays {
        Color BUD_GREEN_COLOR = new Color(0x5ce634);
        Color BUD_RED_COLOR = new Color(0xfc8274);
    }

    interface MenuConstants {

        float SUGGESTION_SPEED = 0.7f; // Adjust the speed as needed

        String[] SUGGESTIONS = {
                "Use Arrows or WASD Keys to move in the menu.",                          // Index 0
                "Press ENTER to select an option in the menu.",                          // Index 1
                "Press ESC to pause the game while playing.",                            // Index 2
                "Use WASD to move the player while playing.",                            // Index 3
                "Press SPACE to jump.",                                                  // Index 4
                "Press ENTER to shoot a magic bubble.",                                  // Index 5
                "If you like VIM try the HJKL keys to move, Z to jump and X to shoot.",  // Index 6
        };

        int[] SUGGESTIONS_WIDTHS = { 677, 662, 633, 625, 313, 555, 998 };

        String SPACE = "            ";
        int SPACE_WIDTH = 108;
    }
}