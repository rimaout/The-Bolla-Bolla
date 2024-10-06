package view.overlays;

import main.Game;
import utilz.Constants;
import utilz.LoadSave;
import gameStates.PlayingModel;

import java.awt.*;
import java.awt.event.KeyEvent;

public abstract class GameOverlay {
    protected final PlayingModel playingModel;
    protected final Font nesFont;
    protected final Font retroFont;

    protected boolean firstUpdate = true;

    public GameOverlay(PlayingModel playingModel) {
        this.playingModel = playingModel;
        this.nesFont = LoadSave.GetNesFont();
        this.retroFont = LoadSave.GetRetroGamingFont();
    }

    public void draw(Graphics g) {
        g.setColor(new Color(0, 0, 0, 220));
        g.fillRect(0, 0, Constants.GAME_WIDTH, Constants.GAME_HEIGHT);

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
