package de.bukkitnews.hotpotato.utils;

import de.bukkitnews.hotpotato.HotPotato;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

/*
CONFIGURATION UTIL CLASS TO STORE AND PULL DATA FROM LOCAL STORAGE
 */
public class ConfigurationUtil {

    private final HotPotato hotPotato;
    private final Location location;
    private final String root;

    public ConfigurationUtil(HotPotato hotPotato, Location location, String root){
        this.hotPotato = hotPotato;
        this.location = location;
        this.root = root;
    }

    public ConfigurationUtil(HotPotato hotPotato, String root){
        this(hotPotato, null, root);
    }

    /*
    STORE LOCATION IN LOCAL CONFIGURATION STORAGE
     */
    public void saveLocation() {
        FileConfiguration fileConfiguration = this.hotPotato.getConfig();
        fileConfiguration.set(this.root+".World", this.location.getWorld().getName());
        fileConfiguration.set(this.root+".X", this.location.getX());
        fileConfiguration.set(this.root+".Y", this.location.getY());
        fileConfiguration.set(this.root+".Z", this.location.getZ());
        fileConfiguration.set(this.root+".Yaw", this.location.getYaw());
        fileConfiguration.set(this.root+".Pitch", this.location.getPitch());
        this.hotPotato.saveConfig();
    }

    /*
    PULL DATA FROM LOCAL CONFIGURATION STORAGE
     */
    public Location loadLocation() {
        FileConfiguration fileConfiguration = this.hotPotato.getConfig();
        if(fileConfiguration.contains(this.root)) {
            World world = Bukkit.getWorld(fileConfiguration.getString(this.root + ".World"));
            double x = fileConfiguration.getDouble(this.root + ".X"),
                    y = fileConfiguration.getDouble(this.root + ".Y"),
                    z = fileConfiguration.getDouble(this.root + ".Z");
            float yaw = (float) fileConfiguration.getDouble(this.root + ".Yaw"),
                    pitch = (float) fileConfiguration.getDouble(this.root + ".Pitch");
            return new Location(world, x, y, z, yaw, pitch);
        }
        return null;
    }
}
