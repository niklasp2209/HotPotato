package de.bukkitnews.hotpotato.maps;

import de.bukkitnews.hotpotato.HotPotato;
import de.bukkitnews.hotpotato.utils.ConfigurationUtil;
import de.bukkitnews.hotpotato.utils.PotatoConstants;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

/*
CREATING MAP OBJECT FOR MAP VOTING AND MAP PLAYING
 */
public class Map {

    private HotPotato hotPotato;
    private String name;
    private String builder;
    private Location[] spawnLocations = new Location[PotatoConstants.MAX_PLAYERS];
    private Location spectatorLocation;
    private int votes;

    public Map(HotPotato hotPotato, String name){
        this.hotPotato = hotPotato;
        this.name = name.toUpperCase();

        if(exists())
            builder = hotPotato.getConfig().getString("Maps."+name+".Builder");
    }

    public void create(String builder){
        this.builder = builder;
        hotPotato.getConfig().set("Maps."+name+".Builder", builder);
        hotPotato.saveConfig();
    }

    public void setSpawnLocation(int locationID, Location location){
        /*
        SETTING PLAYER SPAWN LOCATIONS FOR SPECIFIC MAP
         */
        spawnLocations[locationID-1] = location;
        new ConfigurationUtil(hotPotato, location, "Maps."+name+"."+locationID).saveLocation();
    }

    public void setSpectatorLocation(Location location){
        /*
        SETTING PLAYER SPECTATOR LOCATION FOR SPECIFIC MAP
         */
        spectatorLocation = location;
        new ConfigurationUtil(hotPotato, location, "Maps."+name+".Spectator").saveLocation();
    }

    public boolean playable(){
        /*
        METHOD SCANNING IF MAP IS READY TO PLAY. FIRSTLY SCANNING IF SPECTATOR AND BUILDER IS VALID
        BECAUSE CHECKING THE SPAWNLOCATIONS FIRST WILL BE BAD FOR SERVER RUNTIME
         */
        ConfigurationSection configurationSection = hotPotato.getConfig().getConfigurationSection("Maps."+name);
        if(!configurationSection.contains("Spectator"))return false;
        if(!configurationSection.contains("Builder"))return false;
        for(int i = 1; i < PotatoConstants.MAX_PLAYERS+1; i++){
            if(!(configurationSection.contains(Integer.toString(i))))return false;
        }
        return true;
    }

    public void addVote(){
        votes++;
    }

    public void removeVote(){
        votes--;
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

    public int getVotes() {
        return votes;
    }
}
