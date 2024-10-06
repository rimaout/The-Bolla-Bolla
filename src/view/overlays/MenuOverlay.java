package view.overlays;

import utilz.LoadSave;
import gameStates.MenuModel;

import java.awt.*;
import java.awt.event.KeyEvent;

public abstract class MenuOverlay {
    protected MenuModel menuModel;
    protected final Font nesFont;
    protected final Font retroFont;

    public MenuOverlay(MenuModel menuModel) {
        this.menuModel = menuModel;
        this.nesFont = LoadSave.GetNesFont();
        this.retroFont = LoadSave.GetRetroGamingFont();
    }

    public abstract void update();
    public abstract void draw(Graphics g);
}
