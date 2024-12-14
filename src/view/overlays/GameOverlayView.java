package view.overlays;

import view.utilz.LoadSave;
import model.utilz.Constants;
import model.gameStates.PlayingModel;

import java.awt.*;

public abstract class GameOverlayView {
    protected final PlayingModel playingModel;
    protected final Font nesFont;
    protected final Font retroFont;

    protected boolean firstUpdate = true;

    public GameOverlayView(PlayingModel playingModel) {
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
    }

    public void reset() {
        firstUpdate = true;
    }

    protected abstract void drawTitle(Graphics g);
    protected abstract void drawControls(Graphics g);
    protected abstract void setAudio();
}
