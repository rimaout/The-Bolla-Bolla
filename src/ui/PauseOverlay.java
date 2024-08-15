package ui;

import static Utillz.Constants.UI.PauseButtons.*;
import static Utillz.Constants.UI.UrmButtons.*;
import static Utillz.Constants.UI.VolumeButton.*;

import Utillz.LoadSave;
import gameStates.Gamestate;
import gameStates.Playing;
import main.Game;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class PauseOverlay {
    private Playing playing;
    private BufferedImage backgroundImg;

    private int bgX, bgY, bgWidth, bgHeight; // background position and size
    private SoundButton musicButton, sfxButton;
    private UrmButton menuButton, replayButton, unpauseButton;
    private VolumeButton volumeButton;


    public PauseOverlay(Playing playing) {
        this.playing = playing;
        loadBackground();
        createSoundButton();
        createUrmButtons();
        createVolumeButton();
    }

    private void createVolumeButton() {
        int volumeX = (int) (92 * Game.SCALE);  // volume buttons have the same x position
        int musicY = (int) (130 * Game.SCALE);
        volumeButton = new VolumeButton(volumeX, musicY, VOLUME_SLIDER_WIDTH, VOLUME_SLIDER_HEIGHT);
    }

    private void createUrmButtons() {
        int urmY = (int) (147 * Game.SCALE);    // urm buttons have the same x position
        int menuX = (int) (88 * Game.SCALE);
        int resumeX = (int) (116.5 * Game.SCALE);
        int restartX = (int) (144.7 * Game.SCALE);
        int ButtonsWidth = (int) (URM_BT_WIDTH * Game.SCALE / 3.6);
        int ButtonsHeight = (int) (URM_BT_HEIGHT * Game.SCALE / 3.6);

        menuButton = new UrmButton(menuX, urmY, ButtonsWidth, ButtonsHeight, 2);
        replayButton = new UrmButton(resumeX, urmY, ButtonsWidth, ButtonsHeight, 1);
        unpauseButton = new UrmButton(restartX, urmY, ButtonsWidth, ButtonsHeight, 0);
    }

    private void createSoundButton() {
        int soundX = (int) (141 * Game.SCALE);  // sound buttons have the same x position
        int musicY = (int) (77 * Game.SCALE);
        int sfY = (int) (95 * Game.SCALE);
        int ButtonsWidth = (int) (SOUND_BT_WIDTH * Game.SCALE / 4);
        int ButtonsHeight = (int) (SOUND_BT_HEIGHT * Game.SCALE / 4);

        musicButton = new SoundButton(soundX, musicY, ButtonsWidth, ButtonsHeight);
        sfxButton = new SoundButton(soundX, sfY, ButtonsWidth, ButtonsHeight);

    }

    public void loadBackground() {
        backgroundImg = LoadSave.GetSprite(LoadSave.PAUSE_BACKGROUND);
        bgWidth = (int) (backgroundImg.getWidth() * Game.SCALE / 2.6);
        bgHeight = (int) (backgroundImg.getHeight() * Game.SCALE / 2.6);
        bgX = Game.GAME_WIDTH / 2 - bgWidth / 2;
        bgY = 33 * Game.SCALE;
    }

    public void update() {
        musicButton.update();
        sfxButton.update();
        menuButton.update();
        replayButton.update();
        unpauseButton.update();
        volumeButton.update();
    }

    public void draw(Graphics g) {
        // Draw background
        g.drawImage(backgroundImg, bgX, bgY, bgWidth, bgHeight, null);

        // Draw sound buttons
        musicButton.draw(g);
        sfxButton.draw(g);

        // Draw urm buttons
        menuButton.draw(g);
        replayButton.draw(g);
        unpauseButton.draw(g);

        // Draw volume button
        volumeButton.draw(g);
    }

    private boolean isMouseOverButton(MouseEvent e, PauseButton button) {
        return button.getBounds().contains(e.getX(), e.getY());
    }

    public void mouseDragged(MouseEvent e) {
        if(volumeButton.isMousePressed()){
            volumeButton.changeX(e.getX());
        }
    }

    public void mousePressed(MouseEvent e) {
        if (isMouseOverButton(e, musicButton)) {
            musicButton.setMousePressed(true);
        }
        else if (isMouseOverButton(e, sfxButton)) {
            sfxButton.setMousePressed(true);
        }
        else if (isMouseOverButton(e, menuButton)) {
            menuButton.setMousePressed(true);
        }
        else if (isMouseOverButton(e, replayButton)) {
            replayButton.setMousePressed(true);
        }
        else if (isMouseOverButton(e, unpauseButton)) {
            unpauseButton.setMousePressed(true);
        }
        else if (isMouseOverButton(e, volumeButton)) {
            volumeButton.setMousePressed(true);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (isMouseOverButton(e, musicButton)) {
            musicButton.setMuted(!musicButton.isMuted());
        }
        else if (isMouseOverButton(e, sfxButton)) {
            sfxButton.setMuted(!sfxButton.isMuted());
        }
        else if (isMouseOverButton(e, menuButton)) {
            Gamestate.state = Gamestate.MENU;
            playing.unpauseGame();
        }
        else if (isMouseOverButton(e, replayButton)) {
            playing.resetAll();
            playing.unpauseGame();
        }
        else if (isMouseOverButton(e, unpauseButton)) {
            playing.unpauseGame();
        }

        resetButtons();
    }

    public void mouseMoved(MouseEvent e) {
        resetButtons();

        if (isMouseOverButton(e, musicButton)) {
            musicButton.setMouseOver(true);
        }
        else if (isMouseOverButton(e, sfxButton)) {
            sfxButton.setMouseOver(true);
        }
        else if (isMouseOverButton(e, menuButton)) {
            menuButton.setMouseOver(true);
        }
        else if (isMouseOverButton(e, replayButton)) {
            replayButton.setMouseOver(true);
        }
        else if (isMouseOverButton(e, unpauseButton)) {
            unpauseButton.setMouseOver(true);
        }
        else if (isMouseOverButton(e, volumeButton)) {
            volumeButton.setMouseOver(true);
        }

    }

    public void resetButtons() {
        musicButton.resetBools();
        sfxButton.resetBools();
        menuButton.resetBools();
        replayButton.resetBools();
        unpauseButton.resetBools();
        volumeButton.resetBools();
    }

}
