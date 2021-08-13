package placeblock.towerdefense.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import placeblock.towerdefense.TowerDefense;
import placeblock.towerdefense.game.TDPlayer;

public class PlayerInteract implements Listener {
    @EventHandler
    public void onInteract(org.bukkit.event.player.PlayerInteractEvent e) {
        TDPlayer player = TowerDefense.getInstance().getPlayerRegistry().getPlayer(e.getPlayer());
        if(player == null) return;
        player.getInventorySection().interact(e);
    }

}
