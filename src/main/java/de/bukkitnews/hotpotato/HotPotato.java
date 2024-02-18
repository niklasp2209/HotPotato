package de.bukkitnews.hotpotato;

import de.bukkitnews.hotpotato.achievement.AchievementManager;
import de.bukkitnews.hotpotato.commands.*;
import de.bukkitnews.hotpotato.countdowns.PotatoCountdown;
import de.bukkitnews.hotpotato.game.GameListener;
import de.bukkitnews.hotpotato.game.GameState;
import de.bukkitnews.hotpotato.game.GameStateManager;
import de.bukkitnews.hotpotato.language.LanguageModule;
import de.bukkitnews.hotpotato.maps.Map;
import de.bukkitnews.hotpotato.maps.Voting;
import de.bukkitnews.hotpotato.maps.VotingListener;
import de.bukkitnews.hotpotato.player.PlayerChatListener;
import de.bukkitnews.hotpotato.utils.PotatoConstants;
import de.bukkitnews.hotpotato.utils.WorldListener;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import de.bukkitnews.hotpotato.player.CustomPlayerManager;
import de.bukkitnews.hotpotato.player.PlayerConnectionListener;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class HotPotato extends JavaPlugin {

    private GameStateManager gameStateManager;
    private CustomPlayerManager customPlayerManager;
    private Voting voting;
    private List<Map> mapList;
    private LanguageModule languageModule;
    private WorldListener worldListener;
    private BuildCommand buildCommand;
    private PotatoCountdown potatoCountdown;
    private GameListener gameListener;
    private AchievementManager achievementManager;

    @Override
    public void onEnable() {
        this.gameStateManager = new GameStateManager(this);
        this.customPlayerManager = new CustomPlayerManager();
        this.languageModule = new LanguageModule(this, getConfig());
        this.worldListener = new WorldListener(this);
        this.buildCommand = new BuildCommand(this);
        this.potatoCountdown = new PotatoCountdown(this);
        this.gameListener = new GameListener(this);
        this.achievementManager = new AchievementManager(this);
        this.languageModule.createDefaultConfig();

        this.gameStateManager.setGameState(GameState.LOBBY_STATE);

        registerListener();
        registerCommands();
        this.worldListener.initWorlds();
    }

    @Override
    public void onDisable(){
        super.onDisable();
    }

    private void registerListener() {
        initVoting();

        this.getServer().getPluginManager().registerEvents(new PlayerConnectionListener(this), this);
        this.getServer().getPluginManager().registerEvents(new VotingListener(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerChatListener(this), this);
        this.getServer().getPluginManager().registerEvents(gameListener, this);
        this.getServer().getPluginManager().registerEvents(worldListener, this);
    }

    private void registerCommands() {
        /*
        REGISTER ALL UPCOMING COMMANDS
         */
        this.getCommand("setup").setExecutor(new SetupCommand(this));
        this.getCommand("start").setExecutor(new StartCommand(this));
        this.getCommand("language").setExecutor(new LanguageCommand(this));
        this.getCommand("achievement").setExecutor(new AchievementCommand(this));
        this.getCommand("build").setExecutor(this.buildCommand);
    }

    private void initVoting() {
        /*
        CHECKING ALL CREATED MAP IF THEY FINISHED SETUP
         */
        this.mapList = new ArrayList<>();
        for(String current : getConfig().getConfigurationSection("Maps").getKeys(false)) {
            Map map = new Map(this, current);
            if(map.playable())
                this.mapList.add(map);
            else
                Bukkit.getConsoleSender().sendMessage("§cMap "+map.getName()+ " §cist noch nicht fertig eingerichtet!");
        }
        if(this.mapList.size() >= PotatoConstants.VOTING_MAP_AMOUNT)
            this.voting = new Voting(this, this.mapList);
        else {
            Bukkit.getConsoleSender().sendMessage("§cEs müssen mindestens " + PotatoConstants.VOTING_MAP_AMOUNT + " §cMaps eingerichtet sein.");
            this.voting = null;
        }
    }

    public GameStateManager getGameStateManager() {
        return this.gameStateManager;
    }

    public CustomPlayerManager getCustomPlayerManager() {
        return this.customPlayerManager;
    }

    public Voting getVoting() {
        return this.voting;
    }

    public List<Map> getMapList() {
        return this.mapList;
    }

    public LanguageModule getLanguageModule() {
        return this.languageModule;
    }

    public BuildCommand getBuildCommand() {
        return this.buildCommand;
    }

    public PotatoCountdown getPotatoCountdown() {
        return this.potatoCountdown;
    }

    public GameListener getGameListener() {
        return this.gameListener;
    }

    public AchievementManager getAchievementManager() {
        return achievementManager;
    }
}