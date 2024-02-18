package de.bukkitnews.hotpotato.achievement;

import de.bukkitnews.hotpotato.HotPotato;
import de.bukkitnews.hotpotato.language.LanguageModule;
import de.bukkitnews.hotpotato.player.CustomPlayerCache;
import de.bukkitnews.hotpotato.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;

public class AchievementManager {

    private final HotPotato hotPotato;
    private Inventory inventory;

    public AchievementManager(HotPotato hotPotato){
        this.hotPotato = hotPotato;
    }

    private void initAchievements(Player player) {
        LanguageModule languageModule = this.hotPotato.getLanguageModule();
        CustomPlayerCache customPlayerCache = this.hotPotato.getCustomPlayerManager().getPlayerCacheMap().get(player);

        for(Achievement achievement : Achievement.values()) {
            achievement.setDisplayname(languageModule.getMessage(customPlayerCache.getLocale(), "achievement_"+achievement.toString().toLowerCase()));
        }
    }

    public void initInventory(Player player) {
        CustomPlayerCache customPlayerCache = this.hotPotato.getCustomPlayerManager().getPlayerCacheMap().get(player);
        String inventoryTitle = this.hotPotato.getLanguageModule().getMessage(customPlayerCache.getLocale(), "achievement_inventory_title");

        this.inventory = Bukkit.createInventory(null, 9, inventoryTitle);

        int i = 0;
        initAchievements(player);
        for(Achievement achievement : Achievement.values()) {
            ItemStack itemStack = new ItemBuilder(achievement.getMaterial()).setDisplayname(achievement.getDisplayname()).build();
            if(hasAchievement(player, achievement))
                itemStack.setType(Material.LIME_DYE);

            this.inventory.setItem(i, itemStack);
            System.out.println(hasAchievement(player, achievement));

            i++;
        }
    }

    public void setAchievement(String uuid, Achievement achievement) {
        File configFile = new File(this.hotPotato.getDataFolder(), "achievements.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        String key = uuid + "." + achievement.toString().toLowerCase();

        if (!config.getBoolean(key, false)) {
            if (!config.isConfigurationSection(uuid)) {
                config.createSection(uuid);
            }

            config.set(key, true);

            try {
                config.save(configFile);
            } catch (IOException exception) {
                this.hotPotato.getLogger().warning("Error " + exception.getMessage());
            }
        }
    }

    private boolean hasAchievement(Player player, Achievement achievement) {
        File configFile = new File(this.hotPotato.getDataFolder(), "achievements.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        if (!configFile.exists()) {
            this.hotPotato.getLogger().warning("Die Datei " + configFile.getName() + " konnte nicht gefunden werden.");
            return false;
        }

        String key = player.getUniqueId().toString() + "." + achievement.toString().toLowerCase();
        return config.getBoolean(key);
    }

    public Inventory getInventory() {
        return this.inventory;
    }
}
