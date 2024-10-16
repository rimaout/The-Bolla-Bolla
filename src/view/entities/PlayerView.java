package view.entities;

import entities.Player;
import model.utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static model.utilz.Constants.ANIMATION_SPEED;
import static model.utilz.Constants.PlayerConstants.*;
import static model.utilz.Constants.PlayerConstants.DEFAULT_H;

public class PlayerView {
    private final Player player;

    private BufferedImage[][] sprites;
    private int animationIndex, animationTick;
    private int playerAnimation = IDLE_ANIMATION;

    public PlayerView(Player player) {
        this.player = player;
        loadAnimation();
    }

    public void update() {
        updateAnimationTick();
        setAnimation();
    }

    public void draw(Graphics2D g) {

        if (player.isImmune() && !player.isRespawning()) {
            if (player.getImmuneTimer() % 100 < 40) { // Transparency blink effect
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.55F)); // Set transparency
                g.drawImage(sprites[playerAnimation][animationIndex], (int) (player.getHitbox().x - OFFSET_X) + player.getFlipX(), (int) (player.getHitbox().y - OFFSET_Y), player.getWidth() * player.getFlipW(), player.getHeight(), null);
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1F)); // Set transparency
                return;
            }
        }

        g.drawImage(sprites[playerAnimation][animationIndex],  (int) (player.getHitbox().x - OFFSET_X) + player.getFlipX(), (int) (player.getHitbox().y - OFFSET_Y), player.getWidth() * player.getFlipW(), player.getHeight(), null);
    }

    private void updateAnimationTick() {
        animationTick++;
        if (animationTick > ANIMATION_SPEED) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= getSpriteAmount(playerAnimation)) {
                animationIndex = 0;

                // todo: use observer pattern to notify the player that the animation has ended
                player.setAttackingAnimation(false);
                player.setRespawning(false);
            }
        }

        // todo: use observer pattern to notify the player that the animation has ended
        if (animationIndex == getSpriteAmount(DEAD_ANIMATION)-1)
            player.setCanRespawn(true);
        else
            player.setCanRespawn(false);
    }

    private void setAnimation() {
        int startAnimation = playerAnimation;

        if (player.isMoving())
            playerAnimation = RUNNING_ANIMATION;
        else
            playerAnimation = IDLE_ANIMATION;

        if (player.isInAir()) {
            if (player.getAirSpeed() < 0)
                playerAnimation = JUMPING_ANIMATION;
            else
                playerAnimation = FALLING_ANIMATION;
        }

        if (player.isAttackingAnimation())
            playerAnimation = ATTACK_ANIMATION;

        if (player.isRespawning())
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