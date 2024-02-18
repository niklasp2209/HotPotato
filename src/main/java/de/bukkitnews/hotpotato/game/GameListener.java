package de.bukkitnews.hotpotato.game;

import de.bukkitnews.hotpotato.HotPotato;
import de.bukkitnews.hotpotato.utils.PotatoConstants;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class GameListener implements Listener {

    private final HotPotato hotPotato;

    public GameListener(HotPotato hotPotato){
        this.hotPotato = hotPotato;
    }

    @EventHandler
    public void handleEntityDamage(EntityDamageByEntityEvent event) {
        event.setCancelled(true);
        if(!(event.getDamager() instanceof Player && event.getEntity() instanceof Player)) return;
        if(this.hotPotato.getPotatoCountdown().getPotato() == null)return;

        Player damagedBy = (Player) event.getDamager();
        Player damagedPlayer = (Player) event.getEntity();

        if(PotatoConstants.spectatorList.contains(damagedBy)) {
            event.setCancelled(true);
            return;
        }

        if(damagedBy.equals(this.hotPotato.getPotatoCountdown().getPotato())) {
            this.hotPotato.getPotatoCountdown().setPotato(damagedPlayer);
            this.hotPotato.getPotatoCountdown().setArmor();
        }
    }

    public void eliminatePlayer(Player player) {
        PotatoConstants.playerList.remove(player);
        if(PotatoConstants.playerList.size() > 1) {
            IngameState ingameState = (IngameState) hotPotato.getGameStateManager().getCurrentGameState();
            ingameState.addSpectator(player);
        }
    }
}
