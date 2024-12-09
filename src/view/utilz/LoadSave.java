package view.utilz;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class LoadSave {
    // Sprites
    public static final String PLAYER_SPRITE = "/sprites/Bud.png";
    public static final String PLAYER_TRANSITION_SPRITE = "/sprites/Bud_level_transition.png";
    public static final String ZEN_CHAN_ENEMY_SPRITE = "/sprites/Zen-Chan.png";
    public static final String MAITA_ENEMY_SPRITE = "/sprites/Maita.png";
    public static final String SKEL_MONSTA_ENEMY_SPRITE = "/sprites/Skel-Monsta.png";
    public static final String LEVEL_TILES_SPRITE = "/sprites/level_tiles.png";
    public static final String NUMBERS_TILES_SPRITE = "/sprites/numbers_tiles.png";
    public static final String BUBBLE_BUD_SPRITE = "/sprites/Bud_bubble.png";
    public static final String BUBBLE_TWINKLE = "/sprites/bubble-twinkle.png";
    public static final String ITEM_BUBBLE_REWARD_SPRITE = "/sprites/item_bubble_reward.png";
    public static final String ITEM_POWER_UP_SPRITE = "/sprites/power_ups.png";
    public static final String ITEM_DESPAWN_SPRITE = "/sprites/item_despawn.png";
    public static final String BUD_SMALL_POINTS_SPRITE = "/sprites/Bud_small_points.png";
    public static final String BUD_BIG_POINTS_SPRITE = "/sprites/Bud_big_points.png";
    public static final String PROJECTILE_FIREBALL_SPRITE = "/sprites/fireball.png";
    public static final String WATER_BUBBLE_SPRITE = "/sprites/water_bubble.png";
    public static final String LIGHTNING_BUBBLE_SPRITE = "/sprites/lightning_bubble.png";
    public static final String BUD_BUBBLE_PROJECTILE_SPRITE = "/sprites/bud_bubble_projectile.png";
    public static final String USER_PICTURES = "/sprites/user_pictures.png";
    public static final String ARROWS_LEFT_RIGHT = "/sprites/arrows-left-right.png";
    public static final String ARROWS_UP_DOWN = "/sprites/arrows-up-down.png";

    // IMAGES
    public static final String GAME_LOGO = "/images/logo.png";
    public static final String HURRY_IMAGE = "/images/hurry_image.png";
    public static final String QUESTION_MARK_IMAGE = "/images/question-mark.png";
    public static final String GAME_ICON = "/images/game-icon.png";

    // FONTS
    public static final String NES_FONT = "/fonts/nintendo-nes-font.ttf";
    public static final String RETRO_GAMING_FONT = "/fonts/retro-gaming.ttf";

    public static BufferedImage GetSprite(String spriteFile) {
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

    public static Font GetNesFont() {
        return LoadFont(NES_FONT);
    }

    public static Font GetRetroGamingFont() {
        return LoadFont(RETRO_GAMING_FONT);
    }

    public static Clip GetAudio(String audioName) {
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