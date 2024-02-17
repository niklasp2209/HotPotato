package de.bukkitnews.hotpotato.player;

import de.bukkitnews.hotpotato.HotPotato;
import de.bukkitnews.hotpotato.countdowns.LobbyCountdown;
import de.bukkitnews.hotpotato.game.LobbyState;
import de.bukkitnews.hotpotato.utils.PotatoConstants;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PlayerConnectionListener implements Listener {

    private HotPotato hotPotato;

    public PlayerConnectionListener(HotPotato hotPotato){
        this.hotPotato = hotPotato;
    }

    @EventHandler
    public void handleJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        hotPotato.getCustomPlayerManager().getPlayerCacheMap().put(player, new CustomPlayerCache());

        Executors.newCachedThreadPool().execute(() -> {
            try{
                TimeUnit.MILLISECONDS.sleep(500L);
                /*
                LOADING STATS FROM SQL IN CACHE
                 */
            }catch (InterruptedException exception){
                exception.printStackTrace();
            }
        });

        if(hotPotato.getGameStateManager().getCurrentGameState() instanceof LobbyState){
            PotatoConstants.playerList.add(player);
            event.setJoinMessage(PotatoConstants.PREFIX+"§a"+player.getName()+" §7ist dem Spiel beigetreten." +
                    " §7["+PotatoConstants.playerList.size()+"/"+PotatoConstants.MAX_PLAYERS+"]");

            LobbyState lobbyState = (LobbyState) hotPotato.getGameStateManager().getCurrentGameState();
            LobbyCountdown lobbyCountdown = lobbyState.getLobbyCountdown();
            if(PotatoConstants.playerList.size() >= PotatoConstants.MIN_PLAYERS){
                if(!lobbyCountdown.isRunning()){
                    lobbyCountdown.stopIdle();
                    lobbyCountdown.start();
                }
            }
        }

    }

    @EventHandler
    public void handleQuit(PlayerQuitEvent event){
        Player player  = event.getPlayer();

        if(hotPotato.getCustomPlayerManager().getPlayerCacheMap().containsKey(player))
            hotPotato.getCustomPlayerManager().getPlayerCacheMap().remove(player);

        if(PotatoConstants.playerList.contains(player))
            PotatoConstants.playerList.remove(player);

        if(hotPotato.getGameStateManager().getCurrentGameState() instanceof LobbyState){
            event.setQuitMessage(PotatoConstants.PREFIX+"§a"+player.getName()+" §7hat das Spiel verlassen." +
                    " §7["+PotatoConstants.playerList.size()+"/"+PotatoConstants.MAX_PLAYERS+"]");

            LobbyState lobbyState = (LobbyState) hotPotato.getGameStateManager().getCurrentGameState();
            LobbyCountdown lobbyCountdown = lobbyState.getLobbyCountdown();
            if(PotatoConstants.playerList.size() < PotatoConstants.MIN_PLAYERS){
                if(lobbyCountdown.isRunning()){
                    lobbyCountdown.stop();
                    lobbyCountdown.startIdle();
                }
            }
        }
    }
}
