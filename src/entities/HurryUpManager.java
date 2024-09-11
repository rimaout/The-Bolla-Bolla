package entities;

import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utilz.Constants.HurryUpManager.*;

public class HurryUpManager {
    private static HurryUpManager instance;

    private final BufferedImage hurryImg;
    private float hurryImgX, hurryImgY;

    private boolean animationActive;

    private boolean firstUpdate = true;
    private long lastTimerUpdate;
    private int startAnimationTimer, startHurryUpTimer;

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
        if (firstUpdate)
            firstUpdate();

        updateTimer();
        updateHurryPos();

        if (skelMonsta.isActive())
            skelMonsta.update(player);
    }

    public void firstUpdate(){
        firstUpdate = false;

        hurryImgX = HURRY_IMG_X;
        hurryImgY = HURRY_IMG_Y;
        animationActive = false;

        startAnimationTimer = START_ANIMATION_TIMER;
        startHurryUpTimer = START_HURRY_UP_TIMER;
        lastTimerUpdate = System.currentTimeMillis();
    }

    public void draw(Graphics g) {

        if (animationActive)
            g.drawImage(hurryImg, (int) hurryImgX, (int) hurryImgY, HURRY_IMG_W,  HURRY_IMG_H, null);

        if (skelMonsta.isActive())
            skelMonsta.draw(g);
    }

    public void updateTimer() {
        if (firstUpdate) {
            lastTimerUpdate = System.currentTimeMillis();
            firstUpdate = false;
        }

        long timeDelta = System.currentTimeMillis() - lastTimerUpdate;
        lastTimerUpdate = System.currentTimeMillis();

        startAnimationTimer -= (int) timeDelta;
        startHurryUpTimer -= (int) timeDelta;

        if (EnemyManager.getInstance().areAllEnemiesDead())
            restart();

        if (startAnimationTimer <= 0)
            animationActive = true;

        if (startHurryUpTimer <= 0)
            startHurryUp();
    }

    private void updateHurryPos() {
        if (animationActive) {

            // If image is at center of screen, stop moving
            if (hurryImgY <= Game.GAME_HEIGHT / 2 - HURRY_IMG_H / 2)
                return;

            hurryImgY -= HURRY_IMG_SPEED;
        }
    }

    public void restart() {
        firstUpdate = true;
        skelMonsta.despawn();
    }

    public void resetAll() {
        firstUpdate = true;
        skelMonsta.reset();
    }

    public void startHurryUp() {
        animationActive = false;
        skelMonsta.activate();
        EnemyManager.getInstance().setAllHungry();
    }
}

