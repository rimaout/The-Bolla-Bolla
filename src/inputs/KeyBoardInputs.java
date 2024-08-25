package inputs;

import gameStates.GameState;
import main.GamePanel;

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
                gamePanel.getGame().getHome().keyPressed(e);
                break;
            case MENU:
                gamePanel.getGame().getMenu().keyPressed(e);
                break;
            case PLAYING:
                gamePanel.getGame().getPlaying().keyPressed(e);
                break;
        }
    }

    @Override
    public void keyReleased(java.awt.event.KeyEvent e) {
        switch (GameState.state) {
            case HOME:
                gamePanel.getGame().getHome().keyReleased(e);
                break;
            case MENU:
                gamePanel.getGame().getMenu().keyReleased(e);
                break;
            case PLAYING:
                gamePanel.getGame().getPlaying().keyReleased(e);
                break;
        }
    }
}
