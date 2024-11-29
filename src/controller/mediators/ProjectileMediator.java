package controller.mediators;

import model.bubbles.playerBubbles.PlayerBubblesManagerModel;
import model.projectiles.ProjectileModel;

public interface ProjectileMediator {

    static void deactivateProjectile(ProjectileModel projectileModel) {
        projectileModel.deactivate();
    }

    static void createEmptyBubbleFromPlayerProjectile(ProjectileModel projectileModel) {
        PlayerBubblesManagerModel.getInstance().createEmptyBubble(projectileModel.getHitbox().x, projectileModel.getHitbox().y, projectileModel.getDirection());
    }
}
