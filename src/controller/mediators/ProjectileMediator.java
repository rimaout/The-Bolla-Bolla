package controller.mediators;

import bubbles.playerBubbles.PlayerBubblesManager;
import model.projectiles.ProjectileModel;

public interface ProjectileMediator {

    static void deactivateProjectile(ProjectileModel projectileModel) {
        projectileModel.deactivate();
    }

    static void createEmptyBubbleFromPlayerProjectile(ProjectileModel projectileModel) {
        PlayerBubblesManager.getInstance().createEmptyBubble(projectileModel.getHitbox().x, projectileModel.getHitbox().y, projectileModel.getDirection());
    }
}
