package view.overlays.gameOverlays;

import model.utilz.Constants;
import model.entities.PlayerModel;
import model.gameStates.PlayingModel;
import view.utilz.Load;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The PlayingHud class is responsible for displaying the player's score and lives on the screen.
 */
public class PlayingHud {
    private static PlayingHud instance;

    private PlayerModel playerModelOne;
    private PlayerModel playerModelTwo;
    private BufferedImage[] numbersTiles;

    /**
     * Private constructor to initialize the PlayingHud.
     * Loads the number tiles for displaying scores and lives.
     */
    private PlayingHud() {
        loadNumberTiles();
    }

    /**
     * Returns the singleton instance of PlayingHud.
     * If the instance is null, it creates a new instance.
     *
     * @return the singleton instance of PlayingHud
     */
    public static PlayingHud getInstance() {
        if (instance == null) {
            instance = new PlayingHud();
        }
        return instance;
    }

    /**
     * Sets the player models for the HUD.
     *
     * @param playingModel the model of the playing state
     */
    public void setPlayers(PlayingModel playingModel) {
        this.playerModelOne = playingModel.getPlayerOneModel();
        this.playerModelTwo = playingModel.getPlayerTwoModel();
    }

    /**
     * Draws the HUD on the screen, including player scores and lives.
     *
     * @param g the Graphics object to draw with
     */
    public void draw(Graphics g) {
        drawPlayerOnePoints(g);
        drawPlayerOneLives(g);
        drawPlayerTwoPoints(g);
        drawPlayerTwoLives(g);
    }

    /**
     * Draws the score of player one on the screen.
     *
     * @param g the Graphics object to draw with
     */
    private void drawPlayerOnePoints(Graphics g) {
        String score = String.valueOf(playerModelOne.getPoints());

        if (score.length() == 1) {
            score = "0" + score;
        }

        int yPos = Constants.TILES_SIZE;
        int size = 8 * Constants.SCALE;

        for (int i = 0; i < score.length(); i++) {
            int xPos = (8 - i) * Constants.TILES_SIZE;
            int number = Integer.parseInt(String.valueOf(score.charAt(score.length() - 1 - i)));
            drawNumber(g, number, xPos, yPos, size);
        }
    }

    /**
     * Draws the score of player two on the screen.
     * If player two is null, it draws zeros.
     *
     * @param g the Graphics object to draw with
     */
    private void drawPlayerTwoPoints(Graphics g) {

        if (playerModelTwo == null) {
            int yPos = Constants.TILES_SIZE;
            int size = 8 * Constants.SCALE;
            int xPos1 = Constants.GAME_WIDTH - 9 * Constants.TILES_SIZE;
            int xPos2 = Constants.GAME_WIDTH - 8 * Constants.TILES_SIZE;

            drawNumber(g, 0, xPos1, yPos, size);
            drawNumber(g, 0, xPos2, yPos, size);
        }
    }

    /**
     * Draws the lives of player one on the screen.
     *
     * @param g the Graphics object to draw with
     */
    private void drawPlayerOneLives(Graphics g) {
        int xPos = Constants.TILES_SIZE;
        int yPos = 26 * Constants.TILES_SIZE;
        int size = 8 * Constants.SCALE;

        drawNumber(g, playerModelOne.getLives(), xPos, yPos, size);
    }

    /**
     * Draws the lives of player two on the screen.
     * If player two is null, it does nothing.
     *
     * @param g the Graphics object to draw with
     */
    private void drawPlayerTwoLives(Graphics g) {
       if (playerModelTwo == null)
           return;
    }

    /**
     * Draws a number on the screen at the specified position and size.
     *
     * @param g the Graphics object to draw with
     * @param number the number to draw
     * @param xPos the x position to draw the number
     * @param yPos the y position to draw the number
     * @param size the size of the number
     */
    private void drawNumber(Graphics g, int number, int xPos, int yPos, int size) {
        BufferedImage liveTile = numbersTiles[number];
        g.drawImage(liveTile, xPos, yPos, size, size, null);
    }

    /**
     * Loads the number tiles from the sprite sheet.
     */
    private void loadNumberTiles() {

        numbersTiles = new BufferedImage[10];
        BufferedImage numbersSprite = Load.GetSprite(Load.NUMBERS_TILES_SPRITE);
        for (int i = 0; i < numbersTiles.length; i++) {
            numbersTiles[i] = numbersSprite.getSubimage(i * 8, 0, 8, 8);
        }
    }
}