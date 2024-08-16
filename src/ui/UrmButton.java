package ui;

import utilz.LoadSave;
import static utilz.Constants.UI.UrmButtons.*;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UrmButton extends PauseButton implements Button{
    private BufferedImage[] buttonsImages;
    private boolean mouseOver, mousePressed;
    private int spriteRowIndex, index;

    public UrmButton(int x, int y, int width, int height, int spriteRowIndex) {
        super(x, y, width, height);
        this.spriteRowIndex = spriteRowIndex;
        loadSprites();
    }

    @Override
    public void loadSprites() {
        BufferedImage temp = LoadSave.GetSprite(LoadSave.URM_BUTTONS_SPRITE);
        buttonsImages = new BufferedImage[3];

        for (int i = 0; i < buttonsImages.length; i++) {
            buttonsImages[i] = temp.getSubimage(i * URM_BT_WIDTH_DEFAULT, spriteRowIndex * URM_BT_HEIGHT_DEFAULT, URM_BT_WIDTH_DEFAULT, URM_BT_HEIGHT_DEFAULT);
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(buttonsImages[index], x, y, width, height, null);
    }

    @Override
    public void update() {
        index = 0;
        if (mouseOver)
            index = 1;
        if (mousePressed)
            index = 2;
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
