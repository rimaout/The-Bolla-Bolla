package entities;

import main.Game;

import static Utillz.Constants.EnemyConstants.*;

public class ZenChan extends Enemy {
    public ZenChan(float x, float y) {
        super(x, y, ENEMY_WIDTH, ENEMY_HEIGHT, ZEN_CHAN);
        initHitbox(x, y, ZEN_CHAN_HITBOX_WIDTH , ZEN_CHAN_HITBOX_HEIGHT);
    }
}
