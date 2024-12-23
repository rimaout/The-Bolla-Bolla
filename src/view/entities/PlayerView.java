package view.entities;

import view.utilz.Load;
import view.audio.AudioPlayer;
import model.entities.PlayerModel;

import java.awt.*;
import java.awt.image.BufferedImage;

import static model.utilz.Constants.PlayerConstants.*;
import static model.utilz.Constants.PlayerConstants.DEFAULT_H;
import static view.utilz.Constants.ANIMATION_SPEED;
import static view.utilz.Constants.PlayerConstants.*;
import static view.utilz.Constants.AudioConstants.JUMP;
import static view.utilz.Constants.AudioConstants.PLAYER_DEATH;

/**
 * The PlayerView class represents the view for a {@link PlayerModel}.
 * It handles updating the player's animation, drawing the player, and playing sound effects.
 */
public class PlayerView{
    private final PlayerModel playerModel;

    private BufferedImage[][] sprites;
    private int animationIndex, animationTick;
    private int playerAnimation = IDLE_ANIMATION;
    private boolean activateAttackingAnimation;
    private boolean jumpSoundAlreadyPlayed, deathSoundAlreadyPlayed;

    /**
     * Constructs a PlayerView with the specified PlayerModel.
     *
     * @param playerModel the model for the player entity
     */
    public PlayerView(PlayerModel playerModel) {
        this.playerModel = playerModel;

        loadAnimation();
    }

    /**
     * Updates the player's animation and sound effects.
     */
    public void update() {
        updateAnimationTick();
        setAnimation();
        updateSound();
    }

    /**
     * Draws the player on the screen.
     *
     * @param g the Graphics2D object to draw with
     */
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

    /**
     * Updates the sound effects for the player.
     */
    private void updateSound() {

        // Jump sound Logic
        if (playerModel.getIsJumping() && !jumpSoundAlreadyPlayed) {
            AudioPlayer.getInstance().playSoundEffect(JUMP);
            jumpSoundAlreadyPlayed = true;
        }
        if (!playerModel.getIsJumping())
            jumpSoundAlreadyPlayed = false;

        // Death sound Logic
        if (playerModel.isRespawning() && !deathSoundAlreadyPlayed) {
            AudioPlayer.getInstance().playSoundEffect(PLAYER_DEATH);
            deathSoundAlreadyPlayed = true;
        }
        if (!playerModel.isRespawning())
            deathSoundAlreadyPlayed = false;
    }

    /**
     * Updates the animation tick and index based on the animation speed.
     */
    private void updateAnimationTick() {
        animationTick++;
        if (animationTick > ANIMATION_SPEED) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= getSpriteAmount(playerAnimation)) {
                animationIndex = 0;
                activateAttackingAnimation = false;
            }
        }
    }

    /**
     * Sets the current animation for the player based on its state.
     */
    private void setAnimation() {
        int startAnimation = playerAnimation;

        if (playerModel.isAttacking() && playerModel.canAttack())
            activateAttackingAnimation = true;

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

        if (activateAttackingAnimation)
            playerAnimation = ATTACK_ANIMATION;

        if (playerModel.isRespawning())
            playerAnimation = DEAD_ANIMATION;

        if (startAnimation != playerAnimation){
            animationTick = 0;
            animationIndex = 0;
        }
    }

    /**
     * Loads the animation sprites for the player.
     */
    private void loadAnimation() {
        BufferedImage img = Load.GetSprite(Load.PLAYER_SPRITE);

        sprites = new BufferedImage[6][7];
        for (int j = 0; j < sprites.length; j++)
            for (int i = 0; i < sprites[j].length; i++)
                sprites[j][i] = img.getSubimage(i * DEFAULT_W, j* DEFAULT_H, DEFAULT_W, DEFAULT_H);
    }
}