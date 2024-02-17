package de.bukkitnews.hotpotato.language;


import de.bukkitnews.hotpotato.HotPotato;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LanguageModule {

    private HotPotato hotPotato;
    private final Map<String, Map<String, String>> messages = new HashMap<>();

    public LanguageModule(HotPotato hotPotato, FileConfiguration fileConfiguration){
        this.hotPotato = hotPotato;
        loadMessages(fileConfiguration);
    }

    private void loadMessages(FileConfiguration fileConfiguration){
        File languageFile = new File(hotPotato.getDataFolder(), "language.yml");
        if (!languageFile.exists()) {
            hotPotato.getLogger().severe("Die language.yml wurde nicht gefunden! Stelle sicher, dass sie existiert.");
            return;
        }
        fileConfiguration = YamlConfiguration.loadConfiguration(languageFile);
        for (String localeStr : fileConfiguration.getKeys(false)) {
            String locale = new Locale(localeStr).toString();
            Map<String, String> messageMap = new HashMap<>();
            // Alle Nachrichten für die aktuelle Locale abrufen
            for (String key : fileConfiguration.getConfigurationSection(localeStr).getKeys(false)) {
                String message = fileConfiguration.getString(localeStr + "." + key);
                messageMap.put(key, message);
            }
            messages.put(locale, messageMap);
        }
    }

    public String getMessage(String locale, String key){
        Map<String, String> messageMap = messages.get(locale);
        if(messageMap != null){
            return messageMap.getOrDefault(key, "Message not found");
        }else{
            return "Locale not supported";
        }
    }

    public void createDefaultConfig() {
        File configFile = new File(hotPotato.getDataFolder(), "language.yml");
        if (configFile.exists())
            configFile.delete();

        FileConfiguration defaultConfig = new YamlConfiguration();
        Map<String, String> deMessages = new HashMap<>();
        deMessages.put("join_message", "§e%s §7hat das Spiel betreten.");
        deMessages.put("quit_message", "§e%s §7hat das Spiel verlassen.");
        Map<String, String> enMessages = new HashMap<>();
        enMessages.put("join_message", "§e%s §7joined the game.");
        enMessages.put("quit_message", "§e%s §7left the game.");
        defaultConfig.set("de", deMessages);
        defaultConfig.set("en", enMessages);
        try {
            defaultConfig.save(configFile);
        } catch (IOException e) {
            hotPotato.getLogger().warning("Fehler beim Erstellen der language.yml: " + e.getMessage());
        }
    }
}
