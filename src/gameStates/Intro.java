package gameStates;

import entities.Player;
import levels.Level;
import levels.LevelManager;
import main.Game;
import utilz.LoadSave;

import static utilz.Constants.ANIMATION_SPEED;
import static utilz.Constants.INTRO.IntroState.*;
import static utilz.Constants.INTRO.*;
import static utilz.Constants.PlayerConstants.SPAWN_X;
import static utilz.Constants.PlayerConstants.SPAWN_Y;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Intro extends State implements StateMethods {

    private BufferedImage[] levelTiles;
    private BufferedImage[] numbersTiles;
    private BufferedImage[] playerTransitionSprites;
    private Font customFont;

    Level level;
    float levelY = Game.GAME_HEIGHT;

    private IntroState introState = INTRO;
    private boolean firstUpdate = true;
    private boolean transitionComplete = false;

    private Player player;
    private int playerAnimationTick, playerAnimationIndex;
    private int lapsCompleted = 0;
    private float angle = 0;

    private float textY;

    public Intro(Game game) {
        super(game);

        levelTiles = LevelManager.getInstance().getLevelTiles();
        numbersTiles = LevelManager.getInstance().getNumbersTiles();
        player = game.getPlaying().getPlayerOne();
        customFont = LoadSave.getNesFont();
        loadPlayerTransitionSprites();

        level = LevelManager.getInstance().getCurrentLevel();
    }

    @Override
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
            GameState.state = GameState.PLAYING;
    }

    @Override
    public void draw(Graphics g) {
        if (introState == LEVEL_TRANSITION)
            drawLevel(g, level, (int) levelY);

        drawText(g);
        drawPlayer(g);
    }

    private void firstUpdate(){
        player.getHitbox().x = PLAYER_START_X;
        player.getHitbox().y = PLAYER_START_Y;
        textY = TEXT_START_Y;
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
            float xOffSet = 20 * Game.SCALE;
            float playerTransitionSpeedX = (SPAWN_X - PLAYER_START_X - xOffSet) / (Game.GAME_HEIGHT / TRANSITION_SPEED);
            float playerTransitionSpeedY = (SPAWN_Y - PLAYER_START_Y) / (Game.GAME_HEIGHT / TRANSITION_SPEED);
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

        float xOffSet = 5 * Game.SCALE;
        float yOffSet = 12 * Game.SCALE;

        g.drawImage(playerTransitionSprites[playerAnimationIndex], (int) ( player.getHitbox().x - xOffSet ), (int) ( player.getHitbox().y - yOffSet ) , 31 * Game.SCALE, 34 * Game.SCALE, null);
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
        int extraSpace = 7 * Game.SCALE;
        int y = (int) textY;

        for (String line : lines) {
            int lineWidth = metrics.stringWidth(line);
            int x = (Game.GAME_WIDTH - lineWidth) / 2;
            g.drawString(line, x, y);
            y += lineHeight + extraSpace;
        }

        // Draw last line
        int lastLineWidth = metrics.stringWidth(lastLine);
        int x = (Game.GAME_WIDTH - lastLineWidth) / 2;
        int extraExtraSpace = 6 * Game.SCALE;
        g.drawString(lastLine, x, y + extraExtraSpace);
    }

    private void drawLevel(Graphics g, Level level, int yOffSet) {

        int index;
        BufferedImage tile;

        for (int y = 0; y < Game.TILES_IN_HEIGHT; y++) {
            for (int x = 0; x < Game.TILES_IN_WIDTH; x++) {

                index = level.getSpriteIndex(x, y);

                if (index >= 120)
                    tile = numbersTiles[index - 120];
                else
                    tile = levelTiles[index];

                g.drawImage(tile, x * Game.TILES_SIZE, y * Game.TILES_SIZE + yOffSet, Game.TILES_SIZE, Game.TILES_SIZE, null);
            }
        }
    }

    private void loadPlayerTransitionSprites() {
        playerTransitionSprites = new BufferedImage[2];

        BufferedImage img = LoadSave.GetSprite(LoadSave.PLAYER_TRANSITION_SPRITE);
        playerTransitionSprites[0] = img.getSubimage(0, 0, 31, 34);
        playerTransitionSprites[1] = img.getSubimage(31, 0, 31, 34);
    }

    // ---------- Input Methods (not used) ----------

    @Override
    public void mouseClicked(MouseEvent e) {
        // Not used
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Not used
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // Not used
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // Not used
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // Not used
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Not used
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Not used
    }
}