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

    public Overlay(Playing playing) {
        this.playing = playing;
        this.nesFont = LoadSave.getNesFont();
        this.retroFont = LoadSave.getRetroGamingFont();
    }

    public void draw(Graphics g) {
        g.setColor(new Color(0, 0, 0, 220));
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        drawTitle(g);
        drawControls(g);
    }

    protected abstract void drawTitle(Graphics g);
    protected abstract void drawControls(Graphics g);
    public abstract void keyPressed(KeyEvent e);
}
