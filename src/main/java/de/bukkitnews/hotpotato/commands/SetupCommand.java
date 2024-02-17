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

    private HotPotato hotPotato;

    public SetupCommand(HotPotato hotPotato){
        this.hotPotato = hotPotato;
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if(commandSender instanceof Player){
            Player player = (Player) commandSender;
            CustomPlayerCache customPlayerCache = hotPotato.getCustomPlayerManager().getPlayerCacheMap().get(player);
            if(player.hasPermission("hotpotato.setup")) {
                if(args.length == 0)
                    sendIntroductions(player);
                else{
                    if(args[0].equalsIgnoreCase("lobby") && args.length == 1){
                        /*
                        SET LOBBY-SPAWN SECTION
                         */
                        new ConfigurationUtil(hotPotato, player.getLocation(), "Lobby").saveLocation();
                        String message = hotPotato.getLanguageModule().getMessage(customPlayerCache.getLocale(), "command_setup_spawn");
                        player.sendMessage(PotatoConstants.PREFIX+message);

                    }else if(args[0].equalsIgnoreCase("create") && args.length == 3){
                        /*
                        CREATE MAP SECTION
                         */
                        Map map = new Map(hotPotato, args[1]);
                        if(!map.exists()){
                            map.create(args[2]);
                            String message = hotPotato.getLanguageModule().getMessage(customPlayerCache.getLocale(), "command_setup_map_created");
                            player.sendMessage(PotatoConstants.PREFIX+message);
                            player.sendMessage(String.format(PotatoConstants.PREFIX+message, map.getName()));
                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP ,1F, 1F);
                        }else{
                            String message = hotPotato.getLanguageModule().getMessage(customPlayerCache.getLocale(), "command_map_exists");
                            player.sendMessage(PotatoConstants.PREFIX+message);
                        }
                    }else if(args[0].equalsIgnoreCase("set") && args.length == 3){
                        /*
                        CHECKING IF PLAYER IS SETTING LOCATIONID OR SPECTATOR LOCATION
                         */
                        Map map = new Map(hotPotato, args[1]);
                        if(map.exists()){
                            try{
                                int locationID = Integer.parseInt(args[2]);
                                if(locationID > 0 && locationID <= PotatoConstants.MAX_PLAYERS){
                                    /*
                                    SETTING LOCATIONID FOR PARTICULAR MAP
                                     */
                                    map.setSpawnLocation(locationID, player.getLocation());
                                    String message = hotPotato.getLanguageModule().getMessage(customPlayerCache.getLocale(), "command_setup_location");
                                    player.sendMessage(PotatoConstants.PREFIX+message);
                                    player.sendMessage(String.format(PotatoConstants.PREFIX+message, locationID, map.getName()));
                                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
                                }else{
                                    String message = hotPotato.getLanguageModule().getMessage(customPlayerCache.getLocale(), "command_setup_location_usage");
                                    player.sendMessage(PotatoConstants.PREFIX+message);
                                }
                            }catch (NumberFormatException exception){
                                /*
                                SETTING SPECTATOR LOCATION FOR PARTICULAR MAP
                                 */
                                if(args[2].equalsIgnoreCase("spectator")){
                                    map.setSpectatorLocation(player.getLocation());
                                    String message = hotPotato.getLanguageModule().getMessage(customPlayerCache.getLocale(), "command_setup_spectator");
                                    player.sendMessage(PotatoConstants.PREFIX+message);
                                    player.sendMessage(String.format(PotatoConstants.PREFIX+message, map.getName()));
                                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
                                }else
                                    sendIntroductions(player);
                            }
                        }else{
                            String message = hotPotato.getLanguageModule().getMessage(customPlayerCache.getLocale(), "command_setup_map_no");
                            player.sendMessage(PotatoConstants.PREFIX+message);
                        }
                    }else
                        sendIntroductions(player);
                }
            }else
                player.sendMessage(PotatoConstants.NO_PERMISSION);
        }
        return false;
    }

    private void sendIntroductions(Player player){
        player.sendMessage(PotatoConstants.PREFIX+" §e/setup <Lobby>");
        player.sendMessage(PotatoConstants.PREFIX+" §e/setup create <Name> <Erbauer>");
        player.sendMessage(PotatoConstants.PREFIX+" §e/setup set <Name> <1-"+PotatoConstants.MAX_PLAYERS+"> / Spectator");
    }
}
