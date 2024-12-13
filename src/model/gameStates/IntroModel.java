package model.gameStates;

import model.levels.Level;
import model.utilz.Constants;
import model.entities.PlayerModel;
import model.levels.LevelManagerModel;

import static model.utilz.Constants.INTRO.IntroState.*;
import static model.utilz.Constants.INTRO.*;
import static model.utilz.Constants.PlayerConstants.SPAWN_X;
import static model.utilz.Constants.PlayerConstants.SPAWN_Y;

public class IntroModel {
    private PlayingModel playingModel;
    private PlayerModel playerModel;
    Level level;

    private boolean firstUpdate = true;
    private IntroState introState = INTRO;
    private float textY = TEXT_START_Y;
    float levelY = Constants.GAME_HEIGHT;

    private boolean transitionComplete;

    private int lapsCompleted;
    private float angle;

    public IntroModel(PlayingModel playingModel) {
        this.playingModel = playingModel;
        this.playerModel = playingModel.getPlayerOneModel();

        level = LevelManagerModel.getInstance(playingModel).getCurrentLevel();
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

    private void firstUpdate(){
        playerModel.getHitbox().x = PLAYER_START_X;
        playerModel.getHitbox().y = PLAYER_START_Y;

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
            playerModel.getHitbox().x = newX;
            playerModel.getHitbox().y = newY;

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
            playerModel.getHitbox().x += playerTransitionSpeedX;
            playerModel.getHitbox().y += playerTransitionSpeedY;
        }
    }

    public void newPlayReset() {
        introState = INTRO;
        transitionComplete = false;
        lapsCompleted = 0;
        angle = 0;
        textY = TEXT_START_Y;
        levelY = Constants.GAME_HEIGHT;

        firstUpdate = true;
    }

    // ------- Getters -------

    public IntroState getIntroState() {
        return introState;
    }

    public Level getLevel() {
        return level;
    }

    public float getLevelY() {
        return levelY;
    }

    public PlayerModel getPlayer() {
        return playerModel;
    }

    public float getTextY() {
        return textY;
    }
}