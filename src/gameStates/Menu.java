package gameStates;

import main.Game;
import profiles.UsersManager;
import overlays.MenuLeaderBoardOverlay;
import overlays.MenuUserSelectionOverlay;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import static utilz.Constants.ANIMATION_SPEED;
import static utilz.Constants.MenuConstants.*;
import static utilz.Constants.Overlays.BUD_RED_COLOR;

public class Menu extends State implements StateMethods {
    private final UsersManager usersManager;

    private final Font nesFont;
    private final Font retroFont;

    private String[] dotAnimations = {"", ".", "..", "..."};
    private int dotAnimationIndex, animationTick;

    private float suggestionX = 0;
    private String suggestions;
    private int suggestionsWidth;
    private int selectionIndex = 0;

    // Overlays
    private final MenuUserSelectionOverlay menuUserSelectionOverlay = new MenuUserSelectionOverlay(this);
    private final MenuLeaderBoardOverlay menuLeaderBoardOverlay = new MenuLeaderBoardOverlay(this);

    // Overlays Booleans
    private boolean UserSelectionOverlay = false;
    private boolean ScoreBoardOverlay = false;

    public Menu(Game game) {
        super(game);
        this.usersManager = UsersManager.getInstance();
        this.nesFont = LoadSave.GetNesFont();
        this.retroFont = LoadSave.GetRetroGamingFont();

        generateSuggestions();
    }

    @Override
    public void update() {
        updateAnimationTick();

        if (UserSelectionOverlay)
            menuUserSelectionOverlay.update();
        else if (ScoreBoardOverlay)
            menuLeaderBoardOverlay.update();
    }

    @Override
    public void draw(Graphics g) {

        String text = SPACE;
        g.setFont(retroFont.deriveFont(24f));
        FontMetrics fm = g.getFontMetrics(g.getFont());
        System.out.println(fm.stringWidth(text));

        drawTittle(g);
        drawGratings(g);
        drawSelections(g);
        drawSuggestions(g);

        if (UserSelectionOverlay)
            menuUserSelectionOverlay.draw(g);
        else if (ScoreBoardOverlay)
            menuLeaderBoardOverlay.draw(g);
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
            suggestionX = Game.GAME_WIDTH + 10 * Game.SCALE;
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
        int spacing = 5 * Game.SCALE; // Adjust this value to change the spacing between the words
        int x = (Game.GAME_WIDTH - (totalWidth + spacing)) / 2;
        int y = 23 * Game.SCALE + fm.getHeight();

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
            text2 = usersManager.getCurrentUser().getName() + "!";
            textWidth2 = fm.stringWidth(text2);
        }

        int totalWidth = textWidth1 + textWidth2;
        int spacing = 0 * Game.SCALE; // Adjust this value to change the spacing between the words
        int x = (Game.GAME_WIDTH - (totalWidth + spacing)) / 2;
        int y = 40 * Game.SCALE + fm.getHeight();

        g.drawString(text1, x, y);
        g.drawString(text2, x + textWidth1 + spacing, y);
    }

    private void drawSelections(Graphics g) {

        String text0 = "Play!";           // selectionIndex = 0
        String text1 = "Change User";     // selectionIndex = 1
        String text2 = "Score Board";     // selectionIndex = 2
        String text3 = "Quit";            // selectionIndex = 3

        int text1Y = 75 * Game.SCALE;
        int text2Y = 100 * Game.SCALE;
        int text3Y = 125 * Game.SCALE;
        int text4Y = 150 * Game.SCALE;

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

        int textX = (Game.GAME_WIDTH - fm.stringWidth(text)) / 2;   // center in the screen
        int textY = lineY + fm.getHeight()/2;                           // center in the line

        g.drawString(text, textX, textY);
    }


    private void drawSuggestions(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(retroFont.deriveFont(24f));
        g.drawString(suggestions, (int) suggestionX, Game.GAME_HEIGHT - 4 * Game.SCALE);
    }

    private void generateSuggestions() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < SUGGESTIONS.length; i++) {
            sb.append(SUGGESTIONS[i]).append(SPACE);
            suggestionsWidth += SUGGESTIONS_WHIDTHS[i] + SPACE_WHIDTH;
        }
        suggestions = sb.toString();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (UserSelectionOverlay) {
            menuUserSelectionOverlay.keyPressed(e);
            return;
        }

        if (ScoreBoardOverlay) {
            menuLeaderBoardOverlay.keyPressed(e);
            return;
        }

        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP, KeyEvent.VK_W:
                selectionIndex--;
                if (selectionIndex < 0) {
                    selectionIndex = 3;
                }
                break;
            case KeyEvent.VK_DOWN, KeyEvent.VK_S:
                selectionIndex++;
                if (selectionIndex > 3) {
                    selectionIndex = 0;
                }
                break;

            case KeyEvent.VK_ENTER:
                switch (selectionIndex) {
                    case 0:
                        // Play
                        break;
                    case 1:
                        // Change User
                        UserSelectionOverlay = true;
                        ScoreBoardOverlay = false;
                        break;
                    case 2:
                        // Score Board
                        UserSelectionOverlay = false;
                        ScoreBoardOverlay = true;
                        break;
                    case 3:
                        // Quit
                        System.exit(0);
                        break;
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    // Getters and Setters

    public void setUserSelectionOverlay(boolean userSelectionOverlay) {
        UserSelectionOverlay = userSelectionOverlay;
    }

    public void setScoreBoardOverlay(boolean scoreBoardOverlay) {
        ScoreBoardOverlay = scoreBoardOverlay;
    }
}
