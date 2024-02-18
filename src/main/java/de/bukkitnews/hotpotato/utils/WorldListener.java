package de.bukkitnews.hotpotato.utils;

import de.bukkitnews.hotpotato.HotPotato;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WorldListener implements Listener {

    private final HotPotato hotPotato;

    public WorldListener(HotPotato hotPotato){
        this.hotPotato = hotPotato;
    }

    @EventHandler
    public void handleBreak(BlockBreakEvent event) {
        if(this.hotPotato.getBuildCommand().getBuildList().contains(event.getPlayer()))return;
        event.setCancelled(true);
    }

    @EventHandler
    public void handlePlace(BlockPlaceEvent event) {
        if(this.hotPotato.getBuildCommand().getBuildList().contains(event.getPlayer()))return;
        event.setCancelled(true);
    }

    @EventHandler
    public void handleClick(InventoryClickEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void handleWeather(WeatherChangeEvent event) {
        if(event.toWeatherState())
            event.setCancelled(true);
    }

    @EventHandler
    public void handleCrop(PlayerInteractEvent event) {
        if(this.hotPotato.getBuildCommand().getBuildList().contains(event.getPlayer()))return;
        if(event.getAction().equals(Action.PHYSICAL) && event.getClickedBlock().getType().equals(Material.FARMLAND)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void handleDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player) {
            if(this.hotPotato.getBuildCommand().getBuildList().contains((Player) event.getEntity()))return;
            event.setCancelled(true);
        }
    }

    public void initWorlds() {
        for(World world : Bukkit.getWorlds()) {
            world.setTime(6000L);
            world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
            world.setDifficulty(Difficulty.PEACEFUL);
        }
    }
}
