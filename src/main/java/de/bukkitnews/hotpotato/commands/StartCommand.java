package de.bukkitnews.hotpotato.commands;

import de.bukkitnews.hotpotato.HotPotato;
import de.bukkitnews.hotpotato.game.LobbyState;
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

    private HotPotato hotPotato;

    public StartCommand(HotPotato hotPotato){
        this.hotPotato = hotPotato;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if(commandSender instanceof Player){
            Player player = (Player) commandSender;
            if(player.hasPermission("hotpotato.start")){
                if(args.length == 0){
                    if(hotPotato.getGameStateManager().getCurrentGameState() instanceof LobbyState){
                        LobbyState lobbyState = (LobbyState) hotPotato.getGameStateManager().getCurrentGameState();
                        if(lobbyState.getLobbyCountdown().isRunning() && lobbyState.getLobbyCountdown().getSeconds() > PotatoConstants.FORCE_START_TIME){
                            lobbyState.getLobbyCountdown().setSeconds(PotatoConstants.FORCE_START_TIME);
                            player.sendMessage(PotatoConstants.PREFIX+" §aDu hast das Spiel gestartet.");
                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
                        }else
                            player.sendMessage(PotatoConstants.PREFIX+" §cDas Spiel ist bereits gestartet.");
                    }else
                        player.sendMessage(PotatoConstants.PREFIX+" §cDas Spiel ist bereits gestartet.");
                }else
                    player.sendMessage(PotatoConstants.PREFIX+" §cBenutze /start");
            }else
                player.sendMessage(PotatoConstants.NO_PERMISSION);
        }
        return false;
    }
}
