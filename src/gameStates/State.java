package gameStates;

import main.Game;
import ui.MenuButton;

import java.awt.event.MouseEvent;

public class State {

    protected Game game;

    public State(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public boolean isMouseWithinButton(MouseEvent e, MenuButton menuButton){
        return menuButton.getButtonBounds().contains(e.getX(), e.getY());
    }
}
