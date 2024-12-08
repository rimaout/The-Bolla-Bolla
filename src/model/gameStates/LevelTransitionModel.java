package model.gameStates;

import main.Game;
import model.levels.Level;
import model.Constants;
import model.entities.PlayerModel;

import static model.Constants.PlayerConstants.SPAWN_X;
import static model.Constants.PlayerConstants.SPAWN_Y;
import static model.Constants.LevelTransition.TransitionState;
import static model.Constants.LevelTransition.TransitionState.*;
import static model.Constants.LevelTransition.LEVEL_TRANSITION_SPEED;

public class LevelTransitionModel implements State {
    private PlayingModel playingModel;
    private Game game;

    private Level oldLevel, newLevel;
    private float oldLevelY;
    private float newLevelY;

    private TransitionState transitionState;
    private boolean firstUpdate = true;

    private PlayerModel playerModel;
    private float playerStartX, playerStartY;

    public LevelTransitionModel(Game game) {
        this.game = game;
        this.playingModel = game.getPlaying();

        playerModel = game.getPlaying().getPlayerOneModel();
    }

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

        playerStartX = playerModel.getHitbox().x;
        playerStartY = playerModel.getHitbox().y;

        oldLevelY = 0;
        newLevelY = Constants.GAME_HEIGHT;

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
        float playerTransitionSpeedX = (SPAWN_X - playerStartX) / (Constants.GAME_HEIGHT / LEVEL_TRANSITION_SPEED);
        float playerTransitionSpeedY = (SPAWN_Y - playerStartY) / (Constants.GAME_HEIGHT / LEVEL_TRANSITION_SPEED);
        playerModel.getHitbox().x += playerTransitionSpeedX;
        playerModel.getHitbox().y += playerTransitionSpeedY;
    }

    private void loadNewLevel() {
        playingModel.startNewLevel();
    }

    public void endTransition() {
        GameState.state = GameState.PLAYING;

        firstUpdate = true;
    }

    // ------- Setters -------

    public void setOldLevel(Level oldLevel) {
        this.oldLevel = oldLevel;
    }

    public void setNewLevel(Level newLevel) {
        this.newLevel = newLevel;
    }

    // ------- Getters -------

    public Level getOldLevel() {
        return oldLevel;
    }

    public Level getNewLevel() {
        return newLevel;
    }

    public float getOldLevelY() {
        return oldLevelY;
    }

    public float getNewLevelY() {
        return newLevelY;
    }

    public PlayerModel getPlayer() {
        return playerModel;
    }

    public Game getGame() {
        return game;
    }
}
