package de.bukkitnews.hotpotato.utils;

import de.bukkitnews.hotpotato.HotPotato;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigurationUtil {

    private HotPotato hotPotato;
    private Location location;
    private String root;

    public ConfigurationUtil(HotPotato hotPotato, Location location, String root){
        this.hotPotato = hotPotato;
        this.location = location;
        this.root = root;
    }

    public ConfigurationUtil(HotPotato hotPotato, String root){
        this(hotPotato, null, root);
    }

    public void saveLocation(){
        FileConfiguration fileConfiguration = hotPotato.getConfig();
        fileConfiguration.set(root+".World", location.getWorld().getName());
        fileConfiguration.set(root+".X", location.getX());
        fileConfiguration.set(root+".Y", location.getY());
        fileConfiguration.set(root+".Z", location.getZ());
        fileConfiguration.set(root+".Yaw", location.getYaw());
        fileConfiguration.set(root+".Pitch", location.getPitch());
        hotPotato.saveConfig();
    }

    public Location loadLocation(){
        FileConfiguration fileConfiguration = hotPotato.getConfig();
        if(fileConfiguration.contains(root)) {
            World world = Bukkit.getWorld(fileConfiguration.getString(root + ".World"));
            double x = fileConfiguration.getDouble(root + ".X"),
                    y = fileConfiguration.getDouble(root + ".Y"),
                    z = fileConfiguration.getDouble(root + ".Z");
            float yaw = (float) fileConfiguration.getDouble(root + ".Yaw"),
                    pitch = (float) fileConfiguration.getDouble(root + ".Pitch");
            return new Location(world, x, y, z, yaw, pitch);
        }
        return null;
    }
}
