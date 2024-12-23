package model.overlays;

import model.gameStates.MenuModel;
import model.users.UsersManagerModel;

/**
 * Represents the menu overlay model used for user creation overlay.
 */
public class MenuUserCreationOverlayModel extends MenuOverlayModel {
    private final UsersManagerModel usersManagerModel = UsersManagerModel.getInstance();

    private String newUserName = "";
    private int newUserPictureIndex = -1;
    private boolean enterNameDeactivated = true;
    private boolean userNameAlreadyExists = false;

    /**
     * Constructs a MenuUserCreationOverlayModel with the specified MenuModel.
     *
     * @param menuModel the MenuModel associated with this overlay
     */
    public MenuUserCreationOverlayModel(MenuModel menuModel) {
        super(menuModel);
    }

    /**
     * Updates the state of the menu overlay.
     * <p>
     * Checks if the username already exists and updates the name entering status.
     */
    @Override
    public void update() {

        // Check if the username already exists
        userNameAlreadyExists = usersManagerModel.doesUserAlreadyExist(newUserName);

        // update enterStatus
        if (newUserName.isEmpty() || newUserPictureIndex == -1 || userNameAlreadyExists)
            enterNameDeactivated = true;
        else
            enterNameDeactivated = false;
    }

    /**
     * Sets the new user picture index.
     *
     * @param index the index of the new user picture
     */
    public void setNewUserPictureIndex(int index){
        newUserPictureIndex = index;
    }

    /**
     * Increases the new user picture index by one.
     */
    public void increaseNewUserPictureIndex(){
        newUserPictureIndex++;
    }

    /**
     * Decreases the new user picture index by one.
     */
    public void decreaseNewUserPictureIndex(){
        newUserPictureIndex--;
    }

    /**
     * Returns the new username.
     *
     * @return the new username
     */
    public String getNewUserName(){
        return newUserName;
    }

    /**
     * Sets the new username.
     *
     * @param name the new username
     */
    public void setNewUserName(String name){
        newUserName = name;
    }

    /**
     * Checks if the enter key is deactivated.
     *
     * @return true if the enter name is deactivated, false otherwise
     */
    public boolean isEnterNameDeactivated() {
        return enterNameDeactivated;
    }

    /**
     * Returns the new user picture index.
     *
     * @return the new user picture index
     */
    public int getNewUserPictureIndex(){
        return newUserPictureIndex;
    }

    /**
     * Checks if the username already exists.
     *
     * @return true if the username already exists, false otherwise
     */
    public boolean doesUserNameAlreadyExists() {
        return userNameAlreadyExists;
    }
}