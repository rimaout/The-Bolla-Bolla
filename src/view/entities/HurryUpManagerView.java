package view.entities;

import view.utilz.Load;
import view.utilz.Constants;
import view.audio.AudioPlayer;
import model.entities.HurryUpManagerModel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Observer;
import java.util.Observable;

import static model.utilz.Constants.GAME_HEIGHT;
import static view.utilz.Constants.HurryUpManager.*;
import static view.utilz.Constants.HurryUpManager.HURRY_IMG_H;

/**
 * The HurryUpManagerView class manages the view for the HurryUp state in the game.
 * It handles updating and drawing the "Hurry Up" image and the SkelMonsta enemy.
 */
public class HurryUpManagerView implements Observer {
    private static HurryUpManagerView instance;
    private static HurryUpManagerModel hurryUpManagerModel = HurryUpManagerModel.getInstance();

    private final BufferedImage hurryImg;
    private float hurryImgX = STARTING_HURRY_IMG_X;
    private float hurryImgY = STARTING_HURRY_IMG_Y;

    private boolean playSound = false;
    private boolean alreadyPlayedSound = false;

    private final SkelMonstaView skelMonstaView;

    /**
     * Private constructor to prevent instantiation.
     * Initializes the "Hurry Up" image and the SkelMonsta view.
     */
    private HurryUpManagerView() {
        hurryImg = Load.GetSprite(Load.HURRY_IMAGE);
        skelMonstaView = new SkelMonstaView(hurryUpManagerModel.getSkelMonstaModel());
    }

    /**
     * Returns the singleton instance of HurryUpManagerView.
     *
     * @return the singleton instance of HurryUpManagerView
     */
    public static HurryUpManagerView getInstance() {
        if (instance == null) {
            instance = new HurryUpManagerView();
        }
        return instance;
    }

    /**
     * Updates the state of the "Hurry Up" image and the SkelMonsta view.
     */
    public void update() {
        skelMonstaView.update();
        updateHurryPos();
        checkPlaySound();
    }

    /**
     * Draws the "Hurry Up" image and the SkelMonsta view on the screen.
     *
     * @param g the Graphics object to draw with
     */
    public void draw(Graphics g) {

        if (hurryUpManagerModel.isHurryUpActive())
            g.drawImage(hurryImg, (int) hurryImgX, (int) hurryImgY, HURRY_IMG_W,  HURRY_IMG_H, null);

        if (skelMonstaView.isActive())
            skelMonstaView.draw(g);

        if (playSound) {
            playSound = false;
            alreadyPlayedSound = true;
            AudioPlayer.getInstance().playSoundEffect(Constants.AudioConstants.HURRY_UP);
        }
    }

    /**
     * Checks if the "Hurry Up" sound should be played.
     */
    public void checkPlaySound() {
        if (!alreadyPlayedSound && hurryUpManagerModel.isHurryUpActive())
            playSound = true;
    }

    /**
     * Updates the position of the "Hurry Up" image.
     */
    private void updateHurryPos() {
        if (!hurryUpManagerModel.isHurryUpActive())
            return;

        // If image is at center of screen, stop moving
        if (hurryImgY <= GAME_HEIGHT / 2 - HURRY_IMG_H / 2)
            return;

        hurryImgY -= HURRY_IMG_SPEED;
    }

    /**
     * Restarts the state of the "Hurry Up" image and the SkelMonsta view.
     */
    public void restart() {
        hurryImgX = STARTING_HURRY_IMG_X;
        hurryImgY = STARTING_HURRY_IMG_Y;

        playSound = false;
        alreadyPlayedSound = false;

        skelMonstaView.reset();
    }

    /**
     * Resets the state of the "Hurry Up" image and the SkelMonsta view for a new level.
     */
    public void newLevelReset() {
        restart();
    }

    /**
     * Resets the state of the "Hurry Up" image and the SkelMonsta view for a new play session.
     */
    public void newPlayReset() {
        restart();
    }


    // ----------------- OBSERVER -----------------

    /**
     * Updates the view based on changes in the observed model.
     *
     * @param o the observable object
     * @param arg an argument passed to the notifyObservers method
     */
    @Override
    public void update(Observable o, Object arg) {
        restart();
    }
}