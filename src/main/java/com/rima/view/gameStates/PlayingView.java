package com.rima.view.gameStates;

import com.rima.model.gameStates.PlayingModel;
import com.rima.view.bubbles.playerBubbles.PlayerBubblesManagerView;
import com.rima.view.entities.EnemyManagerView;
import com.rima.view.entities.HurryUpManagerView;
import com.rima.view.entities.PlayerView;
import com.rima.view.itemsAndRewards.ItemManagerView;
import com.rima.view.itemsAndRewards.RewardPointsManagerView;
import com.rima.view.levels.LevelManagerView;
import com.rima.view.overlays.gameOverlays.GameCompletedOverlayView;
import com.rima.view.overlays.gameOverlays.GameOverOverlayView;
import com.rima.view.overlays.gameOverlays.GamePausedOverlayView;
import com.rima.view.overlays.gameOverlays.PlayingHud;
import com.rima.view.projectiles.ProjectileManagerView;
import com.rima.view.bubbles.specialBubbles.SpecialBubbleManagerView;

import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * The PlayingView class represents the view for the {@link PlayingModel}.
 * It handles drawing and updating all game elements during gameplay.
 */
public class PlayingView implements Observer {
    private PlayingModel playingModel;

    private PlayerView playerOneView;
    private PlayerView playerTwoView = null;

    private GamePausedOverlayView gamePausedOverlayView;
    private GameOverOverlayView gameOverOverlayView;
    private GameCompletedOverlayView gameCompletedOverlayView;
    private IntroView introView;

    /**
     * Constructs a PlayingView with the specified models and controllers.
     *
     * @param playingModel the model for the playing state
     */
    public PlayingView(PlayingModel playingModel) {
        this.playingModel = playingModel;

        gamePausedOverlayView = new GamePausedOverlayView(playingModel);
        gameCompletedOverlayView = new GameCompletedOverlayView(playingModel);
        gameOverOverlayView = new GameOverOverlayView(playingModel);

        initClasses();
    }

    /**
     * Updates the state of all game elements.
     */
    public void update() {
        playerOneView.update();
        introView.updatePlayerAnimation();
        ProjectileManagerView.getInstance().update();
        EnemyManagerView.getInstance().update();
        HurryUpManagerView.getInstance().update();
        PlayerBubblesManagerView.getInstance().update();
    }

    /**
     * Draws all game elements on the screen.
     *
     * @param g the Graphics object to draw with
     */
    public void draw(Graphics g) {

        if (playingModel.isIntoRunning()) {
            introView.draw(g);
        }
        else {
            LevelManagerView.getInstance().draw(g);
            ItemManagerView.getInstance().draw(g);
            PlayingHud.getInstance().draw(g);
            EnemyManagerView.getInstance().draw(g);
            HurryUpManagerView.getInstance().draw(g);
            PlayerBubblesManagerView.getInstance().draw(g);
            SpecialBubbleManagerView.getInstance().draw(g);
            ProjectileManagerView.getInstance().draw(g);
            RewardPointsManagerView.getInstance().draw((Graphics2D) g);

            if (playingModel.getPlayerOneModel().isActive())
                playerOneView.draw((Graphics2D) g);
        }

        if (playingModel.isPaused())
            gamePausedOverlayView.draw(g);

        else if (playingModel.isGameOver())
            gameOverOverlayView.draw(g);

        else if (playingModel.isGameCompleted())
            gameCompletedOverlayView.draw(g);
    }

    /**
     * Resets the state of all game elements for a new level.
     */
    public void newLevelReset() {
        ProjectileManagerView.getInstance().newLevelReset();
        RewardPointsManagerView.getInstance().newLevelReset();
        ItemManagerView.getInstance().newLevelReset();
        EnemyManagerView.getInstance().newLevelReset();
        HurryUpManagerView.getInstance().newLevelReset();
        PlayerBubblesManagerView.getInstance().newLevelReset();
        SpecialBubbleManagerView.getInstance().newLevelReset();
    }

    /**
     * Resets the state of all game elements for a new play session.
     */
    public void newPlayReset() {
        gamePausedOverlayView.reset();
        introView.reset();
        ProjectileManagerView.getInstance().newPlayReset();
        RewardPointsManagerView.getInstance().newPlayReset();
        ItemManagerView.getInstance().newPlayReset();
        EnemyManagerView.getInstance().newPlayReset();
        HurryUpManagerView.getInstance().newPlayReset();
        PlayerBubblesManagerView.getInstance().newPlayReset();
        SpecialBubbleManagerView.getInstance().newPlayReset();
    }

    /**
     * Initializes the classes for the player views and intro view.
     */
    public void initClasses() {
        playerOneView = new PlayerView(playingModel.getPlayerOneModel());
        introView = new IntroView(playingModel.getIntroModel());
        PlayingHud.getInstance().setPlayers(playingModel);
    }

    /**
     * Updates the view based on changes in the observed model.
     *
     * @param o the observable object
     * @param arg an argument passed to the notifyObservers method
     */
    @Override
    public void update(Observable o, Object arg) {
        PlayingModel playingModel = (PlayingModel) o;

        if (playingModel.isNewPlayReset())
            newPlayReset();

        if (playingModel.isNewLevelReset())
            newLevelReset();
    }

    // ------------------- Getters  Methods -------------------

    /**
     * Returns the player one view.
     *
     * @return the player one view
     */
    public GameCompletedOverlayView getGameCompletedOverlayView() {
        return gameCompletedOverlayView;
    }

    /**
     * Returns the player one view.
     *
     * @return the player one view
     */
    public GameOverOverlayView getGameOverOverlayView() {
        return gameOverOverlayView;
    }
}