package de.bukkitnews.hotpotato.game;

import org.bukkit.Bukkit;

public class EndingState extends GameState {
    @Override
    public void start() {

    }

    @Override
    public void stop() {
        Bukkit.getServer().shutdown();
    }
}
