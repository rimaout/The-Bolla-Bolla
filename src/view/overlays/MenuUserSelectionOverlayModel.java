package view.overlays;

import main.Game;
import users.User;
import utilz.LoadSave;
import gameStates.MenuModel;
import users.UsersManager;

import java.awt.*;
import java.util.ArrayList;
import java.awt.image.BufferedImage;

public class MenuUserSelectionOverlayModel extends MenuOverlay {
    private final UsersManager usersManager = UsersManager.getInstance();
    private User selectedUser;
    private ArrayList<User> users;

    private BufferedImage[][] arrows;
    private int leftArrowIndex = 0;
    private int rightArrowIndex = 0;

    public MenuUserSelectionOverlayModel(MenuModel menuModel) {
        super(menuModel);
        updateUserList();
        loadArrows();
    }

    @Override
    public void update() {
        // not used
    }

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

    private void drawBox(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        int rectWidth = (int) (Game.GAME_WIDTH * 0.7);
        int rectHeight = (int) (Game.GAME_HEIGHT * 0.4);
        int x = (Game.GAME_WIDTH - rectWidth) / 2;
        int y = 60 * Game.SCALE; // Fixed y-coordinate, adjust as needed

        // Set the thickness of the border
        int borderThickness = (int) 1 * Game.SCALE; // Adjust the thickness as needed
        g2d.setStroke(new BasicStroke(borderThickness));

        // Set the color to white for the border
        g2d.setColor(Color.WHITE);
        g2d.drawRect(x, y, rectWidth, rectHeight);

        // Set the color to black for the filled rectangle
        g2d.setColor(Color.BLACK);
        g2d.fillRect(x + borderThickness, y + borderThickness, rectWidth - 2 * borderThickness, rectHeight - 2 * borderThickness);
    }

    private void drawArrows(Graphics g){

        int y = 93 * Game.SCALE;
        int size = 18 * Game.SCALE;

        //draw left arrow
        int xLeft = 41 * Game.SCALE - size;
        g.drawImage(arrows[0][leftArrowIndex], xLeft, y, size, size, null);

        //draw right arrow
        int xRight = 215 * Game.SCALE;
        g.drawImage(arrows[1][rightArrowIndex], xRight, y, size, size, null);
    }

    private void drawTitle(Graphics g){
        g.setColor(Color.WHITE);
        g.setFont(nesFont.deriveFont(40f));
        FontMetrics fm = g.getFontMetrics(g.getFont());

        String text1 = "USER";
        String text2 = "SELECTION";
        int textWidth1 = fm.stringWidth(text1);
        int textWidth2 = fm.stringWidth(text2);
        int totalWidth = textWidth1 + textWidth2;
        int spacing = 5 * Game.SCALE; // Adjust this value to change the spacing between the words
        int x = (Game.GAME_WIDTH - (totalWidth + spacing)) / 2;
        int y = 23 * Game.SCALE + fm.getHeight();

        g.drawString(text1, x, y);
        g.drawString(text2, x + textWidth1 + spacing, y);
    }

    private void drawUserName(Graphics g){
        g.setColor(usersManager.getUserColor1(selectedUser.getProfilePictureIndex()));
        g.setFont(retroFont.deriveFont(50f));
        FontMetrics fm = g.getFontMetrics(g.getFont());

        String text = selectedUser.getName().toUpperCase();
        int x = 100 * Game.SCALE;
        int y = 60 * Game.SCALE + fm.getHeight();

        g.drawString(text, x, y);
    }

    private void drawUserPicture(Graphics g){

        int x = 45 * Game.SCALE;
        int y = 67 * Game.SCALE;
        int rectSize = (int) ((18 * 2.8 + 1) * Game.SCALE);

        //draw border for the picture
        g.setColor(Color.WHITE);
        g.drawRect(x, y, rectSize, rectSize);

        //draw the picture
        g.drawImage(usersManager.getUserPicture(selectedUser.getProfilePictureIndex()), x + 1, y + 1, rectSize - 1, rectSize - 1, null);
    }

    private void drawUserDetails(Graphics g){

        g.setColor(Color.WHITE);
        g.setFont(retroFont.deriveFont(22f));
        FontMetrics fm = g.getFontMetrics(g.getFont());

        String text1 = "- Best Score: " + selectedUser.getBestScore();
        String text2 = "- Played Games: " + selectedUser.getPlayedGames();
        String text3 = "- Won Games: " + selectedUser.getWonGames();
        int x = 95 * Game.SCALE + fm.getHeight();
        int y = 95 * Game.SCALE;

        g.drawString(text1, x, y);
        g.drawString(text2, x, y + fm.getHeight());
        g.drawString(text3, x, y + 2 * fm.getHeight());
    }

    private void drawControls(Graphics g) {
        g.setFont(retroFont.deriveFont(22f));
        FontMetrics fm = g.getFontMetrics(g.getFont());

        String text1Part1 = "Press ";
        String text1Part2 = "ENTER";
        String text1Part3 = " to select";
        String text2Part1 = "Press ";
        String text2Part2 = "C";
        String text2Part3 = " to create a new user";

        int x = 40 * Game.SCALE + fm.getHeight();
        int y = 95 * Game.SCALE + 4 * fm.getHeight();

        // Draw first suggestion
        g.setColor(Color.WHITE);
        g.drawString(text1Part1, x, y);
        int width1 = fm.stringWidth(text1Part1);

        g.setColor(usersManager.getUserColor2(selectedUser.getProfilePictureIndex()));
        g.drawString(text1Part2, x + width1, y);
        int width2 = fm.stringWidth(text1Part2);

        g.setColor(Color.WHITE);
        g.drawString(text1Part3, x + width1 + width2, y);

        // Draw second suggestion
        g.setColor(Color.WHITE);
        g.drawString(text2Part1, x, y + fm.getHeight());
        int width3 = fm.stringWidth(text2Part1);

        g.setColor(usersManager.getUserColor2(selectedUser.getProfilePictureIndex()));
        g.drawString(text2Part2, x + width3, y + fm.getHeight());
        int width4 = fm.stringWidth(text2Part2);

        g.setColor(Color.WHITE);
        g.drawString(text2Part3, x + width3 + width4, y + fm.getHeight());
    }

    private void loadArrows(){
        arrows = new BufferedImage[2][2];
        BufferedImage img = LoadSave.GetSprite(LoadSave.ARROWS_LEFT_RIGHT);

        arrows[0][0] = img.getSubimage(0, 0, 10, 10);
        arrows[0][1] = img.getSubimage(10, 0, 10, 10);
        arrows[1][0] = img.getSubimage(0, 10, 10, 10);
        arrows[1][1] = img.getSubimage(10, 10, 10, 10);
    }

    public void updateUserList(){
        users = usersManager.getUsers();
        selectedUser = users.getFirst();
    }

    // -------------- Getters and Setters --------------
    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setLeftArrowIndex(int leftArrowIndex) {
        this.leftArrowIndex = leftArrowIndex;
    }

    public void setRightArrowIndex(int rightArrowIndex) {
        this.rightArrowIndex = rightArrowIndex;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }
}
