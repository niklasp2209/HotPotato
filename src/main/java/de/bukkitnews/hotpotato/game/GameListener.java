package de.bukkitnews.hotpotato.game;

import de.bukkitnews.hotpotato.HotPotato;
import de.bukkitnews.hotpotato.countdowns.PotatoCountdown;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class GameListener implements Listener{

    private final HotPotato hotPotato;
    private final PotatoCountdown potatoCountdown;

    public GameListener(HotPotato hotPotato){
        this.hotPotato = hotPotato;
        this.potatoCountdown = new PotatoCountdown(hotPotato);
    }


    @EventHandler
    public void handleEntityDamage(EntityDamageByEntityEvent event){
        event.setCancelled(true);
        if(!(event.getDamager() instanceof Player && event.getEntity() instanceof Player))return;
        if(this.potatoCountdown.getPotato() == null)return;
        if(event.getDamager() == this.potatoCountdown.getPotato()){
            this.potatoCountdown.setPotato((Player) event.getEntity());
        }
    }
}
