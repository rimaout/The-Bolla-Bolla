package controller.classes;

import model.gameStates.MenuModel;
import model.users.UsersManager;
import model.overlays.MenuUserSelectionOverlayModel;
import view.overlays.MenuUserSelectionOverlayView;

import java.awt.event.KeyEvent;

public class MenuUserSelectionOverlayController {
    private final MenuUserSelectionOverlayModel menuUserSelectionOverlayModel;
    private final MenuUserSelectionOverlayView menuUserSelectionOverlayView;
    private final MenuModel menuModel;
    private final UsersManager usersManager = UsersManager.getInstance();

    public MenuUserSelectionOverlayController(MenuModel menuModel, MenuUserSelectionOverlayModel menuUserSelectionOverlayModel, MenuUserSelectionOverlayView menuUserSelectionOverlayView) {
        this.menuModel = menuModel;
        this.menuUserSelectionOverlayModel = menuUserSelectionOverlayModel;
        this.menuUserSelectionOverlayView = menuUserSelectionOverlayView;
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
            menuUserSelectionOverlayView.setLeftArrowIndex(1); // animate the arrow
            int index = menuUserSelectionOverlayModel.getUsers().indexOf(menuUserSelectionOverlayModel.getSelectedUser());
            if (index == 0) {
                menuUserSelectionOverlayModel.setSelectedUser(menuUserSelectionOverlayModel.getUsers().getLast());
            } else {
                menuUserSelectionOverlayModel.setSelectedUser(menuUserSelectionOverlayModel.getUsers().get(index - 1));
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            menuUserSelectionOverlayView.setRightArrowIndex(1); // animate the arrow
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
            menuUserSelectionOverlayView.setLeftArrowIndex(0); // stop animating the arrow

        if (e.getKeyCode() == KeyEvent.VK_RIGHT)
            menuUserSelectionOverlayView.setRightArrowIndex(0); // stop animating the arrow
    }
}
