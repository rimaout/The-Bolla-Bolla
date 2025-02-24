package com.rima.view.overlays.menuOverlays;

import com.rima.model.overlays.MenuUserSelectionOverlayModel;
import com.rima.model.utilz.Constants;
import com.rima.view.users.UsersManagerView;
import com.rima.view.utilz.Load;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The MenuUserSelectionOverlayView class represents the overlay view for the {@link MenuUserSelectionOverlayModel} class.
 * It handles drawing the user selection interface, including the user picture, name, and controls.
 */
public class MenuUserSelectionOverlayView extends MenuOverlayView {
    private final MenuUserSelectionOverlayModel menuUserSelectionOverlayModel;
    private final UsersManagerView usersManagerView = UsersManagerView.getInstance();

    private BufferedImage[][] arrows;
    private int leftArrowIndex = 0;
    private int rightArrowIndex = 0;

    /**
     * Constructs a MenuUserSelectionOverlayView with the specified MenuUserSelectionOverlayModel.
     * Initializes the arrows images.
     *
     * @param menuUserSelectionOverlayModel the model of the user selection overlay
     */
    public MenuUserSelectionOverlayView(MenuUserSelectionOverlayModel menuUserSelectionOverlayModel) {
        this.menuUserSelectionOverlayModel = menuUserSelectionOverlayModel;

        loadArrows();
    }

    /**
     * Draws the user selection overlay on the screen.
     *
     * @param g the Graphics object to draw with
     */
    @Override
    public void draw(Graphics g) {
        drawBox(g);
        drawTitle(g);
        drawArrows(g);
        drawUserName(g);
        drawUserPicture(g);
        drawUserDetails(g);
        drawControls(g);
    }

    /**
     * Draws the box for the user selection overlay.
     *
     * @param g the Graphics object to draw with
     */
    private void drawBox(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        int rectWidth = (int) (Constants.GAME_WIDTH * 0.7);
        int rectHeight = (int) (Constants.GAME_HEIGHT * 0.4);
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

    /**
     * Draws the arrows for selecting the user.
     *
     * @param g the Graphics object to draw with
     */
    private void drawArrows(Graphics g){
        int y = 93 * Constants.SCALE;
        int size = 18 * Constants.SCALE;

        //draw left arrow
        int xLeft = 41 * Constants.SCALE - size;
        g.drawImage(arrows[0][leftArrowIndex], xLeft, y, size, size, null);

        //draw right arrow
        int xRight = 215 * Constants.SCALE;
        g.drawImage(arrows[1][rightArrowIndex], xRight, y, size, size, null);
    }

    /**
     * Draws the title "USER SELECTION" on the screen.
     *
     * @param g the Graphics object to draw with
     */
    private void drawTitle(Graphics g){
        g.setColor(Color.WHITE);
        g.setFont(nesFont.deriveFont(40f));
        FontMetrics fm = g.getFontMetrics(g.getFont());

        String text1 = "USER";
        String text2 = "SELECTION";
        int textWidth1 = fm.stringWidth(text1);
        int textWidth2 = fm.stringWidth(text2);
        int totalWidth = textWidth1 + textWidth2;
        int spacing = 5 * Constants.SCALE; // Adjust this value to change the spacing between the words
        int x = (Constants.GAME_WIDTH - (totalWidth + spacing)) / 2;
        int y = 23 * Constants.SCALE + fm.getHeight();

        g.drawString(text1, x, y);
        g.drawString(text2, x + textWidth1 + spacing, y);
    }

    /**
     * Draws the user name on the screen.
     *
     * @param g the Graphics object to draw with
     */
    private void drawUserName(Graphics g){
        g.setColor(usersManagerView.getUserColor1(menuUserSelectionOverlayModel.getSelectedUser().getProfilePictureIndex()));
        g.setFont(retroFont.deriveFont(50f));
        FontMetrics fm = g.getFontMetrics(g.getFont());

        String text = menuUserSelectionOverlayModel.getSelectedUser().getName().toUpperCase();
        int x = 100 * Constants.SCALE;
        int y = 60 * Constants.SCALE + fm.getHeight();

        g.drawString(text, x, y);
    }

    /**
     * Draws the user picture on the screen.
     *
     * @param g the Graphics object to draw with
     */
    private void drawUserPicture(Graphics g){
        int x = 45 * Constants.SCALE;
        int y = 67 * Constants.SCALE;
        int rectSize = (int) ((18 * 2.8 + 1) * Constants.SCALE);

        //draw border for the picture
        g.setColor(Color.WHITE);
        g.drawRect(x, y, rectSize, rectSize);

        //draw the picture
        g.drawImage(usersManagerView.getUserPicture(menuUserSelectionOverlayModel.getSelectedUser().getProfilePictureIndex()), x + 1, y + 1, rectSize - 1, rectSize - 1, null);
    }

    /**
     * Draws the user details on the screen.
     *
     * @param g the Graphics object to draw with
     */
    private void drawUserDetails(Graphics g){
        g.setColor(Color.WHITE);
        g.setFont(retroFont.deriveFont(22f));
        FontMetrics fm = g.getFontMetrics(g.getFont());

        String text1 = "- Best Score: " + menuUserSelectionOverlayModel.getSelectedUser().getBestScore();
        String text2 = "- Played Games: " + menuUserSelectionOverlayModel.getSelectedUser().getPlayedGames();
        String text3 = "- Won Games: " + menuUserSelectionOverlayModel.getSelectedUser().getWonGames();
        int x = 95 * Constants.SCALE + fm.getHeight();
        int y = 95 * Constants.SCALE;

        g.drawString(text1, x, y);
        g.drawString(text2, x, y + fm.getHeight());
        g.drawString(text3, x, y + 2 * fm.getHeight());
    }

    /**
     * Draws the controls instructions on the screen.
     *
     * @param g the Graphics object to draw with
     */
    private void drawControls(Graphics g) {
        g.setFont(retroFont.deriveFont(22f));
        FontMetrics fm = g.getFontMetrics(g.getFont());

        String text1Part1 = "Press ";
        String text1Part2 = "ENTER";
        String text1Part3 = " to select";
        String text2Part1 = "Press ";
        String text2Part2 = "C";
        String text2Part3 = " to create a new user";

        int x = 40 * Constants.SCALE + fm.getHeight();
        int y = 95 * Constants.SCALE + 4 * fm.getHeight();

        // Draw first suggestion
        g.setColor(Color.WHITE);
        g.drawString(text1Part1, x, y);
        int width1 = fm.stringWidth(text1Part1);

        g.setColor(usersManagerView.getUserColor2(menuUserSelectionOverlayModel.getSelectedUser().getProfilePictureIndex()));
        g.drawString(text1Part2, x + width1, y);
        int width2 = fm.stringWidth(text1Part2);

        g.setColor(Color.WHITE);
        g.drawString(text1Part3, x + width1 + width2, y);

        // Draw second suggestion
        g.setColor(Color.WHITE);
        g.drawString(text2Part1, x, y + fm.getHeight());
        int width3 = fm.stringWidth(text2Part1);

        g.setColor(usersManagerView.getUserColor2(menuUserSelectionOverlayModel.getSelectedUser().getProfilePictureIndex()));
        g.drawString(text2Part2, x + width3, y + fm.getHeight());
        int width4 = fm.stringWidth(text2Part2);

        g.setColor(Color.WHITE);
        g.drawString(text2Part3, x + width3 + width4, y + fm.getHeight());
    }

    /**
     * Loads the arrows images.
     */
    private void loadArrows(){
        arrows = new BufferedImage[2][2];
        BufferedImage img = Load.GetSprite(Load.ARROWS_LEFT_RIGHT);

        arrows[0][0] = img.getSubimage(0, 0, 10, 10);
        arrows[0][1] = img.getSubimage(10, 0, 10, 10);
        arrows[1][0] = img.getSubimage(0, 10, 10, 10);
        arrows[1][1] = img.getSubimage(10, 10, 10, 10);
    }

    // ----------- Getters and Setters -----------

    /**
     * Sets the index of the left arrow image.
     *
     * @param leftArrowIndex the index of the left arrow image
     */
    public void setLeftArrowIndex(int leftArrowIndex) {
        this.leftArrowIndex = leftArrowIndex;
    }

    /**
     * Sets the index of the right arrow image.
     *
     * @param rightArrowIndex the index of the right arrow image
     */
    public void setRightArrowIndex(int rightArrowIndex) {
        this.rightArrowIndex = rightArrowIndex;
    }
}