package de.bukkitnews.hotpotato.countdowns;

import de.bukkitnews.hotpotato.game.GameState;
import de.bukkitnews.hotpotato.game.GameStateManager;
import de.bukkitnews.hotpotato.maps.Map;
import de.bukkitnews.hotpotato.maps.Voting;
import de.bukkitnews.hotpotato.player.CustomPlayerCache;
import de.bukkitnews.hotpotato.utils.PotatoConstants;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/*
CLASS HANDLING THE LOBBYCOUNTDOWN BEFORE STARTING THE GAME INCLUDING BUKKIT-RUNNABLE
 */
public class LobbyCountdown extends Countdown {

    private int idleID;
    private int seconds;
    private boolean isIdling;
    private boolean isRunning;
    private final GameStateManager gameStateManager;

    public LobbyCountdown(GameStateManager gameStateManager) {
        this.gameStateManager = gameStateManager;
        this.seconds = PotatoConstants.LOBBY_COUNTDOWN;
    }

    @Override
    public void start() {
        /*
        STARTING LOBBY COUNTDOWN TO START THE GAME; ENOUGH PLAYERS ARE ONLINE
         */
        this.isRunning = true;
        this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.gameStateManager.getHotPotato(), new Runnable() {
            @Override
            public void run() {
                switch (seconds) {
                    case 60: case 30: case 15: case 10: case 5: case 4: case 3: case 2:
                        Bukkit.getOnlinePlayers().forEach(playerEach -> {
                            CustomPlayerCache customPlayerEachCache = gameStateManager.getHotPotato().getCustomPlayerManager().getPlayerCacheMap().get(playerEach);
                            String message = gameStateManager.getHotPotato().getLanguageModule().getMessage(customPlayerEachCache.getLocale(), "countdown_lobby_broadcast");

                            playerEach.sendMessage(String.format(PotatoConstants.PREFIX+message, seconds));
                            playerEach.playSound(playerEach.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1F, 1F);
                        });


                        /*
                        GETIING MAP WINNER OF MAP VOTING AND BROADCAST
                         */
                        if(seconds == 5) {
                            Voting voting = gameStateManager.getHotPotato().getVoting();
                            Map winnerMap;
                            if(voting != null) {
                                winnerMap = voting.getWinnerMap();
                            } else {
                                List<Map> mapList = gameStateManager.getHotPotato().getMapList();
                                Collections.shuffle(mapList);
                                winnerMap = mapList.get(0);
                            }
                            Bukkit.getOnlinePlayers().forEach(playerEach -> {
                                CustomPlayerCache customPlayerEachCache = gameStateManager.getHotPotato().getCustomPlayerManager().getPlayerCacheMap().get(playerEach);
                                String message = gameStateManager.getHotPotato().getLanguageModule().getMessage(customPlayerEachCache.getLocale(), "voting_winner");

                                playerEach.sendMessage(String.format(PotatoConstants.PREFIX+message, winnerMap.getName()));
                            });
                        }

                        break;

                    case 1:
                        Bukkit.getOnlinePlayers().forEach(playerEach -> {
                            CustomPlayerCache customPlayerEachCache = gameStateManager.getHotPotato().getCustomPlayerManager().getPlayerCacheMap().get(playerEach);
                            String message = gameStateManager.getHotPotato().getLanguageModule().getMessage(customPlayerEachCache.getLocale(), "countdown_lobby_broadcast1");

                            playerEach.sendMessage(String.format(PotatoConstants.PREFIX+message, seconds));
                            playerEach.playSound(playerEach.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1F, 1F);
                        });
                        break;

                    case 0:
                        gameStateManager.setGameState(GameState.INGAME_STATE);
                        break;

                    default:
                        break;
                }
                seconds--;
            }
        }, 0L, 20L);
    }

    @Override
    public void stop() {
        if(this.isRunning) {
            Bukkit.getScheduler().cancelTask(this.taskID);
            this.isRunning = false;
            this.seconds = PotatoConstants.LOBBY_COUNTDOWN;
        }
    }

    public void startIdle(){
        /*
        STARTING IDLE IF NOT ENOUGH PLAYERS ONLINE TO START
         */
        this.isIdling = true;
        this.idleID = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.gameStateManager.getHotPotato(), new Runnable() {
            @Override
            public void run() {
                int amount = PotatoConstants.MIN_PLAYERS-PotatoConstants.playerList.size();
                Bukkit.getOnlinePlayers().forEach(playerEach -> {
                    CustomPlayerCache customPlayerEachCache = gameStateManager.getHotPotato().getCustomPlayerManager().getPlayerCacheMap().get(playerEach);
                    String message = gameStateManager.getHotPotato().getLanguageModule().getMessage(customPlayerEachCache.getLocale(), "countdown_lobby_players");

                    playerEach.sendMessage(String.format(PotatoConstants.PREFIX+message, amount));
                });
            }
        },0L, 20L * PotatoConstants.IDLE_TIME);
    }

    public void stopIdle() {
        if(this.isIdling) {
            Bukkit.getScheduler().cancelTask(this.idleID);
            this.isIdling = false;
        }
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public int getSeconds() {
        return this.seconds;
    }

    public boolean isRunning() {
        return this.isRunning;
    }
}
