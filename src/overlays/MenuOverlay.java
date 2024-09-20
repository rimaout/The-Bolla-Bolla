package overlays;

import gameStates.Menu;

import java.awt.*;
import java.awt.event.KeyEvent;

public abstract class MenuOverlay {
    protected Menu menu;

    public MenuOverlay(Menu menu) {
        this.menu = menu;
    }

    public abstract void update();
    public abstract void draw(Graphics g);
    public abstract void keyPressed(KeyEvent e);
}
