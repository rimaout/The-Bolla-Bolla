package controller;

import model.entities.PlayerModel;

import java.awt.event.KeyEvent;

public class PlayerController {

    private final PlayerModel playerModel;

    public PlayerController(PlayerModel playerModel) {
        this.playerModel = playerModel;
    }

    public void setAttackingAnimation(boolean attackingAnimation) {
        playerModel.setAttackingAnimation(attackingAnimation);
    }

    public void setRespawning(boolean respawning) {
        playerModel.setRespawning(respawning);
    }

    public void setCanRespawn(boolean canRespawn) {
        playerModel.setCanRespawn(canRespawn);
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
                playerModel.setLeft(true);
                break;
            case KeyEvent.VK_D:
                playerModel.setRight(true);
                break;
            case KeyEvent.VK_SPACE:
                playerModel.setJump(true);
                break;
            case KeyEvent.VK_ENTER:
                playerModel.setAttacking(true);
                break;
        }
    }

    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
                playerModel.setLeft(false);
                break;
            case KeyEvent.VK_D:
                playerModel.setRight(false);
                break;
            case KeyEvent.VK_SPACE:
                playerModel.setJump(false);
                break;
            case KeyEvent.VK_ENTER:
                playerModel.setAttacking(false);
                break;
        }
    }

}
