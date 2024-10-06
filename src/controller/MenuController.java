package controller;

import controller.inputs.InputMethods;
import gameStates.GameState;
import gameStates.MenuModel;
import main.Game;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class MenuController implements InputMethods {
    private final Game game;
    private final MenuModel menuModel;

    public MenuController(Game game, MenuModel menuModel) {
        this.game = game;
        this.menuModel = menuModel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (menuModel.isUserSelectionOverlayActive()) {
            game.getMenuUserSelectionOverlayController().keyPressed(e);
            return;
        }

        if (menuModel.isUserCreationOverlayActive()) {
            game.getMenuUserCreationOverlayController().keyPressed(e);
            return;
        }

        if (menuModel.isScoreBoardOverlayActive()) {
            game.getMenuScoreBoardOverlayController().keyPressed(e);
            return;
        }

        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP, KeyEvent.VK_W:
                menuModel.decreaseSelectionIndex();
                if (menuModel.getSelectionIndex() < 0) {
                    menuModel.setSelectionIndex(3);
                }
                break;
            case KeyEvent.VK_DOWN, KeyEvent.VK_S:
                menuModel.increaseSelectionIndex();
                if (menuModel.getSelectionIndex() > 3) {
                    menuModel.setSelectionIndex(0);
                }
                break;

            case KeyEvent.VK_ENTER:
                switch (menuModel.getSelectionIndex()) {
                    case 0:
                        GameState.state = GameState.PLAYING;
                        break;
                    case 1:
                        // Change User
                        game.getMenuUserSelectionOverlayModel().updateUserList();
                        menuModel.setUserSelectionOverlayActive(true);
                        menuModel.setScoreBoardOverlayActive(false);
                        break;
                    case 2:
                        // Score Board
                        game.getMenuScoreBoardOverlayModel().updateUserScores();
                        menuModel.setUserSelectionOverlayActive(false);
                        menuModel.setScoreBoardOverlayActive(true);
                        break;
                    case 3:
                        // Quit
                        System.exit(0);
                        break;
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (menuModel.isUserSelectionOverlayActive())
            game.getMenuUserSelectionOverlayController().keyReleased(e);
        else if (menuModel.isUserCreationOverlayActive())
            game.getMenuUserCreationOverlayController().keyReleased(e);
        else if (menuModel.isScoreBoardOverlayActive())
            game.getMenuScoreBoardOverlayController().keyReleased(e);
    }
}
