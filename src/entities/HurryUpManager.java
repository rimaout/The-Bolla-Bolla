package entities;

import java.awt.*;
import java.awt.image.BufferedImage;

import model.utilz.Constants;
import model.utilz.LoadSave;
import model.audio.AudioPlayer;
import model.utilz.PlayingTimer;
import model.utilz.Constants.AudioConstants;

import static model.utilz.Constants.HurryUpManager.*;

public class HurryUpManager {
    private static HurryUpManager instance;

    private final PlayingTimer timer = PlayingTimer.getInstance();

    private final BufferedImage hurryImg;
    private float hurryImgX = STARTING_HURRY_IMG_X;
    private float hurryImgY = STARTING_HURRY_IMG_Y;

    private boolean animationActive;

    private boolean playSound = false;
    private boolean alreadyPlayedSound = false;

    private int startAnimationTimer = START_ANIMATION_TIMER;
    private int startHurryUpTimer = START_HURRY_UP_TIMER;

    private final SkelMonsta skelMonsta;

    private HurryUpManager() {
        hurryImg = LoadSave.GetSprite(LoadSave.HURRY_IMAGE);
        skelMonsta = new SkelMonsta();
    }

    public static HurryUpManager getInstance() {
        if (instance == null)
            instance = new HurryUpManager();
        return instance;
    }

    public void update(Player player) {
        updateTimer();
        updateHurryPos();

        if (skelMonsta.isActive())
            skelMonsta.update(player);
    }

    public void draw(Graphics g) {

        if (animationActive)
            g.drawImage(hurryImg, (int) hurryImgX, (int) hurryImgY, HURRY_IMG_W,  HURRY_IMG_H, null);

        if (skelMonsta.isActive())
            skelMonsta.draw(g);

        if (playSound) {
            playSound = false;
            alreadyPlayedSound = true;
            AudioPlayer.getInstance().playSoundEffect(AudioConstants.HURRY_UP);
        }
    }

    public void updateTimer() {

        startAnimationTimer -= (int) timer.getTimeDelta();
        startHurryUpTimer -= (int) timer.getTimeDelta();

        if (EnemyManager.getInstance().areAllEnemiesDead())
            restart();

        if (startAnimationTimer <= 0) {
            animationActive = true;
            if (!alreadyPlayedSound)
                playSound = true;
        }

        if (startHurryUpTimer <= 0)
            startHurryUp();
    }

    private void updateHurryPos() {
        if (animationActive) {

            // If image is at center of screen, stop moving
            if (hurryImgY <= Constants.GAME_HEIGHT / 2 - HURRY_IMG_H / 2)
                return;

            hurryImgY -= HURRY_IMG_SPEED;
        }
    }

    public void restart() {
        hurryImgX = STARTING_HURRY_IMG_X;
        hurryImgY = STARTING_HURRY_IMG_Y;
        startAnimationTimer = START_ANIMATION_TIMER;
        startHurryUpTimer = START_HURRY_UP_TIMER;

        animationActive = false;
        playSound = false;
        alreadyPlayedSound = false;

        skelMonsta.despawn();
    }

    public void newLevelReset() {
        hurryImgX = STARTING_HURRY_IMG_X;
        hurryImgY = STARTING_HURRY_IMG_Y;
        startAnimationTimer = START_ANIMATION_TIMER;
        startHurryUpTimer = START_HURRY_UP_TIMER;

        animationActive = false;
        playSound = false;
        alreadyPlayedSound = false;

        skelMonsta.reset();
    }

    public void newPlayReset() {
        newLevelReset();
    }

    public void startHurryUp() {
        animationActive = false;
        skelMonsta.activate();
        EnemyManager.getInstance().setAllHungry();
    }
}

