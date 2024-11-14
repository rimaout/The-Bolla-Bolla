package controller.mediators;

import model.entities.PlayerModel;

public interface PlayerMediator {
    // PlayerMediator is an interface that acts as a mediator between the player view and the player model.

    static void deactivateRespawning(PlayerModel playerModel) {
        playerModel.deactivateRespawning();
    }

    static void activateCanRespawn(PlayerModel playerModel) {
        playerModel.activateCanRespawn();
    }

    static void deactivateCanRespawn(PlayerModel playerModel) {
        playerModel.deactivateCanRespawn();
    }
}