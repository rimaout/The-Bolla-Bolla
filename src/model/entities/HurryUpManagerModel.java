package model.entities;

import model.PlayingTimer;
import view.entities.HurryUpManagerView;

import static model.Constants.HurryUpManager.*;

public class HurryUpManagerModel {
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

        HurryUpManagerView.getInstance().restart(); // todo: use observer pattern to notify the view
        skelMonsta.activateDespawn();
    }

    public void newLevelReset() {
        startHurryUpTimer = START_HURRY_UP_TIMER;
        activateSkelMonstaTimer = ACTIVATE_SKEL_MONSTA_TIMER;

        hurryUpActive = false;

        HurryUpManagerView.getInstance().restart(); // todo: use observer pattern to notify the view
        skelMonsta.reset();
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