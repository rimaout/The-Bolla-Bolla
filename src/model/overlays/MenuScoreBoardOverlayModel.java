package model.overlays;

import users.User;
import model.gameStates.MenuModel;
import users.UsersManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class MenuScoreBoardOverlayModel extends MenuOverlayModel {
    private final UsersManager usersManager = UsersManager.getInstance();
    private ArrayList<User> orderedUsers;


    public MenuScoreBoardOverlayModel(MenuModel menuModel) {
        super(menuModel);
        updateUserScores();
    }

    @Override
    public void update() {
        // not used
    }

    public void updateUserScores() {
        orderedUsers = (ArrayList<User>) usersManager.getUsers().stream()
                .sorted(Comparator.comparingInt(User::getBestScore).reversed())
                .collect(Collectors.toList());
    }

    // ------- getters and setters -------

    public ArrayList<User> getOrderedUsers() {
        return orderedUsers;
    }
}
