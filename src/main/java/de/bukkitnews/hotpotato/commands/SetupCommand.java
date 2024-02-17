package de.bukkitnews.hotpotato.commands;

import de.bukkitnews.hotpotato.HotPotato;
import de.bukkitnews.hotpotato.maps.Map;
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
            if(player.hasPermission("hotpotato.setup")) {
                if(args.length == 0)
                    sendIntroductions(player);
                else{
                    if(args[0].equalsIgnoreCase("lobby") && args.length == 1){
                        /*
                        SET LOBBY-SPAWN SECTION
                         */
                        new ConfigurationUtil(hotPotato, player.getLocation(), "Lobby").saveLocation();
                        player.sendMessage(PotatoConstants.PREFIX+" §cDer Lobby-Spawn wurde gesetzt.");

                    }else if(args[0].equalsIgnoreCase("create") && args.length == 3){
                        /*
                        CREATE MAP SECTION
                         */
                        Map map = new Map(hotPotato, args[1]);
                        if(!map.exists()){
                            map.create(args[2]);
                            player.sendMessage(PotatoConstants.PREFIX+" §7Die Map §a"+map.getName()+ " §7wurde erstellt.");
                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP ,1F, 1F);
                        }else
                            player.sendMessage(PotatoConstants.PREFIX+" §cDiese Map exisitert bereits.");
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
                                    player.sendMessage(PotatoConstants.PREFIX+" §7Du hast LocationID §a"+locationID+" §7für §e"+map.getName()+" §7gesetzt.");
                                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
                                }else
                                    player.sendMessage(PotatoConstants.PREFIX+" §cBenutze eine Zahl zwischen 1 und"+PotatoConstants.MAX_PLAYERS+".");
                            }catch (NumberFormatException exception){
                                /*
                                SETTING SPECTATOR LOCATION FOR PARTICULAR MAP
                                 */
                                if(args[2].equalsIgnoreCase("spectator")){
                                    map.setSpectatorLocation(player.getLocation());
                                    player.sendMessage(PotatoConstants.PREFIX+" §7Du hast die §aSpectator-Location §7für §e"+map.getName()+" §7gesetzt.");
                                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
                                }else
                                    sendIntroductions(player);
                            }
                        }else
                            player.sendMessage(PotatoConstants.PREFIX+ " §cDiese Map konnte nicht gefunden werden.");
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
