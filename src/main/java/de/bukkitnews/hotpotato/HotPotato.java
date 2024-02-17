package de.bukkitnews.hotpotato;

import de.bukkitnews.hotpotato.commands.SetupCommand;
import de.bukkitnews.hotpotato.commands.StartCommand;
import de.bukkitnews.hotpotato.game.GameState;
import de.bukkitnews.hotpotato.game.GameStateManager;
import de.bukkitnews.hotpotato.maps.Map;
import de.bukkitnews.hotpotato.maps.Voting;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import de.bukkitnews.hotpotato.player.CustomPlayerManager;
import de.bukkitnews.hotpotato.player.PlayerConnectionListener;

import java.util.ArrayList;
import java.util.List;

public class HotPotato extends JavaPlugin {

    private GameStateManager gameStateManager;
    private CustomPlayerManager customPlayerManager;
    private Voting voting;

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
        initVoting();

        this.getServer().getPluginManager().registerEvents(new PlayerConnectionListener(this), this);
    }

    private void registerCommands(){
        /*
        REGISTER ALL UPCOMING COMMANDS
         */
        this.getCommand("setup").setExecutor(new SetupCommand(this));
        this.getCommand("start").setExecutor(new StartCommand(this));
    }

    private void initVoting(){
        /*
        CHECKING ALL CREATED MAP IF THEY FINISHED SETUP
         */
        List<Map> mapList = new ArrayList<>();
        for(String current : getConfig().getConfigurationSection("Maps").getKeys(false)){
            Map map = new Map(this, current);
            if(map.playable())
                mapList.add(map);
            else
                Bukkit.getConsoleSender().sendMessage("§cMap "+map.getName()+ " §cist noch nicht fertig eingerichtet!");
        }
        voting = new Voting(this, mapList);
    }

    public GameStateManager getGameStateManager() {
        return gameStateManager;
    }

    public CustomPlayerManager getCustomPlayerManager() {
        return customPlayerManager;
    }

    public Voting getVoting() {
        return voting;
    }
}