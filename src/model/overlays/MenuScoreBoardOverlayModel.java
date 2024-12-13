package model.overlays;

import model.users.User;
import model.users.UsersManagerModel;
import model.gameStates.MenuModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class MenuScoreBoardOverlayModel extends MenuOverlayModel {
    private final UsersManagerModel usersManagerModel = UsersManagerModel.getInstance();
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
        orderedUsers = (ArrayList<User>) usersManagerModel.getUsers().stream()
                .sorted(Comparator.comparingInt(User::getBestScore).reversed())
                .collect(Collectors.toList());
    }

    // ------- getters and setters -------

    public ArrayList<User> getOrderedUsers() {
        return orderedUsers;
    }
}