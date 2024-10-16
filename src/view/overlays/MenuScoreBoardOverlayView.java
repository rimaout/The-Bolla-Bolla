package view.overlays;

import model.overlays.MenuScoreBoardOverlayModel;
import users.UsersManager;
import model.utilz.Constants;
import model.utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MenuScoreBoardOverlayView extends MenuOverlayView {
    private final MenuScoreBoardOverlayModel menuSoreBoardOverlayModel;
    private final UsersManager usersManager = UsersManager.getInstance();
    private BufferedImage questionMark;

    public MenuScoreBoardOverlayView(MenuScoreBoardOverlayModel menuScoreBoardOverlayModel) {
        this.menuSoreBoardOverlayModel = menuScoreBoardOverlayModel;
        questionMark = LoadSave.GetSprite(LoadSave.QUESTION_MARK_IMAGE);
    }

    @Override
    public void draw(Graphics g) {
        drawTitle(g);
        drawCommand(g);

        int startingY = 55 * Constants.SCALE;
        for (int i = 0; i < 4; i++) {   // draw the top 5 users
            drawUser(g, i, startingY);
        }
    }

    private void drawTitle(Graphics g){
        g.setColor(Color.WHITE);
        g.setFont(nesFont.deriveFont(40f));
        FontMetrics fm = g.getFontMetrics(g.getFont());

        String text1 = "SCORE";
        String text2 = "BOARD";
        int textWidth1 = fm.stringWidth(text1);
        int textWidth2 = fm.stringWidth(text2);
        int totalWidth = textWidth1 + textWidth2;
        int spacing = 5 * Constants.SCALE; // Adjust this value to change the spacing between the words
        int x = (Constants.GAME_WIDTH - (totalWidth + spacing)) / 2;
        int y = 23 * Constants.SCALE + fm.getHeight();

        g.drawString(text1, x, y);
        g.drawString(text2, x + textWidth1 + spacing, y);
    }

    private void drawCommand(Graphics g) {
        String textPart1 = "Press ";
        String textPart2 = "ESC";
        String textPart3 = " to go back";

        g.setFont(retroFont.deriveFont(26f));
        FontMetrics fm = g.getFontMetrics(g.getFont());

        int width1 = fm.stringWidth(textPart1);
        int width2 = fm.stringWidth(textPart2);
        int width3 = fm.stringWidth(textPart3);
        int totalWidth = width1 + width2 + width3;

        int y = Constants.GAME_HEIGHT - 25 * Constants.SCALE;
        int x = (Constants.GAME_WIDTH - totalWidth) / 2;

        g.setColor(Color.WHITE);
        g.drawString(textPart1, x, y);

        g.setColor(usersManager.getUserColor2(-1));
        g.drawString(textPart2, x + width1, y);

        g.setColor(Color.WHITE);
        g.drawString(textPart3, x + width1 + width2, y);
    }

    private void drawUser(Graphics g, int positionIndex, int startingY) {
        drawUserBox(g, positionIndex, startingY);
        drawUserPicture(g, positionIndex, startingY);
        drawUserName(g, positionIndex, startingY);
        drawUserScore(g, positionIndex, startingY);
    }

    private void drawUserBox(Graphics g, int positionIndex, int startingY) {
        Graphics2D g2d = (Graphics2D) g;

        int rectWidth = (int) (Constants.GAME_WIDTH * 0.6);
        int rectHeight = 30 * Constants.SCALE;
        int x = (Constants.GAME_WIDTH - rectWidth) / 2;
        int y = startingY + positionIndex * rectHeight;

        // Set the thickness of the border
        // Set the thickness of the border
        int borderThickness = (int) 1 * Constants.SCALE;
        g2d.setStroke(new BasicStroke(borderThickness));
        g.setColor(Color.WHITE);
        g.drawRect(x, y, rectWidth, rectHeight);

        // Set the color to black for the filled rectangle
        g.setColor(Color.BLACK);
        g.fillRect(x + borderThickness, y + borderThickness, rectWidth - 2 * borderThickness, rectHeight - 2 * borderThickness);
    }

    private void drawUserPicture(Graphics g, int positionIndex, int startingY) {
        int rectWidth = (int) (Constants.GAME_WIDTH * 0.6);
        int rectHeight = 30 * Constants.SCALE;
        int x = (Constants.GAME_WIDTH - rectWidth) / 2;
        int y = startingY + positionIndex * rectHeight;

        if (positionIndex >= menuSoreBoardOverlayModel.getOrderedUsers().size())
            g.drawImage(questionMark, x + 10 * Constants.SCALE, y + 5 * Constants.SCALE, 20 * Constants.SCALE, 20 * Constants.SCALE, null);
        else
            g.drawImage(usersManager.getUserPicture(menuSoreBoardOverlayModel.getOrderedUsers().get(positionIndex).getProfilePictureIndex()), x + 10 * Constants.SCALE, y + 5 * Constants.SCALE, 20 * Constants.SCALE, 20 * Constants.SCALE, null);
    }

    private void drawUserName(Graphics g, int positionIndex, int startingY) {

        String text = "________"; // Empty User

        if (positionIndex < menuSoreBoardOverlayModel.getOrderedUsers().size())
            text = menuSoreBoardOverlayModel.getOrderedUsers().get(positionIndex).getName().toUpperCase();

        int rectWidth = (int) (Constants.GAME_WIDTH * 0.6);
        int rectHeight = 30 * Constants.SCALE;
        int x = (Constants.GAME_WIDTH - rectWidth) / 2;
        int y = startingY + positionIndex * rectHeight;

        g.setColor(Color.WHITE);
        g.setFont(retroFont.deriveFont(20f));
        FontMetrics fm = g.getFontMetrics(g.getFont());

        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();
        int textX = x + (rectWidth - textWidth) / 2;
        int textY = y + (rectHeight + textHeight) / 2;

        g.drawString(text, textX, textY);
    }

    private void drawUserScore(Graphics g, int positionIndex, int startingY) {
        String text = "0"; // Empty User
        if (positionIndex < menuSoreBoardOverlayModel.getOrderedUsers().size())
            text = String.valueOf(menuSoreBoardOverlayModel.getOrderedUsers().get(positionIndex).getBestScore());

        int rectWidth = (int) (Constants.GAME_WIDTH * 0.6);
        int rectHeight = 30 * Constants.SCALE;
        int x = (Constants.GAME_WIDTH - rectWidth) / 2;
        int y = startingY + positionIndex * rectHeight;

        g.setColor(Color.WHITE);
        g.setFont(nesFont.deriveFont(18f));
        FontMetrics fm = g.getFontMetrics(g.getFont());

        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();
        int textX = x + rectWidth - textWidth - 10 * Constants.SCALE;
        int textY = y + (rectHeight + textHeight) / 2;

        g.drawString(text, textX, textY);
    }
}
