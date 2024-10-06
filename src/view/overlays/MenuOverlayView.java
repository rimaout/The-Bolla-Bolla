package view.overlays;

import utilz.LoadSave;

import java.awt.*;

public abstract class MenuOverlayView {
    protected final Font nesFont;
    protected final Font retroFont;

    public MenuOverlayView() {
        this.nesFont = LoadSave.GetNesFont();
        this.retroFont = LoadSave.GetRetroGamingFont();
    }

    public abstract void draw(Graphics g);
}
