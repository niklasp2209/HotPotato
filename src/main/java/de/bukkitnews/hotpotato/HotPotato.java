package de.bukkitnews.hotpotato;

import de.bukkitnews.hotpotato.commands.SetupCommand;
import de.bukkitnews.hotpotato.commands.StartCommand;
import de.bukkitnews.hotpotato.game.GameState;
import de.bukkitnews.hotpotato.game.GameStateManager;
import org.bukkit.plugin.java.JavaPlugin;
import de.bukkitnews.hotpotato.player.CustomPlayerManager;
import de.bukkitnews.hotpotato.player.PlayerConnectionListener;

public class HotPotato extends JavaPlugin {

    private GameStateManager gameStateManager;
    private CustomPlayerManager customPlayerManager;

    @Override
    public void onEnable(){
        this.gameStateManager = new GameStateManager(this);
        this.customPlayerManager = new CustomPlayerManager();

        gameStateManager.setGameState(GameState.LOBBY_STATE);

        registerListener();
        registerCommands();
    }

    @Override
    public void onDisable(){
        super.onDisable();
    }

    private void registerListener(){
        this.getServer().getPluginManager().registerEvents(new PlayerConnectionListener(this), this);
    }

    private void registerCommands(){
        this.getCommand("setup").setExecutor(new SetupCommand(this));
        this.getCommand("start").setExecutor(new StartCommand(this));
    }

    public GameStateManager getGameStateManager() {
        return gameStateManager;
    }

    public CustomPlayerManager getCustomPlayerManager() {
        return customPlayerManager;
    }
}