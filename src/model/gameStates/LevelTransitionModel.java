package model.gameStates;

import model.levels.Level;
import model.utilz.Constants;
import model.entities.PlayerModel;

import static model.utilz.Constants.PlayerConstants.SPAWN_X;
import static model.utilz.Constants.PlayerConstants.SPAWN_Y;
import static model.utilz.Constants.LevelTransition.TransitionState;
import static model.utilz.Constants.LevelTransition.TransitionState.*;
import static model.utilz.Constants.LevelTransition.LEVEL_TRANSITION_SPEED;

public class LevelTransitionModel{
    private static LevelTransitionModel instance;

    private PlayerModel playerModel;
    private PlayingModel playingModel;

    private float oldLevelY;
    private float newLevelY;
    private Level oldLevel, newLevel;
    private float playerStartX, playerStartY;

    private TransitionState transitionState;
    private boolean firstUpdate = true;

    private LevelTransitionModel() {
        playerModel = PlayerModel.getInstance();
    }

    public static LevelTransitionModel getInstance() {
        if (instance == null)
            instance = new LevelTransitionModel();
        return instance;
    }

    public void initPlayingModel(PlayingModel playingModel) {
        this.playingModel = playingModel;
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
}