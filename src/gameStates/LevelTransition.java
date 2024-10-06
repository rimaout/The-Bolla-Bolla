package gameStates;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import main.Game;
import levels.Level;
import utilz.LoadSave;
import entities.Player;
import levels.LevelManager;

import static utilz.Constants.ANIMATION_SPEED;
import static utilz.Constants.PlayerConstants.SPAWN_X;
import static utilz.Constants.PlayerConstants.SPAWN_Y;
import static utilz.Constants.LevelTransition.TransitionState;
import static utilz.Constants.LevelTransition.TransitionState.*;
import static utilz.Constants.LevelTransition.LEVEL_TRANSITION_SPEED;

public class LevelTransition extends State implements StateMethods{
    PlayingModel playingModel;

    private BufferedImage[] levelTiles;
    private BufferedImage[] numbersTiles;
    private BufferedImage[] playerTransitionSprites;

    Level oldLevel, newLevel;
    float oldLevelY;
    float newLevelY;

    private TransitionState transitionState;
    private boolean firstUpdate = true;

    private Player player;
    private float playerStartX, playerStartY;
    private int playerAnimationTick, playerAnimationIndex;

    public LevelTransition(Game game) {
        super(game);

        this.playingModel = game.getPlaying();
        levelTiles = LevelManager.getInstance().getLevelTiles();
        numbersTiles = LevelManager.getInstance().getNumbersTiles();
        player = game.getPlaying().getPlayerOne();
        loadPlayerTransitionSprites();
    }

    @Override
    public void update() {

        if (firstUpdate)
            firstUpdate();

        if (transitionState == LEVEL_TRANSITION) {
            updatePlayer();
            updateLevels();
        }

        else if (transitionState == LOADING_NEW_LEVEL) {
            loadNewLevel();
            transitionState = START_NEW_LEVEL;
        }

        else if (transitionState == START_NEW_LEVEL)
            endTransition();
    }

    private void firstUpdate(){
        transitionState = LEVEL_TRANSITION;

        playerStartX = player.getHitbox().x;
        playerStartY = player.getHitbox().y;

        oldLevelY = 0;
        newLevelY = Game.GAME_HEIGHT;

        firstUpdate = false;
    }

    private void updateLevels() {
        oldLevelY -= LEVEL_TRANSITION_SPEED;
        newLevelY -= LEVEL_TRANSITION_SPEED;

        // if transition is complete
        if (newLevelY <= 0)
            transitionState = LOADING_NEW_LEVEL;
    }

    private void updatePlayer() {

        // player position
        float playerTransitionSpeedX = (SPAWN_X - playerStartX) / (Game.GAME_HEIGHT / LEVEL_TRANSITION_SPEED);
        float playerTransitionSpeedY = (SPAWN_Y - playerStartY) / (Game.GAME_HEIGHT / LEVEL_TRANSITION_SPEED);
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

    public void draw(Graphics g) {

        drawLevel(g, newLevel, (int) newLevelY);
        drawLevel(g, oldLevel, (int) oldLevelY);
        drawPlayer(g);
    }

    private void drawPlayer(Graphics g) {

        float xOffSet = 5 * Game.SCALE;
        float yOffSet = 12 * Game.SCALE;

        g.drawImage(playerTransitionSprites[playerAnimationIndex], (int) ( player.getHitbox().x - xOffSet ), (int) ( player.getHitbox().y - yOffSet ) , 31 * Game.SCALE, 34 * Game.SCALE, null);
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

    private void loadNewLevel() {
        playingModel.startNewLevel();
    }

    public void endTransition() {
        GameState.state = GameState.PLAYING;

        firstUpdate = true;
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
}
