package com.rima.model.overlays;

import com.rima.model.gameStates.MenuModel;

/**
 * Abstract class representing a menu overlay model, used as base for menu overlays.
 */
public abstract class MenuOverlayModel {
    protected MenuModel menuModel;

    /**
     * Constructs a MenuOverlayModel with the specified MenuModel.
     *
     * @param menuModel the MenuModel associated with this overlay
     */
    public MenuOverlayModel(MenuModel menuModel) {
        this.menuModel = menuModel;
    }

    /**
     * Updates the state of the menu overlay.
     */
    public abstract void update();
}