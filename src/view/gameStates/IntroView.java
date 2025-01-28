package view.gameStates;

import model.levels.Level;
import model.utilz.Constants;
import model.gameStates.IntroModel;
import view.audio.AudioPlayer;
import view.utilz.Load;
import view.levels.LevelManagerView;

import java.awt.*;
import java.awt.image.BufferedImage;

import static view.utilz.Constants.ANIMATION_SPEED;
import static model.utilz.Constants.INTRO.IntroState.LEVEL_TRANSITION;

/**
 * The IntroView class represents the view for the {@link IntroModel} class.
 * It handles drawing the level transition, player animation, and introduction story text.
 */
public class IntroView {
    private final IntroModel introModel;

    private BufferedImage[] levelTiles;
    private BufferedImage[] numbersTiles;
    private BufferedImage[] playerTransitionSprites;
    private Font customFont;

    private boolean firstDraw = true;
    private int playerAnimationTick, playerAnimationIndex;

    /**
     * Constructs an IntroView with the specified IntroModel.
     *
     * @param introModel the model for the introduction screen
     */
    public IntroView(IntroModel introModel) {
        this.introModel = introModel;

        levelTiles = LevelManagerView.getInstance().getLevelTiles();
        numbersTiles = LevelManagerView.getInstance().getNumbersTiles();
        customFont = Load.GetNesFont();

        loadPlayerTransitionSprites();
    }

    /**
     * Updates the player animation by incrementing the animation tick and index.
     */
    public void updatePlayerAnimation(){
        // update player animation
        playerAnimationTick++;
        if (playerAnimationTick > ANIMATION_SPEED) {
            playerAnimationTick = 0;
            playerAnimationIndex++;

            if (playerAnimationIndex > 1)
                playerAnimationIndex = 0;
        }
    }

    /**
     * Draws the introduction screen elements, including the level, text, and player animation.
     *
     * @param g the Graphics object to draw with
     */
    public void draw(Graphics g) {

        if (introModel.getIntroState() == LEVEL_TRANSITION)
            drawLevel(g, introModel.getLevel(), (int) introModel.getLevelY());

        playSound();
        drawText(g);
        drawPlayer(g);
    }

    /**
     * Plays the introductory sound if it is the first draw.
     */
    private void playSound() {
        if (firstDraw) {
            AudioPlayer.getInstance().playIntroSong();
            firstDraw = false;
        }
    }

    /**
     * Draws the player animation on the screen.
     *
     * @param g the Graphics object to draw with
     */
    private void drawPlayer(Graphics g) {

        float xOffSet = 5 * Constants.SCALE;
        float yOffSet = 12 * Constants.SCALE;

        g.drawImage(playerTransitionSprites[playerAnimationIndex], (int) ( introModel.getPlayer().getHitbox().x - xOffSet ), (int) ( introModel.getPlayer().getHitbox().y - yOffSet ) , 31 * Constants.SCALE, 34 * Constants.SCALE, null);
    }

    /**
     * Draws the introductory text on the screen.
     *
     * @param g the Graphics object to draw with
     */
    private void drawText(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(customFont);
        FontMetrics metrics = g.getFontMetrics(customFont);

        String[] lines = {
                "NOW IT IS THE BEGINNING OF",
                "A FANTASTIC STORY! LET US",
                "MAKE A JOURNEY TO",
                "THE CAVE OF MONSTERS!",
        };

        String lastLine = "GOOD LUCK!";

        int lineHeight = metrics.getHeight();
        int extraSpace = 7 * Constants.SCALE;
        int y = (int) introModel.getTextY();

        for (String line : lines) {
            int lineWidth = metrics.stringWidth(line);
            int x = (Constants.GAME_WIDTH - lineWidth) / 2;
            g.drawString(line, x, y);
            y += lineHeight + extraSpace;
        }

        // Draw last line
        int lastLineWidth = metrics.stringWidth(lastLine);
        int x = (Constants.GAME_WIDTH - lastLineWidth) / 2;
        int extraExtraSpace = 6 * Constants.SCALE;
        g.drawString(lastLine, x, y + extraExtraSpace);
    }

    /**
     * Draws the level tiles on the screen during the level transition.
     *
     * @param g the Graphics object to draw with
     * @param level the level to draw
     * @param yOffSet the vertical offset for drawing the level
     */
    private void drawLevel(Graphics g, Level level, int yOffSet) {

        int index;
        BufferedImage tile;

        for (int y = 0; y < Constants.TILES_IN_HEIGHT; y++) {
            for (int x = 0; x < Constants.TILES_IN_WIDTH; x++) {

                index = level.getTileSpriteIndex(x, y);

                if (index >= 120)
                    tile = numbersTiles[index - 120];
                else
                    tile = levelTiles[index];

                g.drawImage(tile, x * Constants.TILES_SIZE, y * Constants.TILES_SIZE + yOffSet, Constants.TILES_SIZE, Constants.TILES_SIZE, null);
            }
        }
    }

    /**
     * Loads the player transition sprites from the sprite sheet.
     */
    private void loadPlayerTransitionSprites() {
        playerTransitionSprites = new BufferedImage[2];

        BufferedImage img = Load.GetSprite(Load.PLAYER_TRANSITION_SPRITE);
        playerTransitionSprites[0] = img.getSubimage(0, 0, 31, 34);
        playerTransitionSprites[1] = img.getSubimage(31, 0, 31, 34);
    }

    /**
     * Resets the first draw flag to true.
     */
    public void reset() {
        firstDraw = true;
    }
}