package de.bukkitnews.hotpotato.game;

import de.bukkitnews.hotpotato.countdowns.LobbyCountdown;

public class LobbyState extends GameState {

    private LobbyCountdown lobbyCountdown;

    public LobbyState(GameStateManager gameStateManager){
        lobbyCountdown = new LobbyCountdown(gameStateManager);
    }

    @Override
    public void start() {
        lobbyCountdown.startIdle();
    }

    @Override
    public void stop() {

    }

    public LobbyCountdown getLobbyCountdown() {
        return lobbyCountdown;
    }
}
