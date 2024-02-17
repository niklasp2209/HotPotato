package de.bukkitnews.hotpotato.game;

import de.bukkitnews.hotpotato.HotPotato;
import de.bukkitnews.hotpotato.countdowns.StartingCountdown;
import de.bukkitnews.hotpotato.maps.Map;
import de.bukkitnews.hotpotato.utils.PotatoConstants;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class IngameState extends GameState {

    private HotPotato hotPotato;
    private Map map;
    private List<Player> players;
    private StartingCountdown startingCountdown;

    public IngameState(HotPotato hotPotato){
        this.hotPotato = hotPotato;
        startingCountdown = new StartingCountdown(hotPotato);
    }

    @Override
    public void start() {
        Collections.shuffle(PotatoConstants.playerList);
        players = PotatoConstants.playerList;

        map = hotPotato.getVoting().getWinnerMap();
        map.load();

        for(int i = 0; i < players.size(); i++){
            players.get(i).teleport(map.getSpawnLocations()[i]);
            players.get(i).playSound(players.get(i).getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1F, 1F);
        }

        for(Player current : players){
            current.getInventory().clear();
        }
        startingCountdown.start();
    }

    @Override
    public void stop() {

    }
}
