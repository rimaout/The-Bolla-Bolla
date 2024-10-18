package controller.inputs;

import main.GamePanel;
import model.gameStates.GameState;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class MouseMotionInputs implements MouseMotionListener {
    private GamePanel gamePanel;

    public MouseMotionInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        switch (GameState.state) {

            case PLAYING:
                gamePanel.getGame().getPlayingController().mouseDragged(e);
                break;
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        switch (GameState.state) {
            case MENU:
                gamePanel.getGame().getMenuController().mouseMoved(e);
                break;
            case PLAYING:
                gamePanel.getGame().getPlayingController().mouseMoved(e);
                break;
        }
    }
}
