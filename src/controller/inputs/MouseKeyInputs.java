package controller.inputs;

import view.GamePanel;
import model.gameStates.GameState;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * This class implements the {@link MouseListener} interface.
 * Controller for listening to mouse inputs and handling them, rerouting the information to the right input controller based on the game state.
 */
public class MouseKeyInputs implements MouseListener{
    private final GamePanel gamePanel;

    /**
     * Constructs a new MouseKeyInputs.
     *
     * @param gamePanel the game panel
     */
    public MouseKeyInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    /**
     * {@inheritDoc} Overrides mouseClicked method from {@link MouseListener} interface.
     *
     * @param e the MouseEvent to handle
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        switch (GameState.state) {
            case MENU:
                gamePanel.getGameController().getMenuController().mouseClicked(e);
                break;
            case PLAYING:
                gamePanel.getGameController().getPlayingController().mouseClicked(e);
                break;
        }
    }

    /**
     * {@inheritDoc} Overrides mousePressed method from {@link MouseListener} interface.
     *
     * @param e the MouseEvent to handle
     */
    @Override
    public void mousePressed(MouseEvent e) {
        switch (GameState.state) {
            case MENU:
                gamePanel.getGameController().getMenuController().mousePressed(e);
                break;
            case PLAYING:
                gamePanel.getGameController().getPlayingController().mousePressed(e);
                break;
        }
    }

    /**
     * {@inheritDoc} Overrides mouseReleased method from {@link MouseListener} interface.
     *
     * @param e the MouseEvent to handle
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        switch (GameState.state) {
            case MENU:
                gamePanel.getGameController().getMenuController().mouseReleased(e);
                break;
            case PLAYING:
                gamePanel.getGameController().getPlayingController().mouseReleased(e);
                break;
        }
    }

    /**
     * {@inheritDoc} Overrides mouseEntered method from {@link MouseListener} interface.
     *
     * @param e the MouseEvent to handle
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        // not used
    }

    /**
     * {@inheritDoc} Overrides mouseExited method from {@link MouseListener} interface.
     *
     * @param e the MouseEvent to handle
     */
    @Override
    public void mouseExited(MouseEvent e) {
        // not used
    }
}