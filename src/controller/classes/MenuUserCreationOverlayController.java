package controller.classes;

import model.gameStates.MenuModel;
import model.users.UsersManager;
import model.overlays.MenuUserCreationOverlayModel;
import view.overlays.MenuUserCreationOverlayView;

import java.awt.event.KeyEvent;

public class MenuUserCreationOverlayController {
    private final MenuModel menuModel;
    private final UsersManager usersManager = UsersManager.getInstance();
    private final MenuUserCreationOverlayModel menuUserCreationOverlayModel;
    private final MenuUserCreationOverlayView menuUserCreationOverlayView;

    public MenuUserCreationOverlayController(MenuModel menuModel, MenuUserCreationOverlayModel menuUserCreationOverlayModel, MenuUserCreationOverlayView menuUserCreationOverlayView) {
        this.menuModel = menuModel;
        this.menuUserCreationOverlayModel = menuUserCreationOverlayModel;
        this.menuUserCreationOverlayView = menuUserCreationOverlayView;
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            menuModel.setUserSelectionOverlayActive(true);
            menuModel.setUserCreationOverlayActive(false);
        }
        else if (e.getKeyCode() == KeyEvent.VK_ENTER && !menuUserCreationOverlayModel.isEnterKeyDeactivated()) {
            usersManager.createUser(menuUserCreationOverlayModel.getNewUserName(), menuUserCreationOverlayModel.getNewUserPictureIndex());
            menuModel.setUserSelectionOverlayActive(false);
            menuModel.setUserCreationOverlayActive(false);
        }
        else if (e.getKeyCode() == KeyEvent.VK_UP) {
            menuUserCreationOverlayView.setUpArrowIndex(1); // animate the arrow

            if (menuUserCreationOverlayModel.getNewUserPictureIndex() + 1 == usersManager.getUserPicturesCount())
                menuUserCreationOverlayModel.setNewUserPictureIndex(0);
            else
                menuUserCreationOverlayModel.increaseNewUserPictureIndex();
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            menuUserCreationOverlayView.setDownArrowIndex(1); // animate the arrow

            if (menuUserCreationOverlayModel.getNewUserPictureIndex() - 1 == -1 || menuUserCreationOverlayModel.getNewUserPictureIndex() - 1 == -2)
                menuUserCreationOverlayModel.setNewUserPictureIndex(usersManager.getUserPicturesCount() - 1);
            else
                menuUserCreationOverlayModel.decreaseNewUserPictureIndex();
        } else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            if (!menuUserCreationOverlayModel.getNewUserName().isEmpty()) {
                menuUserCreationOverlayModel.setNewUserName(menuUserCreationOverlayModel.getNewUserName().substring(0, menuUserCreationOverlayModel.getNewUserName().length() - 1));
            }
        } else {
            char keyChar = Character.toLowerCase(e.getKeyChar());
            if (Character.isLetterOrDigit(keyChar) && menuUserCreationOverlayModel.getNewUserName().length() < 8) {
                menuUserCreationOverlayModel.setNewUserName(menuUserCreationOverlayModel.getNewUserName() + keyChar);
            }
        }
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP)
            menuUserCreationOverlayView.setUpArrowIndex(0); // stop animating the arrow

        if (e.getKeyCode() == KeyEvent.VK_DOWN)
            menuUserCreationOverlayView.setDownArrowIndex(0); // stop animating the arrow
    }
}