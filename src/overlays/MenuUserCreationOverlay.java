package overlays;

import gameStates.Menu;
import main.Game;
import users.UsersManager;
import utilz.LoadSave;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class MenuUserCreationOverlay extends MenuOverlay {
    private final UsersManager usersManager = UsersManager.getInstance();

    private String newUserName = "";
    private int newUserPictureIndex = -1;
    private boolean blink = true;

    private BufferedImage questionMark;
    private BufferedImage[][] arrows;
    private int upArrowIndex = 0;
    private int downArrowIndex = 0;

    private boolean userNameAlreadyExists = false;
    private boolean enterKeyDeactivated = true;

    public MenuUserCreationOverlay(Menu menu) {
        super(menu);
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
    public void update() {

        // Check if the username already exists
        userNameAlreadyExists = usersManager.doesUserAlreadyExist(newUserName);

        // update enterKeyStatus
        if (newUserName.isEmpty() || newUserPictureIndex == -1 || userNameAlreadyExists)
            enterKeyDeactivated = true;
        else
            enterKeyDeactivated = false;
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
        int rectWidth = (int) (Game.GAME_WIDTH * 0.7);
        int rectHeight = (int) (Game.GAME_HEIGHT * 0.465);
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

        int x = 61 * Game.SCALE;
        int size = 18 * Game.SCALE;

        //draw left arrow
        int yUp = 76 * Game.SCALE - size;
        g.drawImage(arrows[0][upArrowIndex], x, yUp, size, size, null);

        //draw right arrow
        int yDown = 119 * Game.SCALE;
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
        int spacing = 5 * Game.SCALE; // Adjust this value to change the spacing between the words
        int x = (Game.GAME_WIDTH - (totalWidth + spacing)) / 2;
        int y = 23 * Game.SCALE + fm.getHeight();

        g.drawString(text1, x, y);
        g.drawString(text2, x + textWidth1 + spacing, y);
    }

    private void drawUserName(Graphics g) {
        g.setColor(usersManager.getUserColor1(newUserPictureIndex));
        g.setFont(retroFont.deriveFont(50f));
        FontMetrics fm = g.getFontMetrics(g.getFont());

        String text = newUserName.toUpperCase();
        int x = 100 * Game.SCALE;
        int y = 85 * Game.SCALE + fm.getHeight();

        g.drawString(text, x, y);

        // Draw the blinking cursor
        if (blink) {
            int cursorX = x + fm.stringWidth(text) + 3 * Game.SCALE;
            Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(new BasicStroke(3 * Game.SCALE));
            g2d.drawLine(cursorX, y - 13 * Game.SCALE, cursorX, y + 1 * Game.SCALE);
        }
    }

    private void drawUserPicture(Graphics g){

        //draw border for the picture
        int x = 45 * Game.SCALE;
        int y = 73 * Game.SCALE;
        int rectSize = (int) ((18 * 2.8 + 1) * Game.SCALE);


        g.setColor(Color.WHITE);
        g.drawRect(x, y, rectSize, rectSize);

        if (newUserPictureIndex == -1)
            // draw question mark
            g.drawImage(questionMark, x + 1, y + 1, rectSize - 1, rectSize - 1, null);

        else
            // draw the picture
            g.drawImage(usersManager.getUserPicture(newUserPictureIndex), x + 1, y + 1, rectSize - 1, rectSize - 1, null);
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

        int x = 40 * Game.SCALE + fm.getHeight();
        int y = 108  * Game.SCALE + 4 * fm.getHeight();

        if (enterKeyDeactivated) {
            // Draw first suggestion light grey (inactive)
            g.setColor(new Color(0x888888));
            g.drawString("Press ENTER to confirm", x, y);
        }

        if (!enterKeyDeactivated) {
            // Draw first suggestion (active)
            g.setColor(Color.WHITE);
            g.drawString(text1Part1, x, y);
            int width1 = fm.stringWidth(text1Part1);

            g.setColor(usersManager.getUserColor2(newUserPictureIndex));
            g.drawString(text1Part2, x + width1, y);
            int width2 = fm.stringWidth(text1Part2);

            g.setColor(Color.WHITE);
            g.drawString(text1Part3, x + width1 + width2, y);
        }

        // Draw second suggestion
        g.setColor(Color.WHITE);
        g.drawString(text2Part1, x, y + fm.getHeight());
        int width3 = fm.stringWidth(text2Part1);

        g.setColor(usersManager.getUserColor2(newUserPictureIndex));
        g.drawString(text2Part2, x + width3, y + fm.getHeight());
        int width4 = fm.stringWidth(text2Part2);

        g.setColor(Color.WHITE);
        g.drawString(text2Part3, x + width3 + width4, y + fm.getHeight());
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

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            menu.setUserSelectionOverlay(true);
            menu.setUserCreationOverlay(false);
        }
        else if (e.getKeyCode() == KeyEvent.VK_ENTER && !enterKeyDeactivated) {
            usersManager.createUser(newUserName, newUserPictureIndex);
            menu.setUserSelectionOverlay(false);
            menu.setUserCreationOverlay(false);
        }
        else if (e.getKeyCode() == KeyEvent.VK_UP) {
            upArrowIndex = 1; // animate the arrow

            if (newUserPictureIndex + 1 == usersManager.getUserPicturesCount())
                newUserPictureIndex = 0;
            else
                newUserPictureIndex++;
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            downArrowIndex = 1; // animate the arrow

            if (newUserPictureIndex - 1 == -1 || newUserPictureIndex - 1 == -2)
                newUserPictureIndex = usersManager.getUserPicturesCount() - 1;
            else
                newUserPictureIndex--;
        } else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            if (!newUserName.isEmpty()) {
                newUserName = newUserName.substring(0, newUserName.length() - 1);
            }
        } else {
            char keyChar = Character.toLowerCase(e.getKeyChar());
            if (Character.isLetterOrDigit(keyChar) && newUserName.length() < 8) {
                newUserName += keyChar;
            }
        }
    }

    private void drawSuggestions(Graphics g) {

        if (blink) return;

        g.setColor(new Color(0xFF6961));
        g.setFont(retroFont.deriveFont(26f));
        FontMetrics fm = g.getFontMetrics(g.getFont());
        int y = 170 * Game.SCALE + fm.getHeight();

        if (userNameAlreadyExists) {
            String text = "User already exists!";
            int x = (Game.GAME_WIDTH - fm.stringWidth(text)) / 2;
            g.drawString(text, x, y);
        }

        else if (newUserPictureIndex == -1) {
            String text = "Please select a picture!";
            int x = (Game.GAME_WIDTH - fm.stringWidth(text)) / 2;
            g.drawString(text, x, y);
        }

        else if (newUserName.isEmpty()) {
            String text = "Username cannot be empty!";
            int x = (Game.GAME_WIDTH - fm.stringWidth(text)) / 2;
            g.drawString(text, x, y);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP)
            upArrowIndex = 0; // stop animating the arrow

        if (e.getKeyCode() == KeyEvent.VK_DOWN)
            downArrowIndex = 0; // stop animating the arrow
    }
}