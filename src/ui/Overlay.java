package ui;

import gameStates.Playing;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;

public abstract class Overlay {
    protected final Playing playing;
    protected final Font nesFont;
    protected final Font retroFont;

    protected boolean firstUpdate = true;

    public Overlay(Playing playing) {
        this.playing = playing;
        this.nesFont = LoadSave.GetNesFont();
        this.retroFont = LoadSave.GetRetroGamingFont();
    }

    public void draw(Graphics g) {
            setAudio();

        g.setColor(new Color(0, 0, 0, 220));
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        drawTitle(g);
        drawControls(g);
        setAudio();
    }

    public void resetNewGame() {
        firstUpdate = true;
    }

    protected abstract void drawTitle(Graphics g);
    protected abstract void drawControls(Graphics g);
    protected abstract void setAudio();
    public abstract void keyPressed(KeyEvent e);
}
