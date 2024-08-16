package gameStates;

public enum Gamestate {
    MENU, PLAYING, OPTIONS, QUIT, GAMEOVER, GAMECOMPLETED;

    // initialize the state to MENU
    public static Gamestate state = MENU;
}
