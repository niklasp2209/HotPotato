package de.bukkitnews.hotpotato.maps;

import de.bukkitnews.hotpotato.HotPotato;
import de.bukkitnews.hotpotato.player.CustomPlayerCache;
import de.bukkitnews.hotpotato.utils.ItemBuilder;
import de.bukkitnews.hotpotato.utils.PotatoConstants;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Voting {

    private final HotPotato hotPotato;
    private final List<Map> mapList;
    private final Map[] votingMaps;
    private final int[] votingInventoryOrder = new int[]{3, 5};
    private final java.util.Map<String, Integer> playerVotes;
    private Inventory inventory;

    public Voting(HotPotato hotPotato, List<Map> mapList) {
        this.hotPotato = hotPotato;
        this.mapList = mapList;
        this.votingMaps = new Map[PotatoConstants.VOTING_MAP_AMOUNT];
        this.playerVotes = new HashMap<>();

        chooseRandomMaps();
    }

    /*
    CHOOSING RANDOM MAPS FOR MAP VOTING
     */
    private void chooseRandomMaps() {
        for(int i = 0; i < this.votingMaps.length; i++) {
            Collections.shuffle(this.mapList);
            this.votingMaps[i] = this.mapList.remove(0);
        }
    }


    /*
    INITIALIZE VOTING INVENTORY
     */
    public void initInventory(Player player) {
        CustomPlayerCache customPlayerCache = hotPotato.getCustomPlayerManager().getPlayerCacheMap().get(player);
        String inventoryTitle = hotPotato.getLanguageModule().getMessage(customPlayerCache.getLocale(), "voting_inventory_title");

        this.inventory = Bukkit.createInventory(null, 9, inventoryTitle);

        for (int i = 0; i < this.votingMaps.length; i++) {
            Map currentMap = this.votingMaps[i];
            this.inventory.setItem(this.votingInventoryOrder[i], new ItemBuilder(
                    Material.PAPER).setDisplayname("§a" + currentMap.getName()).setLore(
                    "§r", "§7Votes: §e" + currentMap.getVotes(), "§7Von: §e" + currentMap.getBuilder()).build());
        }
    }

    /*
    METHOD FOR UPDATING VOTING INVENTORY BASED ON PLAYER'S LANGUAGE
    */
    public void updateInventory(Player player) {
        player.openInventory(this.inventory);
    }

    /*
    METHOD FOR GETTING MAP WITH MOST VOTES
     */
    public Map getWinnerMap() {
        Map map = this.votingMaps[0];
        for(int i = 1; i < this.votingMaps.length; i++) {
            if(this.votingMaps[i].getVotes() >= map.getVotes())
                map = this.votingMaps[i];
        }
        return map;
    }

    public void vote(Player player, int votingMap) {
        CustomPlayerCache customPlayerCache = this.hotPotato.getCustomPlayerManager().getPlayerCacheMap().get(player);

        if(!this.playerVotes.containsKey(player.getName())) {
            player.closeInventory();
            this.votingMaps[votingMap].addVote();
            String message = this.hotPotato.getLanguageModule().getMessage(customPlayerCache.getLocale(), "voting_voted");
            player.sendMessage(String.format(PotatoConstants.PREFIX+message, this.votingMaps[votingMap].getName()));
            this.playerVotes.put(player.getName(), votingMap);
            initInventory(player);
            updateInventory(player);

            for(Player current : Bukkit.getOnlinePlayers()) {
                if(current.getOpenInventory().getTitle().equals(this.hotPotato.getLanguageModule().getMessage("de", "voting_inventory_title")) ||
                current.getOpenInventory().getTitle().equals(this.hotPotato.getLanguageModule().getMessage("en", "voting_inventory_title"))) {
                    initInventory(current);
                    updateInventory(current);
                }
            }
        }else{
            String message = this.hotPotato.getLanguageModule().getMessage(customPlayerCache.getLocale(), "voting_already");
            player.sendMessage(PotatoConstants.PREFIX+message);
        }
    }

    public java.util.Map<String, Integer> getPlayerVotes() {
        return this.playerVotes;
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public int[] getVotingInventoryOrder() {
        return this.votingInventoryOrder;
    }

    public Map[] getVotingMaps() {
        return this.votingMaps;
    }
}
