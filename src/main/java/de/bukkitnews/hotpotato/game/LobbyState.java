package de.bukkitnews.hotpotato.game;

import de.bukkitnews.hotpotato.countdowns.LobbyCountdown;

public class LobbyState extends GameState {

    private final LobbyCountdown lobbyCountdown;

    public LobbyState(GameStateManager gameStateManager){
        this.lobbyCountdown = new LobbyCountdown(gameStateManager);
    }

    @Override
    public void start() {
        this.lobbyCountdown.startIdle();
    }

    @Override
    public void stop() {

    }

    public LobbyCountdown getLobbyCountdown() {
        return this.lobbyCountdown;
    }
}
