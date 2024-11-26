package view.overlays;

import model.entities.EntityModel;
import model.utilz.Constants;
import model.utilz.Constants.Direction;
import static model.utilz.Constants.Direction.*;

import static model.utilz.Constants.Home.*;
import static model.utilz.Constants.ANIMATION_SPEED;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MenuTwinkleBubble extends EntityModel {
    private final MenuTwinkleBubbleManager manager;

    private BufferedImage[] sprite;
    private Direction direction;
    int animationIndex, animationTick;

    public MenuTwinkleBubble(BufferedImage[] sprite, int startX, int startY, MenuTwinkleBubbleManager manager) {
        super(startX, startY, BUBBLE_W, BUBBLE_H);
        this.sprite = sprite;
        this.manager = manager;

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

        if (manager.IsHomeLogoInPosition()) {
            if (direction == RIGHT)
                x += BUBBLE_SPEED / 2;
            else
                x -= BUBBLE_SPEED / 2;
        }

        // Pacman effect
        if (x < 0)
            x = Constants.GAME_WIDTH - BUBBLE_W;
        else if (x > Constants.GAME_WIDTH - BUBBLE_W)
            x = 0;

        if (y < 0)
            y = Constants.GAME_HEIGHT - BUBBLE_H;
        else if (y > Constants.GAME_HEIGHT - BUBBLE_H)
            y = 0;
    }

    public void draw(Graphics2D g) {
        g.drawImage(sprite[animationIndex], (int) x, (int) y, BUBBLE_W , BUBBLE_H ,  null);
    }
}