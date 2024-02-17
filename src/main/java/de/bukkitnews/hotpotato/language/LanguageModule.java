package de.bukkitnews.hotpotato.language;


import de.bukkitnews.hotpotato.HotPotato;
import de.bukkitnews.hotpotato.utils.PotatoConstants;
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
        if (!configFile.exists()) {
            FileConfiguration defaultConfig = new YamlConfiguration();
            Map<String, String> deMessages = new HashMap<>();
            deMessages.put("join_message", "§e%s §7hat das Spiel betreten.");
            deMessages.put("quit_message", "§e%s §7hat das Spiel verlassen.");
            deMessages.put("command_language_usage", "§7Benutze §e/language DE/EN");
            deMessages.put("command_language_changed", "§aDeine Sprache wurde geändert.");
            deMessages.put("command_start_started", "§aDu hast das Spiel gestartet.");
            deMessages.put("command_start_already_started", "§cDas Spiel ist bereits gestartet.");
            deMessages.put("command_start_usage", "§cBenutze /start");
            deMessages.put("command_setup_spawn", "§cDer Lobby-Spawn wurde gesetzt.");
            deMessages.put("command_setup_map_created", "§7Die Map §a%s §7wurde erstellt.");
            deMessages.put("command_setup_map_exists", "§cDiese Map exisitert bereits.");
            deMessages.put("command_setup_location", "§7Du hast LocationID §a%d §7für §e%s §7gesetzt.");
            deMessages.put("command_setup_location_usage", "§cBenutze eine Zahl zwischen 1 und"+ PotatoConstants.MAX_PLAYERS+".");
            deMessages.put("command_setup_spectator", "§7Du hast die §aSpectator-Location §7für §e%s §7gesetzt.");
            deMessages.put("command_setup_map_no", "§cDiese Map konnte nicht gefunden werden.");
            deMessages.put("countdown_lobby_broadcast", "§7Das Spiel startet in §e%d Sekunden§7.");
            deMessages.put("voting_winner", "§7Sieger des Map-Votings: §a%s");
            deMessages.put("countdown_lobby_broadcast1", "§7Das Spiel startet in §e%d Sekunde§7.");
            deMessages.put("countdown_lobby_players", " §7Es fehlen noch §e%d Spieler §7zum Spielstart.");
            Map<String, String> enMessages = new HashMap<>();
            enMessages.put("join_message", "§e%s §7joined the game.");
            enMessages.put("quit_message", "§e%s §7left the game.");
            enMessages.put("command_language_usage", "§7Usage: §e/language DE/EN");
            enMessages.put("command_language_changed", "§aYour language has been changed.");
            enMessages.put("command_start_started", "§aYou started the Game.");
            enMessages.put("command_start_already_started", "§cThe Game has already started.");
            enMessages.put("command_start_usage", "§Use /start");
            enMessages.put("command_setup_spawn", "§cThe Lobby-Spawn has been set.");
            enMessages.put("command_setup_map_created", "§The Map §a%s §7has been created.");
            enMessages.put("command_setup_map_exists", "§cThis Map already exists.");
            enMessages.put("command_setup_location", "§7You set the LocationID §a%d §7for §e%s.");
            enMessages.put("command_setup_location_usage", "§cUse a Number between 1 and"+ PotatoConstants.MAX_PLAYERS+".");
            enMessages.put("command_setup_spectator", "§7You set the §aSpectator-Location §7for §e%s.");
            enMessages.put("command_setup_map_no", "§cThis Map could not be found.");
            enMessages.put("countdown_lobby_broadcast", "§7The Game starts in §e%d seconds§7.");
            enMessages.put("voting_winner", "§a%s won the Map Vote.");
            enMessages.put("countdown_lobby_broadcast1", "§7The Game Starts in §e%d seconds§7.");
            enMessages.put("countdown_lobby_players", "§7There are still §e%d missing players §7to start the game.");
            defaultConfig.set("de", deMessages);
            defaultConfig.set("en", enMessages);
            try {
                defaultConfig.save(configFile);
            } catch (IOException e) {
                hotPotato.getLogger().warning("Fehler beim Erstellen der language.yml: " + e.getMessage());
            }
            loadMessages(hotPotato.getConfig());
        }
    }
}
