package view.gameStates;

import view.utilz.Load;
import view.overlays.menuOverlays.MenuTwinkleBubbleManager;
import controller.GameController;
import model.utilz.Constants;
import model.gameStates.MenuModel;
import model.users.UsersManagerModel;

import java.awt.*;

import static view.utilz.Constants.ANIMATION_SPEED;
import static view.utilz.Constants.MenuConstants.*;
import static view.utilz.Constants.MenuConstants.SPACE_WIDTH;

/**
 * The MenuView class represents the view for the {@link MenuModel}.
 * It handles drawing the menu elements, updating animations, and managing user interactions.
 */
public class MenuView {
    private final MenuModel menuModel;
    private final GameController gameController;

    private final MenuTwinkleBubbleManager menuTwinkleBubbleManager = MenuTwinkleBubbleManager.getInstance();
    private final UsersManagerModel usersManagerModel = UsersManagerModel.getInstance();

    private Font nesFont;
    private Font retroFont;

    private String[] dotAnimations = {"", ".", "..", "..."};
    private int dotAnimationIndex, animationTick;

    private float suggestionX = 0;
    private String suggestions;
    private int suggestionsWidth;

    /**
     * Constructs a MenuView with the specified MenuModel and GameController.
     *
     * @param menuModel the model for the menu screen
     * @param gameController the controller for the game
     */
    public MenuView(MenuModel menuModel, GameController gameController) {
        this.menuModel = menuModel;
        this.gameController = gameController;
        this.nesFont = new Font("NesFont", Font.PLAIN, 20);
        this.retroFont = new Font("RetroFont", Font.PLAIN, 20);

        this.nesFont = Load.GetNesFont();
        this.retroFont = Load.GetRetroGamingFont();
        generateSuggestions();
    }

    /**
     * Updates the menu view, including animations and user interactions.
     */
    public void update() {
        if (!menuModel.isUserSelectionOverlayActive() && !menuModel.isUserCreationOverlayActive() && !menuModel.isScoreBoardOverlayActive())
            updateAnimationTick();
    }

    /**
     * Draws the menu screen elements, including the title, selections, and suggestions.
     *
     * @param g the Graphics object to draw with
     */
    public void draw(Graphics g) {
        menuTwinkleBubbleManager.update();
        menuTwinkleBubbleManager.draw(g);

        g.setColor(new Color(0, 0, 0, 40));
        g.fillRect(0, 0, Constants.GAME_WIDTH, Constants.GAME_HEIGHT);

        if (menuModel.isScoreBoardOverlayActive()) {
            gameController.getMenuScoreBoardOverlayView().draw(g);
            return;
        }

        drawGratings(g);

        if (menuModel.isUserSelectionOverlayActive())
            gameController.getMenuUserSelectionOverlayView().draw(g);
        else if (menuModel.isUserCreationOverlayActive())
            gameController.getMenuUserCreationOverlayView().draw(g);
        else {
            drawTittle(g);
            drawSelections(g);
            drawSuggestions(g);
        }
    }

    /**
     * Updates the animation tick for the dot animations and moving text.
     */
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

    /**
     * Draws the title of the game on the menu screen.
     *
     * @param g the Graphics object to draw with
     */
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

    /**
     * Draws the greeting message on the menu screen.
     *
     * @param g the Graphics object to draw with
     */
    private void drawGratings(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(retroFont.deriveFont(26f));
        FontMetrics fm = g.getFontMetrics(g.getFont());

        String text1 = "HI, ";
        int textWidth1 = fm.stringWidth(text1);

        String text2 = dotAnimations[dotAnimationIndex];
        int textWidth2 = fm.stringWidth("...");

        if (usersManagerModel.getCurrentUser() != null) {
            text2 = usersManagerModel.getCurrentUser().getName().toUpperCase() + "!";
            textWidth2 = fm.stringWidth(text2);
        }

        int totalWidth = textWidth1 + textWidth2;
        int spacing = 0 * Constants.SCALE; // Adjust this value to change the spacing between the words
        int x = (Constants.GAME_WIDTH - (totalWidth + spacing)) / 2;
        int y = 40 * Constants.SCALE + fm.getHeight();

        g.drawString(text1, x, y);
        g.drawString(text2, x + textWidth1 + spacing, y);
    }

    /**
     * Draws the selection options on the menu screen.
     *
     * @param g the Graphics object to draw with
     */
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

    /**
     * Draws a single selection option on the menu screen.
     *
     * @param g the Graphics object to draw with
     * @param text the text of the selection option
     * @param lineY the y-coordinate of the selection option
     * @param selected whether the selection option is currently selected
     */
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

    /**
     * Draws the suggestions text on the menu screen.
     *
     * @param g the Graphics object to draw with
     */
    private void drawSuggestions(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(retroFont.deriveFont(24f));
        g.drawString(suggestions, (int) suggestionX, Constants.GAME_HEIGHT - 4 * Constants.SCALE);
    }

    /**
     * Generates the suggestions text from the predefined suggestions array.
     */
    private void generateSuggestions() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < SUGGESTIONS.length; i++) {
            sb.append(SUGGESTIONS[i]).append(SPACE);
            suggestionsWidth += SUGGESTIONS_WIDTHS[i] + SPACE_WIDTH;
        }
        suggestions = sb.toString();
    }
}