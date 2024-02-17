package de.bukkitnews.hotpotato.utils;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/*
EVERY GLOBAL CONSTANT OF HOTPOTATO GAME
 */
public class PotatoConstants {

    public static String PREFIX = "§7[§eHotPotato§7] ", NO_PERMISSION = PREFIX+"§cDu hast leider nicht genügend Rechte.", INVENTORY_VOTING = "§eMap Voting";
    public static final int MIN_PLAYERS = 1, MAX_PLAYERS = 12, IDLE_TIME = 15, LOBBY_COUNTDOWN = 60, FORCE_START_TIME = 10,
                            VOTING_MAP_AMOUNT = 2;
    public static List<Player> playerList = new ArrayList<>();
}
