package model.entities;

import model.utilz.PlayingTimer;

import java.util.Observable;

import static model.utilz.Constants.HurryUpManager.*;

public class HurryUpManagerModel extends Observable {
    private static HurryUpManagerModel instance;

    private final PlayingTimer timer = PlayingTimer.getInstance();

    private boolean hurryUpActive;

    private int startHurryUpTimer = START_HURRY_UP_TIMER;
    private int activateSkelMonstaTimer = ACTIVATE_SKEL_MONSTA_TIMER;

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
        startHurryUpTimer -= (int) timer.getTimeDelta();
        activateSkelMonstaTimer -= (int) timer.getTimeDelta();

        if (startHurryUpTimer <= 0)
            hurryUpActive = true;

        if (activateSkelMonstaTimer <= 0)
            startSkelMostaAction();
    }

    public void restart() {
        startHurryUpTimer = START_HURRY_UP_TIMER;
        activateSkelMonstaTimer = ACTIVATE_SKEL_MONSTA_TIMER;

        hurryUpActive = false;
        skelMonsta.activateDespawn();

        // notify observers
        notifyObservers();
        setChanged();
    }

    public void newLevelReset() {
        startHurryUpTimer = START_HURRY_UP_TIMER;
        activateSkelMonstaTimer = ACTIVATE_SKEL_MONSTA_TIMER;

        hurryUpActive = false;
        skelMonsta.reset();

        // notify observers
        notifyObservers();
        setChanged();
    }

    public void newPlayReset() {
        newLevelReset();
    }

    public void startSkelMostaAction() {
        hurryUpActive = false;
        skelMonsta.activate();
        EnemyManagerModel.getInstance().setAllHungry();
    }

    public boolean isHurryUpActive() {
        return hurryUpActive;
    }

    public EnemyModel getSkelMonstaModel() {
        return skelMonsta;
    }
}