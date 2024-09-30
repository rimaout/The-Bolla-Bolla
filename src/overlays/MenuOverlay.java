package overlays;

import utilz.LoadSave;
import gameStates.Menu;

import java.awt.*;
import java.awt.event.KeyEvent;

public abstract class MenuOverlay {
    protected Menu menu;
    protected final Font nesFont;
    protected final Font retroFont;

    public MenuOverlay(Menu menu) {
        this.menu = menu;
        this.nesFont = LoadSave.GetNesFont();
        this.retroFont = LoadSave.GetRetroGamingFont();
    }

    public abstract void update();
    public abstract void draw(Graphics g);
    public abstract void keyPressed(KeyEvent e);
    public abstract void keyReleased(KeyEvent e);
}
