package de.bukkitnews.hotpotato.player;

import de.bukkitnews.hotpotato.HotPotato;
import de.bukkitnews.hotpotato.utils.PotatoConstants;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.help.HelpTopic;

public class PlayerChatListener implements Listener {

    private final HotPotato hotPotato;

    public PlayerChatListener(HotPotato hotPotato){
        this.hotPotato = hotPotato;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleUnkown(PlayerCommandPreprocessEvent event) {
        if(event.isCancelled())return;
        Player player = event.getPlayer();
        String message = event.getMessage().split(" ")[0];
        HelpTopic helpTopic = Bukkit.getServer().getHelpMap().getHelpTopic(message);
        if(helpTopic == null) {
            CustomPlayerCache customPlayerCache = hotPotato.getCustomPlayerManager().getPlayerCacheMap().get(player);
            String playerMessage = this.hotPotato.getLanguageModule().getMessage(customPlayerCache.getLocale(), "chat_no");
            player.sendMessage(PotatoConstants.PREFIX+playerMessage, message);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void handleChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if(PotatoConstants.spectatorList.contains(player)) {
            event.setCancelled(true);
            for(Player current : Bukkit.getOnlinePlayers())
                current.sendMessage("§a%1$s §8» §7%2$s");
            return;
        }

        event.setFormat("§a%1$s §8» §7%2$s");
    }
}
