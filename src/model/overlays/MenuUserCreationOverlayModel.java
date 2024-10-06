package model.overlays;

import gameStates.MenuModel;
import users.UsersManager;

public class MenuUserCreationOverlayModel extends MenuOverlayModel {
    private final UsersManager usersManager = UsersManager.getInstance();

    private String newUserName = "";
    private int newUserPictureIndex = -1;

    private boolean userNameAlreadyExists = false;
    private boolean enterKeyDeactivated = true;

    public MenuUserCreationOverlayModel(MenuModel menuModel) {
        super(menuModel);
    }

    @Override
    public void update() {

        // Check if the username already exists
        userNameAlreadyExists = usersManager.doesUserAlreadyExist(newUserName);

        // update enterKeyStatus
        if (newUserName.isEmpty() || newUserPictureIndex == -1 || userNameAlreadyExists)
            enterKeyDeactivated = true;
        else
            enterKeyDeactivated = false;
    }

    public void setNewUserPictureIndex(int index){
        newUserPictureIndex = index;
    }

    public void increasNewUserPictureIndex(){
        newUserPictureIndex++;
    }

    public void decreaseNewUserPictureIndex(){
        newUserPictureIndex--;
    }

    public String getNewUserName(){
        return newUserName;
    }

    public void setNewUserName(String name){
        newUserName = name;
    }

    public boolean isEnterKeyDeactivated() {
        return enterKeyDeactivated;
    }

    public int getNewUserPictureIndex(){
        return newUserPictureIndex;
    }

    public boolean doesUserNameAlreadyExists() {
        return userNameAlreadyExists;
    }
}