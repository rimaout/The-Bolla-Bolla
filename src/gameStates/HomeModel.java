package gameStates;

import main.Game;
import utilz.LoadSave;
import entities.TwinkleBubbleManager;
import static utilz.Constants.Home.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class HomeModel extends State implements StateMethods {

    private final TwinkleBubbleManager twinkleBubbleManager = TwinkleBubbleManager.getInstance(this);

    private BufferedImage logoImg;
    private float logoX, logoY, logoW, logoH;

    private boolean isLogoInPosition = false;
    private final Font nesFont;

    public HomeModel(Game game) {
        super(game);
        loadLogo();

        nesFont = LoadSave.GetNesFont();
    }

    private void loadLogo() {
        logoImg = LoadSave.GetSprite(LoadSave.GAME_LOGO);
        logoW = logoImg.getWidth() * Game.SCALE;
        logoH = logoImg.getHeight() * Game.SCALE;
        logoX = (float) Game.GAME_WIDTH / 2 - logoW / 2;

        logoY = (int) (- logoImg.getHeight() * Game.SCALE);
    }

    @Override
    public void update() {

        // Update Bubbles
        twinkleBubbleManager.update();

        // Update Logo Position
        if (logoY < LOGO_END_Y)
            logoY += LOGO_SPEED;
        else
            isLogoInPosition = true;
    }

    public void draw(Graphics g) {

        // Draw Bubbles
        twinkleBubbleManager.draw(g);

        g.drawImage(logoImg, (int) logoX, (int) logoY, (int) logoW, (int) logoH, null);

        if (isLogoInPosition) {
            g.setColor(Color.WHITE);
            g.setFont(nesFont);
            g.drawString("PRESS ENTER TO START!", Game.GAME_WIDTH / 2 - 75 * Game.SCALE, Game.GAME_HEIGHT / 2 + 50 * Game.SCALE);

            g.setFont(nesFont.deriveFont(15f));
            g.drawString("© 2024 RIMA CORPORATION", Game.GAME_WIDTH / 2 - 55 * Game.SCALE, Game.GAME_HEIGHT / 2 + 100 * Game.SCALE);
        }
    }

    public boolean IsLogoInPosition() {
        return isLogoInPosition;
    }
}