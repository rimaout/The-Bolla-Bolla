package model.entities;

import model.utilz.PlayingTimer;

import java.util.Observable;

import static model.utilz.Constants.HurryUpManager.*;

/**
 * Manages the "Hurry Up" state in the game.
 *
 * <p>This singleton class handles the timing and activation of the "Hurry Up" state,
 * which includes activating the SkelMonsta enemy and notifying observers of state changes.
 */
public class HurryUpManagerModel extends Observable {
    private static HurryUpManagerModel instance;

    private final PlayingTimer timer = PlayingTimer.getInstance();

    private boolean hurryUpActive;

    private int startHurryUpTimer = START_HURRY_UP_TIMER;
    private int activateSkelMonstaTimer = ACTIVATE_SKEL_MONSTA_TIMER;

    private final SkelMonstaModel skelMonsta;

    /**
     * Private constructor to prevent instantiation.
     */
    private HurryUpManagerModel() {
        skelMonsta = new SkelMonstaModel();
    }

    /**
     * Returns the singleton instance of the HurryUpManagerModel.
     *
     * @return the singleton instance
     */
    public static HurryUpManagerModel getInstance() {
        if (instance == null)
            instance = new HurryUpManagerModel();
        return instance;
    }

    /**
     * Updates the state of the HurryUpManagerModel.
     *
     * @param playerModel the player model used to update the SkelMonsta
     */
    public void update(PlayerModel playerModel) {
        if (EnemyManagerModel.getInstance().areAllEnemiesDead())
            restart();

        updateTimer();

        if (skelMonsta.isActive())
            skelMonsta.update(playerModel);
    }

    /**
     * Updates the timers for the "Hurry Up" state and SkelMonsta activation.
     */
    public void updateTimer() {
        startHurryUpTimer -= (int) timer.getTimeDelta();
        activateSkelMonstaTimer -= (int) timer.getTimeDelta();

        if (startHurryUpTimer <= 0)
            hurryUpActive = true;

        if (activateSkelMonstaTimer <= 0)
            startSkelMostaAction();
    }

    /**
     * Restarts the "Hurry Up" state and resets the SkelMonsta.
     */
    public void restart() {
        startHurryUpTimer = START_HURRY_UP_TIMER;
        activateSkelMonstaTimer = ACTIVATE_SKEL_MONSTA_TIMER;

        hurryUpActive = false;
        skelMonsta.activateDespawn();

        // notify observers
        setChanged();
        notifyObservers();
    }

    /**
     * Resets the "Hurry Up" state and SkelMonsta for a new level.
     */
    public void newLevelReset() {
        startHurryUpTimer = START_HURRY_UP_TIMER;
        activateSkelMonstaTimer = ACTIVATE_SKEL_MONSTA_TIMER;

        hurryUpActive = false;
        skelMonsta.reset();

        // notify observers
        setChanged();
        notifyObservers();
    }

    /**
     * Resets the "Hurry Up" state and SkelMonsta for a new level.
     */
    public void newPlayReset() {
        newLevelReset();
    }

    /**
     * Activates SkelMonsta and sets all enemies to hungry state.
     */
    public void startSkelMostaAction() {
        hurryUpActive = false;
        skelMonsta.activate();
        EnemyManagerModel.getInstance().setAllHungry();
    }

    /**
     * Returns whether the "Hurry Up" state is active.
     *
     * @return true if the "Hurry Up" state is active, false otherwise
     */
    public boolean isHurryUpActive() {
        return hurryUpActive;
    }

    /**
     * Returns the SkelMonsta model.
     *
     * @return the SkelMonsta model
     */
    public EnemyModel getSkelMonstaModel() {
        return skelMonsta;
    }
}