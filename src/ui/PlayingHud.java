package ui;

import entities.Player;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PlayingHud {
    // This class will be used to display: The Player Score and Lives and Powerups

    private Player playerOne;
    private Player playerTwo;
    private BufferedImage[] numbersTiles;

    public PlayingHud(Player playerOne, Player playerTwo) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        loadNumberTiles();
    }

    public void draw(Graphics g) {
        drawPlayerOnePoints(g);
        drawPlayerOneLives(g);
        drawPlayerTwoPoints(g);
        drawPlayerTwoLives(g);
    }

    private void drawPlayerOnePoints(Graphics g) {
        String score = String.valueOf(playerOne.getPoints());

        if (score.length() == 1) {
            score = "0" + score;
        }

        int yPos = Game.TILES_SIZE;
        int size = 8 * Game.SCALE;

        for (int i = 0; i < score.length(); i++) {
            int xPos = (8 - i) * Game.TILES_SIZE;
            int number = Integer.parseInt(String.valueOf(score.charAt(score.length() - 1 - i)));
            drawNumber(g, number, xPos, yPos, size);
        }
    }

    private void drawPlayerTwoPoints(Graphics g) {

        if (playerTwo == null) {
            int yPos = Game.TILES_SIZE;
            int size = 8 * Game.SCALE;
            int xPos1 = Game.GAME_WIDTH - 9 * Game.TILES_SIZE;
            int xPos2 = Game.GAME_WIDTH - 8 * Game.TILES_SIZE;

            drawNumber(g, 0, xPos1, yPos, size);
            drawNumber(g, 0, xPos2, yPos, size);
        }
    }

    private void drawPlayerOneLives(Graphics g) {
        int xPos = Game.TILES_SIZE;
        int yPos = 26 * Game.TILES_SIZE;
        int size = 8 * Game.SCALE;

        drawNumber(g, playerOne.getLives(), xPos, yPos, size);
    }

    private void drawPlayerTwoLives(Graphics g) {
       if (playerTwo == null)
           return;

       // draw lives for player two (still need to implement player two)
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
