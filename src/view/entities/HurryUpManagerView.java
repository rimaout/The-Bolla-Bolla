package view.entities;

import view.utilz.LoadSave;
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

public class HurryUpManagerView implements Observer {
    private static HurryUpManagerView instance;
    private static HurryUpManagerModel hurryUpManagerModel = HurryUpManagerModel.getInstance();

    private final BufferedImage hurryImg;
    private float hurryImgX = STARTING_HURRY_IMG_X;
    private float hurryImgY = STARTING_HURRY_IMG_Y;

    private boolean playSound = false;
    private boolean alreadyPlayedSound = false;

    private final SkelMonstaView skelMonstaView;

    private HurryUpManagerView() {
        hurryImg = LoadSave.GetSprite(LoadSave.HURRY_IMAGE);
        skelMonstaView = new SkelMonstaView(hurryUpManagerModel.getSkelMonstaModel());
    }

    public static HurryUpManagerView getInstance() {
        if (instance == null) {
            instance = new HurryUpManagerView();
        }
        return instance;
    }

    public void update() {
        skelMonstaView.update();
        updateHurryPos();
        checkPlaySound();
    }

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

    public void checkPlaySound() {
        if (!alreadyPlayedSound && hurryUpManagerModel.isHurryUpActive())
            playSound = true;
    }

    private void updateHurryPos() {
        if (!hurryUpManagerModel.isHurryUpActive())
            return;

        // If image is at center of screen, stop moving
        if (hurryImgY <= GAME_HEIGHT / 2 - HURRY_IMG_H / 2)
            return;

        hurryImgY -= HURRY_IMG_SPEED;
    }

    public void restart() {
        hurryImgX = STARTING_HURRY_IMG_X;
        hurryImgY = STARTING_HURRY_IMG_Y;

        playSound = false;
        alreadyPlayedSound = false;

        skelMonstaView.reset();
    }

    public void newLevelReset() {
        restart();
    }

    public void newPlayReset() {
        restart();
    }

    @Override
    public void update(Observable o, Object arg) {
        restart();
    }
}