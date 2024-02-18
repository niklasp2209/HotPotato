package de.bukkitnews.hotpotato.game;

import de.bukkitnews.hotpotato.countdowns.PotatoCountdown;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class GameListener implements Listener {

    private final PotatoCountdown potatoCountdown;

    public GameListener(PotatoCountdown potatoCountdown){
        this.potatoCountdown = potatoCountdown;
    }

    @EventHandler
    public void handleEntityDamage(EntityDamageByEntityEvent event){
        event.setCancelled(true);
        if(!(event.getDamager() instanceof Player && event.getEntity() instanceof Player)) return;
        if(this.potatoCountdown.getPotato() == null)return;

        Player damagedBy = (Player) event.getDamager();
        Player damagedPlayer = (Player) event.getEntity();

        System.out.println("Damaged by: " + damagedBy.getName());
        System.out.println("Current potato player: " + this.potatoCountdown.getPotato().getName());

        if(damagedBy.equals(this.potatoCountdown.getPotato())){
            this.potatoCountdown.setPotato(damagedPlayer);
        }
    }
}
