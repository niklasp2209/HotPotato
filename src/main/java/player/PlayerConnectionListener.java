package player;

import de.bukkitnews.hotpotato.HotPotato;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PlayerConnectionListener implements Listener {

    private HotPotato hotPotato;

    public PlayerConnectionListener(HotPotato hotPotato){
        this.hotPotato = hotPotato;
    }

    @EventHandler
    public void handleJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        hotPotato.getCustomPlayerManager().getPlayerCacheMap().put(player, new CustomPlayerCache());

        Executors.newCachedThreadPool().execute(() -> {
            try{
                TimeUnit.MILLISECONDS.sleep(500L);
                /*
                LOADING STATS FROM SQL IN CACHE
                 */
            }catch (Exception exception){
                exception.printStackTrace();
            }
        });
    }

    @EventHandler
    public void handleQuit(PlayerQuitEvent event){
        Player player  = event.getPlayer();

        if(hotPotato.getCustomPlayerManager().getPlayerCacheMap().containsKey(player))
            hotPotato.getCustomPlayerManager().getPlayerCacheMap().remove(player);
    }
}
