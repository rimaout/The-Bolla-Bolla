package com.rima.view.overlays.menuOverlays;

import com.rima.model.overlays.MenuOverlayModel;
import com.rima.view.utilz.Load;

import java.awt.*;

/**
 * The MenuOverlayView class is an abstract class that view of the {@link MenuOverlayModel} class.
 * It provides the fonts used for drawing the menu overlay.
 */
public abstract class MenuOverlayView {
    protected final Font nesFont;
    protected final Font retroFont;

    /**
     * Constructs a MenuOverlayView and initializes the fonts.
     * Loads the NES and retro gaming fonts.
     */
    public MenuOverlayView() {
        this.nesFont = Load.GetNesFont();
        this.retroFont = Load.GetRetroGamingFont();
    }

    /**
     * Draws the menu overlay.
     *
     * @param g the Graphics object to draw with
     */
    public abstract void draw(Graphics g);
}