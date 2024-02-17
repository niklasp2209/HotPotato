package de.bukkitnews.hotpotato.game;

import de.bukkitnews.hotpotato.HotPotato;

/*
MANAGING MANY GAMESTATES TO HANDLE THE WHOLE GAME
 */
public class GameStateManager {

    private HotPotato hotPotato;
    private GameState[] gameStates;
    private GameState currentGameState;

    public GameStateManager(HotPotato hotPotato){
        this.hotPotato = hotPotato;
        gameStates = new GameState[3];

        gameStates[GameState.LOBBY_STATE] = new LobbyState(this);
        gameStates[GameState.INGAME_STATE] = new IngameState(hotPotato);
        gameStates[GameState.ENDING_STATE] = new EndingState();
    }

    public void setGameState(int gameStateID){
        /*
        REPLACE CURRENT GAMESTATE
         */
        if(currentGameState != null)
            currentGameState.stop();
        currentGameState = gameStates[gameStateID];
        currentGameState.start();
    }

    public void stopCurrentGameState(){
        if(currentGameState != null){
            currentGameState.stop();
            currentGameState = null;
        }
    }

    public HotPotato getHotPotato() {
        return hotPotato;
    }

    public GameState getCurrentGameState() {
        return currentGameState;
    }
}
