package de.bukkitnews.hotpotato.maps;

import de.bukkitnews.hotpotato.HotPotato;
import de.bukkitnews.hotpotato.utils.ConfigurationUtil;
import de.bukkitnews.hotpotato.utils.PotatoConstants;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

public class Map {

    private HotPotato hotPotato;
    private String name;
    private String builder;
    private Location[] spawnLocations = new Location[PotatoConstants.MAX_PLAYERS];
    private Location spectatorLocation;

    public Map(HotPotato hotPotato, String name){
        this.hotPotato = hotPotato;
        this.name = name.toUpperCase();
    }

    public void create(String builder){
        this.builder = builder;
        hotPotato.getConfig().set("Maps."+name+".Builder", builder);
        hotPotato.saveConfig();
    }

    public void setSpawnLocation(int locationID, Location location){
        spawnLocations[locationID-1] = location;
        new ConfigurationUtil(hotPotato, location, "Maps."+name+"."+locationID).saveLocation();
    }

    public void setSpectatorLocation(Location location){
        spectatorLocation = location;
        new ConfigurationUtil(hotPotato, location, "Maps."+name+".Spectator").saveLocation();
    }

    public boolean playable(){
        ConfigurationSection configurationSection = hotPotato.getConfig().getConfigurationSection("Maps."+name);
        if(!configurationSection.contains("Spectator"))return false;
        if(!configurationSection.contains("Builder"))return false;
        for(int i = 1; i < PotatoConstants.MAX_PLAYERS+1; i++){
            if(!(configurationSection.contains(Integer.toString(i))))return false;
        }
        return true;
    }

    public boolean exists(){
        return(hotPotato.getConfig().getString("Maps."+name) != null);
    }

    public String getName() {
        return name;
    }

    public String getBuilder() {
        return builder;
    }

    public Location getSpectatorLocation() {
        return spectatorLocation;
    }

    public Location[] getSpawnLocations() {
        return spawnLocations;
    }
}
