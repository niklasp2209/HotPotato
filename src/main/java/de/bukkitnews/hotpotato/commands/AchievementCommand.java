package de.bukkitnews.hotpotato.commands;

import de.bukkitnews.hotpotato.HotPotato;
import de.bukkitnews.hotpotato.player.CustomPlayerCache;
import de.bukkitnews.hotpotato.utils.PotatoConstants;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AchievementCommand implements CommandExecutor {

    private HotPotato hotPotato;

    public AchievementCommand(HotPotato hotPotato){
        this.hotPotato = hotPotato;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!(commandSender instanceof Player)) return false;

        Player player = (Player) commandSender;
        CustomPlayerCache customPlayerCache = this.hotPotato.getCustomPlayerManager().getPlayerCacheMap().get(player);

        if (args.length == 0) {
            this.hotPotato.getAchievementManager().initInventory(player);
            player.openInventory(this.hotPotato.getAchievementManager().getInventory());
            return true;
        }

        String message = this.hotPotato.getLanguageModule().getMessage(customPlayerCache.getLocale(), "command_achievement_usage");
        player.sendMessage(PotatoConstants.PREFIX + message);
        return true;
    }
}
