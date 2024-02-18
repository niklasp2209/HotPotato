package de.bukkitnews.hotpotato.player;

import de.bukkitnews.hotpotato.HotPotato;
import de.bukkitnews.hotpotato.countdowns.LobbyCountdown;
import de.bukkitnews.hotpotato.game.IngameState;
import de.bukkitnews.hotpotato.game.LobbyState;
import de.bukkitnews.hotpotato.maps.Voting;
import de.bukkitnews.hotpotato.utils.ConfigurationUtil;
import de.bukkitnews.hotpotato.utils.ItemBuilder;
import de.bukkitnews.hotpotato.utils.PotatoConstants;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerConnectionListener implements Listener {

    private final HotPotato hotPotato;
    private final ItemStack voteItem;

    public PlayerConnectionListener(HotPotato hotPotato) {
        this.hotPotato = hotPotato;
        this.voteItem = new ItemBuilder(Material.PAPER).setDisplayname(PotatoConstants.INVENTORY_VOTING).build();
    }

    @EventHandler
    public void handleJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.setJoinMessage(null);

        CustomPlayerCache customPlayerCache = new CustomPlayerCache(false, 0, 0, "de");
        this.hotPotato.getCustomPlayerManager().getPlayerCacheMap().put(player, customPlayerCache);

        if (this.hotPotato.getGameStateManager().getCurrentGameState() instanceof LobbyState) {
            // LOGIC FOR PLAYER JOINING SERVER WHEN GAME ISNT STARTET (LOBBYSTATE)
            handleJoinInLobby(player);
        } else if(this.hotPotato.getGameStateManager().getCurrentGameState() instanceof IngameState) {
            IngameState ingameState = (IngameState) hotPotato.getGameStateManager().getCurrentGameState();
            ingameState.addSpectator(player);
        } else {
            //NO LANGUAGE CHECK BECAUSE PLAYER CANT CHANGE LANGUAGE BEFORE
            player.kickPlayer("Die Runde startet gerade neu.");
        }
    }

    private void handleJoinInLobby(Player player) {
        PotatoConstants.playerList.add(player);
        player.getInventory().clear();
        player.getInventory().setItem(4, this.voteItem);
        player.setGameMode(GameMode.SURVIVAL);
        for(Player current : Bukkit.getOnlinePlayers()) {
            current.showPlayer(player);
            player.showPlayer(current);
        }

        Bukkit.getOnlinePlayers().forEach(playerEach -> {
            CustomPlayerCache customPlayerEachCache = this.hotPotato.getCustomPlayerManager().getPlayerCacheMap().get(playerEach);
            String message = this.hotPotato.getLanguageModule().getMessage(customPlayerEachCache.getLocale(), "join_message");

            playerEach.sendMessage(String.format(PotatoConstants.PREFIX + message +
                    " §7[" + PotatoConstants.playerList.size() + "/" + PotatoConstants.MAX_PLAYERS + "]", player.getName()));
        });

        ConfigurationUtil configurationUtil = new ConfigurationUtil(this.hotPotato, "Lobby");
        if (configurationUtil.loadLocation() != null)
            player.teleport(configurationUtil.loadLocation());
        else
            Bukkit.getConsoleSender().sendMessage(PotatoConstants.PREFIX + " §4Der Lobby Spawn wurde nicht gesetzt.");

        LobbyState lobbyState = (LobbyState) this.hotPotato.getGameStateManager().getCurrentGameState();
        LobbyCountdown lobbyCountdown = lobbyState.getLobbyCountdown();
        if (PotatoConstants.playerList.size() >= PotatoConstants.MIN_PLAYERS && !lobbyCountdown.isRunning()) {
            lobbyCountdown.stopIdle();
            lobbyCountdown.start();
        }
    }

    @EventHandler
    public void handleQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.setQuitMessage(null);

        PotatoConstants.playerList.remove(player);

        if (this.hotPotato.getGameStateManager().getCurrentGameState() instanceof LobbyState) {
            // CHECKING IF THERE ARE STILL ENOUGH PLAYERS TO FORCE GAME START
            handleQuitInLobby(player);
        }else if(this.hotPotato.getGameStateManager().getCurrentGameState() instanceof IngameState){
            IngameState ingameState = (IngameState) this.hotPotato.getGameStateManager().getCurrentGameState();
            ingameState.checkRemainingPlayers();
        }

        this.hotPotato.getCustomPlayerManager().getPlayerCacheMap().remove(player);
    }

    private void handleQuitInLobby(Player player) {
        Bukkit.getOnlinePlayers().forEach(playerEach -> {
            CustomPlayerCache customPlayerEachCache = this.hotPotato.getCustomPlayerManager().getPlayerCacheMap().get(playerEach);
            String message = this.hotPotato.getLanguageModule().getMessage(customPlayerEachCache.getLocale(), "quit_message");

            playerEach.sendMessage(String.format(PotatoConstants.PREFIX + message +
                    " §7[" + PotatoConstants.playerList.size() + "/" + PotatoConstants.MAX_PLAYERS + "]", player.getName()));
        });

        LobbyState lobbyState = (LobbyState) this.hotPotato.getGameStateManager().getCurrentGameState();
        LobbyCountdown lobbyCountdown = lobbyState.getLobbyCountdown();
        if (PotatoConstants.playerList.size() < PotatoConstants.MIN_PLAYERS && lobbyCountdown.isRunning()) {
            lobbyCountdown.stop();
            lobbyCountdown.startIdle();
        }

        Voting voting = this.hotPotato.getVoting();
        if (voting.getPlayerVotes().containsKey(player.getName())) {
            voting.getVotingMaps()[voting.getPlayerVotes().get(player.getName())].removeVote();
            voting.getPlayerVotes().remove(player.getName());
            voting.initInventory(player);
        }
    }
}
