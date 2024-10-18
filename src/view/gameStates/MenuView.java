package view.gameStates;

import entities.TwinkleBubbleManager;
import model.gameStates.MenuModel;
import main.Game;
import users.UsersManager;
import model.utilz.Constants;
import model.utilz.LoadSave;

import java.awt.*;

import static model.utilz.Constants.ANIMATION_SPEED;
import static model.utilz.Constants.MenuConstants.*;
import static model.utilz.Constants.MenuConstants.SPACE_WHIDTH;

public class MenuView {
    private final MenuModel menuModel;
    private final Game game;

    private final TwinkleBubbleManager twinkleBubbleManager = TwinkleBubbleManager.getInstance();
    private final UsersManager usersManager = UsersManager.getInstance();

    private Font nesFont;
    private Font retroFont;

    private String[] dotAnimations = {"", ".", "..", "..."};
    private int dotAnimationIndex, animationTick;

    private float suggestionX = 0;
    private String suggestions;
    private int suggestionsWidth;

    public MenuView(MenuModel menuModel, Game game) {
        this.menuModel = menuModel;
        this.game = game;
        this.nesFont = new Font("NesFont", Font.PLAIN, 20);
        this.retroFont = new Font("RetroFont", Font.PLAIN, 20);

        this.nesFont = LoadSave.GetNesFont();
        this.retroFont = LoadSave.GetRetroGamingFont();
        generateSuggestions();
    }

    public void update() {

        if (!menuModel.isUserSelectionOverlayActive() && !menuModel.isUserCreationOverlayActive() && !menuModel.isScoreBoardOverlayActive())
            updateAnimationTick();
    }

    public void draw(Graphics g) {
        twinkleBubbleManager.update();
        twinkleBubbleManager.draw(g);

        g.setColor(new Color(0, 0, 0, 40));
        g.fillRect(0, 0, Constants.GAME_WIDTH, Constants.GAME_HEIGHT);

        if (menuModel.isScoreBoardOverlayActive()) {
            game.getMenuScoreBoardOverlayView().draw(g);
            return;
        }

        drawGratings(g);

        if (menuModel.isUserSelectionOverlayActive())
            game.getMenuUserSelectionOverlayView().draw(g);
        else if (menuModel.isUserCreationOverlayActive())
            game.getMenuUserCreationOverlayView().draw(g);
        else {
            drawTittle(g);
            drawSelections(g);
            drawSuggestions(g);
        }
    }

    private void updateAnimationTick() {
        animationTick++;
        if (animationTick >= ANIMATION_SPEED) {
            animationTick = 0;
            dotAnimationIndex++;
            if (dotAnimationIndex >= dotAnimations.length) {
                dotAnimationIndex = 0;
            }
        }

        // Update the x-coordinate for the moving text
        suggestionX -= SUGGESTION_SPEED;
        if (suggestionX < - suggestionsWidth)
            suggestionX = Constants.GAME_WIDTH + 10 * Constants.SCALE;
    }

    private void drawTittle(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(nesFont.deriveFont(45f));
        FontMetrics fm = g.getFontMetrics(g.getFont());

        String text1 = "BUBBLE";
        String text2 = "BOBBLE";
        int textWidth1 = fm.stringWidth(text1);
        int textWidth2 = fm.stringWidth(text2);
        int totalWidth = textWidth1 + textWidth2;
        int spacing = 5 * Constants.SCALE; // Adjust this value to change the spacing between the words
        int x = (Constants.GAME_WIDTH - (totalWidth + spacing)) / 2;
        int y = 23 * Constants.SCALE + fm.getHeight();

        g.drawString(text1, x, y);
        g.drawString(text2, x + textWidth1 + spacing, y);
    }

    private void drawGratings(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(retroFont.deriveFont(26f));
        FontMetrics fm = g.getFontMetrics(g.getFont());

        String text1 = "HI, ";
        int textWidth1 = fm.stringWidth(text1);

        String text2 = dotAnimations[dotAnimationIndex];
        int textWidth2 = fm.stringWidth("...");

        if (usersManager.getCurrentUser() != null) {
            text2 = usersManager.getCurrentUser().getName().toUpperCase() + "!";
            textWidth2 = fm.stringWidth(text2);
        }

        int totalWidth = textWidth1 + textWidth2;
        int spacing = 0 * Constants.SCALE; // Adjust this value to change the spacing between the words
        int x = (Constants.GAME_WIDTH - (totalWidth + spacing)) / 2;
        int y = 40 * Constants.SCALE + fm.getHeight();

        g.drawString(text1, x, y);
        g.drawString(text2, x + textWidth1 + spacing, y);
    }

    private void drawSelections(Graphics g) {

        String text0 = "Play!";           // selectionIndex = 0
        String text1 = "Change User";     // selectionIndex = 1
        String text2 = "Score Board";     // selectionIndex = 2
        String text3 = "Quit";            // selectionIndex = 3

        int text1Y = 75 * Constants.SCALE;
        int text2Y = 100 * Constants.SCALE;
        int text3Y = 125 * Constants.SCALE;
        int text4Y = 150 * Constants.SCALE;

        drawSelectionText(g, text0, text1Y, menuModel.getSelectionIndex() == 0);
        drawSelectionText(g, text1, text2Y, menuModel.getSelectionIndex() == 1);
        drawSelectionText(g, text2, text3Y, menuModel.getSelectionIndex() == 2);
        drawSelectionText(g, text3, text4Y, menuModel.getSelectionIndex() == 3);
    }

    private void drawSelectionText(Graphics g, String text, int lineY, boolean selected) {

        if (selected) {
            g.setColor(Color.WHITE);
            g.setFont(retroFont.deriveFont(42f));
        } else {
            g.setColor(new Color(0xCCCCCC)); // Light Gray
            g.setFont(retroFont.deriveFont(32f));
        }

        FontMetrics fm = g.getFontMetrics(g.getFont());
        int textX = (Constants.GAME_WIDTH - fm.stringWidth(text)) / 2;   // center in the screen
        int textY = lineY + fm.getHeight()/2;                           // center in the line

        g.drawString(text, textX, textY);
    }

    private void drawSuggestions(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(retroFont.deriveFont(24f));
        g.drawString(suggestions, (int) suggestionX, Constants.GAME_HEIGHT - 4 * Constants.SCALE);
    }

    private void generateSuggestions() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < SUGGESTIONS.length; i++) {
            sb.append(SUGGESTIONS[i]).append(SPACE);
            suggestionsWidth += SUGGESTIONS_WHIDTHS[i] + SPACE_WHIDTH;
        }
        suggestions = sb.toString();
    }
}

