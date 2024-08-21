package gameStates;

import bubbles.BubbleManager;
import entities.EnemyManager;
import entities.Player;
import levels.Level;
import levels.LevelManager;
import main.Game;
import utilz.LoadSave;

import static utilz.Constants.ANIMATION_SPEED;
import static utilz.Constants.PlayerConstants.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class LevelTransition extends State implements StateMethods{
    private BufferedImage[] levelTiles;
    private BufferedImage[] numbersTiles;
    private BufferedImage[] playerTransitionSprites;

    Level oldLevel, newLevel;
    float oldLevelY = 0;
    float newLevelY = Game.GAME_HEIGHT;

    private boolean firstUpdate = true;
    private final float TRANSITION_SPEED = 0.8f * Game.SCALE;

    private Player player;
    private float playerStartX, playerStartY;
    private int playerAnimationTick, playerAnimationIndex;

    public LevelTransition(Game game) {
        super(game);

        levelTiles = LevelManager.getInstance().getLevelTiles();
        numbersTiles = LevelManager.getInstance().getNumbersTiles();
        player = game.getPlaying().getPlayer();
        loadPlayerTransitionSprites();
    }

    @Override
    public void update() {
        if (firstUpdate) {
            playerStartX = player.getHitbox().x;
            playerStartY = player.getHitbox().y;
            firstUpdate = false;
        }

        updatePlayer();
        levelUpdate();
    }

    @Override
    public void draw(Graphics g) {
       drawLevel(g, oldLevel, (int) oldLevelY);
       drawLevel(g, newLevel, (int) newLevelY);
       drawPlayer(g);
    }

    private void updatePlayer() {

        // player position
        float playerTransitionSpeedX = (SPAWN_X - playerStartX) / (Game.GAME_HEIGHT / TRANSITION_SPEED);
        float playerTransitionSpeedY = (SPAWN_Y - playerStartY) / (Game.GAME_HEIGHT / TRANSITION_SPEED);
        player.getHitbox().x += playerTransitionSpeedX;
        player.getHitbox().y += playerTransitionSpeedY;

        // player animation
        playerAnimationTick++;
        if (playerAnimationTick > ANIMATION_SPEED) {
            playerAnimationTick = 0;
            playerAnimationIndex++;

            if (playerAnimationIndex > 1)
                playerAnimationIndex = 0;
        }
    }

    private void levelUpdate() {
        oldLevelY -= TRANSITION_SPEED;
        newLevelY -= TRANSITION_SPEED;

        // if transition is complete
        if (newLevelY <= 0) {
            initialiseNewLevel();
        }
    }

    private void initialiseNewLevel() {
        GameState.state = GameState.PLAYING;
        game.getPlaying().setLevelCompleted(false);
        resetAll();

        EnemyManager.getInstance().loadEnemies();
        EnemyManager.getInstance().loadLevelData();

        BubbleManager.getInstance().loadLevelData();
        BubbleManager.getInstance().loadLevelData();
        BubbleManager.getInstance().loadWindData();

        game.getPlaying().getPlayer().loadLevelData();
    }

    private void resetAll() {
        game.getPlaying().resetAll();
        oldLevelY = 0;
        newLevelY = Game.GAME_HEIGHT;
        firstUpdate = true;
    }

    private void drawPlayer(Graphics g) {

        float xOffSet = 5 * Game.SCALE;
        float yOffSet = 12 * Game.SCALE;

        g.drawImage(playerTransitionSprites[playerAnimationIndex], (int) ( player.getHitbox().x - xOffSet ), (int) ( player.getHitbox().y - yOffSet ) , 31 * Game.SCALE, 34 * Game.SCALE, null);
    }

    private void drawEnemy(Graphics g) {

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

    public void setOldLevel(Level oldLevel) {
        this.oldLevel = oldLevel;
    }

    public void setNewLevel(Level newLevel) {
        this.newLevel = newLevel;
    }

  // --------------------------- PLAYER INPUT (not used) ---------------------------

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
