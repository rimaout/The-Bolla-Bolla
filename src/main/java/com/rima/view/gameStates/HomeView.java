package com.rima.view.gameStates;

import com.rima.model.utilz.Constants;
import com.rima.controller.GameController;
import com.rima.view.audio.AudioPlayer;
import com.rima.view.utilz.Load;
import com.rima.view.overlays.menuOverlays.MenuTwinkleBubbleManager;

import static com.rima.view.utilz.Constants.AudioConstants.HOME;
import static com.rima.view.utilz.Constants.Home.LOGO_END_Y;
import static com.rima.view.utilz.Constants.Home.LOGO_SPEED;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The HomeView class represents the view for the home screen of the game.
 * It handles loading and drawing the game logo, updating the positions of elements, and displaying the start prompt.
 */
public class HomeView {
    private final MenuTwinkleBubbleManager menuTwinkleBubbleManager = MenuTwinkleBubbleManager.getInstance();
    private final GameController gameController;
    private boolean firstUpdate = true;
    private boolean audioAlreadyPlayed = false;

    private BufferedImage logoImg;
    private float logoX, logoY, logoW, logoH;

    private boolean isLogoInPosition = false;
    private final Font nesFont;

    private long lastTimerUpdate;
    private long drawTimer = 1000;
    private boolean canDraw = false;

    /**
     * Constructs a HomeView with the specified GameController.
     *
     * @param gameController the controller for the game
     */
    public HomeView(GameController gameController) {
        this.gameController = gameController;
        loadLogo();

        nesFont = Load.GetNesFont();
        menuTwinkleBubbleManager.setHomeView(this);
    }

    /**
     * Loads the game logo image and initializes its position and size.
     */
    private void loadLogo() {
        logoImg = Load.GetSprite(Load.GAME_LOGO);
        logoW = logoImg.getWidth() * Constants.SCALE;
        logoH = logoImg.getHeight() * Constants.SCALE;
        logoX = (float) Constants.GAME_WIDTH / 2 - logoW / 2;

        logoY = (int) (- logoImg.getHeight() * Constants.SCALE);
    }

    /**
     * Updates the positions of the logo and bubbles.
     * Moves the logo to its final position if it is not already there.
     */
    public void updatePositions() {
        // Update Bubbles
        menuTwinkleBubbleManager.update();

        if (!canDraw) return;

        // Update Logo Position
        if (logoY < LOGO_END_Y)
            logoY += LOGO_SPEED;
        else
            isLogoInPosition = true;
    }

    /**
     * Draws the home screen elements, including the logo and bubbles.
     * Displays the start prompt when the logo is in position.
     *
     * @param g the Graphics object to draw with
     */
    public void draw(Graphics g) {
        timerUpdate();

        menuTwinkleBubbleManager.draw(g);
        updatePositions();
        playAudio();

        if (!canDraw) return;

        g.drawImage(logoImg, (int) logoX, (int) logoY, (int) logoW, (int) logoH, null);

        if (isLogoInPosition) {
            g.setColor(Color.WHITE);
            g.setFont(nesFont);
            g.drawString("PRESS ENTER TO START!", Constants.GAME_WIDTH / 2 - 75 * Constants.SCALE, Constants.GAME_HEIGHT / 2 + 50 * Constants.SCALE);

            g.setFont(nesFont.deriveFont(15f));
            g.drawString("© 2025 RIMA CORPORATION", Constants.GAME_WIDTH / 2 - 55 * Constants.SCALE, Constants.GAME_HEIGHT / 2 + 100 * Constants.SCALE);
        }
    }

    private void playAudio(){
        if (!audioAlreadyPlayed) {
            AudioPlayer.getInstance().playSoundEffect(HOME);
            audioAlreadyPlayed = true;
        }
    }

    private void timerUpdate(){
        if (firstUpdate) {
            lastTimerUpdate = System.currentTimeMillis();
            firstUpdate = false;
        }

        long timeDelta = System.currentTimeMillis() - lastTimerUpdate;
        drawTimer -= timeDelta;
        lastTimerUpdate = System.currentTimeMillis();

        if (drawTimer <= 0)
            canDraw = true;
    }

    // ----------- Getters ------------ //

    /**
     * Checks if the logo is in its final position.
     *
     * @return true if the logo is in position, false otherwise
     */
    public boolean IsLogoInPosition() {
        return isLogoInPosition;
    }

    /**
     * Returns the GameController associated with this view.
     *
     * @return the GameController
     */
    public GameController getGame() {
        return gameController;
    }
}