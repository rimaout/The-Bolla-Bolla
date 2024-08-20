package bubbles;

import entities.Enemy;
import entities.EnemyManager;
import utilz.Constants;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utilz.Constants.PlayerBubble.*;

public class EnemyBubbles extends PlayerBubble{
    Enemy enemy;

    public EnemyBubbles(float x, float y, Constants.Direction direction, int[][] levelData, Constants.Direction[][] windLevelData, Enemy enemy) {
        super(x, y, direction, levelData, windLevelData);
        this.state = NORMAL;
        this.enemy = enemy;
    }

    @Override
    public void draw(Graphics g) {
        BufferedImage[][] enemyBubbleSprites = EnemyManager.getInstance().getEnemySprite(enemy.getEnemyType());
        g.drawImage(enemyBubbleSprites[state][animationIndex], (int) (hitbox.x - HITBOX_OFFSET_X), (int) (hitbox.y - HITBOX_OFFSET_Y), IMMAGE_W, IMMAGE_H, null);
    }

    @Override
    public void playerPop() {
        if (state != PROJECTILE && state != POP_NORMAL && state != POP_RED) {
            super.playerPop();
            enemy.setAlive(false);
        }
    }

    @Override
    public void timePop() {
        active = false;
    }
}
