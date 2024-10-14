package view.overlays;

import audio.AudioPlayer;
import gameStates.PlayingModel;
import utilz.Constants;

import java.awt.*;

import static utilz.Constants.Overlays.*;

public class GamePausedOverlayView extends GameOverlayView {
    private static GamePausedOverlayView instance;

    private GamePausedOverlayView(PlayingModel playingModel) {
        super(playingModel);
    }

    public static GamePausedOverlayView getInstance(PlayingModel playingModel) {
        if (instance == null) {
            instance = new GamePausedOverlayView(playingModel);
        }
        return instance;
    }

    @Override
    protected void drawTitle(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(nesFont.deriveFont(50f));
        FontMetrics fm = g.getFontMetrics(g.getFont());

        String text = "PAUSE";
        int textWidth = fm.stringWidth(text);
        int x = (Constants.GAME_WIDTH - textWidth) / 2;
        int y = (Constants.GAME_HEIGHT / 10) * 4;

        g.drawString(text, x, y);
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

        // Text for RESUME
        String text2Part1 = "Press ";
        String text2Part2 = "ESC";
        String text2Part3 = " to ";
        String text2Part4 = "RESUME!";
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
    }
}
