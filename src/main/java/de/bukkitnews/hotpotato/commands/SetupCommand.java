package de.bukkitnews.hotpotato.commands;

import de.bukkitnews.hotpotato.HotPotato;
import de.bukkitnews.hotpotato.maps.Map;
import de.bukkitnews.hotpotato.player.CustomPlayerCache;
import de.bukkitnews.hotpotato.utils.ConfigurationUtil;
import de.bukkitnews.hotpotato.utils.PotatoConstants;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/*
THIS CLASS IS MANAGING THE SETUPCOMMAND INCLUDING MAP CREATING AND MAP SETTINGS
 */

public class SetupCommand implements CommandExecutor {

    private final HotPotato hotPotato;

    public SetupCommand(HotPotato hotPotato){
        this.hotPotato = hotPotato;
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if(!(commandSender instanceof Player))return false;

        Player player = (Player) commandSender;
        CustomPlayerCache customPlayerCache = this.hotPotato.getCustomPlayerManager().getPlayerCacheMap().get(player);

        //CHECKING FOR PERMISSIONS
        if (!player.hasPermission("hotpotato.setup")) {
            String message = this.hotPotato.getLanguageModule().getMessage(customPlayerCache.getLocale(), "permission");
            player.sendMessage(PotatoConstants.PREFIX + message);
            return true;
        }

        //SENDIND INTRODUCTIONS IF ARGS WRONG
        if (args.length == 0) {
            sendIntroductions(player);
            return true;
        }

        //SETUP LOBBY SPAWN
        if (args[0].equalsIgnoreCase("lobby") && args.length == 1) {
            new ConfigurationUtil(this.hotPotato, player.getLocation(), "Lobby").saveLocation();
            String message = this.hotPotato.getLanguageModule().getMessage(customPlayerCache.getLocale(), "command_setup_spawn");
            player.sendMessage(PotatoConstants.PREFIX + message);
            return true;
        }

        //MAP CREATION SECTION
        if (args[0].equalsIgnoreCase("create") && args.length == 3) {
            Map map = new Map(this.hotPotato, args[1]);
            if (!map.exists()) {
                map.create(args[2]);
                String message = this.hotPotato.getLanguageModule().getMessage(customPlayerCache.getLocale(), "command_setup_map_created");
                player.sendMessage(PotatoConstants.PREFIX + message);
                player.sendMessage(String.format(PotatoConstants.PREFIX + message, map.getName()));
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
            } else {
                String message = this.hotPotato.getLanguageModule().getMessage(customPlayerCache.getLocale(), "command_map_exists");
                player.sendMessage(PotatoConstants.PREFIX + message);
            }
            return true;
        }

        //MAP SPAWWPOINT SECTION
        if (args[0].equalsIgnoreCase("set") && args.length == 3) {
            Map map = new Map(this.hotPotato, args[1]);
            if (map.exists()) {
                try {
                    int locationID = Integer.parseInt(args[2]);
                    if (locationID > 0 && locationID <= PotatoConstants.MAX_PLAYERS) {
                        map.setSpawnLocation(locationID, player.getLocation());
                        String message = this.hotPotato.getLanguageModule().getMessage(customPlayerCache.getLocale(), "command_setup_location");
                        player.sendMessage(PotatoConstants.PREFIX + message);
                        player.sendMessage(String.format(PotatoConstants.PREFIX + message, locationID, map.getName()));
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
                    } else {
                        String message = this.hotPotato.getLanguageModule().getMessage(customPlayerCache.getLocale(), "command_setup_location_usage");
                        player.sendMessage(PotatoConstants.PREFIX + message);
                    }
                } catch (NumberFormatException exception) {
                    if (args[2].equalsIgnoreCase("spectator")) {
                        map.setSpectatorLocation(player.getLocation());
                        String message = this.hotPotato.getLanguageModule().getMessage(customPlayerCache.getLocale(), "command_setup_spectator");
                        player.sendMessage(PotatoConstants.PREFIX + message);
                        player.sendMessage(String.format(PotatoConstants.PREFIX + message, map.getName()));
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
                    } else {
                        sendIntroductions(player);
                    }
                }
            } else {
                String message = this.hotPotato.getLanguageModule().getMessage(customPlayerCache.getLocale(), "command_setup_map_no");
                player.sendMessage(PotatoConstants.PREFIX + message);
            }
            return true;
        }

        sendIntroductions(player);
        return true;
    }

    private void sendIntroductions(Player player) {
        player.sendMessage(PotatoConstants.PREFIX+" §e/setup <Lobby>");
        player.sendMessage(PotatoConstants.PREFIX+" §e/setup create <Name> <Erbauer>");
        player.sendMessage(PotatoConstants.PREFIX+" §e/setup set <Name> <1-"+PotatoConstants.MAX_PLAYERS+"> / Spectator");
    }
}
