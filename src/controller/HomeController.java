package controller;

import controller.inputs.InputMethods;
import gameStates.GameState;
import view.gameStates.HomeView;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class HomeController implements InputMethods {
    private final HomeView homeView;

    public HomeController(HomeView homeView) {
        this.homeView = homeView;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // Not used
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Not used
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // Not used
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // Not used
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // Not used
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (homeView.IsLogoInPosition())
            if (e.getKeyCode() == KeyEvent.VK_ENTER)
                GameState.state = GameState.MENU;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Not used
    }

}
