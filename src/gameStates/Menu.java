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

        buttons[0] = new MenuButton(13*Game.SCALE, 190*Game.SCALE, 0, GameState.PLAYING);
        buttons[1] = new MenuButton(93*Game.SCALE, 190*Game.SCALE, 1, GameState.OPTIONS);
        buttons[2] = new MenuButton(173*Game.SCALE, 190*Game.SCALE, 2, GameState.QUIT);
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
        // Not used
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
    public void mouseDragged(MouseEvent e) {
        // Not used
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            GameState.state = GameState.PLAYING;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    private void resetButtons() {
        for (MenuButton button : buttons)
            button.resetBools();
    }

}

