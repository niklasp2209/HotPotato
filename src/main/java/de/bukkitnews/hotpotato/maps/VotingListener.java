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

    private HotPotato hotPotato;
    private Voting voting;

    public VotingListener(HotPotato hotPotato){
        this.hotPotato = hotPotato;
        voting = hotPotato.getVoting();
    }

    @EventHandler
    public void handleVote(PlayerInteractEvent event){
        if(!(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_AIR))return;
        if(event.getHand() == EquipmentSlot.OFF_HAND)return;
        if(event.getItem() == null)return;
        if(event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(PotatoConstants.INVENTORY_VOTING))
            event.getPlayer().openInventory(voting.getInventory());
    }

    @EventHandler
    public void handleVoting(InventoryClickEvent event){
        if(!(event.getWhoClicked() instanceof Player))return;
        if(!(event.getView().getTitle().equalsIgnoreCase(PotatoConstants.INVENTORY_VOTING)))return;
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        for(int i = 0; i < voting.getVotingInventoryOrder().length; i++){
            if(voting.getVotingInventoryOrder()[i] == event.getSlot()){
                voting.vote(player, i);
                return;
            }
        }
    }
}
