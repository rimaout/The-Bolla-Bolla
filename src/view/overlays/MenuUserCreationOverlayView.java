package view.overlays;

import model.overlays.MenuUserCreationOverlayModel;
import model.users.UsersManager;
import model.Constants;
import view.LoadSave;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class MenuUserCreationOverlayView extends MenuOverlayView {
    private final MenuUserCreationOverlayModel menuUserCreationOverlayModel;
    private final UsersManager usersManager = UsersManager.getInstance();
    private boolean blink = true;

    private BufferedImage questionMark;
    private BufferedImage[][] arrows;
    private int upArrowIndex = 0;
    private int downArrowIndex = 0;

    public MenuUserCreationOverlayView(MenuUserCreationOverlayModel menuUserCreationOverlayModel) {
        this.menuUserCreationOverlayModel = menuUserCreationOverlayModel;
        loadImages();

        // Initialize the timer for blinking animation
        Timer timer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                blink = !blink;
            }
        });
        timer.start();
    }

    @Override
    public void draw(Graphics g) {
        drawBox(g);
        drawTitle(g);
        drawArrows(g);
        drawUserPicture(g);
        drawUserName(g);
        drawControls(g);
        drawSuggestions(g);
    }

    private void drawBox(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        int rectWidth = (int) (Constants.GAME_WIDTH * 0.7);
        int rectHeight = (int) (Constants.GAME_HEIGHT * 0.465);
        int x = (Constants.GAME_WIDTH - rectWidth) / 2;
        int y = 60 * Constants.SCALE; // Fixed y-coordinate, adjust as needed

        // Set the thickness of the border
        int borderThickness = (int) 1 * Constants.SCALE; // Adjust the thickness as needed
        g2d.setStroke(new BasicStroke(borderThickness));

        // Set the color to white for the border
        g2d.setColor(Color.WHITE);
        g2d.drawRect(x, y, rectWidth, rectHeight);

        // Set the color to black for the filled rectangle
        g2d.setColor(Color.BLACK);
        g2d.fillRect(x + borderThickness, y + borderThickness, rectWidth - 2 * borderThickness, rectHeight - 2 * borderThickness);
    }

    private void drawArrows(Graphics g){

        int x = 61 * Constants.SCALE;
        int size = 18 * Constants.SCALE;

        //draw left arrow
        int yUp = 76 * Constants.SCALE - size;
        g.drawImage(arrows[0][upArrowIndex], x, yUp, size, size, null);

        //draw right arrow
        int yDown = 119 * Constants.SCALE;
        g.drawImage(arrows[1][downArrowIndex], x, yDown, size, size, null);
    }

    private void drawTitle(Graphics g){
        g.setColor(Color.WHITE);
        g.setFont(nesFont.deriveFont(40f));
        FontMetrics fm = g.getFontMetrics(g.getFont());

        String text1 = "USER";
        String text2 = "CREATION";
        int textWidth1 = fm.stringWidth(text1);
        int textWidth2 = fm.stringWidth(text2);
        int totalWidth = textWidth1 + textWidth2;
        int spacing = 5 * Constants.SCALE; // Adjust this value to change the spacing between the words
        int x = (Constants.GAME_WIDTH - (totalWidth + spacing)) / 2;
        int y = 23 * Constants.SCALE + fm.getHeight();

        g.drawString(text1, x, y);
        g.drawString(text2, x + textWidth1 + spacing, y);
    }

    private void drawUserName(Graphics g) {
        g.setColor(usersManager.getUserColor1(menuUserCreationOverlayModel.getNewUserPictureIndex()));
        g.setFont(retroFont.deriveFont(50f));
        FontMetrics fm = g.getFontMetrics(g.getFont());

        String text = menuUserCreationOverlayModel.getNewUserName().toUpperCase();
        int x = 100 * Constants.SCALE;
        int y = 85 * Constants.SCALE + fm.getHeight();

        g.drawString(text, x, y);

        // Draw the blinking cursor
        if (blink) {
            int cursorX = x + fm.stringWidth(text) + 3 * Constants.SCALE;
            Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(new BasicStroke(3 * Constants.SCALE));
            g2d.drawLine(cursorX, y - 13 * Constants.SCALE, cursorX, y + 1 * Constants.SCALE);
        }
    }

    private void drawUserPicture(Graphics g){

        //draw border for the picture
        int x = 45 * Constants.SCALE;
        int y = 73 * Constants.SCALE;
        int rectSize = (int) ((18 * 2.8 + 1) * Constants.SCALE);


        g.setColor(Color.WHITE);
        g.drawRect(x, y, rectSize, rectSize);

        if (menuUserCreationOverlayModel.getNewUserPictureIndex() == -1)
            // draw question mark
            g.drawImage(questionMark, x + 1, y + 1, rectSize - 1, rectSize - 1, null);

        else
            // draw the picture
            g.drawImage(usersManager.getUserPicture(menuUserCreationOverlayModel.getNewUserPictureIndex()), x + 1, y + 1, rectSize - 1, rectSize - 1, null);
    }

    private void drawControls(Graphics g) {
        g.setFont(retroFont.deriveFont(22f));
        FontMetrics fm = g.getFontMetrics(g.getFont());

        String text1Part1 = "Press ";
        String text1Part2 = "ENTER";
        String text1Part3 = " to confirm";
        String text2Part1 = "Press ";
        String text2Part2 = "ESC";
        String text2Part3 = " to go back";

        int x = 40 * Constants.SCALE + fm.getHeight();
        int y = 108  * Constants.SCALE + 4 * fm.getHeight();

        if (menuUserCreationOverlayModel.isEnterKeyDeactivated()) {
            // Draw first suggestion light grey (inactive)
            g.setColor(new Color(0x888888));
            g.drawString("Press ENTER to confirm", x, y);
        }

        if (!menuUserCreationOverlayModel.isEnterKeyDeactivated()) {
            // Draw first suggestion (active)
            g.setColor(Color.WHITE);
            g.drawString(text1Part1, x, y);
            int width1 = fm.stringWidth(text1Part1);

            g.setColor(usersManager.getUserColor2(menuUserCreationOverlayModel.getNewUserPictureIndex()));
            g.drawString(text1Part2, x + width1, y);
            int width2 = fm.stringWidth(text1Part2);

            g.setColor(Color.WHITE);
            g.drawString(text1Part3, x + width1 + width2, y);
        }

        // Draw second suggestion
        g.setColor(Color.WHITE);
        g.drawString(text2Part1, x, y + fm.getHeight());
        int width3 = fm.stringWidth(text2Part1);

        g.setColor(usersManager.getUserColor2(menuUserCreationOverlayModel.getNewUserPictureIndex()));
        g.drawString(text2Part2, x + width3, y + fm.getHeight());
        int width4 = fm.stringWidth(text2Part2);

        g.setColor(Color.WHITE);
        g.drawString(text2Part3, x + width3 + width4, y + fm.getHeight());
    }

    private void drawSuggestions(Graphics g) {

        if (blink) return;

        g.setColor(new Color(0xFF6961));
        g.setFont(retroFont.deriveFont(26f));
        FontMetrics fm = g.getFontMetrics(g.getFont());
        int y = 170 * Constants.SCALE + fm.getHeight();

        if (menuUserCreationOverlayModel.doesUserNameAlreadyExists()) {
            String text = "User already exists!";
            int x = (Constants.GAME_WIDTH - fm.stringWidth(text)) / 2;
            g.drawString(text, x, y);
        }

        else if (menuUserCreationOverlayModel.getNewUserPictureIndex() == -1) {
            String text = "Please select a picture!";
            int x = (Constants.GAME_WIDTH - fm.stringWidth(text)) / 2;
            g.drawString(text, x, y);
        }

        else if (menuUserCreationOverlayModel.getNewUserName().isEmpty()) {
            String text = "Username cannot be empty!";
            int x = (Constants.GAME_WIDTH - fm.stringWidth(text)) / 2;
            g.drawString(text, x, y);
        }
    }

    private void loadImages(){
        // Arrow images
        arrows = new BufferedImage[2][2];
        BufferedImage img = LoadSave.GetSprite(LoadSave.ARROWS_UP_DOWN);

        arrows[0][0] = img.getSubimage(0, 0, 10, 10);
        arrows[0][1] = img.getSubimage(10, 0, 10, 10);
        arrows[1][0] = img.getSubimage(0, 10, 10, 10);
        arrows[1][1] = img.getSubimage(10, 10, 10, 10);

        // Question mark image
        questionMark = LoadSave.GetSprite(LoadSave.QUESTION_MARK_IMAGE);
    }

    // -------- Getters and Setters --------
    public void setUpArrowIndex(int upArrowIndex) {
        this.upArrowIndex = upArrowIndex;
    }

    public void setDownArrowIndex(int downArrowIndex) {
        this.downArrowIndex = downArrowIndex;
    }
}
