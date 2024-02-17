package de.bukkitnews.hotpotato;

import game.GameState;
import game.GameStateManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import player.CustomPlayerManager;
import player.PlayerConnectionListener;

public class HotPotato extends JavaPlugin {

    private GameStateManager gameStateManager;
    private CustomPlayerManager customPlayerManager;

    @Override
    public void onEnable(){
        this.gameStateManager = new GameStateManager(this);
        this.customPlayerManager = new CustomPlayerManager();

        gameStateManager.setGameState(GameState.LOBBY_STATE);

        registerListener(Bukkit.getPluginManager());
        registerCommands();
    }

    @Override
    public void onDisable(){
        super.onDisable();
    }

    private void registerListener(PluginManager pluginManager){
        pluginManager.registerEvents(new PlayerConnectionListener(this), this);
    }

    private void registerCommands(){

    }

    public GameStateManager getGameStateManager() {
        return gameStateManager;
    }

    public CustomPlayerManager getCustomPlayerManager() {
        return customPlayerManager;
    }
}