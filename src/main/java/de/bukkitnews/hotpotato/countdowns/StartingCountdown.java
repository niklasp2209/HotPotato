package de.bukkitnews.hotpotato.countdowns;

import de.bukkitnews.hotpotato.HotPotato;
import de.bukkitnews.hotpotato.player.CustomPlayerCache;
import de.bukkitnews.hotpotato.utils.PotatoConstants;
import org.bukkit.Bukkit;

public class StartingCountdown extends Countdown{

    private HotPotato hotPotato;
    private int seconds = 10;
    public static boolean ready = false;

    public StartingCountdown(HotPotato hotPotato){
        this.hotPotato = hotPotato;
    }

    @Override
    public void start() {
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(hotPotato, new Runnable() {
            @Override
            public void run() {
                switch (seconds){
                    case 10: case 5: case 3: case 2:
                        Bukkit.getOnlinePlayers().forEach(playerEach -> {
                            CustomPlayerCache customPlayerEachCache = hotPotato.getCustomPlayerManager().getPlayerCacheMap().get(playerEach);
                            String message = hotPotato.getLanguageModule().getMessage(customPlayerEachCache.getLocale(), "countdown_starting_broadcast");

                            playerEach.sendMessage(String.format(PotatoConstants.PREFIX+message, seconds));
                        });
                        break;

                    case 1:
                        Bukkit.getOnlinePlayers().forEach(playerEach -> {
                            CustomPlayerCache customPlayerEachCache = hotPotato.getCustomPlayerManager().getPlayerCacheMap().get(playerEach);
                            String message = hotPotato.getLanguageModule().getMessage(customPlayerEachCache.getLocale(), "countdown_starting_broadcast1");

                            playerEach.sendMessage(String.format(PotatoConstants.PREFIX+message, seconds));
                        });
                        break;

                    case 0:
                        ready = true;
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
        Bukkit.getScheduler().cancelTask(taskID);
    }
}
