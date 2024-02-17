package de.bukkitnews.hotpotato;

import de.bukkitnews.hotpotato.commands.LanguageCommand;
import de.bukkitnews.hotpotato.commands.SetupCommand;
import de.bukkitnews.hotpotato.commands.StartCommand;
import de.bukkitnews.hotpotato.game.GameState;
import de.bukkitnews.hotpotato.game.GameStateManager;
import de.bukkitnews.hotpotato.language.LanguageModule;
import de.bukkitnews.hotpotato.maps.Map;
import de.bukkitnews.hotpotato.maps.Voting;
import de.bukkitnews.hotpotato.maps.VotingListener;
import de.bukkitnews.hotpotato.utils.PotatoConstants;
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
    private List<Map> mapList;
    private LanguageModule languageModule;

    @Override
    public void onEnable(){
        this.gameStateManager = new GameStateManager(this);
        this.customPlayerManager = new CustomPlayerManager();
        this.languageModule = new LanguageModule(this, getConfig());
        languageModule.createDefaultConfig();

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
        this.getServer().getPluginManager().registerEvents(new VotingListener(this), this);
    }

    private void registerCommands(){
        /*
        REGISTER ALL UPCOMING COMMANDS
         */
        this.getCommand("setup").setExecutor(new SetupCommand(this));
        this.getCommand("start").setExecutor(new StartCommand(this));
        this.getCommand("language").setExecutor(new LanguageCommand(this));
    }

    private void initVoting(){
        /*
        CHECKING ALL CREATED MAP IF THEY FINISHED SETUP
         */
        mapList = new ArrayList<>();
        for(String current : getConfig().getConfigurationSection("Maps").getKeys(false)){
            Map map = new Map(this, current);
            if(map.playable())
                mapList.add(map);
            else
                Bukkit.getConsoleSender().sendMessage("§cMap "+map.getName()+ " §cist noch nicht fertig eingerichtet!");
        }
        if(mapList.size() >= PotatoConstants.VOTING_MAP_AMOUNT)
            voting = new Voting(this, mapList);
        else {
            Bukkit.getConsoleSender().sendMessage("§cEs müssen mindestens " + PotatoConstants.VOTING_MAP_AMOUNT + " §cMaps eingerichtet sein.");
            voting = null;
        }
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

    public List<Map> getMapList() {
        return mapList;
    }

    public LanguageModule getLanguageModule() {
        return languageModule;
    }
}