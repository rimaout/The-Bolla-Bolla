package com.rima.model.overlays;

import com.rima.model.users.User;
import com.rima.model.users.UsersManagerModel;
import com.rima.model.gameStates.MenuModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * Represents the menu overlay model used for the scoreboard.
 */
public class MenuScoreBoardOverlayModel extends MenuOverlayModel {
    private final UsersManagerModel usersManagerModel = UsersManagerModel.getInstance();
    private ArrayList<User> orderedUsers;

    /**
     * Constructs a MenuScoreBoardOverlayModel with the specified MenuModel.
     *
     * @param menuModel the MenuModel associated with this overlay
     */
    public MenuScoreBoardOverlayModel(MenuModel menuModel) {
        super(menuModel);
        updateUserScores();
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
     * Updates the user scores by getting the list of users from the UsersManagerModel,
     * sorting them by their best score in descending order, and saving the result in the orderedUsers variable.
     */
    public void updateUserScores() {
        orderedUsers = (ArrayList<User>) usersManagerModel.getUsers().stream()
                .sorted(Comparator.comparingInt(User::getBestScore).reversed())
                .collect(Collectors.toList());
    }

    // ------- getters and setters -------

    /**
     * Returns the list of users ordered by their best scores.
     *
     * @return an ArrayList of users ordered by their best scores
     */
    public ArrayList<User> getOrderedUsers() {
        return orderedUsers;
    }
}