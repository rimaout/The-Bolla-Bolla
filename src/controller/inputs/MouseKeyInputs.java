package controller.inputs;

import main.GamePanel;
import model.gameStates.GameState;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseKeyInputs implements MouseListener{
    private GamePanel gamePanel;

    public MouseKeyInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        switch (GameState.state) {
            case MENU:
                gamePanel.getGame().getMenuController().mouseClicked(e);
                break;
            case PLAYING:
                gamePanel.getGame().getPlayingController().mouseClicked(e);
                break;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        switch (GameState.state) {
            case MENU:
                gamePanel.getGame().getMenuController().mousePressed(e);
                break;
            case PLAYING:
                gamePanel.getGame().getPlayingController().mousePressed(e);
                break;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        switch (GameState.state) {
            case MENU:
                gamePanel.getGame().getMenuController().mouseReleased(e);
                break;
            case PLAYING:
                gamePanel.getGame().getPlayingController().mouseReleased(e);
                break;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // not used
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // not used
    }
}
