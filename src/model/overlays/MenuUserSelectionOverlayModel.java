package model.overlays;

import model.users.User;
import model.users.UsersManagerModel;
import model.gameStates.MenuModel;

import java.util.ArrayList;

/**
 * Represents the menu overlay model used for user selection.
 */
public class MenuUserSelectionOverlayModel extends MenuOverlayModel {
    private final UsersManagerModel usersManagerModel = UsersManagerModel.getInstance();
    private User selectedUser;
    private ArrayList<User> users;

    /**
     * Constructs a MenuUserSelectionOverlayModel with the specified MenuModel.
     *
     * @param menuModel the MenuModel associated with this overlay
     */
    public MenuUserSelectionOverlayModel(MenuModel menuModel) {
        super(menuModel);
        updateUserList();
    }

    /**
     * Updates the state of the menu overlay.
     * <p>
     * This method is not used in this class.
     */
    @Override
    public void update() {
        // not used
    }

    /**
     * Updates the user list by getting the list of users from the UsersManagerModel
     * and setting the selected user to the first user in the list.
     */
    public void updateUserList(){
        users = usersManagerModel.getUsers();
        selectedUser = users.getFirst();
    }

    // -------------- Getters and Setters --------------

    /**
     * Returns the selected user.
     *
     * @return the selected user
     */
    public User getSelectedUser() {
        return selectedUser;
    }

    /**
     * Sets the selected user.
     *
     * @param selectedUser the user to be selected
     */
    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }

    /**
     * Returns the list of users.
     *
     * @return an ArrayList of users
     */
    public ArrayList<User> getUsers() {
        return users;
    }

    /**
     * Sets the list of users.
     *
     * @param users the list of users to be set
     */
    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }
}