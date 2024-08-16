package ui;

import java.awt.Graphics;

import gameStates.Gamestate;
import utilz.LoadSave;
import gameStates.Playing;
import main.Game;

import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import static utilz.Constants.UI.UrmButtons.*;

public class LevelCompletedOverlay {
    private Playing playing;
    private UrmButton menuButton, nextButton;
    private BufferedImage backgroundImg;
    private int bgX, bgY, bgWidth, bgHeight;


    public LevelCompletedOverlay(Playing playing) {
        this.playing = playing;
        initBackground();
        initButtons();
    }

    private void initBackground() {
        backgroundImg = LoadSave.GetSprite(LoadSave.LEVEL_COMPLETED_BACKGROUND);
        bgWidth = (int) (backgroundImg.getWidth() * Game.SCALE / 2.6);
        bgHeight = (int) (backgroundImg.getHeight() * Game.SCALE / 2.6);
        bgX = (Game.GAME_WIDTH / 2) - (bgWidth / 2);
        bgY = (Game.GAME_HEIGHT / 2) - (bgHeight / 2);
    }

    private void initButtons() {
        int menuX = (int) (95 * Game.SCALE);
        int nextX = (int) (138 * Game.SCALE);
        int y = (int) (118 * Game.SCALE);
        int ButtonsWidth = (int) (URM_BT_WIDTH * Game.SCALE / 3.6);
        int ButtonsHeight = (int) (URM_BT_HEIGHT * Game.SCALE / 3.6);
        nextButton = new UrmButton(nextX, y, ButtonsWidth, ButtonsHeight, 0);
        menuButton = new UrmButton(menuX, y, ButtonsWidth, ButtonsHeight, 2);

    }

    public void update() {
        menuButton.update();
        nextButton.update();
    }

    public void draw(Graphics g) {
        g.drawImage(backgroundImg, bgX, bgY, bgWidth, bgHeight, null);
        menuButton.draw(g);
        nextButton.draw(g);
    }

    private boolean isInButton(UrmButton b, MouseEvent e) {
        return b.getBounds().contains(e.getX(), e.getY());
    }

    public void mouseMoved(MouseEvent e) {
        nextButton.setMouseOver(false);
        menuButton.setMouseOver(false);

        if(isInButton(nextButton, e))
            nextButton.setMouseOver(true);

        else if(isInButton(menuButton, e))
            menuButton.setMouseOver(true);
    }

    public void mousePressed(MouseEvent e) {
        if(isInButton(nextButton, e))
            nextButton.setMousePressed(true);

        else if(isInButton(menuButton, e))
            menuButton.setMousePressed(true);
    }

    public void mouseReleased(MouseEvent e) {
        if(isInButton(nextButton, e)) {
            if (nextButton.isMousePressed()) {
                playing.loadNextLevel();
            }
        } else if(isInButton(menuButton, e)) {
            if (menuButton.isMousePressed()) {
                playing.resetAll();
                Gamestate.state = Gamestate.MENU;
            }
        }

        menuButton.resetBools();
        nextButton.resetBools();
    }

}
