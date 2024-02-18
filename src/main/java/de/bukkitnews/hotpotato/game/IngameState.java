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
        }
        this.startingCountdown.start();
    }

    @Override
    public void stop() {

    }
}
