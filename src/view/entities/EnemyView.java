package view.entities;

import entities.Enemy;

public class EnemyView {
    private final Enemy enemy;

    public EnemyView(Enemy enemy) {
        this.enemy = enemy;
    }

    public void update() {

    }

    public void draw() {

    }

    public boolean isActive() {
        return enemy.isActive();
    }

    public boolean isAlive() {
        return enemy.isAlive();
    }
}
