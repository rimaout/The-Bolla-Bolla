package view.overlays;


import view.audio.AudioPlayer;
import view.utilz.Constants.AudioConstants;

import model.gameStates.PlayingModel;
import model.utilz.Constants;

import java.awt.*;

import static view.utilz.Constants.Overlays.BUD_RED_COLOR;
import static view.utilz.Constants.Overlays.BUD_GREEN_COLOR;

public class GameCompletedOverlayView extends GameOverlayView {

    public GameCompletedOverlayView(PlayingModel playingModel) {
        super(playingModel);
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
        int spacing = 4 * Constants.SCALE; // Adjust this value to change the spacing between the words
        int x = (Constants.GAME_WIDTH - (totalWidth + spacing)) / 2;
        int y = (Constants.GAME_HEIGHT / 10) * 4;
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
        int x1 = (Constants.GAME_WIDTH - (text1WidthPart1 + text1WidthPart2 + text1WidthPart3 + text1WidthPart4)) / 2;
        int y1 = Constants.GAME_HEIGHT / 2 - 3 * Constants.SCALE;

        // Text for RESTART
        String text2Part1 = "Press ";
        String text2Part2 = "R";
        String text2Part3 = " to ";
        String text2Part4 = "RESTART!";
        int text2WidthPart1 = fm.stringWidth(text2Part1);
        int text2WidthPart2 = fm.stringWidth(text2Part2);
        int text2WidthPart3 = fm.stringWidth(text2Part3);
        int text2WidthPart4 = fm.stringWidth(text2Part4);
        int x2 = (Constants.GAME_WIDTH - (text2WidthPart1 + text2WidthPart2 + text2WidthPart3 + text2WidthPart4)) / 2;
        int y2 = Constants.GAME_HEIGHT / 2 + 10 * Constants.SCALE;

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
    protected void setAudio() {
        AudioPlayer.getInstance().stopSong();

        if (firstUpdate) {
            AudioPlayer.getInstance().playSoundEffect(AudioConstants.GAME_COMPLETED);
            firstUpdate = false;
        }
    }
}