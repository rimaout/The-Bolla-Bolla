package overlays;

import gameStates.Menu;

import java.awt.*;
import java.awt.event.KeyEvent;

public class MenuLeaderBoardOverlay extends MenuOverlay {

    public MenuLeaderBoardOverlay(Menu menu) {
        super(menu);
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Graphics g) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE || e.getKeyCode() == KeyEvent.VK_Q) {
            menu.setUserSelectionOverlay(false);
            menu.setScoreBoardOverlay(false);
        }
    }

    public void updateLeaderBoard() {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
