package controller;

import model.gameStates.MenuModel;

import java.awt.event.KeyEvent;

public class MenuScoreBoardOverlayController {
    private final MenuModel menuModel;

    public MenuScoreBoardOverlayController(MenuModel menuModel) {
        this.menuModel = menuModel;
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            menuModel.setScoreBoardOverlayActive(false);
        }
    }

    public void keyReleased(KeyEvent e) {
        // not used
    }
}
