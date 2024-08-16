package ui;

import gameStates.Gamestate;
import gameStates.Playing;
import main.Game;

import java.awt.*;
import java.awt.event.KeyEvent;

public class GameCompletedOverlay {
    private final Playing playing;

    public GameCompletedOverlay(Playing playing) {
        this.playing = playing;
    }

    public void draw(Graphics g) {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        g.setColor(Color.WHITE);
        g.drawString("Game Completed", Game.GAME_WIDTH / 2 - 48, Game.GAME_HEIGHT / 2);
        g.drawString("Press ESC to enter Menu!", Game.GAME_WIDTH / 2 - 68, Game.GAME_HEIGHT / 2 + 20);
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            playing.resetAll();
            Gamestate.state = Gamestate.MENU;
        }
    }
}
