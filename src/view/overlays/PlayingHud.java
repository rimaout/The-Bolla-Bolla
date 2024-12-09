package view.overlays;

import model.gameStates.PlayingModel;
import model.entities.PlayerModel;
import model.utilz.Constants;
import view.utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PlayingHud {
    // This class will be used to display: The Player Score and Lives and Powerups
    private static PlayingHud instance;

    private PlayerModel playerModelOne;
    private PlayerModel playerModelTwo;
    private BufferedImage[] numbersTiles;

    private PlayingHud(PlayingModel playingModel) {
        this.playerModelOne = playingModel.getPlayerOneModel();
        this.playerModelTwo = playingModel.getPlayerTwoModel();
        loadNumberTiles();
    }

    public static PlayingHud getInstance(PlayingModel playingModel) {
        if (instance == null) {
            instance = new PlayingHud(playingModel);
        }
        return instance;
    }

    public void draw(Graphics g) {
        drawPlayerOnePoints(g);
        drawPlayerOneLives(g);
        drawPlayerTwoPoints(g);
        drawPlayerTwoLives(g);
    }

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

    private void drawPlayerOneLives(Graphics g) {
        int xPos = Constants.TILES_SIZE;
        int yPos = 26 * Constants.TILES_SIZE;
        int size = 8 * Constants.SCALE;

        drawNumber(g, playerModelOne.getLives(), xPos, yPos, size);
    }

    private void drawPlayerTwoLives(Graphics g) {
       if (playerModelTwo == null)
           return;
    }

    private void drawNumber(Graphics g, int number, int xPos, int yPos, int size) {
        BufferedImage liveTile = numbersTiles[number];
        g.drawImage(liveTile, xPos, yPos, size, size, null);
    }

    private void loadNumberTiles() {

        // load numbers
        numbersTiles = new BufferedImage[10];
        BufferedImage numbersSprite = LoadSave.GetSprite(LoadSave.NUMBERS_TILES_SPRITE);
        for (int i = 0; i < numbersTiles.length; i++) {
            numbersTiles[i] = numbersSprite.getSubimage(i * 8, 0, 8, 8);
        }
    }
}