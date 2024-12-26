package model.gameStates;

import model.overlays.MenuScoreBoardOverlayModel;
import model.overlays.MenuUserCreationOverlayModel;
import model.overlays.MenuUserSelectionOverlayModel;

/**
 * The MenuModel class represents the model for the menu state in the game.
 * It manages the different overlays and their states.
 */
public class MenuModel {
    private MenuUserCreationOverlayModel menuUserCreationOverlayModel;
    private MenuUserSelectionOverlayModel menuUserSelectionOverlayModel;
    private MenuScoreBoardOverlayModel menuScoreBoardOverlayModel;
    private int selectionIndex = 0;

    // Overlays Booleans
    private boolean userSelectionOverlayActive = true;
    private boolean userCreationOverlayActive = false;
    private boolean scoreBoardOverlayActive = false;

    /**
     * Constructs a MenuModel and initializes the overlay models.
     */
    public MenuModel() {
        menuUserCreationOverlayModel = new MenuUserCreationOverlayModel(this);
        menuUserSelectionOverlayModel = new MenuUserSelectionOverlayModel(this);
        menuScoreBoardOverlayModel = new MenuScoreBoardOverlayModel(this);
    }

    /**
     * Updates the active overlay model.
     */
    public void update() {
        if (userSelectionOverlayActive)
            menuUserSelectionOverlayModel.update();
        if (userCreationOverlayActive)
            menuUserCreationOverlayModel.update();
        else if (scoreBoardOverlayActive)
            menuScoreBoardOverlayModel.update();
    }

    // ------------ Getters and Setters ------------

    /**
     * Sets the user selection overlay active state.
     *
     * @param userSelectionOverlayActive the new state of the user selection overlay
     */
    public void setUserSelectionOverlayActive(boolean userSelectionOverlayActive) {
        this.userSelectionOverlayActive = userSelectionOverlayActive;
    }

    /**
     * Sets the user creation overlay active state.
     *
     * @param userCreationOverlayActive the new state of the user creation overlay
     */
    public void setUserCreationOverlayActive(boolean userCreationOverlayActive) {
        this.userCreationOverlayActive = userCreationOverlayActive;
    }

    /**
     * Sets the score board overlay active state.
     *
     * @param scoreBoardOverlayActive the new state of the score board overlay
     */
    public void setScoreBoardOverlayActive(boolean scoreBoardOverlayActive) {
        this.scoreBoardOverlayActive = scoreBoardOverlayActive;
    }

    /**
     * Checks if the user selection overlay is active.
     *
     * @return true if the user selection overlay is active, false otherwise
     */
    public boolean isUserSelectionOverlayActive() {
        return userSelectionOverlayActive;
    }

    /**
     * Checks if the user creation overlay is active.
     *
     * @return true if the user creation overlay is active, false otherwise
     */
    public boolean isUserCreationOverlayActive() {
        return userCreationOverlayActive;
    }

    /**
     * Checks if the score board overlay is active.
     *
     * @return true if the score board overlay is active, false otherwise
     */
    public boolean isScoreBoardOverlayActive() {
        return scoreBoardOverlayActive;
    }

    /**
     * Returns the current selection index.
     *
     * @return the current selection index
     */
    public int getSelectionIndex() {
        return selectionIndex;
    }

    /**
     * Sets the selection index.
     *
     * @param selectionIndex the new selection index
     */
    public void setSelectionIndex(int selectionIndex) {
        this.selectionIndex = selectionIndex;
    }

    /**
     * Increases the selection index by one.
     */
    public void increaseSelectionIndex() {
        selectionIndex++;
    }

    /**
     * Decreases the selection index by one.
     */
    public void decreaseSelectionIndex() {
        selectionIndex--;
    }

    /**
     * Returns the MenuUserCreationOverlayModel instance.
     *
     * @return the MenuUserCreationOverlayModel instance
     */
    public MenuUserCreationOverlayModel getMenuUserCreationOverlayModel() {
        return menuUserCreationOverlayModel;
    }

    /**
     * Returns the MenuUserSelectionOverlayModel instance.
     *
     * @return the MenuUserSelectionOverlayModel instance
     */
    public MenuUserSelectionOverlayModel getMenuUserSelectionOverlayModel() {
        return menuUserSelectionOverlayModel;
    }

    /**
     * Returns the MenuScoreBoardOverlayModel instance.
     *
     * @return the MenuScoreBoardOverlayModel instance
     */
    public MenuScoreBoardOverlayModel getMenuScoreBoardOverlayModel() {
        return menuScoreBoardOverlayModel;
    }
}