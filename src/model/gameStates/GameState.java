package model.gameStates;

/**
 * Represents the different states of the game.
 *
 * <p>The GameState enum defines the various states that the game can be in, such as HOME, MENU, PLAYING, etc.
 * It also initializes the default state to HOME.
 */
public enum GameState {
    HOME, MENU, PLAYING, LEVEL_TRANSITION, OPTIONS, QUIT, GAMEOVER, GAMECOMPLETED;

    // initialize the state to HOME
    public static GameState state = HOME;
}