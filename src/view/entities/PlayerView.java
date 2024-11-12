package view.entities;

import controller.PlayerController;
import model.entities.PlayerModel;
import model.utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static model.utilz.Constants.ANIMATION_SPEED;
import static model.utilz.Constants.PlayerConstants.*;
import static model.utilz.Constants.PlayerConstants.DEFAULT_H;

public class PlayerView {
    private final PlayerModel playerModel;
    private final PlayerController playerController;

    private BufferedImage[][] sprites;
    private int animationIndex, animationTick;
    private int playerAnimation = IDLE_ANIMATION;

    public PlayerView(PlayerModel playerModel, PlayerController playerController) {
        this.playerModel = playerModel;
        this.playerController = playerController;

        loadAnimation();
    }

    public void update() {
        updateAnimationTick();
        setAnimation();
    }

    public void draw(Graphics2D g) {

        if (playerModel.isImmune() && !playerModel.isRespawning()) {
            if (playerModel.getImmuneTimer() % 100 < 40) { // Transparency blink effect
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.55F)); // Set transparency
                g.drawImage(sprites[playerAnimation][animationIndex], (int) (playerModel.getHitbox().x - OFFSET_X) + playerModel.getFlipX(), (int) (playerModel.getHitbox().y - OFFSET_Y), playerModel.getWidth() * playerModel.getFlipW(), playerModel.getHeight(), null);
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1F)); // Set transparency
                return;
            }
        }

        g.drawImage(sprites[playerAnimation][animationIndex],  (int) (playerModel.getHitbox().x - OFFSET_X) + playerModel.getFlipX(), (int) (playerModel.getHitbox().y - OFFSET_Y), playerModel.getWidth() * playerModel.getFlipW(), playerModel.getHeight(), null);
    }

    private void updateAnimationTick() {
        animationTick++;
        if (animationTick > ANIMATION_SPEED) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= getSpriteAmount(playerAnimation)) {
                animationIndex = 0;

                playerController.setAttackingAnimation(false);
                playerController.setRespawning(false);
            }
        }

        if (animationIndex == getSpriteAmount(DEAD_ANIMATION)-1)
            playerController.setCanRespawn(true);
        else
            playerController.setCanRespawn(false);
    }

    private void setAnimation() {
        int startAnimation = playerAnimation;

        if (playerModel.isMoving())
            playerAnimation = RUNNING_ANIMATION;
        else
            playerAnimation = IDLE_ANIMATION;

        if (playerModel.isInAir()) {
            if (playerModel.getAirSpeed() < 0)
                playerAnimation = JUMPING_ANIMATION;
            else
                playerAnimation = FALLING_ANIMATION;
        }

        if (playerModel.isAttackingAnimation())
            playerAnimation = ATTACK_ANIMATION;

        if (playerModel.isRespawning())
            playerAnimation = DEAD_ANIMATION;

        if (startAnimation != playerAnimation){
            animationTick = 0;
            animationIndex = 0;
        }
    }

    private void loadAnimation() {
        BufferedImage img = LoadSave.GetSprite(LoadSave.PLAYER_SPRITE);

        sprites = new BufferedImage[6][7];
        for (int j = 0; j < sprites.length; j++)
            for (int i = 0; i < sprites[j].length; i++)
                sprites[j][i] = img.getSubimage(i * DEFAULT_W, j* DEFAULT_H, DEFAULT_W, DEFAULT_H);
    }
}