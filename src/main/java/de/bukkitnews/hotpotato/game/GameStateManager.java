package de.bukkitnews.hotpotato.game;

import de.bukkitnews.hotpotato.HotPotato;

/*
MANAGING MANY GAMESTATES TO HANDLE THE WHOLE GAME
 */
public class GameStateManager {

    private final HotPotato hotPotato;
    private final GameState[] gameStates;
    private GameState currentGameState;

    public GameStateManager(HotPotato hotPotato) {
        this.hotPotato = hotPotato;
        this.gameStates = new GameState[3];

        this.gameStates[GameState.LOBBY_STATE] = new LobbyState(this);
        this.gameStates[GameState.INGAME_STATE] = new IngameState(hotPotato);
        this.gameStates[GameState.ENDING_STATE] = new EndingState();
    }

    public void setGameState(int gameStateID) {
        /*
        REPLACE CURRENT GAMESTATE
         */
        if(this.currentGameState != null)
            this.currentGameState.stop();
        this.currentGameState = this.gameStates[gameStateID];
        this.currentGameState.start();
    }

    public void stopCurrentGameState() {
        if(this.currentGameState != null) {
            this.currentGameState.stop();
            this.currentGameState = null;
        }
    }

    public HotPotato getHotPotato() {
        return this.hotPotato;
    }

    public GameState getCurrentGameState() {
        return this.currentGameState;
    }
}
