package com.rima.view.overlays.gameOverlays;

import com.rima.view.utilz.Load;
import com.rima.model.utilz.Constants;
import com.rima.model.gameStates.PlayingModel;

import java.awt.*;

/**
 * Abstract class representing the base structure of a game overlay view.
 * Handles the drawing of the overlay, including the title and control instructions.
 */
public abstract class GameOverlayView {
    protected final PlayingModel playingModel;
    protected final Font nesFont;
    protected final Font retroFont;

    protected boolean firstUpdate = true;

    /**
     * Constructs a GameOverlayView with the specified PlayingModel.
     * Initializes the fonts used in the overlay.
     *
     * @param playingModel the model of the playing state
     */
    public GameOverlayView(PlayingModel playingModel) {
        this.playingModel = playingModel;
        this.nesFont = Load.GetNesFont();
        this.retroFont = Load.GetRetroGamingFont();
    }

    /**
     * Draws the overlay on the screen.
     * Sets the background color, audio, title, and control instructions.
     *
     * @param g the Graphics object to draw with
     */
    public void draw(Graphics g) {
        g.setColor(new Color(0, 0, 0, 220));
        g.fillRect(0, 0, Constants.GAME_WIDTH, Constants.GAME_HEIGHT);

        setAudio();
        drawTitle(g);
        drawControls(g);
    }

    /**
     * Resets the overlay state for a new update.
     * Sets the firstUpdate flag to true.
     */
    public void reset() {
        firstUpdate = true;
    }

    /**
     * Draws the title of the overlay.
     *
     * @param g the Graphics object to draw with
     */
    protected abstract void drawTitle(Graphics g);

    /**
     * Draws the control instructions of the overlay.
     *
     * @param g the Graphics object to draw with
     */
    protected abstract void drawControls(Graphics g);

    /**
     * Sets the audio for the overlay.
     */
    protected abstract void setAudio();
}
