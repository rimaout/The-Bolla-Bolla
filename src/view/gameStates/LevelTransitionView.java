package view.gameStates;

import model.gameStates.LevelTransitionModel;
import levels.Level;
import levels.LevelManager;
import model.utilz.Constants;
import model.utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static model.utilz.Constants.ANIMATION_SPEED;

public class LevelTransitionView {
    private final LevelTransitionModel levelTransitionModel;

    private int playerAnimationTick, playerAnimationIndex;

    private BufferedImage[] levelTiles;
    private BufferedImage[] numbersTiles;
    private BufferedImage[] playerTransitionSprites;

    public LevelTransitionView(LevelTransitionModel levelTransitionModel) {
        this.levelTransitionModel = levelTransitionModel;

        levelTiles = LevelManager.getInstance().getLevelTiles();
        numbersTiles = LevelManager.getInstance().getNumbersTiles();
        loadPlayerTransitionSprites();
    }

    public void draw(Graphics g) {

        drawLevel(g, levelTransitionModel.getNewLevel(), (int) levelTransitionModel.getNewLevelY());
        drawLevel(g, levelTransitionModel.getOldLevel(), (int) levelTransitionModel.getOldLevelY());
        drawPlayer(g);
    }

    private void drawPlayer(Graphics g) {

        float xOffSet = 5 * Constants.SCALE;
        float yOffSet = 12 * Constants.SCALE;

        g.drawImage(playerTransitionSprites[playerAnimationIndex], (int) ( levelTransitionModel.getPlayer().getHitbox().x - xOffSet ), (int) ( levelTransitionModel.getPlayer().getHitbox().y - yOffSet ) , 31 * Constants.SCALE, 34 * Constants.SCALE, null);
    }

    private void drawLevel(Graphics g, Level level, int yOffSet) {

        int index;
        BufferedImage tile;

        for (int y = 0; y < Constants.TILES_IN_HEIGHT; y++) {
            for (int x = 0; x < Constants.TILES_IN_WIDTH; x++) {

                index = level.getSpriteIndex(x, y);

                if (index >= 120)
                    tile = numbersTiles[index - 120];
                else
                    tile = levelTiles[index];

                g.drawImage(tile, x * Constants.TILES_SIZE, y * Constants.TILES_SIZE + yOffSet, Constants.TILES_SIZE, Constants.TILES_SIZE, null);
            }
        }
    }

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

    private void loadPlayerTransitionSprites() {
        playerTransitionSprites = new BufferedImage[2];

        BufferedImage img = LoadSave.GetSprite(LoadSave.PLAYER_TRANSITION_SPRITE);
        playerTransitionSprites[0] = img.getSubimage(0, 0, 31, 34);
        playerTransitionSprites[1] = img.getSubimage(31, 0, 31, 34);
    }
}
