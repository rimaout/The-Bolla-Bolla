package model.overlays;

import gameStates.MenuModel;


public abstract class MenuOverlayModel {
    protected MenuModel menuModel;

    public MenuOverlayModel(MenuModel menuModel) {
        this.menuModel = menuModel;
    }

    public abstract void update();
}
