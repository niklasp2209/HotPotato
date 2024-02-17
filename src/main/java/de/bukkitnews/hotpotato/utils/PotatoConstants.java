package de.bukkitnews.hotpotato.utils;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PotatoConstants {

    public static String PREFIX = "§7[§eHotPotato§7]", NO_PERMISSION = PREFIX+"§cDu hast leider nicht genügend Rechte.";
    public static final int MIN_PLAYERS = 2, MAX_PLAYERS = 12, IDLE_TIME = 15, LOBBY_COUNTDOWN = 60, FORCE_START_TIME = 10;
    public static List<Player> playerList = new ArrayList<>();
}
