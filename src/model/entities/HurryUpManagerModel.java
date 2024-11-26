package model.entities;

import model.utilz.PlayingTimer;
import view.entities.HurryUpManagerView;

import static model.utilz.Constants.HurryUpManager.*;

public class HurryUpManagerModel {
    private static HurryUpManagerModel instance;

    private final PlayingTimer timer = PlayingTimer.getInstance();

    private boolean animationActive;

    private int startAnimationTimer = START_ANIMATION_TIMER;
    private int startHurryUpTimer = START_HURRY_UP_TIMER;

    private final SkelMonstaModel skelMonsta;

    private HurryUpManagerModel() {
        skelMonsta = new SkelMonstaModel();
    }

    public static HurryUpManagerModel getInstance() {
        if (instance == null)
            instance = new HurryUpManagerModel();
        return instance;
    }

    public void update(PlayerModel playerModel) {
        if (EnemyManagerModel.getInstance().areAllEnemiesDead())
            restart();

        updateTimer();

        if (skelMonsta.isActive())
            skelMonsta.update(playerModel);
    }

    public void updateTimer() {
        startAnimationTimer -= (int) timer.getTimeDelta();
        startHurryUpTimer -= (int) timer.getTimeDelta();

        if (startAnimationTimer <= 0)
            animationActive = true;

        if (startHurryUpTimer <= 0)
            startHurryUp();
    }

    public void restart() {
        startAnimationTimer = START_ANIMATION_TIMER;
        startHurryUpTimer = START_HURRY_UP_TIMER;

        animationActive = false;

        HurryUpManagerView.getInstance().restart(); // todo: use observer pattern to notify the view
        skelMonsta.activateDespawn();
    }

    public void newLevelReset() {
        startAnimationTimer = START_ANIMATION_TIMER;
        startHurryUpTimer = START_HURRY_UP_TIMER;

        animationActive = false;

        HurryUpManagerView.getInstance().restart(); // todo: use observer pattern to notify the view
        skelMonsta.reset();
    }

    public void newPlayReset() {
        newLevelReset();
    }

    public void startHurryUp() {
        animationActive = false;
        skelMonsta.activate();
        EnemyManagerModel.getInstance().setAllHungry();
    }

    public boolean isAnimationActive() {
        return animationActive;
    }

    public EnemyModel getSkelMonstaModel() {
        return skelMonsta;
    }
}