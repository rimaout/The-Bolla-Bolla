package main;

import javax.swing.JPanel;
import java.awt.*;
import inputs.*;
import static main.Game.GAME_WIDTH;
import static main.Game.GAME_HEIGHT;

public class GamePanel extends JPanel {
    private Game game;

    public GamePanel(Game game) {
        this.game = game;
        setPanelSize();
        addKeyListener(new KeyBoardInputs(this));
        addMouseListener(new MouseKeyInputs(this));
        addMouseMotionListener(new MouseMotionInputs(this));
    }

     private void setPanelSize() {
        Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT);
        setPreferredSize(size);

        System.out.println("Size: " + GAME_WIDTH + " | " + GAME_HEIGHT);

    }

    public void update() {

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
