package view.gameStates;

import model.gameStates.IntroModel;
import model.levels.Level;
import model.utilz.Constants;
import view.audio.AudioPlayer;
import view.utilz.LoadSave;
import view.levels.LevelManagerView;

import java.awt.*;
import java.awt.image.BufferedImage;

import static view.utilz.Constants.ANIMATION_SPEED;
import static model.utilz.Constants.INTRO.IntroState.LEVEL_TRANSITION;

public class IntroView {
    private final IntroModel introModel;

    private BufferedImage[] levelTiles;
    private BufferedImage[] numbersTiles;
    private BufferedImage[] playerTransitionSprites;
    private Font customFont;

    private boolean firstDraw = true;
    private int playerAnimationTick, playerAnimationIndex;

    public IntroView(IntroModel introModel) {
        this.introModel = introModel;

        levelTiles = LevelManagerView.getInstance().getLevelTiles();
        numbersTiles = LevelManagerView.getInstance().getNumbersTiles();
        customFont = LoadSave.GetNesFont();

        loadPlayerTransitionSprites();
    }

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

    public void draw(Graphics g) {

        if (introModel.getIntroState() == LEVEL_TRANSITION)
            drawLevel(g, introModel.getLevel(), (int) introModel.getLevelY());

        playSound();
        drawText(g);
        drawPlayer(g);
    }

    private void playSound() {
        if (firstDraw) {
            AudioPlayer.getInstance().playIntroSong();
            firstDraw = false;
        }
    }

    private void drawPlayer(Graphics g) {

        float xOffSet = 5 * Constants.SCALE;
        float yOffSet = 12 * Constants.SCALE;

        g.drawImage(playerTransitionSprites[playerAnimationIndex], (int) ( introModel.getPlayer().getHitbox().x - xOffSet ), (int) ( introModel.getPlayer().getHitbox().y - yOffSet ) , 31 * Constants.SCALE, 34 * Constants.SCALE, null);
    }

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

    private void loadPlayerTransitionSprites() {
        playerTransitionSprites = new BufferedImage[2];

        BufferedImage img = LoadSave.GetSprite(LoadSave.PLAYER_TRANSITION_SPRITE);
        playerTransitionSprites[0] = img.getSubimage(0, 0, 31, 34);
        playerTransitionSprites[1] = img.getSubimage(31, 0, 31, 34);
    }
}
