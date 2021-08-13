package placeblock.towerdefense.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import placeblock.towerdefense.TowerDefense;
import placeblock.towerdefense.game.TDPlayer;
import placeblock.towerdefense.registry.TDPlayerRegistry;

public class PlayerPlaceBlock implements Listener {

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        System.out.println("BLOCK PLACE EVENT");
        TDPlayer player = TowerDefense.getInstance().getPlayerRegistry().getPlayer(e.getPlayer());
        if(player == null) return;
        System.out.println("BUILD EVENT");
        player.getInventorySection().build(e);
    }

}
