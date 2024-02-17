package de.bukkitnews.hotpotato.maps;

import de.bukkitnews.hotpotato.HotPotato;
import de.bukkitnews.hotpotato.player.CustomPlayerCache;
import de.bukkitnews.hotpotato.utils.ItemBuilder;
import de.bukkitnews.hotpotato.utils.PotatoConstants;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Voting {

    private HotPotato hotPotato;
    private List<Map> mapList;
    private Map[] votingMaps;
    private int[] votingInventoryOrder = new int[]{3, 5};
    private HashMap<String, Integer> playerVotes;
    private Inventory inventory;

    public Voting(HotPotato hotPotato, List<Map> mapList){
        this.hotPotato = hotPotato;
        this.mapList = mapList;
        votingMaps = new Map[PotatoConstants.VOTING_MAP_AMOUNT];
        playerVotes = new HashMap<>();

        chooseRandomMaps();
        initInventory();
    }

    /*
    CHOOSING RANDOM MAPS FOR MAP VOTING
     */
    private void chooseRandomMaps(){
        for(int i = 0; i < votingMaps.length; i++){
            Collections.shuffle(mapList);
            votingMaps[i] = mapList.remove(0);
        }
    }


    /*
    INITIALIZE VOTING INVENTORY
     */
    public void initInventory(){
        inventory = Bukkit.createInventory(null, 9, PotatoConstants.INVENTORY_VOTING);
        for(int i = 0; i < votingMaps.length; i++){
            Map currentMap = votingMaps[i];
            inventory.setItem(votingInventoryOrder[i], new ItemBuilder(
                    Material.PAPER).setDisplayname("§a"+currentMap.getName()).setLore(
                            "§r", "§7Votes: §e"+currentMap.getVotes(), "§7Von: §e"+currentMap.getBuilder()).build());
        }
    }

    /*
    METHOD FOR GETTING MAP WITH MOST VOTES
     */
    public Map getWinnerMap(){
        Map map = votingMaps[0];
        for(int i = 1; i < votingMaps.length; i++){
            if(votingMaps[i].getVotes() >= map.getVotes())
                map = votingMaps[i];
        }
        return map;
    }

    public void vote(Player player, int votingMap){
        CustomPlayerCache customPlayerCache = hotPotato.getCustomPlayerManager().getPlayerCacheMap().get(player);

        if(!playerVotes.containsKey(player.getName())){
            player.closeInventory();
            votingMaps[votingMap].addVote();
            String message = hotPotato.getLanguageModule().getMessage(customPlayerCache.getLocale(), "voting_voted");
            player.sendMessage(String.format(PotatoConstants.PREFIX+message, votingMaps[votingMap].getName()));
            playerVotes.put(player.getName(), votingMap);
            initInventory();
        }else{
            String message = hotPotato.getLanguageModule().getMessage(customPlayerCache.getLocale(), "voting_already");
            player.sendMessage(PotatoConstants.PREFIX+message);
        }
    }

    public HashMap<String, Integer> getPlayerVotes() {
        return playerVotes;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public int[] getVotingInventoryOrder() {
        return votingInventoryOrder;
    }

    public Map[] getVotingMaps() {
        return votingMaps;
    }
}
