package view.entities;

import entities.Enemy;

public class EnemyView {
    private final Enemy enemyModel;

    public EnemyView(Enemy enemyModel) {
        this.enemyModel = enemyModel;
    }

    public void update() {

    }

    public void draw() {

    }

    public boolean isActive() {
        return enemyModel.isActive();
    }

    public boolean isAlive() {
        return enemyModel.isAlive();
    }
}
