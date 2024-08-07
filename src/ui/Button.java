package ui;

import java.awt.*;
import java.awt.image.BufferedImage;

public interface Button {

    void loadSprites();
    void draw(Graphics g);
    void update();
    void resetBools();
}
