package view;

import java.awt.*;
import javax.swing.JPanel;

import model.utilz.Constants;
import controller.GameController;
import controller.inputs.KeyBoardInputs;
import controller.inputs.MouseKeyInputs;
import controller.inputs.MouseMotionInputs;


/**
 * The GamePanel class is responsible for rendering the game and handling input events.
 * It extends the JPanel class and sets up the game panel with the necessary input listeners.
 */
public class GamePanel extends JPanel {
    private final GameController gameController;

    /**
     * Constructs a GamePanel with the specified GameController.
     * Sets the background color and panel size, and adds input listeners.
     *
     * @param gameController the GameController to be used by this panel
     */
    public GamePanel(GameController gameController) {
        this.gameController = gameController;
        setBackground(Color.BLACK);
        setPanelSize();

        // Input Listeners
        addKeyListener(new KeyBoardInputs(this));
        addMouseListener(new MouseKeyInputs(this));
        addMouseMotionListener(new MouseMotionInputs(this));
    }

    /**
     * Sets the size of the panel based on the game dimensions defined in Constants.
     */
     private void setPanelSize() {
        Dimension size = new Dimension(Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
        setPreferredSize(size);
    }

    /**
     * Paints the component by synchronizing the graphics state and rendering the game.
     *
     * @param g the Graphics object to protect
     */
    public void paintComponent(Graphics g) {
        Toolkit.getDefaultToolkit().sync();     // Synchronize the graphics state (used to solve lag on linux)
        super.paintComponent(g);                // Use the paintComponent method of the JPanel (super) class to paint the panel

        gameController.render(g);
    }

    /**
     * Returns the GameController associated with this panel.
     *
     * @return the GameController
     */
    public GameController getGameController() {
        return gameController;
    }
}