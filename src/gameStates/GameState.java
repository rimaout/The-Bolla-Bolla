package gameStates;

public enum GameState {
    HOME, MENU, PLAYING, INTRO, LEVEL_TRANSITION , OPTIONS, QUIT, GAMEOVER, GAMECOMPLETED;

    // initialize the state to HOME
    public static GameState state = HOME;
}
