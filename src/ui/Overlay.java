package ui;

import gameStates.GameState;
import gameStates.Playing;
import main.Game;
import utilz.LoadSave;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    protected void setGameStateWithDelay(GameState state, int delay) {
        // Create a Timer to delay the state change
        Timer timer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                GameState.state = state;
            }
        });
        timer.setRepeats(false); // Ensure the timer only runs once
        timer.start();
    }

    protected abstract void drawTitle(Graphics g);
    protected abstract void drawControls(Graphics g);
    protected abstract void setAudio();
    public abstract void keyPressed(KeyEvent e);
}
