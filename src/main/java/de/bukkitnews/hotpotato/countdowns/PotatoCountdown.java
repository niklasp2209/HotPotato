package de.bukkitnews.hotpotato.countdowns;

import de.bukkitnews.hotpotato.HotPotato;
import de.bukkitnews.hotpotato.game.IngameState;
import de.bukkitnews.hotpotato.game.LobbyState;
import de.bukkitnews.hotpotato.utils.ConfigurationUtil;
import de.bukkitnews.hotpotato.utils.ItemBuilder;
import de.bukkitnews.hotpotato.utils.PotatoConstants;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.Collections;

public class PotatoCountdown extends Countdown {

    private int seconds = 20;
    private Player potato;
    private final HotPotato hotPotato;
    private final EndingCountdown endingCountdown;

    public PotatoCountdown(HotPotato hotPotato){
        this.hotPotato = hotPotato;
        this.endingCountdown = new EndingCountdown(hotPotato);
    }


    @Override
    public void start() {
        this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.hotPotato, new Runnable() {
            @Override
            public void run() {
                switch (seconds){
                    case 20:
                        selectPotato();
                        break;

                    case 0:
                        for(Player current : Bukkit.getOnlinePlayers()){
                            current.playSound(potato.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1F, 1F);
                        }
                        hotPotato.getGameListener().eliminatePlayer(potato);
                        if(PotatoConstants.playerList.size() == 1){
                            ConfigurationUtil configurationUtil = new ConfigurationUtil(hotPotato, "Lobby");
                            for(Player current : Bukkit.getOnlinePlayers()){
                                current.teleport(configurationUtil.loadLocation());
                                current.playSound(current.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1F, 1F);
                                current.showPlayer(current);
                                current.setGameMode(GameMode.SURVIVAL);
                            }
                            endingCountdown.start();
                            stop();
                        }

                        potato = null;
                        seconds = 23;
                        break;

                    default:
                        break;
                }

                if(potato != null){
                    Firework firework = (Firework) potato.getWorld().spawnEntity(potato.getLocation(), EntityType.FIREWORK);
                    potato.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, potato.getLocation(), 0, 0, 0, 0);
                    for(Player current : Bukkit.getOnlinePlayers()){
                        current.playSound(potato.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, 1F, 1F);
                    }
                }

                for(Player current : Bukkit.getOnlinePlayers()){
                    if(seconds <= 20){
                        current.setLevel(seconds);
                    }else
                        current.setLevel(0);
                }

                IngameState ingameState = (IngameState) hotPotato.getGameStateManager().getCurrentGameState();
                for(Player current : Bukkit.getOnlinePlayers()) {
                    ingameState.updateScoreboard(current);
                }
                setArmor();

                seconds--;
            }
        },0, 20);
    }

    private void selectPotato(){
        Collections.shuffle(PotatoConstants.playerList);
        this.potato = PotatoConstants.playerList.get(0);
    }

    public void setArmor(){
        ItemStack leatherHelmet = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta leatherHelmetItemMeta = (LeatherArmorMeta) leatherHelmet.getItemMeta();
        leatherHelmetItemMeta.setColor(Color.RED);
        leatherHelmet.setItemMeta(leatherHelmetItemMeta);

        ItemStack leatherChestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta leatherChestplateItemMeta = (LeatherArmorMeta) leatherChestplate.getItemMeta();
        leatherChestplateItemMeta.setColor(Color.RED);
        leatherChestplate.setItemMeta(leatherChestplateItemMeta);

        ItemStack leatherLeggins = new ItemStack(Material.LEATHER_LEGGINGS);
        LeatherArmorMeta leatherLegginsItemMeta = (LeatherArmorMeta) leatherLeggins.getItemMeta();
        leatherLegginsItemMeta.setColor(Color.RED);
        leatherLeggins.setItemMeta(leatherLegginsItemMeta);

        ItemStack leatherBoots = new ItemStack(Material.LEATHER_BOOTS);
        LeatherArmorMeta leatherBootsItemMeta = (LeatherArmorMeta) leatherBoots.getItemMeta();
        leatherBootsItemMeta.setColor(Color.RED);
        leatherBoots.setItemMeta(leatherBootsItemMeta);


        potato.getInventory().setHelmet(leatherHelmet);
        potato.getInventory().setChestplate(leatherChestplate);
        potato.getInventory().setLeggings(leatherLeggins);
        potato.getInventory().setBoots(leatherBoots);

        Bukkit.getOnlinePlayers().forEach(current -> {
            if(current != potato){
                current.getInventory().clear();
            }
        });
    }

    @Override
    public void stop() {
        Bukkit.getScheduler().cancelTask(this.taskID);
    }

    public Player getPotato() {
        return potato;
    }

    public void setPotato(Player potato) {
        this.potato = potato;
    }
}
