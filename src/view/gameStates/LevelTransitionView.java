package view.gameStates;

import model.levels.Level;
import model.utilz.Constants;
import model.gameStates.LevelTransitionModel;
import view.utilz.Load;
import view.levels.LevelManagerView;

import java.awt.*;
import java.awt.image.BufferedImage;

import static view.utilz.Constants.ANIMATION_SPEED;

/**
 * The LevelTransitionView class represents the view for the {@link LevelTransitionModel}.
 * It handles drawing the new and old levels, as well as the player animation during the transition.
 */
public class LevelTransitionView {
    private final LevelTransitionModel levelTransitionModel;

    private int playerAnimationTick, playerAnimationIndex;

    private BufferedImage[] levelTiles;
    private BufferedImage[] numbersTiles;
    private BufferedImage[] playerTransitionSprites;

    /**
     * Constructs a LevelTransitionView with the specified LevelTransitionModel.
     *
     * @param levelTransitionModel the model for the level transition screen
     */
    public LevelTransitionView(LevelTransitionModel levelTransitionModel) {
        this.levelTransitionModel = levelTransitionModel;

        levelTiles = LevelManagerView.getInstance().getLevelTiles();
        numbersTiles = LevelManagerView.getInstance().getNumbersTiles();
        loadPlayerTransitionSprites();
    }

    /**
     * Draws the level transition screen elements, including the new and old levels and the player animation.
     *
     * @param g the Graphics object to draw with
     */
    public void draw(Graphics g) {
        drawLevel(g, levelTransitionModel.getNewLevel(), (int) levelTransitionModel.getNewLevelY());
        drawLevel(g, levelTransitionModel.getOldLevel(), (int) levelTransitionModel.getOldLevelY());
        drawPlayer(g);
    }

    /**
     * Draws the player animation on the screen.
     *
     * @param g the Graphics object to draw with
     */
    private void drawPlayer(Graphics g) {

        float xOffSet = 5 * Constants.SCALE;
        float yOffSet = 12 * Constants.SCALE;

        g.drawImage(playerTransitionSprites[playerAnimationIndex], (int) ( levelTransitionModel.getPlayer().getHitbox().x - xOffSet ), (int) ( levelTransitionModel.getPlayer().getHitbox().y - yOffSet ) , 31 * Constants.SCALE, 34 * Constants.SCALE, null);
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
     * Updates the player animation by incrementing the animation tick and index.
     */
    public void updatePlayerAnimation() {
        // player animation
        playerAnimationTick++;
        if (playerAnimationTick > ANIMATION_SPEED) {
            playerAnimationTick = 0;
            playerAnimationIndex++;

            if (playerAnimationIndex > 1)
                playerAnimationIndex = 0;
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
}