package de.bukkitnews.hotpotato.countdowns;

import de.bukkitnews.hotpotato.HotPotato;
import de.bukkitnews.hotpotato.player.CustomPlayerCache;
import de.bukkitnews.hotpotato.utils.PotatoConstants;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

public class StartingCountdown extends Countdown{

    private final HotPotato hotPotato;
    private int seconds = 10;

    public StartingCountdown(HotPotato hotPotato){
        this.hotPotato = hotPotato;
    }

    @Override
    public void start() {
        this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.hotPotato, new Runnable() {
            @Override
            public void run() {
                switch (seconds){
                    case 10: case 5: case 3: case 2:
                        Bukkit.getOnlinePlayers().forEach(playerEach -> {
                            CustomPlayerCache customPlayerEachCache = hotPotato.getCustomPlayerManager().getPlayerCacheMap().get(playerEach);
                            String message = hotPotato.getLanguageModule().getMessage(customPlayerEachCache.getLocale(), "countdown_starting_broadcast");

                            playerEach.sendMessage(String.format(PotatoConstants.PREFIX+message, seconds));
                            playerEach.playSound(playerEach.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1F, 1F);
                        });
                        break;

                    case 1:
                        Bukkit.getOnlinePlayers().forEach(playerEach -> {
                            CustomPlayerCache customPlayerEachCache = hotPotato.getCustomPlayerManager().getPlayerCacheMap().get(playerEach);
                            String message = hotPotato.getLanguageModule().getMessage(customPlayerEachCache.getLocale(), "countdown_starting_broadcast1");

                            playerEach.sendMessage(String.format(PotatoConstants.PREFIX+message, seconds));
                            playerEach.playSound(playerEach.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1F, 1F);
                        });
                        break;

                    case 0:
                        hotPotato.getPotatoCountdown().start();
                        stop();
                        break;

                    default:
                        break;
                }
                seconds--;
            }
        }, 0, 20);
    }

    @Override
    public void stop() {
        Bukkit.getScheduler().cancelTask(this.taskID);
    }

}
