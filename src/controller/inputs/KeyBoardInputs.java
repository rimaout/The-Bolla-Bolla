package controller.inputs;

import main.GamePanel;
import gameStates.GameState;

import java.awt.event.KeyListener;

public class KeyBoardInputs implements KeyListener {
    private GamePanel gamePanel;

    public KeyBoardInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyTyped(java.awt.event.KeyEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void keyPressed(java.awt.event.KeyEvent e) {
        switch (GameState.state) {
            case HOME:
                gamePanel.getGame().getHomeController().keyPressed(e);
                break;
            case MENU:
                gamePanel.getGame().getMenuController().keyPressed(e);
                break;
            case PLAYING:
                gamePanel.getGame().getPlayingController().keyPressed(e);
                break;
        }
    }

    @Override
    public void keyReleased(java.awt.event.KeyEvent e) {
        switch (GameState.state) {
            case HOME:
                gamePanel.getGame().getHomeController().keyReleased(e);
                break;
            case MENU:
                gamePanel.getGame().getMenuController().keyReleased(e);
                break;
            case PLAYING:
                gamePanel.getGame().getPlayingController().keyReleased(e);
                break;
        }
    }
}
