package de.bukkitnews.hotpotato.game;

import de.bukkitnews.hotpotato.HotPotato;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class GameListener implements Listener {

    HotPotato hotPotato;

    public GameListener(HotPotato hotPotato){
        this.hotPotato = hotPotato;
    }

    @EventHandler
    public void handleEntityDamage(EntityDamageByEntityEvent event){
        event.setCancelled(true);
        if(!(event.getDamager() instanceof Player && event.getEntity() instanceof Player)) return;
        if(this.hotPotato.getPotatoCountdown().getPotato() == null)return;

        Player damagedBy = (Player) event.getDamager();
        Player damagedPlayer = (Player) event.getEntity();

        System.out.println("Damaged by: " + damagedBy.getName());
        System.out.println("Current potato player: " + this.hotPotato.getPotatoCountdown().getPotato().getName());

        if(damagedBy.equals(this.hotPotato.getPotatoCountdown().getPotato())){
            this.hotPotato.getPotatoCountdown().setPotato(damagedPlayer);
        }
    }
}
