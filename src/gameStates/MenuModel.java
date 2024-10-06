package gameStates;

import static utilz.Constants.MenuConstants.*;

import main.Game;
import utilz.Constants;
import utilz.LoadSave;
import users.UsersManager;
import entities.TwinkleBubbleManager;

import java.awt.*;

import static utilz.Constants.ANIMATION_SPEED;

public class MenuModel extends State implements StateMethods {
    private final UsersManager usersManager;
    private final TwinkleBubbleManager twinkleBubbleManager = TwinkleBubbleManager.getInstance();

    private final Font nesFont;
    private final Font retroFont;

    private String[] dotAnimations = {"", ".", "..", "..."};
    private int dotAnimationIndex, animationTick;

    private float suggestionX = 0;
    private String suggestions;
    private int suggestionsWidth;
    private int selectionIndex = 0;

    // Overlays Booleans
    private boolean userSelectionOverlayActive = true;
    private boolean userCreationOverlayActive = false;
    private boolean scoreBoardOverlayActive = false;

    public MenuModel(Game game) {
        super(game);
        this.usersManager = UsersManager.getInstance();
        this.nesFont = LoadSave.GetNesFont();
        this.retroFont = LoadSave.GetRetroGamingFont();

        generateSuggestions();
    }

    @Override
    public void update() {
        twinkleBubbleManager.update();

        if (userSelectionOverlayActive)
            game.getMenuUserSelectionOverlayModel().update();
        if (userCreationOverlayActive)
            game.getMenuUserCreationOverlayModel().update();
        else if (scoreBoardOverlayActive)
            game.getMenuScoreBoardOverlayModel().update();
        else
            updateAnimationTick();
    }

    public void draw(Graphics g) {
        twinkleBubbleManager.draw(g);

        g.setColor(new Color(0, 0, 0, 40));
        g.fillRect(0, 0, Constants.GAME_WIDTH, Constants.GAME_HEIGHT);

        if (scoreBoardOverlayActive) {
            game.getMenuScoreBoardOverlayView().draw(g);
            return;
        }

        drawGratings(g);

        if (userSelectionOverlayActive)
            game.getMenuUserSelectionOverlayView().draw(g);
        else if (userCreationOverlayActive)
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

        drawSelectionText(g, text0, text1Y, selectionIndex == 0);
        drawSelectionText(g, text1, text2Y, selectionIndex == 1);
        drawSelectionText(g, text2, text3Y, selectionIndex == 2);
        drawSelectionText(g, text3, text4Y, selectionIndex == 3);
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

    // ------------ Getters and Setters ------------

    public void setUserSelectionOverlayActive(boolean userSelectionOverlayActive) {
        this.userSelectionOverlayActive = userSelectionOverlayActive;
    }

    public void setUserCreationOverlayActive(boolean userCreationOverlayActive) {
        this.userCreationOverlayActive = userCreationOverlayActive;
    }

    public void setScoreBoardOverlayActive(boolean scoreBoardOverlayActive) {
        this.scoreBoardOverlayActive = scoreBoardOverlayActive;
    }

    public boolean isUserSelectionOverlayActive() {
        return userSelectionOverlayActive;
    }

    public boolean isUserCreationOverlayActive() {
        return userCreationOverlayActive;
    }

    public boolean isScoreBoardOverlayActive() {
        return scoreBoardOverlayActive;
    }


    public int getSelectionIndex() {
        return selectionIndex;
    }

    public void setSelectionIndex(int selectionIndex) {
        this.selectionIndex = selectionIndex;
    }

    public void increaseSelectionIndex() {
        selectionIndex++;
    }

    public void decreaseSelectionIndex() {
        selectionIndex--;
    }
}
