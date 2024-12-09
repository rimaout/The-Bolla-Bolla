package view;

import java.awt.*;
import javax.swing.JPanel;

import controller.inputs.KeyBoardInputs;
import controller.inputs.MouseKeyInputs;
import controller.inputs.MouseMotionInputs;
import controller.Game;
import model.utilz.Constants;

public class GamePanel extends JPanel {
    private final Game game;

    public GamePanel(Game game) {
        this.game = game;
        setBackground(Color.BLACK);
        setPanelSize();

        // Input Listeners
        addKeyListener(new KeyBoardInputs(this));
        addMouseListener(new MouseKeyInputs(this));
        addMouseMotionListener(new MouseMotionInputs(this));
    }

     private void setPanelSize() {
        Dimension size = new Dimension(Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
        setPreferredSize(size);

        System.out.println("Size: " + Constants.GAME_WIDTH + " | " + Constants.GAME_HEIGHT);
    }

    public void paintComponent(Graphics g) {
        Toolkit.getDefaultToolkit().sync();     // Synchronize the graphics state
        super.paintComponent(g);                // Use the paintComponent method of the JPanel (super) class to paint the panel

        game.render(g);
    }

    public Game getGame() {
        return game;
    }
}
