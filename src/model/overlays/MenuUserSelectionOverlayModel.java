package model.overlays;

import model.users.User;
import model.gameStates.MenuModel;
import model.users.UsersManager;

import java.util.ArrayList;

public class MenuUserSelectionOverlayModel extends MenuOverlayModel {
    private final UsersManager usersManager = UsersManager.getInstance();
    private User selectedUser;
    private ArrayList<User> users;

    public MenuUserSelectionOverlayModel(MenuModel menuModel) {
        super(menuModel);
        updateUserList();
    }

    @Override
    public void update() {
        // not used
    }

    public void updateUserList(){
        users = usersManager.getUsers();
        selectedUser = users.getFirst();
    }

    // -------------- Getters and Setters --------------
    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }
}
