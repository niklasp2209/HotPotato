package de.bukkitnews.hotpotato.countdowns;

import de.bukkitnews.hotpotato.HotPotato;
import de.bukkitnews.hotpotato.player.CustomPlayerCache;
import de.bukkitnews.hotpotato.utils.PotatoConstants;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class EndingCountdown extends Countdown {

    private HotPotato hotPotato;
    private int seconds = 16;

    public EndingCountdown(HotPotato hotPotato){
        this.hotPotato = hotPotato;
    }

    @Override
    public void start() {
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(hotPotato, new Runnable() {
            @Override
            public void run() {

                switch (seconds){
                    case 16:
                        /*
                        PLAYER WON
                         */
                        break;

                    case 15: case 10: case 5: case 4: case 3: case 2:
                        Bukkit.getOnlinePlayers().forEach(playerEach -> {
                            CustomPlayerCache customPlayerEachCache = hotPotato.getCustomPlayerManager().getPlayerCacheMap().get(playerEach);
                            String message = hotPotato.getLanguageModule().getMessage(customPlayerEachCache.getLocale(), "countdown_end_broadcast");

                            playerEach.sendMessage(String.format(PotatoConstants.PREFIX+message, seconds));
                        });
                        break;

                    case 1:
                        Bukkit.getOnlinePlayers().forEach(playerEach -> {
                            CustomPlayerCache customPlayerEachCache = hotPotato.getCustomPlayerManager().getPlayerCacheMap().get(playerEach);
                            String message = hotPotato.getLanguageModule().getMessage(customPlayerEachCache.getLocale(), "countdown_end_broadcast1");

                            playerEach.sendMessage(String.format(PotatoConstants.PREFIX+message, seconds));
                        });
                        break;

                    case 0:
                        for(Player all : Bukkit.getOnlinePlayers()){
                            all.kickPlayer("Die Runde wurde neugestartet");
                        }
                        Bukkit.reload();
                        /*
                        ALTERNATIV SERVER STOP
                         */
                        break;
                }

                seconds--;
            }
        },0, 20);
    }

    @Override
    public void stop() {
        Bukkit.getScheduler().cancelTask(taskID);
    }
}
