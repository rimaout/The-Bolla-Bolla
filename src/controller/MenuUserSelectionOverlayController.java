package controller;

import gameStates.MenuModel;
import users.UsersManager;
import view.overlays.MenuUserSelectionOverlayModel;

import java.awt.event.KeyEvent;

public class MenuUserSelectionOverlayController {
    private MenuUserSelectionOverlayModel menuUserSelectionOverlayModel;
    private final MenuModel menuModel;
    private final UsersManager usersManager = UsersManager.getInstance();

    public MenuUserSelectionOverlayController(MenuModel menuModel, MenuUserSelectionOverlayModel menuUserSelectionOverlayModel) {
        this.menuModel = menuModel;
        this.menuUserSelectionOverlayModel = menuUserSelectionOverlayModel;
    }

    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE && usersManager.getCurrentUser() != null) {
            menuModel.setUserSelectionOverlayActive(false);
        }

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            usersManager.setCurrentUser(menuUserSelectionOverlayModel.getSelectedUser());
            menuModel.setUserSelectionOverlayActive(false);
        }

        if (e.getKeyCode() == KeyEvent.VK_C) {
            menuModel.setUserSelectionOverlayActive(false);
            menuModel.setUserCreationOverlayActive(true);
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            menuUserSelectionOverlayModel.setLeftArrowIndex(1); // animate the arrow
            int index = menuUserSelectionOverlayModel.getUsers().indexOf(menuUserSelectionOverlayModel.getSelectedUser());
            if (index == 0) {
                menuUserSelectionOverlayModel.setSelectedUser(menuUserSelectionOverlayModel.getUsers().getLast());
            } else {
                menuUserSelectionOverlayModel.setSelectedUser(menuUserSelectionOverlayModel.getUsers().get(index - 1));
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            menuUserSelectionOverlayModel.setRightArrowIndex(1); // animate the arrow
            int index = menuUserSelectionOverlayModel.getUsers().indexOf(menuUserSelectionOverlayModel.getSelectedUser());
            if (index == menuUserSelectionOverlayModel.getUsers().size() - 1) {
                menuUserSelectionOverlayModel.setSelectedUser(menuUserSelectionOverlayModel.getUsers().getFirst());
            } else {
                menuUserSelectionOverlayModel.setSelectedUser(menuUserSelectionOverlayModel.getUsers().get(index + 1));
            }
        }
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT)
            menuUserSelectionOverlayModel.setLeftArrowIndex(0); // stop animating the arrow

        if (e.getKeyCode() == KeyEvent.VK_RIGHT)
            menuUserSelectionOverlayModel.setRightArrowIndex(0); // stop animating the arrow
    }
}
