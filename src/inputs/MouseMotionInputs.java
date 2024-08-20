package inputs;

import gameStates.GameState;
import main.GamePanel;
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
                gamePanel.getGame().getPlaying().mouseDragged(e);
                break;
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        switch (GameState.state) {
            case MENU:
                gamePanel.getGame().getMenu().mouseMoved(e);
                break;
            case PLAYING:
                gamePanel.getGame().getPlaying().mouseMoved(e);
                break;
        }
    }
}
