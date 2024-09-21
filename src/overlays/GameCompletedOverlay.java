package overlays;

import audio.AudioPlayer;
import gameStates.GameState;
import gameStates.Playing;
import main.Game;
import utilz.Constants.AudioConstants;

import java.awt.*;
import java.awt.event.KeyEvent;

import static utilz.Constants.Overlays.BUD_GREEN_COLOR;
import static utilz.Constants.Overlays.BUD_RED_COLOR;



public class GameCompletedOverlay extends GameOverlay {

    public GameCompletedOverlay(Playing playing) {
        super(playing);
    }

    @Override
    protected void drawTitle(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(nesFont.deriveFont(38f));
        FontMetrics fm = g.getFontMetrics(g.getFont());

        String text1 = "GAME";
        String text2 = "COMPLETED";
        int textWidth1 = fm.stringWidth(text1);
        int textWidth2 = fm.stringWidth(text2);
        int totalWidth = textWidth1 + textWidth2;
        int spacing = 4 * Game.SCALE; // Adjust this value to change the spacing between the words
        int x = (Game.GAME_WIDTH - (totalWidth + spacing)) / 2;
        int y = (Game.GAME_HEIGHT / 10) * 4;
        g.drawString(text1, x, y);
        g.drawString(text2, x + textWidth1 + spacing, y);
    }

    @Override
    protected void drawControls(Graphics g) {
        FontMetrics fm = g.getFontMetrics(retroFont.deriveFont(22f));

        // Text for QUIT
        String text1Part1 = "Press ";
        String text1Part2 = "Q";
        String text1Part3 = " to ";
        String text1Part4 = "QUIT!";
        int text1WidthPart1 = fm.stringWidth(text1Part1);
        int text1WidthPart2 = fm.stringWidth(text1Part2);
        int text1WidthPart3 = fm.stringWidth(text1Part3);
        int text1WidthPart4 = fm.stringWidth(text1Part4);
        int x1 = (Game.GAME_WIDTH - (text1WidthPart1 + text1WidthPart2 + text1WidthPart3 + text1WidthPart4)) / 2;
        int y1 = Game.GAME_HEIGHT / 2 - 3 * Game.SCALE;

        // Text for RESUME
        String text2Part1 = "Press ";
        String text2Part2 = "R";
        String text2Part3 = " to ";
        String text2Part4 = "RESTART!";
        int text2WidthPart1 = fm.stringWidth(text2Part1);
        int text2WidthPart2 = fm.stringWidth(text2Part2);
        int text2WidthPart3 = fm.stringWidth(text2Part3);
        int text2WidthPart4 = fm.stringWidth(text2Part4);
        int x2 = (Game.GAME_WIDTH - (text2WidthPart1 + text2WidthPart2 + text2WidthPart3 + text2WidthPart4)) / 2;
        int y2 = Game.GAME_HEIGHT / 2 + 10 * Game.SCALE;

        g.setFont(retroFont.deriveFont(22f));

        g.setColor(Color.WHITE);
        g.drawString(text1Part1, x1, y1);

        g.setColor(BUD_RED_COLOR);
        g.drawString(text1Part2, x1 + text1WidthPart1, y1);

        g.setColor(Color.WHITE);
        g.drawString(text1Part3, x1 + text1WidthPart1 + text1WidthPart2, y1);

        g.setColor(BUD_RED_COLOR);
        g.drawString(text1Part4, x1 + text1WidthPart1 + text1WidthPart2 + text1WidthPart3, y1);

        g.setColor(Color.WHITE);
        g.drawString(text2Part1, x2, y2);

        g.setColor(BUD_GREEN_COLOR);
        g.drawString(text2Part2, x2 + text2WidthPart1, y2);

        g.setColor(Color.WHITE);
        g.drawString(text2Part3, x2 + text2WidthPart1 + text2WidthPart2, y2);

        g.setColor(BUD_GREEN_COLOR);
        g.drawString(text2Part4, x2 + text2WidthPart1 + text2WidthPart2 + text2WidthPart3, y2);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_Q) {
            playing.newPlayReset();
            playing.restartGame();
            GameState.state = GameState.MENU;
        }

        if (e.getKeyCode() == KeyEvent.VK_R) {
            playing.newPlayReset();
            playing.restartGame();
            GameState.state = GameState.PLAYING;
        }
    }

    @Override
    protected void setAudio() {
        AudioPlayer.getInstance().stopSong();

        if (firstUpdate) {
            AudioPlayer.getInstance().playSoundEffect(AudioConstants.GAME_COMPLETED);
            firstUpdate = false;
        }
    }
}
