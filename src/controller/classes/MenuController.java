package controller.classes;

import controller.GameController;
import controller.inputs.InputMethods;
import model.gameStates.GameState;
import model.gameStates.MenuModel;
import view.gameStates.MenuView;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Controller for handling the user interaction with the main menu.
 */
public class MenuController implements InputMethods {
    private final MenuModel menuModel;

    private MenuUserCreationOverlayController menuUserCreationOverlayController;
    private MenuUserSelectionOverlayController menuUserSelectionOverlayController;
    private MenuScoreBoardOverlayController menuScoreBoardOverlayController;


    /**
     * Constructs a new MenuController.
     *
     * @param menuModel the model for the menu
     */
    public MenuController(MenuModel menuModel, MenuView menuView) {
        this.menuModel = menuModel;

        menuUserCreationOverlayController = new MenuUserCreationOverlayController(menuModel, menuView);
        menuUserSelectionOverlayController = new MenuUserSelectionOverlayController(menuModel, menuView);
        menuScoreBoardOverlayController = new MenuScoreBoardOverlayController(menuModel);
    }

    /**
     * {@inheritDoc} Overrides mouseClicked method from {@link InputMethods} interface.
     *
     * @param e the MouseEvent to handle
     *
     *
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        // not used
    }

    /**
     * {@inheritDoc} Overrides mousePressed method from {@link InputMethods} interface.
     *
     * @param e the MouseEvent to handle
     */
    @Override
    public void mousePressed(MouseEvent e) {
        // not used
    }

    /**
     * {@inheritDoc} Overrides mouseReleased method from {@link InputMethods} interface.
     *
     * @param e the MouseEvent to handle
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        // not used
    }

    /**
     * {@inheritDoc} Overrides mouseMoved method from {@link InputMethods} interface.
     *
     * @param e the MouseEvent to handle
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        // not used
    }

    /**
     * {@inheritDoc} Overrides mouseDragged method from {@link InputMethods} interface.
     *
     * @param e the MouseEvent to handle
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        // not used
    }

    /**
     * {@inheritDoc} Overrides keyPressed method from {@link InputMethods} interface.
     *
     * @param e the KeyEvent to handle
     */
    @Override
    public void keyPressed(KeyEvent e) {

        if (menuModel.isUserSelectionOverlayActive()) {
            menuUserSelectionOverlayController.keyPressed(e);
            return;
        }

        if (menuModel.isUserCreationOverlayActive()) {
            menuUserCreationOverlayController.keyPressed(e);
            return;
        }

        if (menuModel.isScoreBoardOverlayActive()) {
            menuScoreBoardOverlayController.keyPressed(e);
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
                        menuModel.getMenuUserSelectionOverlayModel().updateUserList();
                        menuModel.setUserSelectionOverlayActive(true);
                        menuModel.setScoreBoardOverlayActive(false);
                        break;
                    case 2:
                        // Score Board
                        menuModel.getMenuScoreBoardOverlayModel().updateUserScores();
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

    /**
     * {@inheritDoc} Overrides keyReleased method from {@link InputMethods} interface.
     *
     * @param e the KeyEvent to handle
     */
    @Override
    public void keyReleased(KeyEvent e) {
        if (menuModel.isUserSelectionOverlayActive())
            menuUserSelectionOverlayController.keyReleased(e);
        else if (menuModel.isUserCreationOverlayActive())
            menuUserCreationOverlayController.keyReleased(e);
        else if (menuModel.isScoreBoardOverlayActive())
            menuScoreBoardOverlayController.keyReleased(e);
    }
}