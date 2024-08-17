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
        super(x + width / 2, y, VOLUME_BT_W, height);
        bounds.x -= VOLUME_BT_W / 2;
        buttonX = x + width / 2;
        this.x = x;
        this.width = width;
        minX = x + VOLUME_BT_W / 2;
        maxX = x + width - VOLUME_BT_W / 2;
        loadSprites();
    }

    @Override
    public void loadSprites() {
        BufferedImage temp = LoadSave.GetSprite(LoadSave.VOLUME_BUTTON_SPRITE);

        buttonsImages = new BufferedImage[3];
        for (int i = 0; i < buttonsImages.length; i++) {
            buttonsImages[i] = temp.getSubimage(i * VOLUME_BT_DEFAULT_W, 0, VOLUME_BT_DEFAULT_W, VOLUME_BT_DEFAULT_H);
        }

        sliderImage = temp.getSubimage(3* VOLUME_BT_DEFAULT_W, 0, VOLUME_SLIDER_DEFAULT_W, VOLUME_SLIDER_DEFAULT_H);
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(sliderImage, x, y, width, height , null);
        g.drawImage(buttonsImages[index], buttonX - VOLUME_BT_W /2, y, VOLUME_BT_W, height, null);
    }

    public void changeX(int x) {
        buttonX = x;
        if(buttonX < minX) {
            buttonX = minX;
        }
        if(buttonX > maxX) {
            buttonX = maxX;
        }

        bounds.x = buttonX - VOLUME_BT_W / 2;
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
