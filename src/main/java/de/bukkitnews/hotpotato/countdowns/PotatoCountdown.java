package de.bukkitnews.hotpotato.countdowns;

import de.bukkitnews.hotpotato.HotPotato;
import de.bukkitnews.hotpotato.utils.ConfigurationUtil;
import de.bukkitnews.hotpotato.utils.PotatoConstants;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;

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
                        Collections.shuffle(PotatoConstants.playerList);
                        potato = PotatoConstants.playerList.get(0);
                        break;

                    case 0:
                        for(Player current : Bukkit.getOnlinePlayers()){
                            current.playSound(potato.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1F, 1F);
                        }
                        PotatoConstants.playerList.remove(potato);
                        if(PotatoConstants.playerList.size() == 1){
                            ConfigurationUtil configurationUtil = new ConfigurationUtil(hotPotato, "Lobby");
                            for(Player current : Bukkit.getOnlinePlayers()){
                                current.teleport(configurationUtil.loadLocation());
                                current.playSound(current.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1F, 1F);

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

                seconds--;
            }
        },0, 20);
    }

    @Override
    public void stop() {
        Bukkit.getScheduler().cancelTask(this.taskID);
    }

    public Player getPotato() {
        return this.potato;
    }

    public void setPotato(Player potato) {
        this.potato = potato;
    }
}
