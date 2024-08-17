package ui;

import utilz.LoadSave;
import gameStates.Gamestate;

import static utilz.Constants.UI.MenuButtons.*;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MenuButton {
    private int xPos, yPos, spriteRowIndex, index;
    private boolean mouseOver, mousePressed;

    private Gamestate state;
    private BufferedImage[] buttonImages;
    private Rectangle buttonBounds;

    public MenuButton(int xPos, int yPos, int spriteRowIndex, Gamestate state) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.spriteRowIndex = spriteRowIndex;
        this.state = state;
        loadButtonImages();
        initButtonBounds();
    }

    private void initButtonBounds() {
        buttonBounds = new Rectangle(xPos, yPos, BUTTON_W, BUTTON_H);
    }

    private void loadButtonImages() {
        buttonImages = new BufferedImage[3];
        BufferedImage tempImage = LoadSave.GetSprite(LoadSave.MENU_BUTTONS_SPRITE);

        for(int i=0; i<buttonImages.length; i++) {
            buttonImages[i] = tempImage.getSubimage(i* BUTTON_DEFAULT_W, spriteRowIndex * BUTTON_DEFAULT_H, BUTTON_DEFAULT_W, BUTTON_DEFAULT_H);
        }
    }

    public void draw(Graphics g) {
       g.drawImage(buttonImages[index], xPos, yPos, BUTTON_W, BUTTON_H, null);
    }

    public void update(){
        index = 0;

        if(mouseOver)
            index = 1;
        if(mousePressed)
            index = 2;
    }

    // Getters and Setters
    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public Rectangle getButtonBounds() {
        return buttonBounds;
    }

    public void applyGameState() {
        Gamestate.state = state;
    }

    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }


}

