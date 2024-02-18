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

    private final HotPotato hotPotato;
    private final String name;
    private String builder;
    private final Location[] spawnLocations = new Location[PotatoConstants.MAX_PLAYERS];
    private Location spectatorLocation;
    private int votes;

    public Map(HotPotato hotPotato, String name){
        this.hotPotato = hotPotato;
        this.name = name.toUpperCase();

        if(exists())
            this.builder = hotPotato.getConfig().getString("Maps."+name+".Builder");
    }

    public void create(String builder){
        this.builder = builder;
        this.hotPotato.getConfig().set("Maps."+this.name+".Builder", builder);
        this.hotPotato.saveConfig();
    }

    public void load(){
        this.spectatorLocation = new ConfigurationUtil(this.hotPotato, "Maps."+this.name+".Spectator").loadLocation();
        for(int i = 0; i < this.spawnLocations.length; i++)
            this.spawnLocations[i] = new ConfigurationUtil(this.hotPotato, "Maps."+this.name+"."+(i+1)).loadLocation();
    }

    public void setSpawnLocation(int locationID, Location location){
        /*
        SETTING PLAYER SPAWN LOCATIONS FOR SPECIFIC MAP
         */
        this.spawnLocations[locationID-1] = location;
        new ConfigurationUtil(this.hotPotato, location, "Maps."+this.name+"."+locationID).saveLocation();
    }

    public void setSpectatorLocation(Location location){
        /*
        SETTING PLAYER SPECTATOR LOCATION FOR SPECIFIC MAP
         */
        this.spectatorLocation = location;
        new ConfigurationUtil(this.hotPotato, location, "Maps."+this.name+".Spectator").saveLocation();
    }

    public boolean playable(){
        /*
        METHOD SCANNING IF MAP IS READY TO PLAY. FIRSTLY SCANNING IF SPECTATOR AND BUILDER IS VALID
        BECAUSE CHECKING THE SPAWNLOCATIONS FIRST WILL BE BAD FOR SERVER RUNTIME
         */
        ConfigurationSection configurationSection = this.hotPotato.getConfig().getConfigurationSection("Maps."+this.name);
        if(!configurationSection.contains("Spectator"))return false;
        if(!configurationSection.contains("Builder"))return false;
        for(int i = 1; i < PotatoConstants.MAX_PLAYERS+1; i++){
            if(!(configurationSection.contains(Integer.toString(i))))return false;
        }
        return true;
    }

    public void addVote(){
        this.votes++;
    }

    public void removeVote(){
        this.votes--;
    }

    public boolean exists(){
        return(this.hotPotato.getConfig().getString("Maps."+this.name) != null);
    }

    public String getName() {
        return this.name;
    }

    public String getBuilder() {
        return this.builder;
    }

    public Location getSpectatorLocation() {
        return this.spectatorLocation;
    }

    public Location[] getSpawnLocations() {
        return this.spawnLocations;
    }

    public int getVotes() {
        return this.votes;
    }
}
