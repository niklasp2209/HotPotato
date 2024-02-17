package de.bukkitnews.hotpotato.countdowns;

import de.bukkitnews.hotpotato.game.GameState;
import de.bukkitnews.hotpotato.game.GameStateManager;
import de.bukkitnews.hotpotato.maps.Map;
import de.bukkitnews.hotpotato.maps.Voting;
import de.bukkitnews.hotpotato.utils.PotatoConstants;
import org.bukkit.Bukkit;

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
    private GameStateManager gameStateManager;

    public LobbyCountdown(GameStateManager gameStateManager){
        this.gameStateManager = gameStateManager;
        seconds = PotatoConstants.LOBBY_COUNTDOWN;
    }

    @Override
    public void start() {
        /*
        STARTING LOBBY COUNTDOWN TO START THE GAME; ENOUGH PLAYERS ARE ONLINE
         */
        isRunning = true;
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(gameStateManager.getHotPotato(), new Runnable() {
            @Override
            public void run() {
                switch (seconds){
                    case 60: case 30: case 15: case 10: case 5: case 4: case 3: case 2:
                        Bukkit.broadcastMessage(PotatoConstants.PREFIX+" §7Das Spiel startet in §e"+seconds+" Sekunden§7.");


                        /*
                        GETIING MAP WINNER OF MAP VOTING AND BROADCAST
                         */
                        if(seconds == 5){
                            Voting voting = gameStateManager.getHotPotato().getVoting();
                            Map winnerMap;
                            if(voting != null){
                                winnerMap = voting.getWinnerMap();
                            }else{
                                List<Map> mapList = gameStateManager.getHotPotato().getMapList();
                                Collections.shuffle(mapList);
                                winnerMap = mapList.get(0);
                            }
                            Bukkit.broadcastMessage(PotatoConstants.PREFIX+" §7Sieger des Map-Votings: §a"+winnerMap.getName());
                        }

                        break;

                    case 1:
                        Bukkit.broadcastMessage(PotatoConstants.PREFIX+" §7Das Spiel startet in §e"+seconds+" Sekunde§7.");
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
        if(isRunning){
            Bukkit.getScheduler().cancelTask(taskID);
            isRunning = false;
            seconds = PotatoConstants.LOBBY_COUNTDOWN;
        }
    }

    public void startIdle(){
        /*
        STARTING IDLE IF NOT ENOUGH PLAYERS ONLINE TO START
         */
        isIdling = true;
        idleID = Bukkit.getScheduler().scheduleSyncRepeatingTask(gameStateManager.getHotPotato(), new Runnable() {
            @Override
            public void run() {
                Bukkit.broadcastMessage(PotatoConstants.PREFIX+" §7Es fehlen noch §e"+
                        (PotatoConstants.MIN_PLAYERS-PotatoConstants.playerList.size())+" Spieler §7zum Spielstart.");
            }
        },0L, 20L * PotatoConstants.IDLE_TIME);
    }

    public void stopIdle(){
        if(isIdling){
            Bukkit.getScheduler().cancelTask(idleID);
            isIdling = false;
        }
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public int getSeconds() {
        return seconds;
    }

    public boolean isRunning() {
        return isRunning;
    }
}
