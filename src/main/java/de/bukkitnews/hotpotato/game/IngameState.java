package de.bukkitnews.hotpotato.game;

import de.bukkitnews.hotpotato.HotPotato;
import de.bukkitnews.hotpotato.countdowns.StartingCountdown;
import de.bukkitnews.hotpotato.maps.Map;
import de.bukkitnews.hotpotato.player.CustomPlayerCache;
import de.bukkitnews.hotpotato.utils.PotatoConstants;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Collections;
import java.util.List;

public class IngameState extends GameState {

    private final HotPotato hotPotato;
    private Map map;
    private List<Player> players;
    private final StartingCountdown startingCountdown;

    public IngameState(HotPotato hotPotato){
        this.hotPotato = hotPotato;
        this.startingCountdown = new StartingCountdown(hotPotato);
    }

    @Override
    public void start() {
        Collections.shuffle(PotatoConstants.playerList);
        this.players = PotatoConstants.playerList;

        this.map = this.hotPotato.getVoting().getWinnerMap();
        this.map.load();

        for(int i = 0; i < this.players.size(); i++){
            this.players.get(i).teleport(this.map.getSpawnLocations()[i]);
            this.players.get(i).playSound(this.players.get(i).getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1F, 1F);
        }

        for(Player current : players){
            current.getInventory().clear();
            updateScoreboard(current);
        }
        this.startingCountdown.start();
    }

    public void addSpectator(Player player){
        PotatoConstants.spectatorList.add(player);
        player.setGameMode(GameMode.CREATIVE);
        player.teleport(map.getSpectatorLocation());
        for(Player current : Bukkit.getOnlinePlayers()){
            current.hidePlayer(player);
        }
    }

    public void updateScoreboard(Player player){
        CustomPlayerCache customPlayerCache = hotPotato.getCustomPlayerManager().getPlayerCacheMap().get(player);

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("abcd", "abcd");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName("§aHotPotato");
        String message = this.hotPotato.getLanguageModule().getMessage(customPlayerCache.getLocale(), "scoreboard");
        objective.getScore("").setScore(2);
        objective.getScore("§7"+message).setScore(1);
        objective.getScore("§7» §e"+PotatoConstants.playerList.size()).setScore(0);
        player.setScoreboard(scoreboard);
    }

    @Override
    public void stop() {

    }
}
