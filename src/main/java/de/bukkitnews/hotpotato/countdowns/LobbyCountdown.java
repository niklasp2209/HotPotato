package de.bukkitnews.hotpotato.countdowns;

import de.bukkitnews.hotpotato.HotPotato;
import de.bukkitnews.hotpotato.game.GameState;
import de.bukkitnews.hotpotato.game.GameStateManager;
import de.bukkitnews.hotpotato.utils.PotatoConstants;
import org.bukkit.Bukkit;

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
        isRunning = true;
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(gameStateManager.getHotPotato(), new Runnable() {
            @Override
            public void run() {
                switch (seconds){
                    case 60: case 30: case 15: case 10: case 5: case 4: case 3: case 2:
                        Bukkit.broadcastMessage(PotatoConstants.PREFIX+" §7Das Spiel startet in §e"+seconds+" Sekunden§7.");
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

    public boolean isRunning() {
        return isRunning;
    }
}
