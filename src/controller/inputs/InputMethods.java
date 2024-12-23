package controller.inputs;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Interface for handling user input methods.
 */
public interface InputMethods {
    /**
     * Handles mouse click events.
     *
     * @param e the MouseEvent to handle
     */
    void mouseClicked(MouseEvent e);

    /**
     * Handles mouse press events.
     *
     * @param e the MouseEvent to handle
     */
    void mousePressed(MouseEvent e);

    /**
     * Handles mouse release events.
     *
     * @param e the MouseEvent to handle
     */
    void mouseReleased(MouseEvent e);

    /**
     * Handles mouse move events.
     *
     * @param e the MouseEvent to handle
     */
    void mouseMoved(MouseEvent e);

    /**
     * Handles mouse drag events.
     *
     * @param e the MouseEvent to handle
     */
    void mouseDragged(MouseEvent e);

    /**
     * Handles key press events.
     *
     * @param e the KeyEvent to handle
     */
    void keyPressed(KeyEvent e);

    /**
     * Handles key release events.
     *
     * @param e the KeyEvent to handle
     */
    void keyReleased(KeyEvent e);
}