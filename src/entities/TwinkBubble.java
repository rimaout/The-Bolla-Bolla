package entities;

import entities.Entity;
import gameStates.Home;
import main.Game;
import utilz.Constants;
import utilz.Constants.Direction;
import static utilz.Constants.Direction.*;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utilz.Constants.ANIMATION_SPEED;
import static utilz.Constants.Home.*;

public class TwinkBubble extends Entity {
    private Home home;

    private BufferedImage[] sprite;
    private Direction direction;

    public TwinkBubble(BufferedImage[] sprite, int startX, int startY, Home home) {
        super(startX, startY, BUBBLE_W, BUBBLE_H);
        this.sprite = sprite;
        this.home = home;

        // Randomize the animation index
        animationIndex = (int) (Math.random() * 4);

        // Randomize the direction (between right and left)
        direction = (Math.random() > 0.5) ? RIGHT : LEFT;
    }

    public void update() {
        updateAnimation();
        updatePosition();
    }

    private void updateAnimation() {
        animationTick++;
        if (animationTick > ANIMATION_SPEED) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= sprite.length) {
                animationIndex = 0;
            }
        }
    }

    private void updatePosition() {

        y -= BUBBLE_SPEED;

        if (home.getIsLogoInPosition()) {
            if (direction == RIGHT)
                x += BUBBLE_SPEED / 2;
            else
                x -= BUBBLE_SPEED / 2;
        }

        // Pacman effect
        if (x < 0)
            x = Game.GAME_WIDTH - BUBBLE_W;
        else if (x > Game.GAME_WIDTH - BUBBLE_W)
            x = 0;

        if (y < 0)
            y = Game.GAME_HEIGHT - BUBBLE_H;
        else if (y > Game.GAME_HEIGHT - BUBBLE_H)
            y = 0;

    }

    public void draw(Graphics2D g) {
        g.drawImage(sprite[animationIndex], (int) x, (int) y, BUBBLE_W , BUBBLE_H ,  null);
    }

    public boolean isOutOfScreen() {
        return y < -BUBBLE_H;
    }
}
