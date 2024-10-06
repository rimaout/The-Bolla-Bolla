package view.entities;

import java.util.ArrayList;

public class EnemyManagerView {
    private static EnemyManagerView instance;

    private ArrayList<EnemyView> enemiesViews;

    private EnemyManagerView() {}

    public static EnemyManagerView getInstance() {
        if (instance == null) {
            instance = new EnemyManagerView();
        }
        return instance;
    }

    public void update() {

    }
}
