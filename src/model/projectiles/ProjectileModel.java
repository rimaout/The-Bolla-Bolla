package model.projectiles;

import model.entities.EnemyModel;
import model.entities.EntityModel;
import model.entities.PlayerModel;
import model.utilz.Constants.Direction;
import model.utilz.Constants.Projectiles.ProjectileState;

import static model.utilz.Constants.Projectiles.*;
import static model.utilz.Constants.Projectiles.ProjectileState.MOVING;

public abstract class ProjectileModel extends EntityModel {
    protected final ProjectileManagerModel projectileManagerModel = ProjectileManagerModel.getInstance();

    protected ProjectileState state = MOVING;
    protected ProjectileType type;
    protected Direction direction;

    public ProjectileModel(float x, float y, Direction direction, ProjectileType type) {
        super(x, y, H, W);
        this.direction = direction;
        this.type = type;

        initHitbox(HITBOX_W, HITBOX_H);
    }

    public void update() {
        updatePos();
    }

    protected abstract void updatePos();
    protected abstract void checkEnemyHit(EnemyModel enemyModel, PlayerModel playerModel);
    protected abstract void checkPlayerHit(PlayerModel playerModel);

    public ProjectileState getState() {
        return state;
    }

    public ProjectileType getType() {
        return type;
    }

    public Direction getDirection() {
        return direction;
    }
}
