package gameStates;

import utilz.LoadSave;
import main.Game;
import ui.MenuButton;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Menu extends State implements StateMethods {

    private MenuButton[] buttons;
    private BufferedImage backgroundImg;
    private int backgroundX, backgroundY, backgroundWidth, backgroundHeight;

    public Menu(Game game) {
        super(game);
        loadBackground();
        loadButtons();
    }

    private void loadBackground() {
        backgroundImg = LoadSave.GetSprite(LoadSave.GAME_LOGO);
        backgroundWidth = (int) (backgroundImg.getWidth() * Game.SCALE);
        backgroundHeight = (int) (backgroundImg.getHeight() * Game.SCALE);
        backgroundX = Game.GAME_WIDTH / 2 - backgroundWidth / 2;
        backgroundY = (int) (20 * Game.SCALE);

    }

    private void loadButtons() {
        buttons = new MenuButton[3];

        buttons[0] = new MenuButton(13*Game.SCALE, 190*Game.SCALE, 0, Gamestate.PLAYING);
        buttons[1] = new MenuButton(93*Game.SCALE, 190*Game.SCALE, 1, Gamestate.OPTIONS);
        buttons[2] = new MenuButton(173*Game.SCALE, 190*Game.SCALE, 2, Gamestate.QUIT);
    }

    @Override
    public void update() {
        for (MenuButton button : buttons) {
            button.update();
        }
    }

    @Override
    public void draw(Graphics g) {

        g.drawImage(backgroundImg, backgroundX, backgroundY, backgroundWidth, backgroundHeight, null);

        for (MenuButton button : buttons) {
            button.draw(g);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (MenuButton button : buttons) {
            if (isMouseWithinButton(e, button)) {
                button.setMousePressed(true);
                break;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (MenuButton button : buttons) {
            if (isMouseWithinButton(e, button)) {
                if (button.isMousePressed())
                    button.applyGameState();
                break;
            }
        }
        resetButtons();
    }

    private void resetButtons() {
        for (MenuButton button : buttons)
            button.resetBools();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (MenuButton button : buttons) {
            button.setMouseOver(false);
        }

        for (MenuButton button : buttons) {
            if (isMouseWithinButton(e, button)) {
                button.setMouseOver(true);
                break;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            Gamestate.state = Gamestate.PLAYING;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}

