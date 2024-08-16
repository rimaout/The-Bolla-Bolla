package ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import utilz.LoadSave;
import static utilz.Constants.UI.VolumeButton.*;

public class VolumeButton extends PauseButton implements Button {
    private BufferedImage[] buttonsImages;
    private BufferedImage sliderImage;
    private int index = 0;
    private int buttonX, minX, maxX;

    private boolean mouseOver, mousePressed;

    public VolumeButton(int x, int y, int width, int height) {
        super(x + width / 2, y, VOLUME_BT_WIDTH, height);
        bounds.x -= VOLUME_BT_WIDTH / 2;
        buttonX = x + width / 2;
        this.x = x;
        this.width = width;
        minX = x + VOLUME_BT_WIDTH / 2;
        maxX = x + width - VOLUME_BT_WIDTH / 2;
        loadSprites();
    }

    @Override
    public void loadSprites() {
        BufferedImage temp = LoadSave.GetSprite(LoadSave.VOLUME_BUTTON_SPRITE);

        buttonsImages = new BufferedImage[3];
        for (int i = 0; i < buttonsImages.length; i++) {
            buttonsImages[i] = temp.getSubimage(i * VOLUME_BT_WIDTH_DEFAULT, 0, VOLUME_BT_WIDTH_DEFAULT, VOLUME_BT_HEIGHT_DEFAULT);
        }

        sliderImage = temp.getSubimage(3*VOLUME_BT_WIDTH_DEFAULT, 0, VOLUME_SLIDER_WIDTH_DEFAULT, VOLUME_SLIDER_HEIGHT_DEFAULT);
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(sliderImage, x, y, width, height , null);
        g.drawImage(buttonsImages[index], buttonX - VOLUME_BT_WIDTH/2, y, VOLUME_BT_WIDTH, height, null);
    }

    public void changeX(int x) {
        buttonX = x;
        if(buttonX < minX) {
            buttonX = minX;
        }
        if(buttonX > maxX) {
            buttonX = maxX;
        }

        bounds.x = buttonX - VOLUME_BT_WIDTH / 2;
    }

    @Override
    public void update() {
        index = 0;
        if(mouseOver) {
            index = 1;
        }
        if(mousePressed) {
            index = 2;
        }
    }

    @Override
    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

}
