package de.bukkitnews.hotpotato.commands;

import de.bukkitnews.hotpotato.HotPotato;
import de.bukkitnews.hotpotato.utils.ConfigurationUtil;
import de.bukkitnews.hotpotato.utils.PotatoConstants;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetupCommand implements CommandExecutor {

    private HotPotato hotPotato;

    public SetupCommand(HotPotato hotPotato){
        this.hotPotato = hotPotato;
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if(commandSender instanceof Player){
            Player player = (Player) commandSender;
            if(player.hasPermission("hotpotato.setup")) {
                if(args.length == 0)
                    sendIntroductions(player);
                else{
                    if(args[0].equalsIgnoreCase("lobby") && args.length == 1){
                        new ConfigurationUtil(hotPotato, player.getLocation(), "Lobby").saveLocation();
                        player.sendMessage(PotatoConstants.PREFIX+"§cDer Lobby-Spawn wurde gesetzt.");
                    }else
                        sendIntroductions(player);
                }
            }else
                player.sendMessage(PotatoConstants.NO_PERMISSION);
        }
        return false;
    }

    private void sendIntroductions(Player player){
        player.sendMessage(PotatoConstants.PREFIX+" §7Benutze §e/setup <Lobby>");
    }
}
