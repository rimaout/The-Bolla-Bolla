package controller.classes;

import controller.GameController;
import controller.inputs.InputMethods;
import model.gameStates.GameState;
import model.gameStates.MenuModel;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class MenuController implements InputMethods {
    private final GameController gameController;
    private final MenuModel menuModel;

    public MenuController(GameController gameController, MenuModel menuModel) {
        this.gameController = gameController;
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
            gameController.getMenuUserSelectionOverlayController().keyPressed(e);
            return;
        }

        if (menuModel.isUserCreationOverlayActive()) {
            gameController.getMenuUserCreationOverlayController().keyPressed(e);
            return;
        }

        if (menuModel.isScoreBoardOverlayActive()) {
            gameController.getMenuScoreBoardOverlayController().keyPressed(e);
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
                        gameController.getMenuUserSelectionOverlayModel().updateUserList();
                        menuModel.setUserSelectionOverlayActive(true);
                        menuModel.setScoreBoardOverlayActive(false);
                        break;
                    case 2:
                        // Score Board
                        gameController.getMenuScoreBoardOverlayModel().updateUserScores();
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
            gameController.getMenuUserSelectionOverlayController().keyReleased(e);
        else if (menuModel.isUserCreationOverlayActive())
            gameController.getMenuUserCreationOverlayController().keyReleased(e);
        else if (menuModel.isScoreBoardOverlayActive())
            gameController.getMenuScoreBoardOverlayController().keyReleased(e);
    }
}