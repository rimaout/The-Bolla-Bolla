package view.utilz;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public interface LoadSave {
    // Sprites
    String PLAYER_SPRITE = "/sprites/Bud.png";
    String PLAYER_TRANSITION_SPRITE = "/sprites/Bud_level_transition.png";
    String ZEN_CHAN_ENEMY_SPRITE = "/sprites/Zen-Chan.png";
    String MAITA_ENEMY_SPRITE = "/sprites/Maita.png";
    String SKEL_MONSTA_ENEMY_SPRITE = "/sprites/Skel-Monsta.png";
    String LEVEL_TILES_SPRITE = "/sprites/level_tiles.png";
    String NUMBERS_TILES_SPRITE = "/sprites/numbers_tiles.png";
    String BUBBLE_BUD_SPRITE = "/sprites/Bud_bubble.png";
    String BUBBLE_TWINKLE = "/sprites/bubble-twinkle.png";
    String ITEM_BUBBLE_REWARD_SPRITE = "/sprites/item_bubble_reward.png";
    String ITEM_POWER_UP_SPRITE = "/sprites/power_ups.png";
    String ITEM_DESPAWN_SPRITE = "/sprites/item_despawn.png";
    String BUD_SMALL_POINTS_SPRITE = "/sprites/Bud_small_points.png";
    String BUD_BIG_POINTS_SPRITE = "/sprites/Bud_big_points.png";
    String PROJECTILE_FIREBALL_SPRITE = "/sprites/fireball.png";
    String WATER_BUBBLE_SPRITE = "/sprites/water_bubble.png";
    String LIGHTNING_BUBBLE_SPRITE = "/sprites/lightning_bubble.png";
    String BUD_BUBBLE_PROJECTILE_SPRITE = "/sprites/bud_bubble_projectile.png";
    String USER_PICTURES = "/sprites/user_pictures.png";
    String ARROWS_LEFT_RIGHT = "/sprites/arrows-left-right.png";
    String ARROWS_UP_DOWN = "/sprites/arrows-up-down.png";

    String GAME_LOGO = "/images/logo.png";
    String HURRY_IMAGE = "/images/hurry_image.png";
    String QUESTION_MARK_IMAGE = "/images/question-mark.png";
    String GAME_ICON = "res/images/game-icon.png";

    String NES_FONT = "/fonts/nintendo-nes-font.ttf";
    String RETRO_GAMING_FONT = "/fonts/retro-gaming.ttf";

    static BufferedImage GetSprite(String spriteFile) {
        InputStream is = LoadSave.class.getResourceAsStream(spriteFile);
        BufferedImage img = null;

        // it's a good practice ti use try-catch when using ImageIO.read to avoid runtime exceptions particularly when the image is not found
        try {
            img = ImageIO.read(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {

            try {
                is.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return img;
    }

    private static Font LoadFont(String fontFile) {
        Font font = null;
        try {
            InputStream is = LoadSave.class.getResourceAsStream(fontFile);
            font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(22f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        return font;
    }

    static Font GetNesFont() {
        return LoadFont(NES_FONT);
    }

    static Font GetRetroGamingFont() {
        return LoadFont(RETRO_GAMING_FONT);
    }

    static Clip GetAudio(String audioName) {
        URL url = LoadSave.class.getResource("/audioFiles/" + audioName + ".wav");
        AudioInputStream audio;

        try {
            audio = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            return clip;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }

        return null;
    }
}