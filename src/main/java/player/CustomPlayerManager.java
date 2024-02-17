package player;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class CustomPlayerManager {

    private Map<Player, CustomPlayerCache> playerCacheMap = new HashMap<>();

    public CustomPlayerManager(){}

    public Map<Player, CustomPlayerCache> getPlayerCacheMap() {
        return playerCacheMap;
    }
}
