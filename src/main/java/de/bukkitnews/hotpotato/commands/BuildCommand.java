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

import java.util.ArrayList;
import java.util.List;

public class BuildCommand implements CommandExecutor {

    private final HotPotato hotPotato;
    private final List<Player> buildList;

    public BuildCommand(HotPotato hotPotato){
        this.hotPotato = hotPotato;
        this.buildList = new ArrayList<>();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if(!(commandSender instanceof Player))return false;
        Player player = (Player) commandSender;
        CustomPlayerCache customPlayerCache = this.hotPotato.getCustomPlayerManager().getPlayerCacheMap().get(player);

        //CHECKING FOR PERMISSION
        if(!player.hasPermission("hotpotato.build")){
            String message = this.hotPotato.getLanguageModule().getMessage(customPlayerCache.getLocale(), "permission");
            player.sendMessage(PotatoConstants.PREFIX+message);
            return true;
        }

        //CHECKING IF WRONG ARGS
        if(args.length != 0){
            String message = this.hotPotato.getLanguageModule().getMessage(customPlayerCache.getLocale(), "command_build_usage");
            player.sendMessage(PotatoConstants.PREFIX+message);
            return true;
        }

        //CHANGE BUILD MODE
        if(this.buildList.contains(player)){
            this.buildList.remove(player);
            String message = this.hotPotato.getLanguageModule().getMessage(customPlayerCache.getLocale(), "command_build_deactivated");
            player.sendMessage(PotatoConstants.PREFIX+message);
        }else{
            this.buildList.add(player);
            String message = this.hotPotato.getLanguageModule().getMessage(customPlayerCache.getLocale(), "command_build_activated");
            player.sendMessage(PotatoConstants.PREFIX+message);
        }

        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1F, 1F);
        return true;
    }

    public List<Player> getBuildList() {
        return this.buildList;
    }
}
