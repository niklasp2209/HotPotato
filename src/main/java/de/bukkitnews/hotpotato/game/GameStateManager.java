package de.bukkitnews.hotpotato.game;

import de.bukkitnews.hotpotato.HotPotato;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GameStateManager {

    private HotPotato hotPotato;
    private GameState[] gameStates;
    private GameState currentGameState;
    private List<Player> players;

    public GameStateManager(HotPotato hotPotato){
        this.hotPotato = hotPotato;
        this.players = new ArrayList<>();
        gameStates = new GameState[3];

        gameStates[GameState.LOBBY_STATE] = new LobbyState();
        gameStates[GameState.INGAME_STATE] = new IngameState();
        gameStates[GameState.ENDING_STATE] = new EndingState();
    }

    public void setGameState(int gameStateID){
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

    public GameState getCurrentGameState() {
        return currentGameState;
    }

    public List<Player> getPlayers() {
        return players;
    }
}
