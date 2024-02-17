package de.bukkitnews.hotpotato.commands;

import de.bukkitnews.hotpotato.HotPotato;
import de.bukkitnews.hotpotato.player.CustomPlayerCache;
import de.bukkitnews.hotpotato.utils.PotatoConstants;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LanguageCommand implements CommandExecutor {

    private HotPotato hotPotato;

    public LanguageCommand(HotPotato hotPotato){
        this.hotPotato = hotPotato;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if(commandSender instanceof Player){
            Player player = (Player) commandSender;
            CustomPlayerCache customPlayerCache = hotPotato.getCustomPlayerManager().getPlayerCacheMap().get(player);

            if(args.length == 1){
                if(args[0].equalsIgnoreCase("DE")){
                    customPlayerCache.setLocale("de");
                    String message = hotPotato.getLanguageModule().getMessage(customPlayerCache.getLocale(), "command_language_changed");
                    player.sendMessage(PotatoConstants.PREFIX+message);
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);

                }else if(args[0].equalsIgnoreCase("EN")){
                    customPlayerCache.setLocale("en");
                    String message = hotPotato.getLanguageModule().getMessage(customPlayerCache.getLocale(), "command_language_changed");
                    player.sendMessage(PotatoConstants.PREFIX+message);
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);

                }else{
                    String message = hotPotato.getLanguageModule().getMessage(customPlayerCache.getLocale(), "command_language_usage");
                    player.sendMessage(PotatoConstants.PREFIX+message);
                }
            }else{
                String message = hotPotato.getLanguageModule().getMessage(customPlayerCache.getLocale(), "command_language_usage");
                player.sendMessage(PotatoConstants.PREFIX+message);
            }
        }
        return false;
    }
}
