package view.overlays.menuOverlays;

import model.entities.EntityModel;
import model.utilz.Constants;
import model.utilz.Constants.Direction;
import static model.utilz.Constants.Direction.*;
import static view.utilz.Constants.Home.*;
import static view.utilz.Constants.ANIMATION_SPEED;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The MenuTwinkleBubble class represents a twinkling bubble in the menu.
 *
 * <p> Many twinkling bubbles are used as a background effect in the menus.
 *
 * <p>Thi class handles the animation and movement of the bubble.
 */
public class MenuTwinkleBubble extends EntityModel {
    private final MenuTwinkleBubbleManager manager;

    private BufferedImage[] sprite;
    private Direction direction;
    int animationIndex, animationTick;

    /**
     * Constructs a MenuTwinkleBubble with the specified sprite, starting position, and manager.
     * Randomizes the initial animation index and direction.
     *
     * @param sprite the array of images for the bubble animation
     * @param startX the starting x-coordinate of the bubble
     * @param startY the starting y-coordinate of the bubble
     * @param manager the manager that handles the bubble
     */
    public MenuTwinkleBubble(BufferedImage[] sprite, int startX, int startY, MenuTwinkleBubbleManager manager) {
        super(startX, startY, BUBBLE_W, BUBBLE_H);
        this.sprite = sprite;
        this.manager = manager;

        // Randomize the animation index
        animationIndex = (int) (Math.random() * 4);

        // Randomize the direction (between right and left)
        direction = (Math.random() > 0.5) ? RIGHT : LEFT;
    }

    /**
     * Updates the bubble's animation and position.
     */
    public void update() {
        updateAnimation();
        updatePosition();
    }

    /**
     * Updates the bubble's animation by incrementing the animation tick and index.
     * Resets the animation index if it exceeds the length of the sprite array.
     */
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

    /**
     * Updates the bubble's position based on its direction and speed.
     * Implements a "Pacman effect" where the bubble teleports around the screen edges to the opposite edge.
     */
    private void updatePosition() {
        y -= BUBBLE_SPEED;

        if (manager.IsHomeLogoInPosition()) {
            if (direction == RIGHT)
                x += BUBBLE_SPEED / 2;
            else
                x -= BUBBLE_SPEED / 2;
        }

        // Pacman effect
        if (x < -BUBBLE_W)
            x = Constants.GAME_WIDTH;
        else if (x > Constants.GAME_WIDTH)
            x = -BUBBLE_W;

        if (y < -BUBBLE_H)
            y = Constants.GAME_HEIGHT;
        else if (y > Constants.GAME_HEIGHT)
            y = -BUBBLE_H;
    }

    /**
     * Draws the bubble on the screen at its current position and animation frame.
     *
     * @param g the Graphics2D object to draw with
     */
    public void draw(Graphics2D g) {
        g.drawImage(sprite[animationIndex], (int) x, (int) y, BUBBLE_W , BUBBLE_H ,  null);
    }
}