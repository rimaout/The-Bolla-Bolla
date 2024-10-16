package gameStates;

import java.awt.*;
import java.awt.image.BufferedImage;

import levels.Level;
import model.utilz.Constants;
import model.utilz.LoadSave;
import entities.Player;
import audio.AudioPlayer;
import levels.LevelManager;

import static model.utilz.Constants.ANIMATION_SPEED;
import static model.utilz.Constants.INTRO.IntroState.*;
import static model.utilz.Constants.INTRO.*;
import static model.utilz.Constants.PlayerConstants.SPAWN_X;
import static model.utilz.Constants.PlayerConstants.SPAWN_Y;

public class Intro {
    private PlayingModel playingModel;
    private Player player;
    Level level;

    private BufferedImage[] levelTiles;
    private BufferedImage[] numbersTiles;
    private BufferedImage[] playerTransitionSprites;
    private Font customFont;

    private boolean firstUpdate = true;
    private IntroState introState = INTRO;
    private float textY = TEXT_START_Y;
    float levelY = Constants.GAME_HEIGHT;

    private boolean transitionComplete;
    private int playerAnimationTick, playerAnimationIndex;
    private int lapsCompleted;
    private float angle;

    public Intro(PlayingModel playingModel) {
        this.playingModel = playingModel;
        this.player = playingModel.getPlayerOne();

        levelTiles = LevelManager.getInstance().getLevelTiles();
        numbersTiles = LevelManager.getInstance().getNumbersTiles();
        customFont = LoadSave.GetNesFont();
        loadPlayerTransitionSprites();

        level = LevelManager.getInstance().getCurrentLevel();
    }

    public void update() {

        if (firstUpdate)
            firstUpdate();

        updateState();
        updatePlayer();

        if (introState == LEVEL_TRANSITION) {
            updateText();
            updateLevel();
        }

        if (introState == START_NEW_LEVEL)
            playingModel.endIntro();
    }

    public void draw(Graphics g) {
        if (introState == LEVEL_TRANSITION)
            drawLevel(g, level, (int) levelY);

        drawText(g);
        drawPlayer(g);
    }

    private void firstUpdate(){
        AudioPlayer.getInstance().playIntroSong();
        player.getHitbox().x = PLAYER_START_X;
        player.getHitbox().y = PLAYER_START_Y;

        firstUpdate = false;
    }

    private void updateState() {

        if (introState == INTRO && lapsCompleted == 4)
            introState = LEVEL_TRANSITION;

        if (transitionComplete)
            introState = START_NEW_LEVEL;
    }

    private void updateLevel() {
        levelY -= TRANSITION_SPEED;

        if (levelY <= 0) {
            levelY = 0;
            transitionComplete = true;
        }
    }

    private void updateText() {
        textY -= TRANSITION_SPEED;
    }

    private void updatePlayer() {

        if (introState == INTRO) {

            float newX = PLAYER_START_X + (float) (RADIUS * Math.cos(angle));
            float newY = PLAYER_START_Y + (float) (RADIUS * Math.sin(angle));
            angle += ANGLE_INCREMENT;
            // P.S. I hate trigonometry :/

            // Update player position
            player.getHitbox().x = newX;
            player.getHitbox().y = newY;

            // Check if a lap is completed
            if (angle >= 2 * Math.PI) {
                angle = 0;
                lapsCompleted++;
            }

            if (lapsCompleted == TOTAL_LAPS)
                introState = LEVEL_TRANSITION;
        }

        if (introState == LEVEL_TRANSITION) {
            float xOffSet = 20 * Constants.SCALE;
            float playerTransitionSpeedX = (SPAWN_X - PLAYER_START_X - xOffSet) / (Constants.GAME_HEIGHT / TRANSITION_SPEED);
            float playerTransitionSpeedY = (SPAWN_Y - PLAYER_START_Y) / (Constants.GAME_HEIGHT / TRANSITION_SPEED);
            player.getHitbox().x += playerTransitionSpeedX;
            player.getHitbox().y += playerTransitionSpeedY;
        }

        // update player animation
        playerAnimationTick++;
        if (playerAnimationTick > ANIMATION_SPEED) {
            playerAnimationTick = 0;
            playerAnimationIndex++;

            if (playerAnimationIndex > 1)
                playerAnimationIndex = 0;
        }
    }

    private void drawPlayer(Graphics g) {

        float xOffSet = 5 * Constants.SCALE;
        float yOffSet = 12 * Constants.SCALE;

        g.drawImage(playerTransitionSprites[playerAnimationIndex], (int) ( player.getHitbox().x - xOffSet ), (int) ( player.getHitbox().y - yOffSet ) , 31 * Constants.SCALE, 34 * Constants.SCALE, null);
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
        int y = (int) textY;

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

    public void newPlayReset() {
        introState = INTRO;
        transitionComplete = false;
        playerAnimationTick = 0;
        playerAnimationIndex = 0;
        lapsCompleted = 0;
        angle = 0;
        textY = TEXT_START_Y;
        levelY = Constants.GAME_HEIGHT;

        firstUpdate = true;
    }
}