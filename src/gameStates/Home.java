package gameStates;

import entities.TwinkBubble;
import main.Game;
import utilz.LoadSave;
import static utilz.Constants.Home.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Home extends State implements StateMethods {

    private BufferedImage logoImg;
    private int logoX, logoY, logoW, logoH;

    private BufferedImage[] twinkleBubbleSprite;
    private List<TwinkBubble> bubbles;

    private boolean isLogoInPosition = false;
    private Font customFont;

    public Home(Game game) {
        super(game);
        loadLogo();
        loadCustomFont();

        loadTwinkleBubble();
        initializeBubbles();
    }

    private void loadLogo() {
        logoImg = LoadSave.GetSprite(LoadSave.GAME_LOGO);
        logoW = (int) (logoImg.getWidth() * Game.SCALE);
        logoH = (int) (logoImg.getHeight() * Game.SCALE);
        logoX = Game.GAME_WIDTH / 2 - logoW / 2;

        logoY = (int) (- logoImg.getHeight() * Game.SCALE);
    }

    @Override
    public void update() {

        // Update Bubbles
        bubbles.forEach(TwinkBubble::update);

        // Update Logo Position
        if (logoY < LOGO_END_Y)
            logoY += LOGO_SPEED;
        else
            isLogoInPosition = true;
    }

    @Override
    public void draw(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        bubbles.forEach(bubble -> bubble.draw(g2d));

        g.drawImage(logoImg, (int) logoX, (int) logoY, (int) logoW, (int) logoH, null);

        if (isLogoInPosition) {
            g.setColor(Color.WHITE);
            g.setFont(customFont);
            g.drawString("PRESS ENTER TO START!", Game.GAME_WIDTH / 2 - 75 * Game.SCALE, Game.GAME_HEIGHT / 2 + 50 * Game.SCALE);

            Font smallerFont = customFont.deriveFont(15f);
            g.setFont(smallerFont);
            g.drawString("© 2024 RIMA CORPORATION", Game.GAME_WIDTH / 2 - 55 * Game.SCALE, Game.GAME_HEIGHT / 2 + 100 * Game.SCALE);
        }
    }

    private void loadCustomFont() {
        try {
            InputStream is = getClass().getResourceAsStream(LoadSave.FONT);
            customFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(22f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }

    private void loadTwinkleBubble() {
        BufferedImage img = LoadSave.GetSprite(LoadSave.BUBBLE_TWINKLE);

        twinkleBubbleSprite = new BufferedImage[4];
        for (int i = 0; i < 4; i++) {
            twinkleBubbleSprite[i] = img.getSubimage(i * BUBBLE_DEFAULT_W, 0, BUBBLE_DEFAULT_W, BUBBLE_DEFAULT_H);
        }
    }

    private void initializeBubbles() {
        bubbles = new ArrayList<>();
        int bubbleCount = 50;

        Random random = new Random();

        for (int i = 0; i < bubbleCount; i++) {
            int x = random.nextInt(Game.GAME_WIDTH);
            int y = random.nextInt(Game.GAME_HEIGHT);
            bubbles.add(new TwinkBubble(twinkleBubbleSprite, x, y, this));
        }
    }

    public boolean getIsLogoInPosition() {
        return isLogoInPosition;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // Not used
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Not used
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // Not used
    }

    @Override
    public void mouseMoved(MouseEvent e) {
      // Not used
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
}