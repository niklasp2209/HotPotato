package de.bukkitnews.hotpotato.player;

import de.bukkitnews.hotpotato.HotPotato;
import de.bukkitnews.hotpotato.countdowns.LobbyCountdown;
import de.bukkitnews.hotpotato.game.LobbyState;
import de.bukkitnews.hotpotato.utils.ConfigurationUtil;
import de.bukkitnews.hotpotato.utils.PotatoConstants;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
        event.setJoinMessage(null);

        CustomPlayerCache customPlayerCache = new CustomPlayerCache(false, 0, 0, "de");
        hotPotato.getCustomPlayerManager().getPlayerCacheMap().put(player, customPlayerCache);

        System.out.println(player.getLocale());

        /*
        THREADED FUNCTION TO PULL STATS FROM SQL
         */
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
            /*
            LOGIC FOR PLAYER JOINING SERVER WHEN GAME ISNT STARTET (LOBBYSTATE)
             */
            PotatoConstants.playerList.add(player);
            /*
            GETTING PLAYERS LANGUAGE
             */
            Bukkit.getOnlinePlayers().forEach(playerEach -> {
                CustomPlayerCache customPlayerEachCache = hotPotato.getCustomPlayerManager().getPlayerCacheMap().get(playerEach);
                String message = hotPotato.getLanguageModule().getMessage(customPlayerEachCache.getLocale(), "join_message");

                player.sendMessage(String.format(PotatoConstants.PREFIX+ message +
                        " §7["+PotatoConstants.playerList.size()+"/"+PotatoConstants.MAX_PLAYERS+"]", player.getName()));
            });

            /*
            CHECKING IF LOBBY-SPAWN WAS SETUP
             */
            ConfigurationUtil configurationUtil = new ConfigurationUtil(hotPotato, "Lobby");
            if(configurationUtil.loadLocation() != null)
                player.teleport(configurationUtil.loadLocation());
            else
                Bukkit.getConsoleSender().sendMessage(PotatoConstants.PREFIX+" §4Der Lobby Spawn wurde nicht gesetzt.");

            /*
            CHECKING IF THERE ARE ENOUGH PLAYERS TO START LOBBYCOUNTDOWN
             */
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
        event.setQuitMessage(null);

        if(hotPotato.getCustomPlayerManager().getPlayerCacheMap().containsKey(player))
            hotPotato.getCustomPlayerManager().getPlayerCacheMap().remove(player);

        if(PotatoConstants.playerList.contains(player))
            PotatoConstants.playerList.remove(player);

        if(hotPotato.getGameStateManager().getCurrentGameState() instanceof LobbyState){

            Bukkit.getOnlinePlayers().forEach(playerEach -> {
                CustomPlayerCache customPlayerEachCache = hotPotato.getCustomPlayerManager().getPlayerCacheMap().get(playerEach);
                String message = hotPotato.getLanguageModule().getMessage(customPlayerEachCache.getLocale(), "quit_message");

                player.sendMessage(String.format(PotatoConstants.PREFIX+ message +
                        " §7["+PotatoConstants.playerList.size()+"/"+PotatoConstants.MAX_PLAYERS+"]", player.getName()));
            });

            /*
            CHECKING IF THERE ARE STILL ENOUGH PLAYERS TO FORCE GAME START
             */
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
