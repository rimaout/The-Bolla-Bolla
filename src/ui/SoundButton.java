package ui;

import utilz.LoadSave;
import static utilz.Constants.UI.PauseButtons.*;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SoundButton extends PauseButton implements Button {
    private BufferedImage[][] buttonsImages;
    private boolean mouseOver, mousePressed;
    private boolean muted = false;
    private int spriteRowIndex, spriteColIndex;

    public SoundButton(int x, int y, int width, int height) {
        super(x, y, width, height);
        loadSprites();
    }

    @Override
    public void loadSprites() {
        BufferedImage temp = LoadSave.GetSprite(LoadSave.SOUND_BUTTONS_SPRITE);
        buttonsImages = new BufferedImage[2][3];

        for (int i = 0; i < buttonsImages.length; i++) {
            for (int j = 0; j < buttonsImages[0].length; j++) {
                buttonsImages[i][j] = temp.getSubimage(j * SOUND_BT_DEFAULT_W, i * SOUND_BT_DEFAULT_H, SOUND_BT_DEFAULT_W, SOUND_BT_DEFAULT_H);
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(buttonsImages[spriteRowIndex][spriteColIndex], x, y, width, height, null);
    }

    @Override
    public void update() {
        if (muted)
            spriteRowIndex = 1;
        else
            spriteRowIndex = 0;

        spriteColIndex = 0;
        if (mouseOver)
            spriteColIndex = 1;
        if (mousePressed)
            spriteColIndex = 2;

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

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }
}
