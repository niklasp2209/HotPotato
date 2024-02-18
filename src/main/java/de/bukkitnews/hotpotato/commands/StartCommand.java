package de.bukkitnews.hotpotato.commands;

import de.bukkitnews.hotpotato.HotPotato;
import de.bukkitnews.hotpotato.game.LobbyState;
import de.bukkitnews.hotpotato.player.CustomPlayerCache;
import de.bukkitnews.hotpotato.utils.PotatoConstants;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/*
THIS CLASS IS HANDLING THE STARTCOMMAND TO FORCE START
THE GAME IN LOBBYSTATE
 */
public class StartCommand implements CommandExecutor {

    private final HotPotato hotPotato;

    public StartCommand(HotPotato hotPotato){
        this.hotPotato = hotPotato;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if(!(commandSender instanceof Player))return false;
        Player player = (Player) commandSender;
        CustomPlayerCache customPlayerCache = this.hotPotato.getCustomPlayerManager().getPlayerCacheMap().get(player);

        //CHECKING FOR PERMISSION
        if(!player.hasPermission("hotpotato.start")) {
            String message = this.hotPotato.getLanguageModule().getMessage(customPlayerCache.getLocale(), "permission");
            player.sendMessage(PotatoConstants.PREFIX+message);
            return true;
        }

        //CHECKING IF WRONG ARGS OR NOT IN LOBBYSTATE
        if(args.length != 0 || !(this.hotPotato.getGameStateManager().getCurrentGameState() instanceof LobbyState)) {
            String message = this.hotPotato.getLanguageModule().getMessage(customPlayerCache.getLocale(), "command_start_usage");
            player.sendMessage(PotatoConstants.PREFIX+message);
            return true;
        }

        //CHECKING IF COUNTDOWN ALREADY STARTED
        LobbyState lobbyState = (LobbyState) this.hotPotato.getGameStateManager().getCurrentGameState();
        if(!(lobbyState.getLobbyCountdown().isRunning() && lobbyState.getLobbyCountdown().getSeconds() > PotatoConstants.FORCE_START_TIME)) {
            String message = this.hotPotato.getLanguageModule().getMessage(customPlayerCache.getLocale(), "command_start_already_started");
            player.sendMessage(PotatoConstants.PREFIX+message);
            return true;
        }

            //FORCE STARTING GAME
        lobbyState.getLobbyCountdown().setSeconds(PotatoConstants.FORCE_START_TIME);
        String message = this.hotPotato.getLanguageModule().getMessage(customPlayerCache.getLocale(), "command_start_started");
        player.sendMessage(PotatoConstants.PREFIX+message);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
        return true;
    }
}
