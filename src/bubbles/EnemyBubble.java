package bubbles;

import entities.Enemy;
import entities.EnemyManager;
import utilz.Constants.Direction;

import static utilz.Constants.EnemyConstants.*;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utilz.Constants.Bubble.*;

public class EnemyBubble extends PlayerBubble{
    Enemy enemy;
    private boolean playerPopped;

    public EnemyBubble(float x, float y, Direction direction, int[][] levelData, Direction[][] windLevelData, Enemy enemy) {
        super(x, y, direction, levelData, windLevelData);
        this.state = NORMAL;
        this.enemy = enemy;
    }

    @Override
    public void draw(Graphics g) {
        BufferedImage[][] enemyBubbleSprites = EnemyManager.getInstance().getEnemySprite(enemy.getEnemyType());

        if (state == NORMAL)
            g.drawImage(enemyBubbleSprites[BOBBLE_GREEN_ANIMATION][animationIndex], (int) (hitbox.x - HITBOX_OFFSET_X), (int) (hitbox.y - HITBOX_OFFSET_Y), IMMAGE_W, IMMAGE_H, null);

        else if (state == RED || state == BLINKING)
            g.drawImage(enemyBubbleSprites[BOBBLE_RED_ANIMATION][animationIndex], (int) (hitbox.x - HITBOX_OFFSET_X), (int) (hitbox.y - HITBOX_OFFSET_Y), IMMAGE_W, IMMAGE_H, null);

        else if (state == POP_NORMAL)
            g.drawImage(enemyBubbleSprites[BOBBLE_GREEN_POP_ANIMATION][animationIndex], (int) (hitbox.x - HITBOX_OFFSET_X), (int) (hitbox.y - HITBOX_OFFSET_Y), IMMAGE_W, IMMAGE_H, null);

        else if (state == POP_RED)
            g.drawImage(enemyBubbleSprites[BOBBLE_RED_POP_ANIMATION][animationIndex], (int) (hitbox.x - HITBOX_OFFSET_X), (int) (hitbox.y - HITBOX_OFFSET_Y), IMMAGE_W, IMMAGE_H, null);
    }


    public void respawnEnemy() {
        if (!playerPopped) {
            enemy.getHitbox().x = hitbox.x;
            enemy.getHitbox().y = hitbox.y;
            enemy.setEnemyState(HUNGRY_STATE);
            enemy.setActive(true);
        }
    }

    @Override
    public void playerPop() {
        playerPopped = true;

        if (state != PROJECTILE && state != POP_NORMAL && state != POP_RED) {
            if (state == RED)
                state = POP_RED;
            else
                state = POP_NORMAL;

            super.playerPop();
            enemy.setAlive(false);
        }
    }
}
