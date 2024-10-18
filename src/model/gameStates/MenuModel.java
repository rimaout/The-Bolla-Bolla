package model.gameStates;

import main.Game;
import model.users.UsersManager;

public class MenuModel extends State {
    private final UsersManager usersManager;

    private int selectionIndex = 0;

    // Overlays Booleans
    private boolean userSelectionOverlayActive = true;
    private boolean userCreationOverlayActive = false;
    private boolean scoreBoardOverlayActive = false;

    public MenuModel(Game game) {
        super(game);
        this.usersManager = UsersManager.getInstance();
    }

    public void update() {

        if (userSelectionOverlayActive)
            game.getMenuUserSelectionOverlayModel().update();
        if (userCreationOverlayActive)
            game.getMenuUserCreationOverlayModel().update();
        else if (scoreBoardOverlayActive)
            game.getMenuScoreBoardOverlayModel().update();
    }

    // ------------ Getters and Setters ------------

    public void setUserSelectionOverlayActive(boolean userSelectionOverlayActive) {
        this.userSelectionOverlayActive = userSelectionOverlayActive;
    }

    public void setUserCreationOverlayActive(boolean userCreationOverlayActive) {
        this.userCreationOverlayActive = userCreationOverlayActive;
    }

    public void setScoreBoardOverlayActive(boolean scoreBoardOverlayActive) {
        this.scoreBoardOverlayActive = scoreBoardOverlayActive;
    }

    public boolean isUserSelectionOverlayActive() {
        return userSelectionOverlayActive;
    }

    public boolean isUserCreationOverlayActive() {
        return userCreationOverlayActive;
    }

    public boolean isScoreBoardOverlayActive() {
        return scoreBoardOverlayActive;
    }


    public int getSelectionIndex() {
        return selectionIndex;
    }

    public void setSelectionIndex(int selectionIndex) {
        this.selectionIndex = selectionIndex;
    }

    public void increaseSelectionIndex() {
        selectionIndex++;
    }

    public void decreaseSelectionIndex() {
        selectionIndex--;
    }
}
