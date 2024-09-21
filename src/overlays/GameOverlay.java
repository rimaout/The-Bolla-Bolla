package overlays;

import gameStates.GameState;
import gameStates.Playing;
import main.Game;
import utilz.LoadSave;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public abstract class GameOverlay {
    protected final Playing playing;
    protected final Font nesFont;
    protected final Font retroFont;

    protected boolean firstUpdate = true;

    public GameOverlay(Playing playing) {
        this.playing = playing;
        this.nesFont = LoadSave.GetNesFont();
        this.retroFont = LoadSave.GetRetroGamingFont();
    }

    public void draw(Graphics g) {
        g.setColor(new Color(0, 0, 0, 220));
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        setAudio();
        drawTitle(g);
        drawControls(g);
        setAudio();
    }

    public void newPlayReset() {
        firstUpdate = true;
    }

    protected abstract void drawTitle(Graphics g);
    protected abstract void drawControls(Graphics g);
    protected abstract void setAudio();
    public abstract void keyPressed(KeyEvent e);
}
