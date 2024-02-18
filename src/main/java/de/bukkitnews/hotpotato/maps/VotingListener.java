package de.bukkitnews.hotpotato.maps;

import de.bukkitnews.hotpotato.HotPotato;
import de.bukkitnews.hotpotato.utils.PotatoConstants;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class VotingListener implements Listener {

    private final HotPotato hotPotato;
    private final Voting voting;

    public VotingListener(HotPotato hotPotato) {
        this.hotPotato = hotPotato;
        this.voting = hotPotato.getVoting();
    }

    @EventHandler
    public void handleVote(PlayerInteractEvent event) {
        if(!(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK))return;
        if(event.getHand() == EquipmentSlot.OFF_HAND)return;
        if(event.getItem() == null)return;
        if(event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(PotatoConstants.INVENTORY_VOTING)) {
            this.voting.initInventory(event.getPlayer());
            event.getPlayer().openInventory(this.voting.getInventory());
        }
    }

    @EventHandler
    public void handleVoting(InventoryClickEvent event){
        if(!(event.getWhoClicked() instanceof Player))return;
        if(!(event.getView().getTitle().equals(this.hotPotato.getLanguageModule().getMessage("de", "voting_inventory_title")) ||
                event.getView().getTitle().equals(this.hotPotato.getLanguageModule().getMessage("en", "voting_inventory_title"))))return;
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        for(int i = 0; i < this.voting.getVotingInventoryOrder().length; i++) {
            if(this.voting.getVotingInventoryOrder()[i] == event.getSlot()){
                this.voting.vote(player, i);
                return;
            }
        }
    }
}
